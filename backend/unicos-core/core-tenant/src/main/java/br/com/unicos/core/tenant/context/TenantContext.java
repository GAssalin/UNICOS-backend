package br.com.unicos.core.tenant.context;

import br.com.unicos.core.tenant.exception.TenantNotDefinedException;

/**
 * Contexto responsável por armazenar informações do tenant
 * durante o ciclo de vida de uma execução.
 *
 * <p>
 * Implementação baseada em {@link ThreadLocal}, apropriada para aplicações
 * síncronas (Spring MVC). Em cenários reativos (WebFlux), recomenda-se
 * adaptação para Reactor Context.
 * </p>
 */
public final class TenantContext {

    private static final ThreadLocal<Long> EMPRESA_ID = new ThreadLocal<>();

    private TenantContext() {
        // impede instanciação
    }

    public static void setEmpresaId(Long empresaId) {
        System.out.println("TenantContext - setEmpresaId");
        EMPRESA_ID.set(empresaId);
    }

    public static Long getEmpresaId() {
        System.out.println("TenantContext - getEmpresaId");
        Long empresaId = EMPRESA_ID.get();
        if (empresaId == null)
            throw new TenantNotDefinedException();
        return empresaId;
    }

    public static boolean isEmpresaDefined() {
        System.out.println("TenantContext - isEmpresaDefined");
        return EMPRESA_ID.get() != null;
    }

    public static void clear() {
        System.out.println("TenantContext - clear");
        EMPRESA_ID.remove();
    }
}
