package br.com.unicos.core.produto.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Modelo base que representa os preços essenciais de um produto.
 *
 * <p>
 * Este modelo fornece uma estrutura padronizada para valores monetários utilizados
 * por diversos microserviços do ecossistema UNICOS, como compras, vendas,
 * estoque e financeiro.
 * </p>
 *
 * <p>
 * Não é uma entidade JPA e não contém regras de negócio.
 * Seu propósito é servir como estrutura de transporte compartilhada (DTO base)
 * dentro do módulo <b>core-produto</b>.
 * </p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PrecoBase {

    /**
     * Preço de custo do produto.
     */
    private BigDecimal precoCusto;

    /**
     * Preço de venda sugerido ao consumidor final.
     */
    private BigDecimal precoVenda;

    /**
     * Preço mínimo permitido para a venda, conforme políticas internas.
     */
    private BigDecimal precoMinimo;

    /**
     * Margem de lucro padrão configurada para o produto.
     */
    private BigDecimal margemPadrao;

}
