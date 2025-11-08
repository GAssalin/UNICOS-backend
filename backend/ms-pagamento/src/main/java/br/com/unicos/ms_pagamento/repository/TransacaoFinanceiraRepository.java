package br.com.unicos.ms_pagamento.repository;

import br.com.unicos.ms_pagamento.model.gateway.TransacaoPagamento;
import br.com.unicos.ms_pagamento.enums.StatusTransacao;
import br.com.unicos.ms_pagamento.enums.TipoFormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade TransacaoFinanceira.
 *
 * Fornece métodos personalizados para consultas específicas de transações,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface TransacaoFinanceiraRepository extends JpaRepository<TransacaoPagamento, Long> {

    /**
     * Busca uma transação financeira pelo código único atribuído a ela.
     *
     * @param codigoTransacao Código identificador da transação (ex: hash ou ID de gateway).
     * @return Optional contendo a transação, se encontrada.
     */
    Optional<TransacaoPagamento> findByCodigoTransacao(String codigoTransacao);

    /**
     * Lista todas as transações com um determinado status.
     *
     * @param status Status da transação (PROCESSANDO, CONFIRMADA, FALHA).
     * @return Lista de transações com o status informado.
     */
    List<TransacaoPagamento> findByStatus(StatusTransacao status);

    /**
     * Lista todas as transações de um tipo específico de pagamento.
     *
     * @param tipo Tipo de pagamento (PIX, CARTÃO, BOLETO, etc).
     * @return Lista de transações com o tipo informado.
     */
    List<TransacaoPagamento> findByTipo(TipoFormaPagamento tipo);

    /**
     * Lista todas as transações realizadas dentro de um intervalo de datas.
     *
     * @param inicio Data e hora inicial.
     * @param fim    Data e hora final.
     * @return Lista de transações dentro do intervalo informado.
     */
    List<TransacaoPagamento> findByDataTransacaoBetween(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Conta quantas transações apresentaram falha.
     *
     * @return Quantidade total de transações com status FALHA.
     */
    @Query("SELECT COUNT(t) FROM TransacaoFinanceira t WHERE t.status = 'FALHA'")
    Long contarTransacoesFalhas();
}