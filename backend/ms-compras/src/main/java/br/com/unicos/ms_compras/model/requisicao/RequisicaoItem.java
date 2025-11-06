package br.com.unicos.ms_compras.model.requisicao;

import br.com.unicos.core.base.model.EntidadeAuditavel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Entidade que representa um item solicitado em uma requisição de compra.
 *
 * <p>Define o produto, a quantidade e observações adicionais do solicitante.</p>
 */
@Entity
@Table(name = "requisicao_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class RequisicaoItem extends EntidadeAuditavel {

    /**
     * Identificador do produto solicitado (referência ao ms-produtos).
     */
    @Column(nullable = false)
    private Long produtoId;

    /**
     * Quantidade solicitada pelo requisitante.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeSolicitada;

    /**
     * Quantidade atendida pela compra (pode ser preenchida posteriormente).
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal quantidadeAtendida;

    /**
     * Observação sobre o item (justificativa, prioridade, etc.).
     */
    @Column(length = 300)
    private String observacao;

    /**
     * Requisição à qual o item pertence.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requisicao_compra_id", nullable = false)
    private RequisicaoCompra requisicaoCompra;
}
