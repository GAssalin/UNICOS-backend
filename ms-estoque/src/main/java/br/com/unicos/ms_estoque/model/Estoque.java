package br.com.unicos.ms_estoque.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_estoque.enums.StatusEstoque;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Representa um estoque dentro do sistema.
 */
@Entity
@Table(
        name = "estoque",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_estoque_empresa_codigo",
                        columnNames = {"empresa_id", "codigo"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Estoque extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código interno do estoque.
     */
    @NotBlank(message = "O código do estoque é obrigatório.")
    @Column(name = "codigo", nullable = false, length = 30)
    private String codigo;

    /**
     * Nome do estoque.
     */
    @NotBlank(message = "O nome do estoque é obrigatório.")
    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    /**
     * Descrição livre do estoque.
     */
    @Column(name = "descricao", length = 500)
    private String descricao;

    /**
     * Status do estoque.
     */
    @NotNull(message = "O status do estoque é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(name = "status_estoque", nullable = false, length = 20)
    private StatusEstoque statusEstoque;

    /**
     * Identificador do estoque pai.
     *
     * <p>Permanece como relacionamento lógico por ID para manter simplicidade no MVP.</p>
     */
    @Column(name = "estoque_pai_id")
    private Long estoquePaiId;
}