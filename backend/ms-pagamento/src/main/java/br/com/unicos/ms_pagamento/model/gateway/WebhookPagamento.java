package br.com.unicos.ms_pagamento.model.gateway;

import br.com.unicos.ms_pagamento.enums.StatusWebhook;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entidade que armazena os callbacks (webhooks) recebidos dos gateways de pagamento.
 * <p>
 * Permite rastrear notificações de status de pagamento, conciliação e reembolso
 * enviadas de forma assíncrona pelos provedores externos.
 */
@Entity
@Table(name = "webhook_pagamento")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookPagamento {

    /**
     * Identificador único do webhook recebido.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador da transação no sistema externo (gateway).
     */
    @Size(max = 100)
    private String codigoExterno;

    /**
     * Data e hora de recebimento do webhook.
     */
    @NotNull(message = "A data de recebimento é obrigatória.")
    @Column(nullable = false)
    private LocalDateTime dataRecebimento;

    /**
     * Payload original recebido do provedor (geralmente em JSON).
     */
    @NotBlank(message = "O payload do webhook é obrigatório.")
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    /**
     * Status de processamento interno do webhook (pendente, processado, erro).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusWebhook statusProcessamento;

    /**
     * Mensagem de erro ou log de processamento.
     */
    @Size(max = 255)
    private String mensagemProcessamento;

    /**
     * Transação à qual este webhook está associado.
     * Permite rastrear o impacto direto do callback no fluxo de pagamento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transacao_pagamento_id")
    private TransacaoPagamento transacaoPagamento;

    /**
     * Data de criação do registro (auditoria).
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /**
     * Data da última atualização do registro (auditoria).
     */
    @LastModifiedDate
    private LocalDateTime dataAtualizacao;
}
