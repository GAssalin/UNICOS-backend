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
 * Entidade que representa o vínculo entre um fornecedor e um produto,
 * sempre no contexto de uma empresa (tenant).
 *
 * <p>
 * O fornecedor pertence a outro microserviço (ex.: ms-pessoas ou ms-fornecedor),
 * portanto o vínculo aqui é apenas referencial.
 * </p>
 */
@Entity
@Table(name = "fornecedor_produto")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FornecedorProduto {

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
     * ID do fornecedor proveniente de outro microserviço.
     * Não possui FK física para evitar acoplamento entre domínios.
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
     * O produto sempre pertence à mesma empresa.
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
     * Data de criação do vínculo.
     */
    @CreatedDate
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /**
     * Data da última atualização do vínculo.
     */
    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}
