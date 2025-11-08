package br.com.unicos.ms_pagamento.model.core;

import br.com.unicos.core.financeiro.model.ContaFinanceira;
import br.com.unicos.core.financeiro.enums.FormaPagamento;
import br.com.unicos.ms_pagamento.enums.StatusPagamento;
import br.com.unicos.ms_pagamento.enums.TipoTransacao;
import br.com.unicos.ms_pagamento.model.caixa.MovimentoCaixa;
import br.com.unicos.ms_pagamento.model.gateway.TransacaoPagamento;
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
import java.util.List;

/**
 * Entidade que representa um pagamento efetivo ou pendente.
 * <p>
 * Armazena informações de valor, datas, forma e status,
 * bem como os vínculos com transações financeiras e movimentos de caixa.
 */
@Entity
@Table(name = "pagamento")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagamento {

    /** Identificador único do pagamento. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Valor total do pagamento. */
    @NotNull(message = "O valor do pagamento é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior que zero.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    /** Data prevista para vencimento do pagamento. */
    @NotNull(message = "A data de vencimento é obrigatória.")
    @Column(nullable = false)
    private LocalDate dataVencimento;

    /** Data em que o pagamento foi efetivamente realizado. */
    private LocalDate dataPagamento;

    /** Status atual do pagamento (pendente, pago, cancelado etc.). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPagamento status;

    /** Tipo de transação associada (crédito, débito, estorno, etc.). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoTransacao tipoTransacao;

    /** Código de referência externo (pedido, fatura, cobrança etc.). */
    @Column(length = 100)
    private String referenciaId;

    /** Observações gerais do pagamento. */
    @Column(length = 255)
    private String observacao;

    /**
     * Forma de pagamento utilizada.
     * Importado do core-financeiro para garantir padronização global.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FormaPagamento formaPagamento;

    /**
     * Conta financeira de destino/origem do pagamento.
     * Entidade proveniente do core-financeiro.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_financeira_id")
    private ContaFinanceira contaFinanceira;

    /** Parcelas vinculadas a este pagamento. */
    @OneToMany(mappedBy = "pagamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParcelaPagamento> parcelas;

    /** Transação associada ao gateway financeiro (PIX, cartão, etc.). */
    @OneToOne(mappedBy = "pagamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private TransacaoPagamento transacaoFinanceira;

    /** Movimentos de caixa internos relacionados ao pagamento. */
    @OneToMany(mappedBy = "pagamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovimentoCaixa> movimentosCaixa;

    /** Data de criação do registro (auditoria). */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate dataCriacao;

    /** Data da última atualização do registro (auditoria). */
    @LastModifiedDate
    private LocalDate dataAtualizacao;
}
