package br.com.unicos.ms_empresa.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de endereço associados à empresa.
 * <p>
 * Utilizado para diferenciar endereços conforme sua finalidade
 * administrativa, fiscal ou operacional.
 */
@Getter
public enum TipoEnderecoEmpresa {

    /**
     * Endereço da sede principal da empresa.
     */
    SEDE("Sede"),

    /**
     * Endereço de uma filial da empresa.
     */
    FILIAL("Filial"),

    /**
     * Endereço utilizado para cobranças.
     */
    COBRANCA("Cobrança"),

    /**
     * Endereço utilizado para fins fiscais.
     */
    FISCAL("Fiscal");

    private final String descricao;

    TipoEnderecoEmpresa(String descricao) {
        this.descricao = descricao;
    }
}
