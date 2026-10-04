package br.com.unicos.core.funcionario.enums;

/**
 * Clientes que um usuário pode acessar, definido pelo papel do funcionário vinculado a ele.
 *
 * <p>
 * O escopo restringe quais clientes são visíveis; as ações permitidas continuam sendo
 * definidas pelas permissões da role do usuário.
 * </p>
 */
public enum EscopoCarteira {

    /**
     * Apenas os clientes dos quais o usuário é o vendedor responsável.
     */
    PROPRIA,

    /**
     * Todos os clientes da empresa.
     */
    TODAS
}
