package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Representa um atributo configurável do catálogo de produtos no UniCoS.
 *
 * <p>
 * Permite que a empresa defina atributos padronizados para seus produtos,
 * como "Cor", "Tamanho", "Voltagem", "Material", etc.
 * </p>
 *
 * <p>
 * Os valores atribuídos a produtos específicos são armazenados em
 * {@link ProdutoAtributoValor}.
 * </p>
 */
@Entity
@Table(name = "produto_atributo",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_produto_atributo_empresa_nome",
                        columnNames = {"empresa_id", "nome"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProdutoAtributo extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do atributo.
     * Ex.: "Cor", "Tamanho", "Voltagem".
     */
    @NotBlank
    @Column(nullable = false, length = 120)
    private String nome;

    /**
     * Descrição do atributo.
     */
    @Column(length = 400)
    private String descricao;

}
