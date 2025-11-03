package br.com.unicos.ms_empresa.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de endereços vinculados a uma empresa.
 * <p>
 * Cada tipo define uma finalidade específica dentro da estrutura corporativa,
 * podendo ser utilizado para fins administrativos, fiscais e logísticos.
 */
@Getter
public enum TipoEnderecoEmpresa {

    /**
     * Endereço principal da empresa, utilizado como referência oficial.
     */
    MATRIZ("Matriz"),

    /**
     * Endereço de uma filial da empresa, responsável por operações regionais.
     */
    FILIAL("Filial"),

    /**
     * Endereço utilizado para emissão de notas fiscais e documentos de cobrança.
     */
    FATURAMENTO("Faturamento"),

    /**
     * Endereço destinado à entrega de mercadorias ou materiais.
     */
    ENTREGA("Entrega"),

    /**
     * Endereço utilizado exclusivamente para correspondências e cobranças.
     */
    COBRANCA("Cobrança");

    private final String descricao;

    TipoEnderecoEmpresa(String descricao) {
        this.descricao = descricao;
    }
}
