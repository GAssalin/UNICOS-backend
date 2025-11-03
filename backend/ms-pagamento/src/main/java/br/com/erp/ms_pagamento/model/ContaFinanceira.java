package br.com.erp.ms_pagamento.model;

import br.com.erp.ms_pagamento.enums.TipoConta;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidade que representa uma conta financeira da empresa.
 */
@Entity
@Table(name = "conta_financeira")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaFinanceira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição da conta é obrigatória.")
    @Column(nullable = false, length = 100)
    private String descricao;

    @Size(max = 50)
    private String banco;

    @Size(max = 20)
    private String agencia;

    @Size(max = 30)
    private String numeroConta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoConta tipoConta;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldoAtual = BigDecimal.ZERO;
}