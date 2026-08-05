package br.com.unicos.ms_estoque.enums;

import lombok.Getter;

/**
 * Enum que representa o status de processamento de uma movimentação de estoque.
 * <p>
 * Permite controlar o ciclo de vida da movimentação e manter rastreabilidade.
 */
@Getter
public enum StatusMovimentacaoEstoque {

    /**
     * Movimentação criada, mas ainda não processada.
     */
    PENDENTE("Pendente"),

    /**
     * Movimentação processada com sucesso.
     */
    PROCESSADA("Processada"),

    /**
     * Movimentação cancelada.
     */
    CANCELADA("Cancelada"),

    /**
     * Movimentação estornada.
     */
    ESTORNADA("Estornada"),

    /**
     * Movimentação rejeitada por regra de negócio ou integração.
     */
    REJEITADA("Rejeitada");

    private final String descricao;

    StatusMovimentacaoEstoque(String descricao) {
        this.descricao = descricao;
    }
}