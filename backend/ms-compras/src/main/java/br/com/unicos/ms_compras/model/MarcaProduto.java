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
 * Representa a marca associada a um produto dentro do catálogo UniCoS.
 *
 * <p>
 * A marca permite classificar produtos por fabricante ou identidade comercial,
 * auxiliando em relatórios, filtros e organização do catálogo.
 * </p>
 */
@Entity
@Table(name = "marca_produto",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_marca_produto_empresa_nome",
                        columnNames = {"empresa_id", "nome"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MarcaProduto extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome da marca.
     * Deve ser único por empresa (tenant).
     */
    @NotBlank
    @Column(nullable = false, length = 120)
    private String nome;

    /**
     * Descrição complementar da marca.
     */
    @Column(length = 400)
    private String descricao;

}
