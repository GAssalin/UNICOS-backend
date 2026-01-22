package br.com.unicos.ms_filial.filter;

import br.com.unicos.core.auth.context.AuthContext;
import br.com.unicos.core.auth.dto.TokenValidationResponse;
import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_filial.client.AuthClient;
import br.com.unicos.ms_filial.client.PermissaoClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
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
public class FilialRequestFilter extends OncePerRequestFilter {

    private final PermissaoClient permissaoClient;
    private final AuthClient authClient;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.startsWith("/internal")
                || path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs");
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

        var roles = Optional.ofNullable(tokenInfo.roles()).orElseGet(java.util.Set::of);

        AuthContext.setToken(authorizationHeader);
        AuthContext.setRoles(roles);
        TenantContext.setEmpresaId(tokenInfo.empresaId());

        String path = request.getServletPath();
        if(path.startsWith("/v1/contatos-filial"))
            verificarPermissao(request.getMethod(), "FILIAL_CONTATO_");
        else if(path.startsWith("/v1/enderecos-filial"))
            verificarPermissao(request.getMethod(), "FILIAL_ENDERECO_");
        else if(path.startsWith("/v1/filiais"))
            verificarPermissao(request.getMethod(), "FILIAL_");
        else if(path.startsWith("/v1/filiais-parametros"))
            verificarPermissao(request.getMethod(), "FILIAL_PARAMETRO_");
        else if(path.startsWith("/v1/filiais-status-historico"))
            verificarPermissao(request.getMethod(), "FILIAL_STATUS_HISTORICO_");
        else if(path.startsWith("/v1/horarios-filial"))
            verificarPermissao(request.getMethod(), "FILIAL_HORARIO_");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                tokenInfo.usuarioId(),
                null,
                roles.stream().map(SimpleGrantedAuthority::new).toList()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void verificarPermissao(String metodo, String inicioEnpoint) {
        boolean permitido = switch (metodo) {
            case "GET" -> permissaoClient.usuarioPossuiPermissao(inicioEnpoint.concat("LISTAR"));
            case "POST" -> permissaoClient.usuarioPossuiPermissao(inicioEnpoint.concat("CRIAR"));
            case "PUT" -> permissaoClient.usuarioPossuiPermissao(inicioEnpoint.concat("EDITAR"));
            case "DELETE" -> permissaoClient.usuarioPossuiPermissao(inicioEnpoint.concat("EXCLUIR"));
            default -> true;
        };

        if(!permitido)
            throw new AccessDeniedException("Usuário não possui permissão.");
    }
}
