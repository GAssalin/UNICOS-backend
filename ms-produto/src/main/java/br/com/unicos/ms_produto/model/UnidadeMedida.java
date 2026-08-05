package br.com.unicos.ms_produto.model;

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
 * Representa uma unidade de medida utilizada no catálogo de produtos do UniCoS.
 *
 * <p>
 * Permite padronizar unidades como UN, KG, L, CX, evitando inconsistências
 * de digitação e facilitando integrações com Estoque, Compras e Vendas.
 * </p>
 */
@Entity
@Table(name = "unidade_medida",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_unidade_medida_empresa_codigo",
                        columnNames = {"empresa_id", "codigo"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UnidadeMedida extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código da unidade de medida.
     * Ex.: "UN", "KG", "L", "CX".
     */
    @NotBlank
    @Column(nullable = false, length = 10)
    private String codigo;

    /**
     * Descrição da unidade de medida.
     * Ex.: "Unidade", "Quilograma", "Litro", "Caixa".
     */
    @NotBlank
    @Column(nullable = false, length = 80)
    private String descricao;

    /**
     * Define se a unidade pode ser utilizada com valores fracionados.
     * Ex.: KG e L geralmente permitem; UN normalmente não.
     */
    @NotNull
    @Column(name = "fracionavel", nullable = false)
    private Boolean fracionavel;

}
