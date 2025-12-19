package br.com.unicos.ms_auth.config;

import br.com.unicos.ms_auth.security_access.AuthenticatedUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<Long> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(AuthenticatedUser.class::isInstance)
                .map(AuthenticatedUser.class::cast)
                .map(AuthenticatedUser::getUserId);
    }
}
