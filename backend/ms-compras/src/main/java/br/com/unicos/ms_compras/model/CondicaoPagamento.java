package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma condição de pagamento utilizada em compras.
 * <p>
 * A definição das parcelas (prazo e percentual) é feita por {@link CondicaoPagamentoParcela},
 * que é a fonte de verdade para cálculo de vencimentos e rateio.
 */
@Entity
@Table(
        name = "condicao_pagamento",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_condicao_pagamento_codigo", columnNames = {"codigo"})
        },
        indexes = {
                @Index(name = "ix_condicao_pagamento_nome", columnList = "nome")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class CondicaoPagamento extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código interno da condição de pagamento (ex.: "CP-AVISTA", "CP-30-60-90").
     */
    @NotBlank
    @Size(max = 30)
    @Column(name = "codigo", nullable = false, length = 30)
    private String codigo;

    /**
     * Nome descritivo da condição (ex.: "À vista", "30/60/90").
     */
    @NotBlank
    @Size(max = 150)
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    /**
     * Descrição detalhada (opcional).
     */
    @Size(max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    /**
     * Indica se a condição possui mais de uma parcela.
     * <p>
     * Mesmo em condições "à vista", pode haver 1 parcela com {@code diasAposEmissao = 0} e {@code percentual = 100}.
     */
    @NotNull
    @Column(name = "parcelado", nullable = false)
    private Boolean parcelado;

    /**
     * Parcelas da condição (fonte de verdade).
     */
    @Valid
    @NotNull
    @Builder.Default
    @OneToMany(mappedBy = "condicaoPagamento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("ordem ASC")
    private List<CondicaoPagamentoParcela> parcelas = new ArrayList<>();
}
