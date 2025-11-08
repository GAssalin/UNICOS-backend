package br.com.unicos.ms_compras.model.recebimento;

import br.com.unicos.core.base.model.EntidadeAuditavel;
import br.com.unicos.ms_compras.enums.StatusRecebimentoCompra;
import br.com.unicos.ms_compras.enums.TipoRecebimento;
import br.com.unicos.ms_compras.model.fiscal.NotaFiscalCompra;
import br.com.unicos.ms_compras.model.pedido.PedidoCompra;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa o processo de recebimento de uma compra,
 * controlando a entrada física e fiscal dos produtos.
 *
 * <p>Relaciona-se a um {@link PedidoCompra} e pode ter vínculo com uma {@link NotaFiscalCompra}.</p>
 */
@Entity
@Table(name = "recebimento_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class RecebimentoCompra extends EntidadeAuditavel {

    /**
     * Código identificador do recebimento.
     */
    @Column(nullable = false, length = 50, unique = true)
    private String codigo;

    /**
     * Tipo do recebimento (total, parcial, devolvido, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoRecebimento tipoRecebimento;

    /**
     * Status atual do processo de recebimento.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusRecebimentoCompra status;

    /**
     * Data em que o recebimento foi iniciado.
     */
    @Column(nullable = false)
    private LocalDate dataRecebimento;

    /**
     * Data de conclusão do recebimento (caso aplicável).
     */
    private LocalDate dataConclusao;

    /**
     * Observações gerais sobre o recebimento.
     */
    @Column(length = 500)
    private String observacao;

    /**
     * Pedido de compra vinculado a este recebimento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_compra_id", nullable = false)
    private PedidoCompra pedidoCompra;

    /**
     * Nota fiscal associada ao recebimento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nota_fiscal_compra_id")
    private NotaFiscalCompra notaFiscalCompra;

    /**
     * Itens recebidos neste processo.
     */
    @OneToMany(mappedBy = "recebimentoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RecebimentoItem> itens = new ArrayList<>();
}
