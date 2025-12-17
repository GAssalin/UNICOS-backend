package br.com.unicos.core.tenant.filter;

import br.com.unicos.core.tenant.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TenantContextFilter extends OncePerRequestFilter {

    private static final String USUARIO_HEADER = "X-Usuario-Id";
    private static final String TENANT_HEADER = "X-Tenant-Id";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();

        return path.startsWith("/v1/autenticacao/login")
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

        String usuarioId = request.getHeader(USUARIO_HEADER);
        String tenantId = request.getHeader(TENANT_HEADER);

        if (usuarioId == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Usuário não informado");
            return;
        }

        if (tenantId == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Tenant não informado");
            return;
        }

        try {
            TenantContext.setUsuarioId(Long.valueOf(usuarioId));
            TenantContext.setEmpresaId(Long.valueOf(tenantId));
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
