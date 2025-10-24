package br.com.erp.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    @NotNull
    @Column(name = "fornecedor_id", nullable = false)
    private Long fornecedorId; // será referenciado via MS Pessoa/Fornecedor futuramente

    @NotNull
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "preco_custo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoCusto;

    @PositiveOrZero
    @Column(name = "prazo_entrega_dias")
    private Integer prazoEntregaDias;

}
