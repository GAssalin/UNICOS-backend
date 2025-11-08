package br.com.unicos.ms_pagamento.dto.gateway;

import br.com.unicos.ms_pagamento.enums.TipoGateway;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para criação ou atualização de uma configuração de gateway.
 *
 * <p>
 * Contém os dados necessários para registrar credenciais, endpoints
 * e parâmetros de autenticação utilizados na comunicação com provedores
 * externos de pagamento.
 */
public record ConfiguracaoGatewayRequest(

        /** Nome interno que identifica o gateway (ex: "PagSeguro Produção"). */
        @NotBlank(message = "O nome do gateway é obrigatório.")
        @Size(max = 100, message = "O nome do gateway deve ter no máximo 100 caracteres.")
        String nome,

        /** Tipo do gateway (PagSeguro, MercadoPago, Stripe, etc.). */
        @NotNull(message = "O tipo de gateway é obrigatório.")
        TipoGateway tipoGateway,

        /** URL base da API do gateway. */
        @NotBlank(message = "O endpoint da API é obrigatório.")
        @Size(max = 255, message = "O endpoint da API deve ter no máximo 255 caracteres.")
        String endpointApi,

        /** Chave pública fornecida pelo provedor. */
        @Size(max = 255, message = "A chave pública deve ter no máximo 255 caracteres.")
        String apiKeyPublica,

        /** Chave privada ou token de autenticação. */
        @Size(max = 255, message = "A chave privada deve ter no máximo 255 caracteres.")
        String apiKeyPrivada,

        /** Define se o ambiente é de homologação (true) ou produção (false). */
        boolean ambienteHomologacao,

        /** Define se a configuração está ativa. */
        boolean ativo,

        /** Observações adicionais (instruções ou observações específicas). */
        @Size(max = 255, message = "A observação deve ter no máximo 255 caracteres.")
        String observacao
) {}
