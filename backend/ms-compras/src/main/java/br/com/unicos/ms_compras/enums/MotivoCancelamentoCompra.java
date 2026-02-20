package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Motivos padronizados para cancelamento de pedidos de compra.
 */
@Getter
public enum MotivoCancelamentoCompra {

    /**
     * Erro no lançamento do pedido.
     */
    ERRO_LANCAMENTO("Erro de lançamento"),

    /**
     * Alteração da necessidade da empresa.
     */
    ALTERACAO_DEMANDA("Alteração de demanda"),

    /**
     * Fornecedor não conseguiu atender o pedido.
     */
    FORNECEDOR_INDISPONIVEL("Fornecedor indisponível"),

    /**
     * Preço não aprovado.
     */
    PRECO_INVIAVEL("Preço inviável"),

    /**
     * Pedido duplicado.
     */
    DUPLICIDADE("Duplicidade"),

    /**
     * Outros motivos não catalogados.
     */
    OUTROS("Outros");

    private final String descricao;

    MotivoCancelamentoCompra(String descricao) {
        this.descricao = descricao;
    }
}
