package br.com.unicos.ms_pagamento.model;

import br.com.unicos.ms_pagamento.enums.StatusPagamento;
import br.com.unicos.ms_pagamento.enums.TipoTransacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Entidade que representa um pagamento realizado ou pendente.
 */
@Entity
@Table(name = "pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O valor do pagamento é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior que zero.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @NotNull(message = "A data de vencimento é obrigatória.")
    @Column(nullable = false)
    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPagamento status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoTransacao tipoTransacao;

    @Column(length = 100)
    private String referenciaId;

    @Column(length = 255)
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "forma_pagamento_id", nullable = false)
    private FormaPagamento formaPagamento;

    @OneToMany(mappedBy = "pagamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParcelaPagamento> parcelas;

    @OneToOne(mappedBy = "pagamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private TransacaoFinanceira transacaoFinanceira;

    @OneToMany(mappedBy = "pagamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovimentoCaixa> movimentosCaixa;
}