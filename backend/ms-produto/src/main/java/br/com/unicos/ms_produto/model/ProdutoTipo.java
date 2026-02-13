package br.com.unicos.ms_produto.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Representa um tipo de produto configurável dentro do catálogo UniCoS.
 *
 * <p>
 * Permite que a empresa (tenant) cadastre tipos customizados para classificar
 * itens do catálogo (ex.: "Mercadoria", "Matéria-Prima", "Embalagem", "Serviço",
 * "Assinatura", etc.).
 * </p>
 *
 * <p>
 * Recomenda-se usar este modelo quando a classificação precisa ser dinâmica
 * e extensível. Para cenários simples, um enum pode ser suficiente.
 * </p>
 */
@Entity
@Table(name = "produto_tipo",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_produto_tipo_empresa_nome",
                        columnNames = {"empresa_id", "nome"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProdutoTipo extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do tipo de produto.
     * Ex.: "Mercadoria", "Serviço", "Embalagem".
     */
    @NotBlank
    @Column(nullable = false, length = 120)
    private String nome;

    /**
     * Descrição complementar do tipo.
     */
    @Column(length = 400)
    private String descricao;

}
