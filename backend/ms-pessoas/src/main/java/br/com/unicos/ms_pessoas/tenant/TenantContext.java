package br.com.unicos.ms_pessoas.tenant;

/**
 * Contexto responsável por armazenar o identificador
 * da empresa (tenant) durante o ciclo de vida da requisição.
 *
 * <p>
 * Utiliza ThreadLocal para garantir isolamento entre requisições
 * concorrentes.
 * </p>
 */
public final class TenantContext {

    private static final ThreadLocal<Long> EMPRESA_ID = new ThreadLocal<>();

    private TenantContext() {
        // impede instanciação
    }

    /**
     * Define o ID da empresa (tenant) no contexto atual.
     *
     * @param empresaId ID da empresa
     */
    public static void setEmpresaId(Long empresaId) {
        EMPRESA_ID.set(empresaId);
    }

    /**
     * Recupera o ID da empresa (tenant) do contexto atual.
     *
     * @return ID da empresa
     * @throws IllegalStateException se não houver tenant definido
     */
    public static Long getEmpresaId() {
        Long empresaId = EMPRESA_ID.get();
        if (empresaId == null)
            throw new IllegalStateException("Empresa (tenant) não definida no contexto.");

        return empresaId;
    }

    /**
     * Remove o tenant do contexto atual.
     * <p>
     * Deve ser chamado obrigatoriamente ao final da requisição.
     * </p>
     */
    public static void clear() {
        EMPRESA_ID.remove();
    }
}
