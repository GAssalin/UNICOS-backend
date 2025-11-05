package br.com.unicos.ms_estoque.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa o saldo atual de um produto em um determinado local de estoque.
 */
@Entity
@Table(name = "produto_estoque")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "produto_id", nullable = false)
    private Long produtoId; // Referência ao ms-produtos

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_local_id", nullable = false)
    private EstoqueLocal estoqueLocal;

    @Column(nullable = false)
    private Double quantidade;

    @Column(nullable = false)
    @Builder.Default
    private Double quantidadeMinima = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private Double quantidadeMaxima = 0.0;
}
