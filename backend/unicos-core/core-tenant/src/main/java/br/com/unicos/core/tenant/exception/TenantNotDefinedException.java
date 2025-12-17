package br.com.unicos.core.tenant.exception;

/**
 * Exceção lançada quando o tenant (empresa) não está definido
 * no contexto de execução atual.
 *
 * <p>
 * Em arquitetura multi-tenant, operações de leitura/escrita devem
 * ocorrer sempre associadas a um {@code empresaId}. Quando o tenant
 * não é resolvido (por exemplo, ausência de header/JWT no microserviço),
 * esta exceção sinaliza falha de contexto.
 * </p>
 */
public class TenantNotDefinedException extends IllegalStateException {

    /**
     * Cria a exceção com mensagem padrão.
     */
    public TenantNotDefinedException() {
        super("Empresa (tenant) não definida no contexto.");
    }

    /**
     * Cria a exceção com mensagem customizada.
     *
     * @param message mensagem detalhada
     */
    public TenantNotDefinedException(String message) {
        super(message);
    }
}
