package br.com.unicos.ms_ativos.enums;

import lombok.Getter;

/**
 * Enum que define os tipos de transferência de ativos.
 */
@Getter
public enum TipoTransferencia {

    /**
     * Transferência dentro da mesma filial ou departamento.
     */
    INTERNA("Transferência interna na mesma unidade"),

    /**
     * Transferência entre filiais diferentes.
     */
    ENTRE_FILIAIS("Transferência entre filiais ou unidades distintas"),

    /**
     * Transferência por baixa contábil (descarte ou alienação).
     */
    BAIXA("Transferência para baixa ou descarte do ativo"),

    /**
     * Transferência para manutenção externa.
     */
    MANUTENCAO("Transferência para manutenção externa"),

    /**
     * Outro tipo de movimentação não classificada.
     */
    OUTRO("Outro tipo de transferência de ativo");

    private final String descricao;

    TipoTransferencia(String descricao) {
        this.descricao = descricao;
    }
}
