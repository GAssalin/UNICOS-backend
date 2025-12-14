package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que registra o histórico de alterações de preço de um produto,
 * sempre no contexto de uma empresa (tenant).
 *
 * <p>
 * Permite rastrear mudanças financeiras ao longo do tempo,
 * servindo como base para auditoria, relatórios e análises comerciais.
 * </p>
 */
@Entity
@Table(name = "historico_preco")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoPreco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador da empresa (tenant).
     * Campo obrigatório para isolamento multi-tenant.
     */
    @NotNull(message = "O identificador da empresa é obrigatório.")
    @Column(name = "empresa_id", nullable = false, updatable = false)
    private Long empresaId;

    /**
     * Produto ao qual este histórico de preço pertence.
     * O produto sempre pertence à mesma empresa.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Produto produto;

    /**
     * Preço anterior antes da modificação.
     */
    @NotNull
    @Column(name = "preco_anterior", nullable = false, precision = 15, scale = 2)
    private BigDecimal precoAnterior;

    /**
     * Novo preço após a alteração.
     */
    @NotNull
    @Column(name = "novo_preco", nullable = false, precision = 15, scale = 2)
    private BigDecimal novoPreco;

    /**
     * Data em que a alteração foi registrada.
     */
    @CreatedDate
    @Column(name = "data_alteracao", nullable = false, updatable = false)
    private LocalDateTime dataAlteracao;

    /**
     * Motivo da alteração de preço.
     */
    @Column(length = 500)
    private String motivo;
}
