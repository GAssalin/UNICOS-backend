package br.com.unicos.core.usuario.auth.exception;

/**
 * Exceção lançada quando o usuario não está definido
 * no contexto de execução atual.
 *
 * <p>
 * Em arquitetura multi-tenant, operações de leitura/escrita devem
 * ocorrer sempre associadas a um {@code usuario}. Quando o usuario
 * não é resolvido (por exemplo, ausência de header/JWT no microserviço),
 * esta exceção sinaliza falha de contexto.
 * </p>
 */
public class UserNotDefinedException extends IllegalStateException {

    /**
     * Cria a exceção com mensagem padrão.
     */
    public UserNotDefinedException() {
        super("Usuário não definido no contexto.");
    }

    /**
     * Cria a exceção com mensagem customizada.
     *
     * @param message mensagem detalhada
     */
    public UserNotDefinedException(String message) {
        super(message);
    }
}
