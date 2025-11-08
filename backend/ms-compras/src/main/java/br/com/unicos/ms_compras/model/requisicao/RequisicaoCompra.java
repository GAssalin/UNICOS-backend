package br.com.unicos.ms_compras.model.requisicao;

import br.com.unicos.core.base.model.EntidadeAuditavel;
import br.com.unicos.ms_compras.enums.TipoRequisicaoCompra;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa uma requisição de compra feita por um departamento interno
 * ou por demanda de reposição de estoque.
 */
@Entity
@Table(name = "requisicao_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class RequisicaoCompra extends EntidadeAuditavel {

    /**
     * Código identificador da requisição.
     */
    @Column(nullable = false, length = 50, unique = true)
    private String codigo;

    /**
     * Tipo de requisição (reposição, interna, urgência, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoRequisicaoCompra tipoRequisicao;

    /**
     * Data em que a requisição foi aberta.
     */
    @Column(nullable = false)
    private LocalDate dataAbertura;

    /**
     * Data limite desejada para atendimento.
     */
    private LocalDate dataLimite;

    /**
     * Identificador do solicitante (FK futura para ms-pessoas).
     */
    @Column(nullable = false)
    private Long solicitanteId;

    /**
     * Observações gerais sobre a requisição.
     */
    @Column(length = 500)
    private String observacao;

    /**
     * Itens solicitados na requisição.
     */
    @OneToMany(mappedBy = "requisicaoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RequisicaoItem> itens = new ArrayList<>();
}
