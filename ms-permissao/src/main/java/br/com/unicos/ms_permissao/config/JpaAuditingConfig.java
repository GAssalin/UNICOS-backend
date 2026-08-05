package br.com.unicos.ms_permissao.config;

import br.com.unicos.core.usuario.auth.context.UserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<Long> auditorAware() {
        return () -> {
            if (UserContext.isUsuarioDefined())
                return Optional.of(UserContext.getUsuarioId());
            return Optional.empty();
        };
    }
}

