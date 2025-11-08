package br.com.unicos.core.tesouraria.model;

import br.com.unicos.core.tesouraria.enums.StatusConciliacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade que representa a conciliação bancária de uma conta.
 * <p>
 * Permite comparar o saldo interno do sistema com o saldo informado
 * pelo banco em uma determinada data.
 */
@Entity
@Table(name = "conciliacao_bancaria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConciliacaoBancaria {

    /**
     * Identificador único da conciliação bancária.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Conta financeira associada à conciliação.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_financeira_id", nullable = false)
    private ContaFinanceira conta;

    /**
     * Data de referência da conciliação.
     */
    @NotNull
    @Column(nullable = false)
    private LocalDate data;

    /**
     * Saldo registrado internamente no sistema.
     */
    @NotNull
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoSistema;

    /**
     * Saldo informado pelo banco para a mesma data.
     */
    @NotNull
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoBanco;

    /**
     * Status da conciliação (pendente, conciliado ou divergente).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusConciliacao status;

    /**
     * Observações adicionais sobre a conciliação.
     */
    @Column(length = 255)
    private String observacao;
}
