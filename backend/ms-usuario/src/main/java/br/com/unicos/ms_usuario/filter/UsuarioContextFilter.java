package br.com.unicos.ms_usuario.filter;

import br.com.unicos.core.auth.dto.TokenValidationResponse;
import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_usuario.client.AuthValidationClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UsuarioContextFilter extends OncePerRequestFilter {

    private final AuthValidationClient authValidationClient;

    public UsuarioContextFilter(AuthValidationClient authValidationClient) {
        this.authValidationClient = authValidationClient;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/internal/")
                || path.equals("/v1/autenticacao/login")
                || path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || authHeader.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token não informado");
            return;
        }

        TokenValidationResponse validation = authValidationClient.validateToken(authHeader);

        try {
            // =========================
            // TenantContext
            // =========================
            TenantContext.setEmpresaId(validation.empresaId());
            TenantContext.setUsuarioId(validation.usuarioId());
            TenantContext.setRoles(validation.roles());

            // =========================
            // Spring Security Context
            // =========================
            Set<SimpleGrantedAuthority> authorities =
                    validation.roles()
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(validation.usuarioId(), null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
            TenantContext.clear();
        }
    }
}
