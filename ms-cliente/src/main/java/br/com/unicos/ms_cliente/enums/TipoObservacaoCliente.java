package br.com.unicos.ms_cliente.enums;

import lombok.Getter;

/**
 * Enum que representa o tipo de observação vinculada ao cliente.
 *
 * <p>
 * Permite classificar observações internas para facilitar consultas,
 * filtros e regras comerciais futuras.
 * </p>
 */
@Getter
public enum TipoObservacaoCliente {

    /**
     * Observação geral sobre o cliente.
     */
    GERAL("Geral"),

    /**
     * Observação relacionada ao relacionamento comercial.
     */
    COMERCIAL("Comercial"),

    /**
     * Observação relacionada a crédito, pagamentos ou restrições financeiras.
     */
    FINANCEIRA("Financeira"),

    /**
     * Observação de alerta para atenção operacional ou comercial.
     */
    ALERTA("Alerta"),

    /**
     * Observação relacionada a restrições cadastrais, comerciais ou operacionais.
     */
    RESTRICAO("Restrição");

    private final String descricao;

    TipoObservacaoCliente(String descricao) {
        this.descricao = descricao;
    }

}