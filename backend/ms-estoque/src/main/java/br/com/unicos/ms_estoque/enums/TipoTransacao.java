package br.com.unicos.ms_estoque.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos possíveis de transações de estoque.
 *
 * Utilizado para identificar a natureza de cada movimentação registrada,
 * seja entrada, saída, transferência ou ajuste de saldo.
 */
@Getter
public enum TipoTransacao {

    /**
     * Representa a entrada de produtos no estoque,
     * normalmente originada de compras, devoluções ou produções internas.
     */
    ENTRADA("Entrada de produtos no estoque"),

    /**
     * Representa a saída de produtos do estoque,
     * comum em processos de venda, consumo interno ou perdas.
     */
    SAIDA("Saída de produtos do estoque"),

    /**
     * Representa a movimentação de produtos entre locais de estoque diferentes.
     */
    TRANSFERENCIA("Transferência entre locais de estoque"),

    /**
     * Representa ajustes manuais realizados para corrigir diferenças de inventário.
     */
    AJUSTE("Ajuste manual de estoque");

    private final String descricao;

    TipoTransacao(String descricao) {
        this.descricao = descricao;
    }
}
