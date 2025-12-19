package br.com.unicos.core.tenant.context;

import br.com.unicos.core.tenant.exception.TenantNotDefinedException;
import br.com.unicos.core.tenant.exception.UsuarioNotDefinedException;

import java.util.Set;

/**
 * Contexto responsável por armazenar informações do usuário e do tenant
 * durante o ciclo de vida de uma execução.
 *
 * <p>
 * Implementação baseada em {@link ThreadLocal}, apropriada para aplicações
 * síncronas (Spring MVC). Em cenários reativos (WebFlux), recomenda-se
 * adaptação para Reactor Context.
 * </p>
 */
public final class TenantContext {

    private static final ThreadLocal<Long> USUARIO_ID = new ThreadLocal<>();
    private static final ThreadLocal<Long> EMPRESA_ID = new ThreadLocal<>();
    private static final ThreadLocal<Set<String>> ROLES = new ThreadLocal<>();

    private TenantContext() {
        // impede instanciação
    }

    /* =========================
       USUÁRIO
       ========================= */

    public static void setUsuarioId(Long usuarioId) {
        USUARIO_ID.set(usuarioId);
    }

    public static Long getUsuarioId() {
        Long usuarioId = USUARIO_ID.get();
        if (usuarioId == null)
            throw new UsuarioNotDefinedException();
        return usuarioId;
    }

    public static boolean isUsuarioDefined() {
        return USUARIO_ID.get() != null;
    }

    /* =========================
       TENANT
       ========================= */

    public static void setEmpresaId(Long empresaId) {
        EMPRESA_ID.set(empresaId);
    }

    public static Long getEmpresaId() {
        Long empresaId = EMPRESA_ID.get();
        if (empresaId == null)
            throw new TenantNotDefinedException();
        return empresaId;
    }

    public static boolean isEmpresaDefined() {
        return EMPRESA_ID.get() != null;
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

    /* =========================
       LIMPEZA
       ========================= */

    public static void clear() {
        USUARIO_ID.remove();
        EMPRESA_ID.remove();
        ROLES.remove();
    }
}
