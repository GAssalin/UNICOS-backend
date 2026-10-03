package br.com.unicos.core.auth.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuração do token interno compartilhado entre gateway e microserviços.
 *
 * <pre>
 * security.internal.token=${UNICOS_INTERNAL_TOKEN}
 * </pre>
 */
@ConfigurationProperties(prefix = "security.internal")
public class TokenInternoProperties {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
