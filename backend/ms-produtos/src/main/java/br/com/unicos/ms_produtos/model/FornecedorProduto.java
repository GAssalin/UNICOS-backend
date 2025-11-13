package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa o vínculo entre um fornecedor e um produto.
 *
 * <p>
 * Armazena informações operacionais relacionadas ao preço de custo,
 * prazos de entrega e identificação externa do fornecedor no ecossistema.
 * </p>
 */
@Entity
@Table(name = "fornecedor_produto")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FornecedorProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID do fornecedor proveniente do ms-pessoas.
     */
    @NotNull
    @Column(name = "fornecedor_id", nullable = false)
    private Long fornecedorId;

    /**
     * Código interno utilizado pelo fornecedor (opcional).
     */
    @Column(name = "codigo_fornecedor", length = 50)
    private String codigoFornecedor;

    /**
     * Produto associado ao fornecedor.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Produto produto;

    /**
     * Preço de custo negociado com o fornecedor.
     */
    @NotNull
    @DecimalMin(value = "0.01", message = "O preço de custo deve ser maior que zero.")
    @Column(name = "preco_custo", nullable = false, precision = 15, scale = 2)
    private BigDecimal precoCusto;

    /**
     * Prazo médio de entrega em dias.
     */
    @PositiveOrZero
    @Column(name = "prazo_entrega_dias")
    private Integer prazoEntregaDias;

    /**
     * Datas de criação e atualização do vínculo.
     */
    @CreatedDate
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}
