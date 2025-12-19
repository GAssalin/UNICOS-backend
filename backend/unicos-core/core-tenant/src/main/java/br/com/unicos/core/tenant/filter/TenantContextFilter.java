package br.com.unicos.core.tenant.filter;

import br.com.unicos.core.tenant.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

public class TenantContextFilter extends OncePerRequestFilter {

    private static final String USUARIO_HEADER = "X-Usuario-Id";
    private static final String TENANT_HEADER = "X-Tenant-Id";
    private static final String ROLES_HEADER = "X-Roles";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/internal/")
                || path.startsWith("/v1/autenticacao/login")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String usuarioId = request.getHeader(USUARIO_HEADER);
            String tenantId = request.getHeader(TENANT_HEADER);
            String roles = request.getHeader(ROLES_HEADER);

            if (usuarioId != null)
                TenantContext.setUsuarioId(Long.valueOf(usuarioId));

            if (tenantId != null)
                TenantContext.setEmpresaId(Long.valueOf(tenantId));

            if (roles != null)
                TenantContext.setRoles(Set.of(roles.split(",")));

            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
