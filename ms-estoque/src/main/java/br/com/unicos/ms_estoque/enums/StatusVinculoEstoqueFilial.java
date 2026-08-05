package br.com.unicos.ms_estoque.enums;

import lombok.Getter;

/**
 * Enum que representa o status do vínculo entre estoque e filial.
 * <p>
 * Controla se a relação Estoque x Filial está ativa e vigente,
 * impactando lotações, relatórios e permissões associadas.
 */
@Getter
public enum StatusVinculoEstoqueFilial {

    /**
     * Vínculo ativo e vigente entre o estoque e a filial.
     */
    ATIVO("Ativo"),

    /**
     * Vínculo inativo (encerrado/descontinuado).
     */
    INATIVO("Inativo");

    private final String descricao;

    StatusVinculoEstoqueFilial(String descricao) {
        this.descricao = descricao;
    }
}
