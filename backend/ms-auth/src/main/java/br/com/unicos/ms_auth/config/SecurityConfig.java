package br.com.unicos.ms_auth.config;

import br.com.unicos.ms_auth.model.RoleHierarchyRelation;
import br.com.unicos.ms_auth.repository.RoleHierarchyRelationRepository;
import br.com.unicos.ms_auth.security.FiltroTokenAcesso;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final FiltroTokenAcesso filtroTokenAcesso;

    @Bean
    public SecurityFilterChain filtrosSeguranca(HttpSecurity http) throws Exception {

        String[] SWAGGER_WHITELIST = {
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/webjars/**"
        };

        return http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(
                            "/v1/autenticacao/login",
                            "/v1/autenticacao/atualizar-token"
                    ).permitAll();
                    req.requestMatchers(SWAGGER_WHITELIST).permitAll();
                    req.anyRequest().authenticated();
                })
                .addFilterBefore(filtroTokenAcesso, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public PasswordEncoder encriptador() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public RoleHierarchy hierarquiaPerfis(RoleHierarchyRelationRepository repository) {

        // Busca todas as relações parent > child do banco
        List<RoleHierarchyRelation> relacoes = repository.findAll();

        if (relacoes.isEmpty())
            return RoleHierarchyImpl.fromHierarchy("");

        // Monta dinamicamente a string no formato esperado pelo Spring Security
        StringBuilder builder = new StringBuilder();

        for (RoleHierarchyRelation relacao : relacoes) {
            builder.append(relacao.getParentRole())
                    .append(" > ")
                    .append(relacao.getChildRole())
                    .append("\n");
        }

        return RoleHierarchyImpl.fromHierarchy(builder.toString());
    }

}
