package br.com.unicos.core.tenant.context;

/**
 * Facade utilitária para acesso ao tenant atual.
 *
 * <p>
 * Centraliza o ponto de leitura do {@code empresaId} permite que os microserviços
 * dependam de um único ponto de acesso ao contexto.
 * </p>
 */
public final class TenantContextHolder {

    private TenantContextHolder() {
        // impede instanciação
    }

    /**
     * Recupera o usuário atual.
     *
     * @return identificador do usuário
     */
    public static Long usuarioId() {
        return TenantContext.getUsuarioId();
    }

    /**
     * Verifica se há usuário definido.
     *
     * @return {@code true} se houver usuário; caso contrário {@code false}
     */
    public static boolean isUsuarioDefined() {
        return TenantContext.isUsuarioDefined();
    }

    /**
     * Recupera o tenant atual.
     *
     * @return identificador da empresa (tenant)
     */
    public static Long empresaId() {
        return TenantContext.getEmpresaId();
    }

    /**
     * Verifica se há tenant definido.
     *
     * @return {@code true} se houver tenant; caso contrário {@code false}
     */
    public static boolean isEmpresaDefined() {
        return TenantContext.isEmpresaDefined();
    }
}
