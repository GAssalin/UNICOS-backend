package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidade que representa uma variação de um produto,
 * como cor, tamanho ou outro atributo diferenciado.
 */
@Entity
@Table(name = "produto_variacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoVariacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Produto principal ao qual esta variação pertence
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Produto produto;

    /**
     * Nome descritivo da variação (ex: "Camisa Azul M")
     */
    @NotBlank(message = "O nome da variação é obrigatório.")
    @Column(nullable = false, length = 150)
    private String nome;

    /**
     * SKU único da variação
     */
    @NotBlank(message = "O SKU da variação é obrigatório.")
    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    /**
     * Preço específico da variação (se diferente do produto base)
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero.")
    @Column(precision = 15, scale = 2)
    private BigDecimal preco;

    /**
     * Código de barras (EAN/UPC) da variação
     */
    @Column(length = 13, unique = true)
    private String codigoBarras;

    /**
     * Indica se a variação está ativa para venda
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    /**
     * Cor (opcional, exemplo de atributo específico)
     */
    @Size(max = 50)
    private String cor;

    /**
     * Tamanho (opcional, exemplo de atributo específico)
     */
    @Size(max = 50)
    private String tamanho;

    /**
     * Material ou outro identificador opcional
     */
    @Size(max = 100)
    private String material;
}
