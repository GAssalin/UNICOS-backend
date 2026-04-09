package br.com.unicos.ms_permissao.filter;

import br.com.unicos.core.auth.context.AuthContext;
import br.com.unicos.core.auth.dto.TokenValidationResponse;
import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.usuario.auth.context.UserContext;
import br.com.unicos.ms_permissao.client.AuthClient;
import br.com.unicos.ms_permissao.service.PermissaoService;
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
public class PermissaoRequestFilter extends OncePerRequestFilter {

    private final PermissaoService permissaoService;
    private final AuthClient authClient;

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
        resolveAuthorizationHeader(request)
                .ifPresent(header -> authenticateRequest(request, header));
        filterChain.doFilter(request, response);
    }

    private Optional<String> resolveAuthorizationHeader(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer "))
            return Optional.empty();
        return Optional.of(header);
    }

    private void authenticateRequest(HttpServletRequest request, String authorizationHeader) {
        if (SecurityContextHolder.getContext().getAuthentication() != null)
            return;

        TokenValidationResponse tokenInfo = authClient.validateToken(authorizationHeader);

        AuthContext.setToken(authorizationHeader);
        TenantContext.setEmpresaId(tokenInfo.empresaId());
        UserContext.setUsuarioId(tokenInfo.usuarioId());

        String path = request.getServletPath();
        if (path.startsWith("/v1/permissoes"))
            verificarPermissao(request.getMethod(), "PERMISSAO_");
        else if (path.startsWith("/v1/roles"))
            verificarPermissao(request.getMethod(), "ROLE_");
        else if (path.startsWith("/v1/role-permissao"))
            verificarPermissao(request.getMethod(), "ROLE_PERMISSAO_");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                tokenInfo.usuarioId(),
                null
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void verificarPermissao(String metodo, String inicioEnpoint) {
        boolean permitido = switch (metodo) {
            case "GET" -> permissaoService.usuarioPossuiPermissao(inicioEnpoint.concat("LISTAR"));
            case "POST" -> permissaoService.usuarioPossuiPermissao(inicioEnpoint.concat("CRIAR"));
            case "PUT", "PATCH" -> permissaoService.usuarioPossuiPermissao(inicioEnpoint.concat("EDITAR"));
            case "DELETE" -> permissaoService.usuarioPossuiPermissao(inicioEnpoint.concat("EXCLUIR"));
            default -> false;
        };

        if (!permitido)
            throw new AccessDeniedException("Usuário não possui permissão.");
    }
}
