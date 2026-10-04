package br.com.unicos.core.auth.autoconfigure;

import br.com.unicos.core.auth.interno.TokenInternoService;
import br.com.unicos.core.auth.service.TokenCoreService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuração dos componentes disponibilizados pelo core-auth.
 */
@AutoConfiguration
@ConditionalOnClass(TokenCoreService.class)
@EnableConfigurationProperties({CoreAuthProperties.class, TokenInternoProperties.class})
public class CoreAuthAutoConfiguration {

    /**
     * Tamanho mínimo exigido para a chave HMAC-SHA256 (256 bits).
     */
    static final int TAMANHO_MINIMO_SEGREDO_JWT = 32;
    static final int TAMANHO_MINIMO_TOKEN_INTERNO = 16;

    @Bean
    @ConditionalOnMissingBean
    public TokenCoreService tokenCoreService(CoreAuthProperties properties) {
        return new TokenCoreService(
                requiredProperty(properties.getSecret(), "jwt.secret", TAMANHO_MINIMO_SEGREDO_JWT),
                requiredProperty(properties.getIssuer(), "jwt.issuer", 1)
        );
    }

    /**
     * Criado apenas quando {@code security.internal.token} está configurado: o gateway não precisa
     * do segredo, enquanto os microserviços dependem deste bean e falham na inicialização sem ele.
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "security.internal", name = "token")
    public TokenInternoService tokenInternoService(TokenInternoProperties properties) {
        return new TokenInternoService(
                requiredProperty(properties.getToken(), "security.internal.token", TAMANHO_MINIMO_TOKEN_INTERNO)
        );
    }

    private String requiredProperty(String value, String propertyName, int tamanhoMinimo) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "A propriedade obrigatória '" + propertyName + "' não foi configurada."
            );
        }

        if (value.length() < tamanhoMinimo) {
            throw new IllegalStateException(
                    "A propriedade '" + propertyName + "' deve possuir pelo menos " + tamanhoMinimo + " caracteres."
            );
        }

        return value;
    }
}
