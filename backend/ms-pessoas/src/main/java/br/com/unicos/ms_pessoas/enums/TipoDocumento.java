package br.com.unicos.ms_pessoas.enums;

import lombok.Getter;

/**
 * Enum que define os tipos de documentos que uma pessoa pode possuir.
 */
@Getter
public enum TipoDocumento {

    /**
     * Cadastro de Pessoa Física.
     */
    CPF("CPF"),

    /**
     * Registro Geral de Identidade.
     */
    RG("RG"),

    /**
     * Carteira Nacional de Habilitação.
     */
    CNH("CNH"),

    /**
     * Cadastro Nacional da Pessoa Jurídica.
     */
    CNPJ("CNPJ"),

    /**
     * Inscrição Estadual.
     */
    INSCRICAO_ESTADUAL("Inscrição Estadual"),

    /**
     * Inscrição Municipal.
     */
    INSCRICAO_MUNICIPAL("Inscrição Municipal"),

    /**
     * Passaporte.
     */
    PASSAPORTE("Passaporte"),

    /**
     * Certidão de nascimento ou casamento.
     */
    CERTIDAO("Certidão"),

    /**
     * Outro documento não listado.
     */
    OUTRO("Outro");

    private final String descricao;

    TipoDocumento(String descricao) {
        this.descricao = descricao;
    }

}
