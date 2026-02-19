package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Representa um código de barras associado a um produto no catálogo UniCoS.
 *
 * <p>
 * Permite que um produto possua múltiplos códigos de barras (EAN/GTIN),
 * suportando cenários como diferentes embalagens, códigos antigos e
 * padronizações por fornecedor.
 * </p>
 */
@Entity
@Table(name = "produto_codigo_barras",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_produto_cod_barras_empresa_codigo",
                        columnNames = {"empresa_id", "codigo_barras"})
        },
        indexes = {
                @Index(name = "idx_produto_cod_barras_empresa_produto", columnList = "empresa_id, produto_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProdutoCodigoBarras extends BaseTenantEntity {

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
     * Código de barras (EAN/GTIN) sem formatação.
     */
    @NotBlank
    @Column(name = "codigo_barras", nullable = false, length = 20)
    private String codigoBarras;

    /**
     * Define se este código de barras é o principal do produto.
     */
    @NotNull
    @Column(name = "principal", nullable = false)
    private Boolean principal;

}
