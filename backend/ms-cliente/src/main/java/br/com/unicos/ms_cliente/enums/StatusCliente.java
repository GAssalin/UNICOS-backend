package br.com.unicos.ms_cliente.enums;

import lombok.Getter;

/**
 * Enum que representa o status comercial do cliente.
 *
 * <p>
 * Controla se o cliente está apto para operações comerciais,
 * como vendas, orçamentos, pedidos e análises futuras.
 * </p>
 */
@Getter
public enum StatusCliente {

    /**
     * Cliente ativo e disponível para operações comerciais.
     */
    ATIVO("Ativo"),

    /**
     * Cliente inativo, sem utilização em novas operações.
     */
    INATIVO("Inativo"),

    /**
     * Cliente bloqueado para operações comerciais.
     */
    BLOQUEADO("Bloqueado"),

    /**
     * Cliente em análise cadastral, comercial ou financeira.
     */
    EM_ANALISE("Em análise");

    private final String descricao;

    StatusCliente(String descricao) {
        this.descricao = descricao;
    }
}