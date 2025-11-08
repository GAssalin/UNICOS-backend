package br.com.unicos.ms_pagamento.model.caixa;

import br.com.unicos.core.financeiro.model.ContaFinanceira;
import br.com.unicos.ms_pagamento.enums.TipoMovimentoCaixa;
import br.com.unicos.ms_pagamento.model.core.Pagamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
 * Entidade que representa um movimento financeiro de entrada ou saída no caixa.
 * <p>
 * Utilizada para registrar os lançamentos operacionais derivados de pagamentos
 * (como recebimentos, estornos ou saídas manuais).
 */
@Entity
@Table(name = "movimento_caixa")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimentoCaixa {

    /**
     * Identificador único do movimento de caixa.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Data em que o movimento foi registrado no caixa.
     */
    @NotNull(message = "A data do movimento é obrigatória.")
    @Column(nullable = false)
    private LocalDate dataMovimento;

    /**
     * Valor monetário do movimento.
     */
    @NotNull(message = "O valor é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior que zero.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    /**
     * Tipo do movimento (entrada, saída, estorno, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimentoCaixa tipoMovimento;

    /**
     * Descrição adicional do movimento (ex: “Recebimento via PIX”).
     */
    @Size(max = 255)
    @Column(length = 255)
    private String descricao;

    /**
     * Conta financeira vinculada ao movimento.
     * Proveniente do módulo core-financeiro.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_financeira_id", nullable = false)
    private ContaFinanceira contaFinanceira;

    /**
     * Pagamento relacionado ao movimento.
     * Relação N:1 com {@link Pagamento}, usada para rastreabilidade.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagamento_id")
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
