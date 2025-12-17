package br.com.unicos.core.tenant.context;

import br.com.unicos.core.tenant.exception.TenantNotDefinedException;

/**
 * Contexto responsável por armazenar o identificador da empresa (tenant)
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

    /**
     * Define o tenant (empresa) no contexto atual.
     *
     * @param empresaId identificador da empresa
     */
    public static void setEmpresaId(Long empresaId) {
        EMPRESA_ID.set(empresaId);
    }

    /**
     * Obtém o tenant (empresa) do contexto atual.
     *
     * @return identificador da empresa
     * @throws TenantNotDefinedException se o tenant não estiver definido
     */
    public static Long getEmpresaId() {
        Long empresaId = EMPRESA_ID.get();
        if (empresaId == null)
            throw new TenantNotDefinedException();
        return empresaId;
    }

    /**
     * Verifica se existe tenant definido no contexto atual.
     *
     * @return {@code true} se houver tenant; caso contrário {@code false}
     */
    public static boolean isEmpresaDefined() {
        return EMPRESA_ID.get() != null;
    }

    /**
     * Remove o tenant do contexto atual.
     * <p>
     * Deve ser chamado obrigatoriamente ao final do processamento (ex.: em filter/interceptor).
     * </p>
     */
    public static void clear() {
        EMPRESA_ID.remove();
    }
}
