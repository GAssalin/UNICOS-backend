package br.com.unicos.ms_compras.model.cotacao;

import br.com.unicos.core.base.model.EntidadeAuditavel;
import br.com.unicos.ms_compras.enums.StatusFornecedorCotacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa a proposta de um fornecedor dentro de uma cotação.
 *
 * <p>Contém os valores ofertados e o vínculo com os itens correspondentes.</p>
 */
@Entity
@Table(name = "cotacao_fornecedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CotacaoFornecedor extends EntidadeAuditavel {

    /**
     * Identificador do fornecedor (pode ser uma FK futura para o ms-pessoas).
     */
    @Column(nullable = false)
    private Long fornecedorId;

    /**
     * Valor total proposto pelo fornecedor.
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    /**
     * Prazo de entrega informado pelo fornecedor (em dias).
     */
    private Integer prazoEntrega;

    /**
     * Status atual da proposta do fornecedor.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusFornecedorCotacao status;

    /**
     * Observações específicas desta proposta.
     */
    @Column(length = 500)
    private String observacao;

    /**
     * Itens cotados por este fornecedor.
     */
    @OneToMany(mappedBy = "cotacaoFornecedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CotacaoItem> itens = new ArrayList<>();

    /**
     * Cotação principal à qual este fornecedor está vinculado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cotacao_compra_id", nullable = false)
    private CotacaoCompra cotacaoCompra;
}
