package br.com.unicos.core.usuario.auth.context;

import br.com.unicos.core.usuario.auth.exception.UserNotDefinedException;

public class UserContext {
    private static final ThreadLocal<Long> USUARIO_ID = new ThreadLocal<>();

    private UserContext() {
        // impede instanciação
    }

    public static void setUsuarioId(Long usuarioId) {
        System.out.println("UserContext - setUsuarioId");
        USUARIO_ID.set(usuarioId);
    }

    public static Long getUsuarioId() {
        System.out.println("UserContext - getUsuarioId");
        Long usuarioId = USUARIO_ID.get();
        if (usuarioId == null)
            throw new UserNotDefinedException();
        return usuarioId;
    }

    public static boolean isUsuarioDefined() {
        System.out.println("UserContext - isUsuarioDefined");
        return USUARIO_ID.get() != null;
    }

    public static void clear() {
        System.out.println("UserContext - clear");
        USUARIO_ID.remove();
    }
}
