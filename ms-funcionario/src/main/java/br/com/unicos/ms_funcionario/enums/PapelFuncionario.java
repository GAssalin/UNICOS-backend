package br.com.unicos.ms_funcionario.enums;

import br.com.unicos.core.funcionario.enums.EscopoCarteira;
import lombok.Getter;

/**
 * Papel desempenhado pelos funcionários de um cargo.
 *
 * <p>
 * Define o escopo de acesso à carteira de clientes: vendedores acessam apenas os clientes dos
 * quais são responsáveis; os cargos acima deles (supervisor, gerente, diretor) acessam todos os
 * clientes da empresa. Funções administrativas não restringem a carteira: o acesso aos clientes
 * depende apenas das permissões da role do usuário.
 * </p>
 */
@Getter
public enum PapelFuncionario {

    /**
     * Vendedor com carteira própria de clientes.
     */
    VENDEDOR("Vendedor", EscopoCarteira.PROPRIA),

    /**
     * Supervisor da equipe de vendas.
     */
    SUPERVISOR("Supervisor", EscopoCarteira.TODAS),

    /**
     * Gerente.
     */
    GERENTE("Gerente", EscopoCarteira.TODAS),

    /**
     * Diretor.
     */
    DIRETOR("Diretor", EscopoCarteira.TODAS),

    /**
     * Funções administrativas e operacionais, fora da hierarquia comercial.
     */
    ADMINISTRATIVO("Administrativo", EscopoCarteira.TODAS);

    private final String descricao;
    private final EscopoCarteira escopoCarteira;

    PapelFuncionario(String descricao, EscopoCarteira escopoCarteira) {
        this.descricao = descricao;
        this.escopoCarteira = escopoCarteira;
    }
}
