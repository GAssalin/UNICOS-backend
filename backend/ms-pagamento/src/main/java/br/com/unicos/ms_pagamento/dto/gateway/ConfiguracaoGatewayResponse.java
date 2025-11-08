package br.com.unicos.ms_pagamento.dto.gateway;

import br.com.unicos.ms_pagamento.enums.TipoGateway;

import java.time.LocalDateTime;

/**
 * DTO de saída que representa uma configuração de gateway registrada no sistema.
 *
 * <p>
 * Retorna os dados essenciais de configuração e informações de auditoria
 * para consulta e gerenciamento administrativo.
 */
public record ConfiguracaoGatewayResponse(

        /** Identificador único da configuração de gateway. */
        Long id,

        /** Nome interno que identifica o gateway (ex: "PagSeguro Produção"). */
        String nome,

        /** Tipo do gateway (PagSeguro, MercadoPago, Stripe, etc.). */
        TipoGateway tipoGateway,

        /** URL base da API do gateway. */
        String endpointApi,

        /** Indica se a configuração está ativa. */
        boolean ativo,

        /** Indica se está em ambiente de homologação. */
        boolean ambienteHomologacao,

        /** Observações adicionais ou instruções de integração. */
        String observacao,

        /** Data de criação do registro (auditoria). */
        LocalDateTime dataCriacao,

        /** Data da última atualização do registro (auditoria). */
        LocalDateTime dataAtualizacao
) {}
