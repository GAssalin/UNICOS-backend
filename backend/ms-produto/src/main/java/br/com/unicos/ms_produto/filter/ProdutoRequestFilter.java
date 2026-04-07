package br.com.unicos.ms_produto.filter;

import br.com.unicos.core.auth.context.AuthContext;
import br.com.unicos.core.auth.dto.TokenValidationResponse;
import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_produto.client.AuthClient;
import br.com.unicos.ms_produto.client.PermissaoClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProdutoRequestFilter extends OncePerRequestFilter {

    private final PermissaoClient permissaoClient;
    private final AuthClient authClient;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.startsWith("/internal")
                || path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/error");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        resolveAuthorizationHeader(request)
                .ifPresent(header -> authenticateRequest(request, header));
        filterChain.doFilter(request, response);
    }

    private Optional<String> resolveAuthorizationHeader(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer "))
            return Optional.empty();

        // 🔥 REMOVE possíveis duplicações
        if (header.contains(",")) {
            header = header.split(",")[0].trim();
        }

        return Optional.of(header);
    }

    private void authenticateRequest(HttpServletRequest request, String authorizationHeader) {
        if (SecurityContextHolder.getContext().getAuthentication() != null)
            return;

        TokenValidationResponse tokenInfo = authClient.validateToken(authorizationHeader);

        AuthContext.setToken(authorizationHeader);
        TenantContext.setEmpresaId(tokenInfo.empresaId());

        String path = request.getServletPath();
        if (path.startsWith("/v1/produtos/categorias"))
            verificarPermissao(request.getMethod(), "PRODUTO_CATEGORIA_");
        else if (path.startsWith("/v1/produtos/marcas-produto"))
            verificarPermissao(request.getMethod(), "PRODUTO_MARCAS_PRODUTO_");
        else if (path.startsWith("/v1/produtos/atributos-valores"))
            verificarPermissao(request.getMethod(), "PRODUTO_ATRIBUTOS_VALORES_");
        else if (path.startsWith("/v1/produtos/atributos"))
            verificarPermissao(request.getMethod(), "PRODUTO_ATRIBUTOS_");
        else if (path.startsWith("/v1/produtos/codigo-barras"))
            verificarPermissao(request.getMethod(), "PRODUTO_CODIGO_BARRAS_");
        else if (path.startsWith("/v1/produtos/imagens"))
            verificarPermissao(request.getMethod(), "PRODUTO_IMAGENS_");
        else if (path.startsWith("/v1/produtos/precos-base"))
            verificarPermissao(request.getMethod(), "PRODUTO_PRECO_BASE_");
        else if (path.startsWith("/v1/produtos/tipos"))
            verificarPermissao(request.getMethod(), "PRODUTO_TIPOS_");
        else if (path.startsWith("/v1/produtos/unidades-medida"))
            verificarPermissao(request.getMethod(), "PRODUTO_UNIDADE_MEDIDA_");
        else if (path.startsWith("/v1/produtos"))
            verificarPermissao(request.getMethod(), "PRODUTO_");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                tokenInfo.usuarioId(),
                null
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void verificarPermissao(String metodo, String inicioEnpoint) {
        boolean permitido = switch (metodo) {
            case "GET" -> permissaoClient.usuarioPossuiPermissao(inicioEnpoint.concat("LISTAR"));
            case "POST" -> permissaoClient.usuarioPossuiPermissao(inicioEnpoint.concat("CRIAR"));
            case "PUT", "PATCH" -> permissaoClient.usuarioPossuiPermissao(inicioEnpoint.concat("EDITAR"));
            case "DELETE" -> permissaoClient.usuarioPossuiPermissao(inicioEnpoint.concat("EXCLUIR"));
            default -> false;
        };

        if (!permitido)
            throw new AccessDeniedException("Usuário não possui permissão.");
    }
}
