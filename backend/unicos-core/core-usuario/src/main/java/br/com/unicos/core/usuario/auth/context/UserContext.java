package br.com.unicos.core.usuario.auth.context;

import br.com.unicos.core.usuario.auth.exception.UserNotDefinedException;

public class UserContext {
    private static final ThreadLocal<Long> USUARIO_ID = new ThreadLocal<>();

    private UserContext() {
        // impede instanciação
    }

    public static void setUsuarioId(Long usuarioId) {
        USUARIO_ID.set(usuarioId);
    }

    public static Long getUsuarioId() {
        Long usuarioId = USUARIO_ID.get();
        if (usuarioId == null)
            throw new UserNotDefinedException();
        return usuarioId;
    }

    public static boolean isUsuarioDefined() {
        return USUARIO_ID.get() != null;
    }

    public static void clear() {
        USUARIO_ID.remove();
    }
}
