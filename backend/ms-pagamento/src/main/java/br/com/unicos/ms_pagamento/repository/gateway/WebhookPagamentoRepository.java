package br.com.unicos.ms_pagamento.repository.gateway;

import br.com.unicos.ms_pagamento.enums.StatusWebhook;
import br.com.unicos.ms_pagamento.model.gateway.WebhookPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link WebhookPagamento}.
 * <p>
 * Usado para rastrear e processar callbacks de gateways de pagamento.
 */
@Repository
public interface WebhookPagamentoRepository extends JpaRepository<WebhookPagamento, Long> {

    /**
     * Lista todos os webhooks ainda pendentes de processamento.
     *
     * @return Lista de webhooks com status PENDENTE.
     */
    List<WebhookPagamento> findByStatusProcessamento(StatusWebhook statusProcessamento);

    /**
     * Busca webhooks vinculados a uma transação específica.
     *
     * @param transacaoPagamentoId ID da transação vinculada.
     * @return Lista de webhooks relacionados à transação.
     */
    List<WebhookPagamento> findByTransacaoPagamentoId(Long transacaoPagamentoId);
}
