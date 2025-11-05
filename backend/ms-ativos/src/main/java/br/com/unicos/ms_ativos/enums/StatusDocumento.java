package br.com.unicos.ms_ativos.enums;

import lombok.Getter;

/**
 * Enum que representa o status atual de um documento associado a um ativo.
 */
@Getter
public enum StatusDocumento {

    /**
     * Documento válido e em vigência.
     */
    VALIDO("Documento válido e dentro da vigência"),

    /**
     * Documento expirado, vencido ou fora do prazo.
     */
    VENCIDO("Documento expirado ou fora da validade"),

    /**
     * Documento anulado ou inválido.
     */
    INVALIDO("Documento anulado ou considerado inválido"),

    /**
     * Documento expirado por tempo ou condição de uso.
     */
    EXPIRADO("Documento expirado"),

    /**
     * Documento em processo de atualização ou substituição.
     */
    EM_REVISAO("Documento em revisão ou atualização");

    private final String descricao;

    StatusDocumento(String descricao) {
        this.descricao = descricao;
    }
}
