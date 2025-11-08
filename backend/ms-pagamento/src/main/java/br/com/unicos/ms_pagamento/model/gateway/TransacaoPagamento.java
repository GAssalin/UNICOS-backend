package br.com.unicos.ms_pagamento.model.gateway;

import br.com.unicos.ms_pagamento.enums.StatusTransacao;
import br.com.unicos.ms_pagamento.enums.TipoFormaPagamento;
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
import java.time.LocalDateTime;

/**
 * Entidade que representa uma transação financeira junto a um gateway de pagamento.
 * <p>
 * Armazena informações detalhadas sobre a tentativa de processamento do pagamento
 * (PIX, cartão, boleto, etc.), incluindo status e retorno do provedor.
 */
@Entity
@Table(name = "transacao_pagamento")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransacaoPagamento {

    /**
     * Identificador único da transação.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código interno ou externo que identifica a transação no gateway.
     */
    @Size(max = 100)
    @Column(length = 100, unique = true)
    private String codigoTransacao;

    /**
     * Data e hora em que a transação foi registrada.
     */
    @NotNull(message = "A data da transação é obrigatória.")
    @Column(nullable = false)
    private LocalDateTime dataTransacao;

    /**
     * Valor processado nesta transação.
     */
    @NotNull(message = "O valor é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior que zero.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    /**
     * Status atual da transação (sucesso, falha, pendente, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusTransacao status;

    /**
     * Tipo de forma de pagamento utilizada (PIX, CARTÃO, BOLETO, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoFormaPagamento tipo;

    /**
     * Mensagem de retorno do gateway, útil para auditoria e troubleshooting.
     */
    @Size(max = 255)
    @Column(length = 255)
    private String mensagemRetorno;

    /**
     * Código de autorização ou identificador fornecido pelo provedor (ex: NSU, TXID, etc.).
     */
    @Size(max = 100)
    @Column(length = 100)
    private String codigoAutorizacao;

    /**
     * Configuração de gateway responsável pela transação.
     * Permite rastrear qual provedor foi utilizado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "configuracao_gateway_id")
    private ConfiguracaoGateway configuracaoGateway;

    /**
     * Pagamento vinculado a esta transação.
     * Representa a relação 1:1 com {@link Pagamento}.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagamento_id", nullable = false)
    private Pagamento pagamento;

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
