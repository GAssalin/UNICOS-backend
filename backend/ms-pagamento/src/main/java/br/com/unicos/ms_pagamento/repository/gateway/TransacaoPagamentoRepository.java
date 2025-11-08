package br.com.unicos.ms_pagamento.repository.gateway;

import br.com.unicos.ms_pagamento.enums.StatusTransacao;
import br.com.unicos.ms_pagamento.model.gateway.TransacaoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pela persistência da entidade {@link TransacaoPagamento}.
 * <p>
 * Oferece consultas para rastrear transações por status e código.
 */
@Repository
public interface TransacaoPagamentoRepository extends JpaRepository<TransacaoPagamento, Long> {

    /**
     * Busca uma transação pelo código de referência do gateway.
     *
     * @param codigoTransacao Código externo ou interno da transação.
     * @return Transação correspondente, se encontrada.
     */
    TransacaoPagamento findByCodigoTransacao(String codigoTransacao);

    /**
     * Lista todas as transações com determinado status.
     *
     * @param status Status da transação (SUCESSO, FALHA, PENDENTE, etc.).
     * @return Lista de transações no status informado.
     */
    List<TransacaoPagamento> findByStatus(StatusTransacao status);
}
