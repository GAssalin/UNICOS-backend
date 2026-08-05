package br.com.unicos.core.auth.exception;

/**
 * Exceção lançada quando o token não está definido
 * no contexto de execução atual.
 *
 * <p>
 * Em arquitetura multi-tenant, operações de leitura/escrita devem
 * ocorrer sempre associadas a um {@code token}. Quando o token
 * não é resolvido (por exemplo, ausência de header/JWT no microserviço),
 * esta exceção sinaliza falha de contexto.
 * </p>
 */
public class TokenNotDefinedException extends IllegalStateException {

    /**
     * Cria a exceção com mensagem padrão.
     */
    public TokenNotDefinedException() {
        super("Token não definido no contexto.");
    }

    /**
     * Cria a exceção com mensagem customizada.
     *
     * @param message mensagem detalhada
     */
    public TokenNotDefinedException(String message) {
        super(message);
    }
}
