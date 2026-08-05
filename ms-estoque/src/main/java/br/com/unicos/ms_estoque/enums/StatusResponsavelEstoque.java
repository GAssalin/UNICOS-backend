package br.com.unicos.ms_estoque.enums;

import lombok.Getter;

/**
 * Enum que representa o status do vínculo do responsável com o estoque.
 * <p>
 * Permite controlar se o responsável está vigente/ativo para fins de gestão,
 * evitando perda de histórico.
 */
@Getter
public enum StatusResponsavelEstoque {

    /**
     * Responsável ativo e vigente para o estoque.
     */
    ATIVO("Ativo"),

    /**
     * Responsável inativo (sem vigência/sem atuação atual no estoque).
     */
    INATIVO("Inativo");

    private final String descricao;

    StatusResponsavelEstoque(String descricao) {
        this.descricao = descricao;
    }
}
