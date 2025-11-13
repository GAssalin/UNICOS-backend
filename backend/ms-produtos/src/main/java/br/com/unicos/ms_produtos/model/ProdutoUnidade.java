package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entidade que representa a unidade de medida padrão ou alternativa
 * associada a um produto.
 *
 * <p>
 * Permite definir quantidades padrão e fatores de conversão
 * (ex.: 1 caixa = 12 unidades), auxiliando operações de vendas,
 * estoque e movimentação.
 * </p>
 */
@Entity
@Table(
        name = "produto_unidade",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_produto_unidade",
                columnNames = {"produto_id", "unidade_medida_id"}
        )
)
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoUnidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Produto ao qual esta unidade está associada.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Produto produto;

    /**
     * Unidade de medida utilizada (ex.: unidade, caixa, kg).
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_medida_id", nullable = false)
    private UnidadeMedida unidadeMedida;

    /**
     * Quantidade padrão do produto para esta unidade.
     */
    @NotNull
    @Positive(message = "A quantidade padrão deve ser maior que zero.")
    @Column(name = "quantidade_padrao", nullable = false)
    private Double quantidadePadrao;

    /**
     * Fator de conversão entre unidades.
     * Ex.: 1 caixa = 12 unidades → fatorConversao = 12.
     */
    @Positive
    @Column(name = "fator_conversao", nullable = false)
    @Builder.Default
    private Double fatorConversao = 1.0;
}
