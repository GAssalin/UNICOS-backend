package br.com.unicos.ms_ativos.enums;

import lombok.Getter;

/**
 * Enum que define o tipo de manutenção aplicada a um ativo.
 */
@Getter
public enum TipoManutencao {

    /**
     * Manutenção preventiva, realizada periodicamente para evitar falhas.
     */
    PREVENTIVA("Manutenção preventiva para evitar falhas"),

    /**
     * Manutenção corretiva, realizada após a ocorrência de falhas.
     */
    CORRETIVA("Manutenção corretiva após falha detectada"),

    /**
     * Manutenção preditiva, baseada em monitoramento de desempenho.
     */
    PREDITIVA("Manutenção preditiva com base em medições e dados"),

    /**
     * Calibração ou ajuste técnico do ativo.
     */
    CALIBRACAO("Calibração técnica do ativo"),

    /**
     * Outro tipo de manutenção não especificada.
     */
    OUTRO("Outro tipo de manutenção aplicada ao ativo");

    private final String descricao;

    TipoManutencao(String descricao) {
        this.descricao = descricao;
    }
}
