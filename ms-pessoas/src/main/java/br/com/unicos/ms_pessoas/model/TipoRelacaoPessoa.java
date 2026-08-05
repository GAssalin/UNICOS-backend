package br.com.unicos.ms_pessoas.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Representa um tipo de relacionamento possível entre pessoas dentro do UniCoS.
 * <p>
 * Exemplos comuns incluem: Pai, Mãe, Dependente, Sócio, Representante Legal,
 * responsável financeiro, entre outros. Esta tabela permite flexibilidade
 * para que o administrador inclua novos tipos sem alterações no código.
 */
@Entity
@Table(name = "tipo_relacao_pessoa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TipoRelacaoPessoa extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do tipo de relação (ex: "Pai", "Sócio", "Responsável Legal").
     */
    @NotBlank
    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    /**
     * Descrição detalhada da relação, quando necessário.
     */
    @Column(length = 255)
    private String descricao;
}
