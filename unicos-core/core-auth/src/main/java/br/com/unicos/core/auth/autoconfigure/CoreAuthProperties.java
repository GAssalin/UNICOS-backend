package br.com.unicos.core.auth.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configurações utilizadas pelos componentes de autenticação do core-auth.
 *
 * <p>Exemplo:</p>
 * <pre>
 * jwt.secret=minha-chave-secreta
 * jwt.issuer=unicos
 * </pre>
 */
@ConfigurationProperties(prefix = "jwt")
public class CoreAuthProperties {

    private String secret;
    private String issuer;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
