package br.com.unicos.ms_ativos.enums;

import lombok.Getter;

/**
 * Enum que define os tipos de documentos vinculados a um ativo.
 */
@Getter
public enum TipoDocumentoAtivo {

    /**
     * Nota fiscal de compra do ativo.
     */
    NOTA_FISCAL("Nota fiscal de aquisição do ativo"),

    /**
     * Certificado ou termo de garantia.
     */
    GARANTIA("Certificado ou termo de garantia do ativo"),

    /**
     * Laudo técnico de avaliação ou vistoria.
     */
    LAUDO_TECNICO("Laudo técnico ou relatório de vistoria"),

    /**
     * Certificado de calibração, conformidade, segurança, etc.
     */
    CERTIFICADO("Certificado técnico do ativo"),

    /**
     * Outro tipo de documento não classificado.
     */
    OUTRO("Outro tipo de documento relacionado ao ativo");

    private final String descricao;

    TipoDocumentoAtivo(String descricao) {
        this.descricao = descricao;
    }
}
