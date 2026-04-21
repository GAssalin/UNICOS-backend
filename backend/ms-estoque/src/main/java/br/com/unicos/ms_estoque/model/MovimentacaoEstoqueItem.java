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
@Table(name = "movimentacao_estoque_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MovimentacaoEstoqueItem extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "movimentacao_id", nullable = false)
    private Long movimentacaoId;

    @NotNull
    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    @NotNull
    @Column(name = "quantidade", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantidade;

    @Column(name = "valor_unitario", precision = 19, scale = 4)
    private BigDecimal valorUnitario;
}