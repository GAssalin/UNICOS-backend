package br.com.unicos.ms_pessoas.config;

import br.com.unicos.core.web.security.SegurancaPadrao;
import br.com.unicos.ms_pessoas.filter.PessoaRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filtrosSeguranca(HttpSecurity http, PessoaRequestFilter pessoaRequestFilter) throws Exception {
        return SegurancaPadrao.configurar(http, pessoaRequestFilter, auth -> auth
                // Confirmação de e-mail: o token recebido pelo usuário é a credencial.
                .requestMatchers(HttpMethod.PATCH, "/v1/verificacao-email/confirmar").permitAll());
    }
}
