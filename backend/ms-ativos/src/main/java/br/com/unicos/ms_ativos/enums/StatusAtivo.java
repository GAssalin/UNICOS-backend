package br.com.unicos.ms_ativos.enums;

import lombok.Getter;

/**
 * Enum que representa o status atual de um ativo no sistema patrimonial.
 */
@Getter
public enum StatusAtivo {

    /**
     * Ativo em uso e operacional.
     */
    ATIVO("Ativo em uso e operacional"),

    /**
     * Ativo temporariamente indisponível por manutenção.
     */
    EM_MANUTENCAO("Ativo em manutenção ou inspeção"),

    /**
     * Ativo aguardando baixa, substituição ou descarte.
     */
    INATIVO("Ativo inativo, aguardando baixa ou substituição"),

    /**
     * Ativo já baixado ou descartado.
     */
    BAIXADO("Ativo baixado do patrimônio"),

    /**
     * Ativo emprestado ou deslocado temporariamente.
     */
    EMPRESTADO("Ativo emprestado ou deslocado temporariamente");

    private final String descricao;

    StatusAtivo(String descricao) {
        this.descricao = descricao;
    }
}
