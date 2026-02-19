package br.com.unicos.ms_vendas.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Representa uma categoria de produtos dentro do catálogo da plataforma UniCoS.
 *
 * <p>
 * Categorias são utilizadas para organizar o catálogo e facilitar buscas,
 * relatórios e integrações com módulos como Estoque, Compras e Vendas.
 * </p>
 */
@Entity
@Table(name = "categoria_produto",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_categoria_produto_empresa_nome",
                        columnNames = {"empresa_id", "nome"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CategoriaProduto extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome da categoria.
     * Deve ser único por empresa (tenant).
     */
    @NotBlank
    @Column(nullable = false, length = 120)
    private String nome;

    /**
     * Descrição da categoria.
     */
    @Column(length = 400)
    private String descricao;

    /**
     * Categoria pai (hierarquia).
     * Integração lógica, sem FK física obrigatória.
     */
    @Column(name = "categoria_pai_id")
    private Long categoriaPaiId;

}
