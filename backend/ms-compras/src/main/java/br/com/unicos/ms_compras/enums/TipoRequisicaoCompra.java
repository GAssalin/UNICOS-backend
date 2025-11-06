package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Enum que define os tipos de requisições de compra.
 */
@Getter
public enum TipoRequisicaoCompra {

    /**
     * Requisição feita para reposição de estoque.
     */
    REPOSICAO_ESTOQUE("Requisição voltada à reposição de estoque."),

    /**
     * Requisição feita por um departamento interno.
     */
    DEMANDA_INTERNA("Requisição interna de um setor específico."),

    /**
     * Requisição de urgência.
     */
    URGENCIA("Requisição emergencial com prioridade máxima."),

    /**
     * Requisição para material de consumo administrativo.
     */
    MATERIAL_CONSUMO("Requisição para compra de materiais de consumo.");

    private final String descricao;

    TipoRequisicaoCompra(String descricao) {
        this.descricao = descricao;
    }
}
