package br.com.unicos.ms_ativos.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de manutenção que podem ser realizadas em um ativo.
 * <p>
 * Cada tipo define uma finalidade distinta na gestão de ativos, seja para
 * prevenir falhas ou corrigir problemas existentes.
 */
@Getter
public enum TipoManutencao {

    /**
     * Manutenção realizada de forma preventiva, com o objetivo de evitar falhas
     * e garantir o bom funcionamento do ativo.
     */
    PREVENTIVA("Preventiva"),

    /**
     * Manutenção corretiva, realizada após a ocorrência de falhas ou defeitos
     * no ativo.
     */
    CORRETIVA("Corretiva");

    private final String descricao;

    TipoManutencao(String descricao) {
        this.descricao = descricao;
    }
}
