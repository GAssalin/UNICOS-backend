package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que registra o histórico de alterações de preço de um produto.
 *
 * <p>
 * Cada registro contém o preço anterior, o novo preço, a data da alteração
 * e um motivo opcional. Essa estrutura permite rastrear mudanças financeiras
 * ao longo do tempo para auditoria, relatórios e cálculos comerciais.
 * </p>
 */
@Entity
@Table(name = "historico_preco")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoPreco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Produto ao qual este histórico de preço pertence.
     */
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
