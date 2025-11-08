package br.com.unicos.ms_pagamento.model.gateway;

import br.com.unicos.ms_pagamento.enums.TipoGateway;
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
import java.util.List;

/**
 * Entidade que representa as configurações de integração com um provedor de pagamento (gateway).
 * <p>
 * Armazena as credenciais, endpoints e parâmetros necessários para comunicação
 * com APIs externas de pagamento, permitindo o gerenciamento de múltiplos provedores
 * (como PagSeguro, Mercado Pago, Stripe, etc.) dentro do UniCoS.
 */
@Entity
@Table(name = "configuracao_gateway")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracaoGateway {

    /**
     * Identificador único da configuração de gateway.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome interno para identificação do gateway (ex: “PagSeguro Produção”).
     */
    @NotBlank(message = "O nome do gateway é obrigatório.")
    @Size(max = 100)
    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    /**
     * Tipo de gateway utilizado (PagSeguro, MercadoPago, Stripe, etc.).
     */
    @NotNull(message = "O tipo de gateway é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoGateway tipoGateway;

    /**
     * URL base da API do gateway.
     */
    @NotBlank(message = "O endpoint da API é obrigatório.")
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String endpointApi;

    /**
     * Chave pública de autenticação no gateway.
     */
    @Size(max = 255)
    private String apiKeyPublica;

    /**
     * Chave privada ou token secreto do gateway.
     */
    @Size(max = 255)
    private String apiKeyPrivada;

    /**
     * Indica se esta configuração é utilizada em ambiente de homologação.
     */
    @Column(nullable = false)
    private boolean ambienteHomologacao;

    /**
     * Indica se a configuração está ativa para uso.
     */
    @Column(nullable = false)
    private boolean ativo;

    /**
     * Observações gerais ou instruções específicas de integração.
     */
    @Size(max = 255)
    private String observacao;

    /**
     * Lista de transações financeiras associadas a este gateway.
     */
    @OneToMany(mappedBy = "configuracaoGateway", cascade = CascadeType.ALL)
    private List<TransacaoPagamento> transacoes;

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
