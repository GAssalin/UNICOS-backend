package br.com.unicos.ms_empresa.enums;

import lombok.Getter;

/**
 * Enum que representa o tipo estrutural da empresa no UniCoS.
 * <p>
 * Utilizado para diferenciar a empresa principal (matriz)
 * de unidades subordinadas (filiais).
 */
@Getter
public enum TipoEmpresa {

    /**
     * Empresa principal do grupo empresarial.
     */
    MATRIZ("Matriz"),

    /**
     * Unidade subordinada à empresa matriz.
     */
    FILIAL("Filial");

    private final String descricao;

    TipoEmpresa(String descricao) {
        this.descricao = descricao;
    }
}
