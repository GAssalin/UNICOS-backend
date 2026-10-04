package br.com.unicos.core.web.security;

import br.com.unicos.core.web.error.RespostaProblema;
import br.com.unicos.core.web.filter.ContextoRequisicaoFilter;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuração de segurança padrão dos microserviços: API stateless, sem sessão, CSRF,
 * form login ou HTTP Basic, com respostas de erro em Problem Details.
 *
 * <p>São públicos apenas health/info do Actuator, a documentação OpenAPI e {@code /error}.
 * Endpoints {@code /internal/**} passam pelo {@link ContextoRequisicaoFilter}, que exige o token interno.</p>
 *
 * <pre>
 * &#64;Bean
 * SecurityFilterChain filtrosSeguranca(HttpSecurity http, MeuRequestFilter filter) throws Exception {
 *     return SegurancaPadrao.configurar(http, filter, auth -> auth
 *             .requestMatchers(HttpMethod.PATCH, "/v1/rota-publica").permitAll());
 * }
 * </pre>
 */
public final class SegurancaPadrao {

    public static final String[] CAMINHOS_PUBLICOS = {
            "/actuator/health",
            "/actuator/health/**",
            "/actuator/info",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/error"
    };

    private SegurancaPadrao() {
    }

    public static SecurityFilterChain configurar(HttpSecurity http, ContextoRequisicaoFilter filter) throws Exception {
        return configurar(http, filter, Customizer.withDefaults());
    }

    /**
     * @param regrasAdicionais regras aplicadas antes da regra final {@code anyRequest().authenticated()}
     */
    public static SecurityFilterChain configurar(
            HttpSecurity http,
            ContextoRequisicaoFilter filter,
            Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> regrasAdicionais
    ) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler()))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(CAMINHOS_PUBLICOS).permitAll();
                    auth.requestMatchers("/internal/**").permitAll();
                    regrasAdicionais.customize(auth);
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    public static AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, ex) -> RespostaProblema.escrever(
                request, response, HttpStatus.UNAUTHORIZED.value(), "Autenticação necessária.");
    }

    public static AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> RespostaProblema.escrever(
                request, response, HttpStatus.FORBIDDEN.value(), "Acesso negado a este recurso.");
    }
}
