package br.com.unicos.ms_produtos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ms-produtos/v1/produtos/**").authenticated()
                        .requestMatchers("/ms-produtos/v1/categorias/**").authenticated()
                        .requestMatchers("/ms-produtos/v1/atributos-personalizados/**").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.disable());

        return http.build();
    }

}
