package br.com.unicos.ms_departamento.enums;

import lombok.Getter;

/**
 * Enum que representa o status do vínculo entre departamento e filial.
 * <p>
 * Controla se a relação Departamento x Filial está ativa e vigente,
 * impactando lotações, relatórios e permissões associadas.
 */
@Getter
public enum StatusVinculoDepartamentoFilial {

    /**
     * Vínculo ativo e vigente entre o departamento e a filial.
     */
    ATIVO("Ativo"),

    /**
     * Vínculo inativo (encerrado/descontinuado).
     */
    INATIVO("Inativo");

    private final String descricao;

    StatusVinculoDepartamentoFilial(String descricao) {
        this.descricao = descricao;
    }
}
