package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

/**
 * Entidade que representa uma variação de um produto,
 * como cor, tamanho, material ou qualquer combinação que diferencie
 * uma unidade específica do produto principal.
 *
 * <p>
 * Cada variação possui seu próprio SKU, código de barras,
 * preço opcional, além de atributos específicos que permitem
 * granularidade no catálogo e nas operações de venda/estoque.
 * </p>
 */
@Entity
@Table(
        name = "produto_variacao",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_variacao_sku", columnNames = "sku"),
                @UniqueConstraint(name = "uk_variacao_codigo_barras", columnNames = "codigo_barras")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoVariacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Produto principal ao qual esta variação está vinculada.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Produto produto;

    /**
     * Nome descritivo da variação (ex.: "Camisa Azul M").
     */
    @NotBlank(message = "O nome da variação é obrigatório.")
    @Column(nullable = false, length = 150)
    private String nome;

    /**
     * SKU único da variação, garantindo rastreamento unitário no ERP.
     */
    @NotBlank(message = "O SKU da variação é obrigatório.")
    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    /**
     * Preço específico para a variação, caso seja diferente do produto base.
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero.")
    @Column(precision = 15, scale = 2)
    private BigDecimal preco;

    /**
     * Código de barras único da variação.
     */
    @Column(name = "codigo_barras", length = 13, unique = true)
    private String codigoBarras;

    /**
     * Indica se esta variação está ativa para venda.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    /**
     * Cor associada à variação (opcional).
     */
    @Size(max = 50)
    private String cor;

    /**
     * Tamanho associado à variação (opcional).
     */
    @Size(max = 50)
    private String tamanho;

    /**
     * Material ou outra característica opcional da variação.
     */
    @Size(max = 100)
    private String material;

    /**
     * ID da empresa associada à variação, para arquitetura multi-tenant.
     */
    @NotNull
    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;
}
