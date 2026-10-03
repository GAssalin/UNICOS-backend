package br.com.unicos.ms_cliente.config;

import br.com.unicos.core.web.security.SegurancaPadrao;
import br.com.unicos.ms_cliente.filter.ClienteRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filtrosSeguranca(HttpSecurity http, ClienteRequestFilter clienteRequestFilter) throws Exception {
        return SegurancaPadrao.configurar(http, clienteRequestFilter);
    }
}
