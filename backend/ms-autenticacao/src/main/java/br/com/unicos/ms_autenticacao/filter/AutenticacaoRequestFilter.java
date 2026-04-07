package br.com.unicos.ms_autenticacao.filter;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.usuario.auth.context.UserContext;
import br.com.unicos.ms_autenticacao.model.AuthenticatedUser;
import br.com.unicos.ms_autenticacao.service.TokenService;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AutenticacaoRequestFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.equals("/v1/autenticacao/login")
                || path.equals("/v1/autenticacao/atualizar-token")
                || path.equals("/internal/autenticacao/validate-token")
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

            AuthenticatedUser principal = new AuthenticatedUser(
                    userId,
                    username,
                    null,
                    tenantId
            );

            TenantContext.setEmpresaId(tenantId);
            UserContext.setUsuarioId(userId);

            Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null);

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
