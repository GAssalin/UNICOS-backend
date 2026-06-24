package br.com.unicos.ms_cliente.filter;

import br.com.unicos.core.auth.context.AuthContext;
import br.com.unicos.core.auth.dto.TokenValidationResponse;
import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.usuario.auth.context.UserContext;
import br.com.unicos.ms_cliente.client.AuthClient;
import br.com.unicos.ms_cliente.client.PermissaoClient;
import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClienteRequestFilter extends OncePerRequestFilter {

    private static final String USUARIO_PREFIXO = "USUARIO_";
    private static final String USUARIO_EMAIL_PREFIXO = "USUARIO_EMAIL_";

    private final PermissaoClient permissaoClient;
    private final AuthClient authClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/error");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        boolean contextoAplicado = false;

        try {
            Optional<String> authorizationHeader = resolveAuthorizationHeader(request);

            if (authorizationHeader.isPresent()) {
                authenticateRequest(request, authorizationHeader.get());
                contextoAplicado = true;
            }

            filterChain.doFilter(request, response);

        } catch (ResponseStatusException ex) {
            escreverErro(response, request, ex.getStatusCode().value(), ex.getReason());
        } catch (BadCredentialsException | InsufficientAuthenticationException ex) {
            escreverErro(response, request, 401, ex.getMessage());
        } finally {
            if (contextoAplicado) {
                clearContexts();
            }
        }
    }

    private void escreverErro(
            HttpServletResponse response,
            HttpServletRequest request,
            int status,
            String message
    ) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String body = """
                {
                  "status": %d,
                  "error": "%s",
                  "message": "%s",
                  "path": "%s"
                }
                """.formatted(
                status,
                HttpStatus.valueOf(status).getReasonPhrase(),
                message == null ? "" : message.replace("\"", "\\\""),
                request.getRequestURI()
        );

        response.getWriter().write(body);
        response.getWriter().flush();
    }

    private Optional<String> resolveAuthorizationHeader(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || header.isBlank() || !header.startsWith("Bearer ")) {
            return Optional.empty();
        }

        return Optional.of(header);
    }

    private void authenticateRequest(HttpServletRequest request, String authorizationHeader) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }

        TokenValidationResponse tokenInfo = validarTokenComResiliencia(authorizationHeader);

        applyContexts(authorizationHeader, tokenInfo);
        validarPermissaoPorRota(request);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                tokenInfo.usuarioId(),
                null,
                Collections.emptyList()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private TokenValidationResponse validarTokenComResiliencia(String authorizationHeader) {
        try {
            return circuitBreakerFactory.create("auth-client").run(
                    () -> authClient.validateToken(authorizationHeader),
                    throwable -> {
                        throw traduzirFalhaAutenticacao(throwable);
                    }
            );
        } catch (FeignException.BadRequest | FeignException.Unauthorized | FeignException.Forbidden ex) {
            log.warn("Token inválido ou não autorizado: {}", ex.getMessage());
            throw new BadCredentialsException("Token inválido, expirado ou não autorizado.", ex);
        } catch (FeignException ex) {
            log.error(
                    "Falha ao validar token no ms-autenticacao. status={}, mensagem={}",
                    ex.status(),
                    ex.getMessage(),
                    ex
            );
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Serviço de autenticação temporariamente indisponível.",
                    ex
            );
        }
    }

    private RuntimeException traduzirFalhaAutenticacao(Throwable throwable) {
        if (throwable instanceof FeignException.BadRequest
                || throwable instanceof FeignException.Unauthorized
                || throwable instanceof FeignException.Forbidden) {
            return new BadCredentialsException("Token inválido, expirado ou não autorizado.", throwable);
        }

        log.error("Circuit breaker acionado ao validar token no ms-autenticacao.", throwable);

        return new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de autenticação temporariamente indisponível.",
                throwable
        );
    }

    private void validarPermissaoPorRota(HttpServletRequest request) {
        String permissao = resolverPermissao(request.getMethod(), request.getServletPath());

        if (permissao == null) {
            return;
        }

        boolean permitido = verificarPermissaoComResiliencia(permissao);

        if (!permitido) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Usuário não possui permissão para acessar este recurso."
            );
        }
    }

    private String resolverPermissao(String metodoHttp, String path) {
        String prefixo;

        if (path.startsWith("/v1/clientes/categorias")) {
            prefixo = "CLIENTE_CATEGORIA_";
        } else if (path.startsWith("/v1/clientes/observacoes")) {
            prefixo = "CLIENTE_OBSERVACAO_";
        } else if (path.startsWith("/v1/clientes")) {
            prefixo = "CLIENTE_";
        } else {
            return null;
        }

        return switch (metodoHttp) {
            case "GET" -> prefixo.concat("LISTAR");
            case "POST" -> prefixo.concat("CRIAR");
            case "PUT", "PATCH" -> prefixo.concat("EDITAR");
            case "DELETE" -> prefixo.concat("EXCLUIR");
            default -> null;
        };
    }

    private boolean verificarPermissaoComResiliencia(String nomePermissao) {
        try {
            return circuitBreakerFactory.create("permissao-client-check").run(
                    () -> permissaoClient.usuarioPossuiPermissao(nomePermissao, AuthContext.getToken()),
                    throwable -> {
                        throw traduzirFalhaPermissao(nomePermissao, throwable);
                    }
            );
        } catch (FeignException.BadRequest | FeignException.Unauthorized | FeignException.Forbidden ex) {
            log.warn(
                    "Falha ao validar permissão {} por problema de autenticação/autorização: {}",
                    nomePermissao,
                    ex.getMessage()
            );
            throw new InsufficientAuthenticationException("Não foi possível validar as permissões do usuário.", ex);
        } catch (FeignException ex) {
            log.error(
                    "Falha ao consultar permissão {} no ms-permissao. status={}, mensagem={}",
                    nomePermissao,
                    ex.status(),
                    ex.getMessage(),
                    ex
            );
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Serviço de permissões temporariamente indisponível.",
                    ex
            );
        }
    }

    private RuntimeException traduzirFalhaPermissao(String nomePermissao, Throwable throwable) {
        if (throwable instanceof FeignException.BadRequest
                || throwable instanceof FeignException.Unauthorized
                || throwable instanceof FeignException.Forbidden) {
            return new InsufficientAuthenticationException(
                    "Não foi possível validar a permissão do usuário.",
                    throwable
            );
        }

        log.error("Circuit breaker acionado ao validar permissão {} no ms-permissao.", nomePermissao, throwable);

        return new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de permissões temporariamente indisponível.",
                throwable
        );
    }

    private void applyContexts(String authorizationHeader, TokenValidationResponse tokenInfo) {
        AuthContext.setToken(authorizationHeader);
        UserContext.setUsuarioId(tokenInfo.usuarioId());
        TenantContext.setEmpresaId(tokenInfo.empresaId());
    }

    private void clearContexts() {
        SecurityContextHolder.clearContext();

        try {
            AuthContext.clear();
        } catch (Exception ex) {
            log.debug("Não foi possível limpar AuthContext explicitamente.", ex);
        }

        try {
            UserContext.clear();
        } catch (Exception ex) {
            log.debug("Não foi possível limpar UserContext explicitamente.", ex);
        }

        try {
            TenantContext.clear();
        } catch (Exception ex) {
            log.debug("Não foi possível limpar TenantContext explicitamente.", ex);
        }
    }
}