package br.com.unicos.ms_estoque.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detalha os itens movimentados em uma transação de estoque.
 */
@Entity
@Table(name = "movimentacao_estoque")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transacao_id", nullable = false)
    private TransacaoEstoque transacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_estoque_id", nullable = false)
    private ProdutoEstoque produtoEstoque;

    @Column(nullable = false)
    private Double quantidade;

    @Column(name = "lote_id")
    private Long loteId; // Referência opcional a LoteSerie
}
