package br.com.unicos.core.auth.autoconfigure;

import br.com.unicos.core.auth.service.TokenCoreService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuração dos componentes disponibilizados pelo core-auth.
 */
@AutoConfiguration
@ConditionalOnClass(TokenCoreService.class)
@EnableConfigurationProperties(CoreAuthProperties.class)
public class CoreAuthAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TokenCoreService tokenCoreService(CoreAuthProperties properties) {
        return new TokenCoreService(
                requiredProperty(properties.getSecret(), "jwt.secret"),
                requiredProperty(properties.getIssuer(), "jwt.issuer")
        );
    }

    private String requiredProperty(String value, String propertyName) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "A propriedade obrigatória '" + propertyName + "' não foi configurada."
            );
        }
        return value;
    }
}
