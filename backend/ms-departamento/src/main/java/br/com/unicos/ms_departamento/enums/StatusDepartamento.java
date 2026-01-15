package br.com.unicos.ms_departamento.enums;

import lombok.Getter;

/**
 * Enum que representa o status do departamento.
 * <p>
 * Controla a disponibilidade do departamento dentro do sistema,
 * impactando rotinas de cadastro, vinculações e integrações.
 */
@Getter
public enum StatusDepartamento {

    /**
     * Departamento ativo e disponível para uso.
     */
    ATIVO("Ativo"),

    /**
     * Departamento inativo (não deve ser usado em novas vinculações).
     */
    INATIVO("Inativo");

    private final String descricao;

    StatusDepartamento(String descricao) {
        this.descricao = descricao;
    }
}
