package br.com.unicos.ms_estoque.enums;

import lombok.Getter;

/**
 * Enum que representa o status do estoque.
 * <p>
 * Controla a disponibilidade do estoque dentro do sistema,
 * impactando rotinas de cadastro, vinculações e integrações.
 */
@Getter
public enum StatusEstoque {

    /**
     * Estoque ativo e disponível para uso.
     */
    ATIVO("Ativo"),

    /**
     * Estoque inativo (não deve ser usado em novas vinculações).
     */
    INATIVO("Inativo");

    private final String descricao;

    StatusEstoque(String descricao) {
        this.descricao = descricao;
    }
}
