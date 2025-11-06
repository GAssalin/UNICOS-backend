package br.com.unicos.ms_compras.model.fiscal;

import br.com.unicos.core.base.model.EntidadeAuditavel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Entidade que representa um item da Nota Fiscal de Compra.
 *
 * <p>Contém as informações detalhadas do produto,
 * quantidades, valores e tributos.</p>
 */
@Entity
@Table(name = "nota_fiscal_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class NotaFiscalItem extends EntidadeAuditavel {

    /**
     * Identificador do produto (referência ao ms-produtos).
     */
    @Column(nullable = false)
    private Long produtoId;

    /**
     * Descrição do produto no momento da nota (texto fiscal).
     */
    @Column(nullable = false, length = 255)
    private String descricaoProduto;

    /**
     * Quantidade de produto faturada.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;

    /**
     * Valor unitário do produto.
     */
    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal valorUnitario;

    /**
     * Valor total do item (quantidade × valor unitário).
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    /**
     * Valor de ICMS destacado.
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal valorICMS;

    /**
     * Valor de IPI destacado.
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal valorIPI;

    /**
     * Valor de desconto aplicado ao item.
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal valorDesconto;

    /**
     * Nota fiscal à qual o item pertence.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nota_fiscal_compra_id", nullable = false)
    private NotaFiscalCompra notaFiscalCompra;
}
