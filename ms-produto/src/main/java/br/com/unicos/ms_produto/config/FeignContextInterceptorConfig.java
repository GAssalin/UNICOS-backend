package br.com.unicos.ms_produto.config;

import br.com.unicos.core.auth.context.AuthContext;
import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.usuario.auth.context.UserContext;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class FeignContextInterceptorConfig {

    @Bean
    public RequestInterceptor contextForwardingInterceptor() {
        return template -> {
            if (UserContext.isUsuarioDefined()) {
                template.header(
                        "X-Usuario-Id",
                        UserContext.getUsuarioId().toString()
                );
            }

            if (TenantContext.isEmpresaDefined()) {
                template.header(
                        "X-Tenant-Id",
                        TenantContext.getEmpresaId().toString()
                );
            }

            if (AuthContext.isTokenDefined()) {
                template.header(
                        HttpHeaders.AUTHORIZATION,
                        AuthContext.getToken()
                );
            }
        };
    }
}