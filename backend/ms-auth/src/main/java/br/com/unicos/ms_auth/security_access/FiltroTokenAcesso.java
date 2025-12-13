package br.com.unicos.ms_auth.security_access;

import br.com.unicos.ms_auth.model.Usuario;
import br.com.unicos.ms_auth.repository.UsuarioRepository;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FiltroTokenAcesso extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.equals("/v1/autenticacao/login")
                || path.equals("/v1/autenticacao/atualizar-token")
                || path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recuperarTokenRequisicao(request);

        if (token != null) {

            DecodedJWT decodedJWT = tokenService.verificarToken(token);

            String email = decodedJWT.getSubject();

            Usuario usuario = usuarioRepository
                    .findByEmailIgnoreCaseAndEmailVerificadoTrue(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

            String tenantId = decodedJWT.getClaim("tenant_id").asString();

            if (tenantId == null || tenantId.isBlank()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Tenant não informado");
                return;
            }

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            usuario,
                            null,
                            usuario.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarTokenRequisicao(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null)
            return authorizationHeader.replace("Bearer ", "");
        return null;
    }
}