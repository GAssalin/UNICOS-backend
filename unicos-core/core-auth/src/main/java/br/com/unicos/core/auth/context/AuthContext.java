package br.com.unicos.core.auth.context;

import br.com.unicos.core.auth.exception.TokenNotDefinedException;

/**
 * Contexto responsável por armazenar informações do token
 * durante o ciclo de vida de uma execução.
 *
 * <p>
 * Implementação baseada em {@link ThreadLocal}, apropriada para aplicações
 * síncronas (Spring MVC). Em cenários reativos (WebFlux), recomenda-se
 * adaptação para Reactor Context.
 * </p>
 */
public final class AuthContext {
    private static final ThreadLocal<String> TOKEN = new ThreadLocal<>();

    private AuthContext() {
        // impede instanciação
    }

    /* =========================
       TOKEN
       ========================= */

    public static void setToken(String token) {
        TOKEN.set(token);
    }

    public static String getToken() {
        String token = TOKEN.get();
        if (token == null)
            throw new TokenNotDefinedException();
        return token;
    }

    public static boolean isTokenDefined() {
        return TOKEN.get() != null;
    }

    public static void clear() {
        TOKEN.remove();
    }
}
