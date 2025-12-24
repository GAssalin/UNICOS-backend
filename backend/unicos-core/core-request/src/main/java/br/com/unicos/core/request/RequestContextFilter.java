package br.com.unicos.core.request;

import br.com.unicos.core.auth.AuthContextInitializer;
import br.com.unicos.core.auth.context.AuthContext;
import br.com.unicos.core.tenant.TenantContextInitializer;
import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.usuario.auth.UserContextInitializer;
import br.com.unicos.core.usuario.auth.context.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class RequestContextFilter extends OncePerRequestFilter {

    private final TenantContextInitializer tenantInitializer;
    private final AuthContextInitializer authInitializer;
    private final UserContextInitializer userInitializer;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
            System.out.println("RequestContextFilter");

            tenantInitializer.initialize(request);
            authInitializer.initialize(request);
            userInitializer.initialize(request);

            filterChain.doFilter(request, response);

        } finally {
            TenantContext.clear();
            AuthContext.clear();
            UserContext.clear();
        }
    }
}