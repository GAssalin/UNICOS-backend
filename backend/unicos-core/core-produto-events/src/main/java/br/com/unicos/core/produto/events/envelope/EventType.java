package br.com.unicos.core.produto.events.envelope;

import lombok.Getter;

/**
 * Enum que representa os tipos de eventos do subdomínio de Produto.
 * <p>
 * Utilizado para identificação, roteamento e observabilidade
 * dos eventos publicados em mensageria.
 */
@Getter
public enum EventType {

    /**
     * Evento disparado quando um produto é criado.
     */
    PRODUTO_CRIADO("Produto criado"),

    /**
     * Evento disparado quando um produto é atualizado.
     */
    PRODUTO_ATUALIZADO("Produto atualizado"),

    /**
     * Evento disparado quando um produto é inativado.
     */
    PRODUTO_INATIVADO("Produto inativado"),

    /**
     * Evento disparado quando um produto é reativado.
     */
    PRODUTO_REATIVADO("Produto reativado"),

    /**
     * Evento disparado quando uma categoria é criada.
     */
    CATEGORIA_CRIADA("Categoria criada"),

    /**
     * Evento disparado quando uma categoria é atualizada.
     */
    CATEGORIA_ATUALIZADA("Categoria atualizada");

    /**
     * Descrição legível do tipo de evento.
     */
    private final String descricao;

    EventType(String descricao) {
        this.descricao = descricao;
    }
}