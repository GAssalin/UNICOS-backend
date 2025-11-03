package br.com.unicos.ms_ativos.enums;

import lombok.Getter;

/**
 * Enum que representa o status atual de um ativo dentro da empresa.
 * <p>
 * Indica a condição operacional ou patrimonial do bem.
 */
@Getter
public enum StatusAtivo {

    /**
     * Ativo em uso normal nas operações da empresa.
     */
    ATIVO("Ativo"),

    /**
     * Ativo temporariamente indisponível devido à manutenção.
     */
    EM_MANUTENCAO("Em manutenção"),

    /**
     * Ativo baixado do patrimônio, sem utilização futura prevista.
     */
    BAIXADO("Baixado"),

    /**
     * Ativo transferido para outra unidade, filial ou empresa.
     */
    TRANSFERIDO("Transferido"),

    /**
     * Ativo vendido e removido do patrimônio.
     */
    VENDIDO("Vendido");

    private final String descricao;

    StatusAtivo(String descricao) {
        this.descricao = descricao;
    }
}
