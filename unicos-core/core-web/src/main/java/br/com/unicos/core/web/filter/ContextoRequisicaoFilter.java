package br.com.unicos.core.web.filter;

import br.com.unicos.core.auth.context.AuthContext;
import br.com.unicos.core.auth.interno.TokenInternoService;
import br.com.unicos.core.auth.service.JwtClaims;
import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.usuario.context.UserContext;
import br.com.unicos.core.web.error.RespostaProblema;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UrlPathHelper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Filtro base que estabelece o contexto de segurança de cada requisição nos microserviços.
 *
 * <ol>
 *     <li>Caminhos {@code /internal/**} exigem o token interno ({@link TokenInternoService#HEADER}).</li>
 *     <li>Quando há header {@code Authorization}, o JWT é validado localmente (assinatura, emissor,
 *     tipo e expiração) e dele são extraídos usuário e empresa. Nenhum header de identidade
 *     enviado pelo cliente é considerado.</li>
 *     <li>Para usuários autenticados, a permissão exigida pela rota é verificada.</li>
 * </ol>
 *
 * <p>
 * Requisições sem token seguem anônimas; cabe ao {@code SecurityFilterChain} decidir se a rota
 * é pública. Os contextos ({@link UserContext}, {@link TenantContext}, {@link AuthContext} e o
 * {@code SecurityContext}) são sempre limpos ao final.
 * </p>
 */
@Slf4j
public abstract class ContextoRequisicaoFilter extends OncePerRequestFilter {

    private static final String PREFIXO_INTERNO = "/internal/";

    private final TokenCoreService tokenCoreService;
    private final TokenInternoService tokenInternoService;

    protected ContextoRequisicaoFilter(TokenCoreService tokenCoreService, TokenInternoService tokenInternoService) {
        this.tokenCoreService = tokenCoreService;
        this.tokenInternoService = tokenInternoService;
    }

    /**
     * Resolve o nome da permissão exigida para a rota.
     *
     * <p>Pode lançar {@link AccessDeniedException} para recusar a requisição (resposta 403),
     * por exemplo quando o caminho referencia dados de outra empresa.</p>
     *
     * @param metodoHttp método HTTP da requisição
     * @param path       caminho da requisição, sem o context path
     * @return nome da permissão ou {@code null} quando a rota não exige permissão específica
     */
    protected abstract String resolverPermissao(String metodoHttp, String path);

    /**
     * Verifica se o usuário do contexto atual possui a permissão informada.
     *
     * <p>Pode lançar {@link ResponseStatusException} quando o serviço de permissões estiver indisponível.</p>
     */
    protected abstract boolean usuarioPossuiPermissao(String permissao);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String path = extrairPath(request);
            boolean caminhoInterno = isCaminhoInterno(path);

            if (caminhoInterno && !tokenInternoService.isValido(request.getHeader(TokenInternoService.HEADER))) {
                RespostaProblema.escrever(request, response, HttpStatus.FORBIDDEN.value(),
                        "Recurso disponível apenas para chamadas internas.");
                return;
            }

            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authorization != null && !authorization.isBlank()) {
                if (!autenticar(authorization)) {
                    RespostaProblema.escrever(request, response, HttpStatus.UNAUTHORIZED.value(),
                            "Token ausente, inválido ou expirado.");
                    return;
                }

                if (!caminhoInterno && !autorizar(request, response, path))
                    return;
            }

            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
            TenantContext.clear();
            AuthContext.clear();
            SecurityContextHolder.clearContext();
        }
    }

    static boolean isCaminhoInterno(String path) {
        return path.equals("/internal") || path.startsWith(PREFIXO_INTERNO);
    }

    /**
     * Prefixo de permissão do primeiro recurso (na ordem de inserção) que contém o caminho.
     *
     * <p>A comparação é feita por segmento: {@code /v1/pessoas} captura {@code /v1/pessoas/1},
     * mas não {@code /v1/pessoas-fisicas}.</p>
     *
     * @param prefixos caminho base do recurso → prefixo da permissão (ex.: {@code PESSOA_})
     * @return prefixo encontrado ou {@code null} quando o caminho não pertence a nenhum recurso
     */
    protected static String prefixoDoRecurso(Map<String, String> prefixos, String path) {
        for (Map.Entry<String, String> entry : prefixos.entrySet()) {
            if (path.equals(entry.getKey()) || path.startsWith(entry.getKey() + "/"))
                return entry.getValue();
        }
        return null;
    }

    /**
     * Permissão CRUD padrão de um recurso: {@code <prefixo>LISTAR|CRIAR|EDITAR|EXCLUIR}.
     *
     * <p>Métodos sem permissão correspondente são recusados. Sem isso, um {@code HEAD}
     * (atendido pelos handlers de {@code GET}) ou outro método não mapeado seguiria
     * sem nenhuma verificação de permissão.</p>
     *
     * @throws AccessDeniedException para métodos HTTP não mapeados
     */
    protected static String permissaoPorMetodo(String prefixo, String metodoHttp) {
        return switch (metodoHttp) {
            case "GET", "HEAD" -> prefixo + "LISTAR";
            case "POST" -> prefixo + "CRIAR";
            case "PUT", "PATCH" -> prefixo + "EDITAR";
            case "DELETE" -> prefixo + "EXCLUIR";
            default -> throw new AccessDeniedException("Método HTTP não permitido para este recurso.");
        };
    }

    private boolean autenticar(String authorization) {
        DecodedJWT jwt;
        try {
            jwt = tokenCoreService.validarToken(authorization);
        } catch (JWTVerificationException ex) {
            log.debug("Token rejeitado: {}", ex.getMessage());
            return false;
        }

        Long usuarioId = jwt.getClaim(JwtClaims.USUARIO_ID).asLong();
        Long empresaId = jwt.getClaim(JwtClaims.TENANT_ID).asLong();

        UserContext.setUsuarioId(usuarioId);
        TenantContext.setEmpresaId(empresaId);
        AuthContext.setToken(authorization);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(usuarioId, null, List.of())
        );
        return true;
    }

    private boolean autorizar(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
        String permissao = null;

        try {
            permissao = resolverPermissao(request.getMethod(), path);

            if (permissao == null || usuarioPossuiPermissao(permissao))
                return true;

            RespostaProblema.escrever(request, response, HttpStatus.FORBIDDEN.value(),
                    "Usuário não possui permissão para acessar este recurso.");
        } catch (AccessDeniedException ex) {
            RespostaProblema.escrever(request, response, HttpStatus.FORBIDDEN.value(), ex.getMessage());
        } catch (ResponseStatusException ex) {
            RespostaProblema.escrever(request, response, ex.getStatusCode().value(), ex.getReason());
        } catch (RuntimeException ex) {
            log.error("Falha ao verificar a permissão [{}].", permissao, ex);
            RespostaProblema.escrever(request, response, HttpStatus.SERVICE_UNAVAILABLE.value(),
                    "Não foi possível verificar as permissões do usuário.");
        }
        return false;
    }

    /**
     * Caminho decodificado e sem parâmetros de matriz ({@code ;...}), o mesmo usado pelo Spring MVC
     * para localizar o controller. Evita que variações como {@code /%69nternal/...} escapem das regras.
     */
    private static String extrairPath(HttpServletRequest request) {
        String path = UrlPathHelper.defaultInstance.getPathWithinApplication(request);
        return path.isEmpty() ? "/" : path;
    }
}
