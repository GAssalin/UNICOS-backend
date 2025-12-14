package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entidade que representa uma unidade de medida utilizada na padronização de produtos,
 * como "Unidade (UN)", "Quilograma (KG)", "Caixa (CX)" ou qualquer outra forma de
 * quantificação operacional.
 *
 * <p>
 * A unidade de medida é essencial para controle de estoque, vendas, conversões,
 * operações logísticas e consistência de dados entre diversos microserviços.
 * </p>
 */
@Entity
@Table(
        name = "unidade_medida",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_unidade_medida_sigla", columnNames = "sigla")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnidadeMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome completo da unidade de medida
     * (ex.: "Quilograma", "Unidade", "Caixa").
     */
    @NotBlank(message = "O nome da unidade é obrigatório.")
    @Column(nullable = false, length = 50)
    private String nome;

    /**
     * Sigla padronizada da unidade de medida
     * (ex.: "KG", "UN", "CX").
     */
    @NotBlank(message = "A sigla é obrigatória.")
    @Column(nullable = false, length = 10, unique = true)
    private String sigla;

    /**
     * Descrição complementar da unidade,
     * utilizada para exibir detalhes adicionais no catálogo.
     */
    @Column(length = 255)
    private String descricao;

    /**
     * Indica se a unidade está ativa para uso no ERP.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    /**
     * ID da empresa associada à unidade de medida, para arquitetura multi-tenant.
     */
    @NotNull
    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;
}
