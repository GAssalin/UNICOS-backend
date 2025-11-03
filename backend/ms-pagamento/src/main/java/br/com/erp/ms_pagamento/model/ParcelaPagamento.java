package br.com.erp.ms_pagamento.model;

import br.com.erp.ms_pagamento.enums.StatusParcela;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade que representa uma parcela de um pagamento.
 */
@Entity
@Table(name = "parcela_pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Integer numeroParcela;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @NotNull
    @Column(nullable = false)
    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusParcela status;

    @ManyToOne
    @JoinColumn(name = "pagamento_id", nullable = false)
    private Pagamento pagamento;
}