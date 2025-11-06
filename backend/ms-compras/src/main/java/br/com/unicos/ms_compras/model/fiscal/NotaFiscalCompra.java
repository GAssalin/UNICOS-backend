package br.com.unicos.ms_compras.model.fiscal;

import br.com.unicos.core.base.model.EntidadeAuditavel;
import br.com.unicos.ms_compras.enums.StatusNotaFiscalCompra;
import br.com.unicos.ms_compras.enums.TipoNotaFiscal;
import br.com.unicos.ms_compras.model.pedido.PedidoCompra;
import jakarta.persistence.*;
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
 * Entidade que representa a Nota Fiscal de Compra.
 *
 * <p>Relaciona-se a um {@link PedidoCompra} e contém informações fiscais
 * como número da nota, chave de acesso, valores e impostos.</p>
 */
@Entity
@Table(name = "nota_fiscal_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class NotaFiscalCompra extends EntidadeAuditavel {

    /**
     * Número da nota fiscal.
     */
    @Column(nullable = false, length = 20)
    private String numeroNota;

    /**
     * Série da nota fiscal.
     */
    @Column(length = 10)
    private String serie;

    /**
     * Chave de acesso da nota fiscal eletrônica (NFe).
     */
    @Column(length = 44, unique = true)
    private String chaveAcesso;

    /**
     * Tipo da nota fiscal (entrada, devolução, complementar, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoNotaFiscal tipoNotaFiscal;

    /**
     * Status atual da nota fiscal.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusNotaFiscalCompra status;

    /**
     * Data de emissão da nota fiscal.
     */
    @Column(nullable = false)
    private LocalDate dataEmissao;

    /**
     * Data de entrada dos produtos no estoque.
     */
    private LocalDate dataEntrada;

    /**
     * Identificador do fornecedor (FK futura para ms-pessoas).
     */
    @Column(nullable = false)
    private Long fornecedorId;

    /**
     * Valor total da nota fiscal.
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    /**
     * Valor total dos impostos incidentes.
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal valorImpostos;

    /**
     * Observações gerais sobre a nota fiscal.
     */
    @Column(length = 500)
    private String observacao;

    /**
     * Pedido de compra associado a esta nota fiscal.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_compra_id")
    private PedidoCompra pedidoCompra;

    /**
     * Itens que compõem a nota fiscal.
     */
    @OneToMany(mappedBy = "notaFiscalCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotaFiscalItem> itens = new ArrayList<>();
}
