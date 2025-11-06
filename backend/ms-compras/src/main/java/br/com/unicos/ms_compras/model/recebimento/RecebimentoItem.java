package br.com.unicos.ms_compras.model.recebimento;

import br.com.unicos.core.base.model.EntidadeAuditavel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Entidade que representa um item dentro do processo de recebimento.
 *
 * <p>Relaciona um produto específico recebido e suas quantidades conferidas.</p>
 */
@Entity
@Table(name = "recebimento_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class RecebimentoItem extends EntidadeAuditavel {

    /**
     * Identificador do produto recebido (referência ao ms-produtos).
     */
    @Column(nullable = false)
    private Long produtoId;

    /**
     * Quantidade recebida fisicamente.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeRecebida;

    /**
     * Quantidade prevista no pedido original.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal quantidadePrevista;

    /**
     * Quantidade devolvida, se aplicável.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal quantidadeDevolvida;

    /**
     * Observações sobre o item (avarias, divergências, etc.).
     */
    @Column(length = 300)
    private String observacao;

    /**
     * Recebimento ao qual o item pertence.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recebimento_compra_id", nullable = false)
    private RecebimentoCompra recebimentoCompra;
}
