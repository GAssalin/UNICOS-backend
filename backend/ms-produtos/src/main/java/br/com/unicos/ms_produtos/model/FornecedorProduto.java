package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa o vínculo entre um fornecedor e um produto.
 * Contém o preço de custo, prazo médio de entrega e o identificador do fornecedor.
 */
@Entity
@Table(name = "fornecedor_produto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FornecedorProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID do fornecedor proveniente do MS Pessoa
     */
    @NotNull
    @Column(name = "fornecedor_id", nullable = false)
    private Long fornecedorId;

    /**
     * Código interno do fornecedor (opcional)
     */
    @Column(name = "codigo_fornecedor", length = 50)
    private String codigoFornecedor;

    /**
     * Produto vinculado ao fornecedor
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Produto produto;

    /**
     * Preço de custo fornecido
     */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "preco_custo", nullable = false, precision = 15, scale = 2)
    private BigDecimal precoCusto;

    /**
     * Prazo médio de entrega em dias
     */
    @PositiveOrZero
    @Column(name = "prazo_entrega_dias")
    private Integer prazoEntregaDias;

    /**
     * Data de criação e atualização (auditoria)
     */
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @PreUpdate
    public void preUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}
