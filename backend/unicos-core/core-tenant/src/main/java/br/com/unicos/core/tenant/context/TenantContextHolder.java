package br.com.unicos.core.tenant.context;

import java.util.Set;

/**
 * Facade utilitária para acesso ao tenant atual.
 *
 * <p>
 * Centraliza o ponto de leitura do {@code empresaId} permite que os microserviços
 * dependam de um único ponto de acesso ao contexto.
 * </p>
 */
public final class TenantContextHolder {

    private TenantContextHolder() {}

    public static Long usuarioId() {
        return TenantContext.getUsuarioId();
    }

    public static boolean isUsuarioDefined() {
        return TenantContext.isUsuarioDefined();
    }

    public static Long empresaId() {
        return TenantContext.getEmpresaId();
    }

    public static boolean isEmpresaDefined() {
        return TenantContext.isEmpresaDefined();
    }

    public static Set<String> roles() {
        return TenantContext.getRoles();
    }

    public static boolean hasRole(String role) {
        return TenantContext.hasRole(role);
    }
}
