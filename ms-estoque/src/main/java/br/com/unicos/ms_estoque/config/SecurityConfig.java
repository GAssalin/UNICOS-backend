package br.com.unicos.ms_estoque.config;

import br.com.unicos.core.web.security.SegurancaPadrao;
import br.com.unicos.ms_estoque.filter.EstoqueRequestFilter;
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
    public SecurityFilterChain filtrosSeguranca(HttpSecurity http, EstoqueRequestFilter estoqueRequestFilter) throws Exception {
        return SegurancaPadrao.configurar(http, estoqueRequestFilter);
    }
}
