package br.com.unicos.ms_estoque.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Relaciona quais lotes foram afetados em uma movimentação de estoque.
 */
@Entity
@Table(name = "movimentacao_lote")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimentacaoLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movimentacao_id", nullable = false)
    private MovimentacaoEstoque movimentacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private LoteSerie lote;

    @Column(nullable = false)
    private Double quantidade;
}
