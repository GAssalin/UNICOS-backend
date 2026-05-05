package br.com.unicos.ms_cliente.config;

import br.com.unicos.core.auth.context.AuthContext;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class FeignAuthInterceptorConfig {

    @Bean
    public RequestInterceptor authForwardingInterceptor() {
        return template -> {
            if (AuthContext.isTokenDefined())
                template.header(HttpHeaders.AUTHORIZATION, AuthContext.getToken());
        };
    }
}
