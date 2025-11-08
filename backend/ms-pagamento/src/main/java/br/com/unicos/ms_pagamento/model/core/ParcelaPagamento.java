package br.com.unicos.ms_pagamento.model.core;

import br.com.unicos.ms_pagamento.enums.StatusParcela;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade que representa uma parcela individual de um pagamento.
 * <p>
 * Cada parcela contém informações de valor, vencimento e status,
 * podendo estar vinculada a uma transação ou liquidação parcial.
 */
@Entity
@Table(name = "parcela_pagamento")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelaPagamento {

    /**
     * Identificador único da parcela.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Número sequencial da parcela dentro do pagamento.
     */
    @NotNull(message = "O número da parcela é obrigatório.")
    @Column(nullable = false)
    private Integer numeroParcela;

    /**
     * Valor monetário da parcela.
     */
    @NotNull(message = "O valor da parcela é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior que zero.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    /**
     * Data prevista para vencimento da parcela.
     */
    @NotNull(message = "A data de vencimento é obrigatória.")
    @Column(nullable = false)
    private LocalDate dataVencimento;

    /**
     * Data em que a parcela foi efetivamente quitada.
     */
    private LocalDate dataPagamento;

    /**
     * Status atual da parcela (pendente, paga, atrasada etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusParcela status;

    /**
     * Pagamento ao qual esta parcela pertence.
     * Representa a relação N:1 com a entidade {@link Pagamento}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagamento_id", nullable = false)
    private Pagamento pagamento;

    /**
     * Data de criação do registro (auditoria).
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate dataCriacao;

    /**
     * Data da última atualização do registro (auditoria).
     */
    @LastModifiedDate
    private LocalDate dataAtualizacao;
}
