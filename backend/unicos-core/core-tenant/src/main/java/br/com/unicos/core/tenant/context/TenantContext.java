package br.com.unicos.core.tenant.context;

import br.com.unicos.core.tenant.exception.TenantNotDefinedException;
import br.com.unicos.core.tenant.exception.UsuarioNotDefinedException;

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

    private static final ThreadLocal<Long> USUARIO_ID = new ThreadLocal<>();
    private static final ThreadLocal<Long> EMPRESA_ID = new ThreadLocal<>();

    private TenantContext() {
        // impede instanciação
    }

    /**
     * Define o usuário no contexto atual.
     *
     * @param usuarioId identificador do usuário
     */
    public static void setUsuarioId(Long usuarioId) {
        USUARIO_ID.set(usuarioId);
    }

    /**
     * Obtém o usuário do contexto atual.
     *
     * @return identificador do usuário
     * @throws UsuarioNotDefinedException se o usuário não estiver definido
     */
    public static Long getUsuarioId() {
        Long usuarioId = USUARIO_ID.get();
        if (usuarioId == null)
            throw new UsuarioNotDefinedException();
        return usuarioId;
    }

    /**
     * Verifica se existe usuário definido no contexto atual.
     *
     * @return {@code true} se houver usuário; caso contrário {@code false}
     */
    public static boolean isUsuarioDefined() {
        return USUARIO_ID.get() != null;
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
        USUARIO_ID.remove();
        EMPRESA_ID.remove();
    }
}
