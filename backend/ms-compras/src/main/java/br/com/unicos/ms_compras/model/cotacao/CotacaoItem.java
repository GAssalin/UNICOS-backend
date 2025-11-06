package br.com.unicos.ms_compras.model.cotacao;

import br.com.unicos.core.base.model.EntidadeAuditavel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Entidade que representa um item de uma cotação,
 * vinculado a um fornecedor específico.
 */
@Entity
@Table(name = "cotacao_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CotacaoItem extends EntidadeAuditavel {

    /**
     * Identificador do produto cotado (referência ao ms-produtos).
     */
    @Column(nullable = false)
    private Long produtoId;

    /**
     * Quantidade solicitada para o produto.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;

    /**
     * Valor unitário ofertado pelo fornecedor.
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorUnitario;

    /**
     * Valor total calculado para este item.
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    /**
     * Proposta do fornecedor à qual este item pertence.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cotacao_fornecedor_id", nullable = false)
    private CotacaoFornecedor cotacaoFornecedor;
}
