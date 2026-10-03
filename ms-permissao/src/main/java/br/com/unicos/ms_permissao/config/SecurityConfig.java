package br.com.unicos.ms_permissao.config;

import br.com.unicos.core.web.security.SegurancaPadrao;
import br.com.unicos.ms_permissao.filter.PermissaoRequestFilter;
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
    public SecurityFilterChain filtrosSeguranca(HttpSecurity http, PermissaoRequestFilter permissaoRequestFilter) throws Exception {
        return SegurancaPadrao.configurar(http, permissaoRequestFilter);
    }
}
