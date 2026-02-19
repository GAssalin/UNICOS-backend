package br.com.unicos.ms_vendas.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Representa a precificação base de um produto dentro do catálogo UniCoS.
 *
 * <p>
 * Este modelo guarda valores de referência (ex.: custo e preço de venda base),
 * podendo ser evoluído futuramente para suportar tabelas de preço, vigências,
 * promoções e variações por canal/filial.
 * </p>
 */
@Entity
@Table(name = "produto_preco_base",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_produto_preco_base_empresa_produto",
                        columnNames = {"empresa_id", "produto_id"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProdutoPrecoBase extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador do produto.
     * Integração lógica, sem FK física obrigatória.
     */
    @NotNull
    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    /**
     * Custo base do produto.
     */
    @Column(name = "custo_base", precision = 15, scale = 4)
    private BigDecimal custoBase;

    /**
     * Preço base de venda do produto.
     */
    @Column(name = "preco_venda_base", precision = 15, scale = 4)
    private BigDecimal precoVendaBase;

    /**
     * Margem base (percentual).
     * Ex.: 30.00 = 30%
     */
    @Column(name = "margem_base", precision = 7, scale = 2)
    private BigDecimal margemBase;

}
