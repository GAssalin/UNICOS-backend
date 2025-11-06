package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Enum que representa o status da proposta de um fornecedor em uma cotação.
 */
@Getter
public enum StatusFornecedorCotacao {

    /**
     * Proposta aguardando avaliação do comprador.
     */
    AGUARDANDO_AVALIACAO("Proposta enviada e aguardando análise."),

    /**
     * Proposta aceita pelo comprador.
     */
    APROVADA("Proposta aprovada pelo comprador."),

    /**
     * Proposta recusada.
     */
    RECUSADA("Proposta rejeitada durante o processo de cotação."),

    /**
     * Fornecedor não respondeu à cotação.
     */
    SEM_RESPOSTA("Fornecedor não enviou proposta dentro do prazo.");

    private final String descricao;

    StatusFornecedorCotacao(String descricao) {
        this.descricao = descricao;
    }
}
