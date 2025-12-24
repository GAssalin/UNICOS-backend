package br.com.unicos.ms_usuario.filter;

import br.com.unicos.core.auth.context.AuthContext;
import br.com.unicos.core.auth.dto.TokenValidationResponse;
import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.usuario.auth.context.UserContext;
import br.com.unicos.ms_usuario.client.AuthValidationClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioRequestFilter extends OncePerRequestFilter {

    private final AuthValidationClient authValidationClient;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            resolveAuthorizationHeader(request)
                    .ifPresent(header -> authenticateRequest(request, header));

            filterChain.doFilter(request, response);
        } finally {
            AuthContext.clear();
            TenantContext.clear();
            UserContext.clear();
        }
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

        TokenValidationResponse tokenInfo = authValidationClient.validateToken(authorizationHeader);

        var roles = Optional.ofNullable(tokenInfo.roles()).orElseGet(java.util.Set::of);

        AuthContext.setToken(authorizationHeader);
        AuthContext.setRoles(roles);
        TenantContext.setEmpresaId(tokenInfo.empresaId());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                tokenInfo.usuarioId(),
                null,
                roles.stream().map(SimpleGrantedAuthority::new).toList()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
