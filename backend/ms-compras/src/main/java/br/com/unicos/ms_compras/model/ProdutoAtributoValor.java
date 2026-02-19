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
 * Representa o valor de um atributo associado a um produto no catálogo UniCoS.
 *
 * <p>
 * Vincula um {@code produtoId} a um {@code atributoId}, armazenando o valor
 * correspondente (ex.: atributo "Cor" => "Preto").
 * </p>
 */
@Entity
@Table(name = "produto_atributo_valor",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_produto_atributo_valor_empresa_produto_atributo",
                        columnNames = {"empresa_id", "produto_id", "atributo_id"})
        },
        indexes = {
                @Index(name = "idx_produto_atributo_valor_empresa_produto", columnList = "empresa_id, produto_id"),
                @Index(name = "idx_produto_atributo_valor_empresa_atributo", columnList = "empresa_id, atributo_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProdutoAtributoValor extends BaseTenantEntity {

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
     * Identificador do atributo.
     * Integração lógica, sem FK física obrigatória.
     */
    @NotNull
    @Column(name = "atributo_id", nullable = false)
    private Long atributoId;

    /**
     * Valor do atributo para o produto.
     * Ex.: "Preto", "M", "220V".
     */
    @NotBlank
    @Column(name = "valor", nullable = false, length = 250)
    private String valor;

}
