package br.com.unicos.ms_departamento.enums;

import lombok.Getter;

/**
 * Enum que representa o tipo de atuação do departamento em uma filial.
 * <p>
 * Indica se o departamento opera localmente, de forma corporativa
 * (centralizada) ou compartilhada entre unidades.
 */
@Getter
public enum TipoAtuacaoDepartamento {

    /**
     * Atuação restrita à filial vinculada.
     */
    LOCAL("Local"),

    /**
     * Atuação corporativa (centralizada), normalmente vinculada à matriz/sede.
     */
    CORPORATIVO("Corporativo"),

    /**
     * Atuação compartilhada entre filiais (ex.: time regional atendendo várias unidades).
     */
    COMPARTILHADO("Compartilhado");

    private final String descricao;

    TipoAtuacaoDepartamento(String descricao) {
        this.descricao = descricao;
    }
}
