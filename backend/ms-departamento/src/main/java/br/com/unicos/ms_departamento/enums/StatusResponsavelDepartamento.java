package br.com.unicos.ms_departamento.enums;

import lombok.Getter;

/**
 * Enum que representa o status do vínculo do responsável com o departamento.
 * <p>
 * Permite controlar se o responsável está vigente/ativo para fins de gestão,
 * evitando perda de histórico.
 */
@Getter
public enum StatusResponsavelDepartamento {

    /**
     * Responsável ativo e vigente para o departamento.
     */
    ATIVO("Ativo"),

    /**
     * Responsável inativo (sem vigência/sem atuação atual no departamento).
     */
    INATIVO("Inativo");

    private final String descricao;

    StatusResponsavelDepartamento(String descricao) {
        this.descricao = descricao;
    }
}
