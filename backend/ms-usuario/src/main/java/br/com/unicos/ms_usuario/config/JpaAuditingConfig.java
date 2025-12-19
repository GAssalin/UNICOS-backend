package br.com.unicos.ms_usuario.config;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_usuario.model.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<Long> auditorAware() {
        return () -> {
            if (TenantContext.isUsuarioDefined())
                return Optional.of(TenantContext.getUsuarioId());
            return Optional.empty();
        };
    }
}

