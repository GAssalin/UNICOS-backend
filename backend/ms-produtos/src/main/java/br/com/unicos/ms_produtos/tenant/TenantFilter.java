package br.com.unicos.ms_produtos.tenant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter responsável por resolver o tenant (empresa)
 * a partir da requisição HTTP.
 *
 * <p>
 * O tenant é obtido via header HTTP e armazenado no TenantContext.
 * </p>
 */
@Component
public class TenantFilter extends OncePerRequestFilter {

    private static final String TENANT_ID = "tenant_id";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String empresaIdHeader = request.getHeader(TENANT_ID);

            if (empresaIdHeader == null || empresaIdHeader.isBlank()) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Header tenant_id é obrigatório."
                );
                return;
            }

            Long empresaId = Long.valueOf(empresaIdHeader);
            TenantContext.setEmpresaId(empresaId);

            filterChain.doFilter(request, response);

        } finally {
            TenantContext.clear();
        }
    }
}
