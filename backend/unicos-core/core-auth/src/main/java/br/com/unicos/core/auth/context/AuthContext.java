package br.com.unicos.core.auth.context;

import br.com.unicos.core.auth.exception.TokenNotDefinedException;

import java.util.Set;

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
    private static final ThreadLocal<Set<String>> ROLES = new ThreadLocal<>();

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
        String usuarioId = TOKEN.get();
        if (usuarioId == null)
            throw new TokenNotDefinedException();
        return usuarioId;
    }

    public static boolean isTokenDefined() {
        return TOKEN.get() != null;
    }

    /* =========================
       ROLES
       ========================= */

    public static void setRoles(Set<String> roles) {
        ROLES.set(roles);
    }

    public static Set<String> getRoles() {
        return ROLES.get();
    }

    public static boolean hasRole(String role) {
        Set<String> roles = ROLES.get();
        return roles != null && roles.contains(role);
    }

    public static void clear() {
        TOKEN.remove();
        ROLES.remove();
    }
}
