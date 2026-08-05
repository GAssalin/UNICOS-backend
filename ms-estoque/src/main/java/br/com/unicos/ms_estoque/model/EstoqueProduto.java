package br.com.unicos.ms_estoque.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(
        name = "estoque_produto",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_estoque_produto_empresa_estoque_produto",
                        columnNames = {"empresa_id", "estoque_id", "produto_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EstoqueProduto extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "estoque_id", nullable = false)
    private Long estoqueId;

    @NotNull
    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    @NotNull
    @Column(name = "quantidade_atual", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantidadeAtual;

    @NotNull
    @Column(name = "quantidade_reservada", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantidadeReservada;

    @NotNull
    @Column(name = "quantidade_disponivel", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantidadeDisponivel;
}