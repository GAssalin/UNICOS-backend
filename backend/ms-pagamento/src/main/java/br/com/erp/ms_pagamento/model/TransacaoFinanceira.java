package br.com.erp.ms_pagamento.model;

import br.com.erp.ms_pagamento.enums.StatusTransacao;
import br.com.erp.ms_pagamento.enums.TipoFormaPagamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa uma transação financeira.
 */
@Entity
@Table(name = "transacao_financeira")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransacaoFinanceira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, unique = true)
    private String codigoTransacao;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataTransacao;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusTransacao status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoFormaPagamento tipo;

    @OneToOne
    @JoinColumn(name = "pagamento_id", nullable = false)
    private Pagamento pagamento;
}