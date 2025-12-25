package br.com.unicos.ms_auth.filter;

import br.com.unicos.ms_auth.security_access.AuthenticatedUser;
import br.com.unicos.ms_auth.security_access.TokenService;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthRequestFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.equals("/v1/autenticacao/login")
                || path.startsWith("/internal")
                || path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = recuperarTokenRequisicao(request);

        if (token != null) {

            DecodedJWT decodedJWT;
            try {
                decodedJWT = tokenService.verificarAccessToken(token);
            } catch (Exception ex) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado");
                return;
            }

            Long tenantId = decodedJWT.getClaim("tenantId").asLong();
            if (tenantId == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Tenant não informado");
                return;
            }

            Long userId = decodedJWT.getClaim("usuarioId").asLong();
            String username = decodedJWT.getSubject();

            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

            var authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            AuthenticatedUser principal = new AuthenticatedUser(
                    userId,
                    username,
                    null,
                    tenantId,
                    roles
            );

            Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarTokenRequisicao(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
            return authorizationHeader.substring(7);
        return null;
    }
}
