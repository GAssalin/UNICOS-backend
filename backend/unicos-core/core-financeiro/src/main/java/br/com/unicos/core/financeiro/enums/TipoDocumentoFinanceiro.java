package br.com.unicos.core.financeiro.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de documentos financeiros.
 * <p>
 * Indica o tipo de comprovante ou documento vinculado a um lançamento financeiro.
 */
@Getter
public enum TipoDocumentoFinanceiro {

    /**
     * Documento fiscal emitido para registro de operação comercial.
     */
    NOTA_FISCAL("Nota Fiscal"),

    /**
     * Documento utilizado para formalizar o recebimento de valores.
     */
    RECIBO("Recibo"),

    /**
     * Documento utilizado para cobrança bancária.
     */
    BOLETO("Boleto"),

    /**
     * Documento de cobrança consolidando diversas operações.
     */
    FATURA("Fatura"),

    /**
     * Documento que comprova o pagamento realizado.
     */
    COMPROVANTE("Comprovante de Pagamento"),

    /**
     * Documento genérico não classificado nos demais tipos.
     */
    OUTRO("Outro");

    private final String descricao;

    TipoDocumentoFinanceiro(String descricao) {
        this.descricao = descricao;
    }

}
