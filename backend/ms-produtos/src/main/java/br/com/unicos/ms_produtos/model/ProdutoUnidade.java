package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entidade que representa a unidade de medida padrão ou alternativa
 * associada a um produto, sempre no contexto de uma empresa (tenant).
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
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_produto_unidade_empresa",
                        columnNames = {"empresa_id", "produto_id", "unidade_medida_id"}
                )
        },
        indexes = {
                @Index(name = "idx_prod_unidade_empresa", columnList = "empresa_id"),
                @Index(name = "idx_prod_unidade_empresa_produto", columnList = "empresa_id, produto_id")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoUnidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador da empresa (tenant).
     * Campo obrigatório para isolamento multi-tenant.
     */
    @NotNull(message = "O identificador da empresa é obrigatório.")
    @Column(name = "empresa_id", nullable = false, updatable = false)
    private Long empresaId;

    /**
     * Produto ao qual esta unidade está associada.
     * O produto sempre pertence à mesma empresa.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Produto produto;

    /**
     * Unidade de medida utilizada (ex.: unidade, caixa, kg).
     * A unidade também pertence à mesma empresa.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_medida_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
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
    @NotNull
    @Positive
    @Column(name = "fator_conversao", nullable = false)
    @Builder.Default
    private Double fatorConversao = 1.0;
}
