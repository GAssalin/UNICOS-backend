package br.com.unicos.core.auth.interno;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Segredo compartilhado que identifica chamadas originadas dentro da plataforma
 * (gateway e comunicação entre microserviços via Feign).
 *
 * <p>
 * Endpoints {@code /internal/**} só aceitam requisições que apresentem este token no header
 * {@value #HEADER}. O gateway bloqueia esses caminhos para o tráfego externo e nunca repassa
 * o header recebido do cliente.
 * </p>
 */
public class TokenInternoService {

    public static final String HEADER = "X-Internal-Token";

    private final byte[] token;

    public TokenInternoService(String token) {
        this.token = token.getBytes(StandardCharsets.UTF_8);
    }

    public String getToken() {
        return new String(token, StandardCharsets.UTF_8);
    }

    /**
     * Compara o token recebido em tempo constante.
     */
    public boolean isValido(String tokenRecebido) {
        if (tokenRecebido == null || tokenRecebido.isEmpty())
            return false;

        return MessageDigest.isEqual(token, tokenRecebido.getBytes(StandardCharsets.UTF_8));
    }
}
