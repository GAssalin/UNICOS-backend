package br.com.unicos.ms_produto.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_produto.enums.TipoProduto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Representa um produto ou serviço dentro do catálogo da plataforma UniCoS.
 *
 * <p>
 * É a entidade central do microserviço ms-produto, responsável por
 * armazenar informações cadastrais, classificações e atributos básicos
 * utilizados pelos módulos de Estoque, Compras, Vendas e Financeiro.
 * </p>
 */
@Entity
@Table(name = "produto",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_produto_empresa_codigo",
                        columnNames = {"empresa_id", "codigo"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Produto extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código interno (SKU) do produto.
     * Deve ser único por empresa (tenant).
     */
    @NotBlank
    @Column(nullable = false, length = 50)
    private String codigo;

    /**
     * Nome principal do produto.
     */
    @NotBlank
    @Column(nullable = false, length = 200)
    private String nome;

    /**
     * Descrição detalhada do produto.
     */
    @Column(length = 1000)
    private String descricao;

    /**
     * Tipo do produto (PRODUTO ou SERVICO).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoProduto tipoProduto;

    /**
     * Unidade de medida padrão do produto.
     * Ex: UN, KG, L, CX.
     */
    @NotBlank
    @Column(name = "unidade_medida", nullable = false, length = 10)
    private String unidadeMedida;

    /**
     * Identificador da categoria do produto.
     * Integração lógica, sem FK física.
     */
    @Column(name = "categoria_id")
    private Long categoriaId;

    /**
     * Identificador da marca do produto.
     * Integração lógica, sem FK física.
     */
    @Column(name = "marca_id")
    private Long marcaId;

    /**
     * Código de barras principal (EAN/GTIN).
     */
    @Column(name = "codigo_barras", length = 20)
    private String codigoBarras;

    /**
     * Preço base de venda.
     */
    @Column(name = "preco_base", precision = 15, scale = 4)
    private BigDecimal precoBase;

    /**
     * Peso bruto do produto.
     */
    @Column(precision = 15, scale = 4)
    private BigDecimal peso;

    /**
     * Volume do produto.
     */
    @Column(precision = 15, scale = 4)
    private BigDecimal volume;

}
