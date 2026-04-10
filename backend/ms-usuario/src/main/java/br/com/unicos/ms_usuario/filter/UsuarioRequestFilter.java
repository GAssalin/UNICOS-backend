package br.com.unicos.ms_usuario.filter;

import br.com.unicos.core.auth.context.AuthContext;
import br.com.unicos.core.auth.dto.TokenValidationResponse;
import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.usuario.auth.context.UserContext;
import br.com.unicos.ms_usuario.client.AuthClient;
import br.com.unicos.ms_usuario.client.PermissaoClient;
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
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioRequestFilter extends OncePerRequestFilter {

    private final PermissaoClient permissaoClient;
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
        UserContext.setUsuarioId(tokenInfo.usuarioId());
        TenantContext.setEmpresaId(tokenInfo.empresaId());

        String path = request.getServletPath();
        if (path.startsWith("/v1/usuarios"))
            verificarPermissao(request.getMethod(), "USUARIO_");
        else if (path.startsWith("/v1/verificacao-email"))
            verificarPermissao(request.getMethod(), "USUARIO_EMAIL_");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                tokenInfo.usuarioId(),
                null,
                Collections.emptyList()
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
