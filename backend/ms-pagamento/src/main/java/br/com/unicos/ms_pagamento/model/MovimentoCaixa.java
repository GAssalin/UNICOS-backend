package br.com.unicos.ms_pagamento.model;

import br.com.unicos.ms_pagamento.enums.TipoMovimentoCaixa;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade que representa um movimento de entrada ou saída no caixa.
 */
@Entity
@Table(name = "movimento_caixa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimentoCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDate dataMovimento;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimentoCaixa tipoMovimento;

    @Size(max = 255)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "conta_financeira_id", nullable = false)
    private ContaFinanceira contaFinanceira;

    @ManyToOne
    @JoinColumn(name = "pagamento_id")
    private Pagamento pagamento;
}