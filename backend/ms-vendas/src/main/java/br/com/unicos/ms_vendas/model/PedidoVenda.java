package br.com.unicos.ms_vendas.model;

import br.com.unicos.core.pedido.model.Pedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa o pedido de venda no sistema UniCoS.
 *
 * <p>
 * Estende a classe {@link br.com.unicos.core.pedido.model.Pedido},
 * herdando os atributos e comportamentos comuns de um pedido comercial,
 * e adiciona informações específicas ao domínio de vendas — como cliente,
 * previsão de entrega, condição de pagamento e itens.
 * </p>
 */
@Entity
@Table(name = "pedido_venda")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class PedidoVenda extends Pedido {

    /**
     * Identificador do cliente (referência ao ms-pessoas).
     */
    @NotNull
    @Column(nullable = false)
    private Long clienteId;

    /**
     * Data prevista para entrega do pedido.
     */
    private LocalDate dataEntregaPrevista;

    /**
     * Valor total de descontos aplicados ao pedido.
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal valorDesconto;

    /**
     * ID da condição de pagamento (referência à tabela condicao_pagamento).
     */
    private Long condicaoPagamentoId;

    /**
     * Itens vinculados ao pedido de venda.
     */
    @OneToMany(mappedBy = "pedidoVenda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItemVenda> itens = new ArrayList<>();
}
