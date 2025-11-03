package br.com.unicos.ms_pagamento.service;

import br.com.unicos.ms_pagamento.dto.TransacaoFinanceiraRequest;
import br.com.unicos.ms_pagamento.dto.TransacaoFinanceiraResponse;
import br.com.unicos.ms_pagamento.enums.StatusTransacao;
import br.com.unicos.ms_pagamento.enums.TipoFormaPagamento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade TransacaoFinanceira.
 *
 * Define métodos para criação, atualização, exclusão e consultas específicas,
 * além de operações de análise e controle de status de transações.
 */
public interface TransacaoFinanceiraService {

    /**
     * Cria uma nova transação financeira.
     *
     * @param request Dados da transação a ser criada.
     * @return TransacaoFinanceiraResponse representando a transação criada.
     */
    TransacaoFinanceiraResponse salvar(TransacaoFinanceiraRequest request);

    /**
     * Atualiza uma transação existente.
     *
     * @param id      Identificador da transação.
     * @param request Dados atualizados da transação.
     * @return TransacaoFinanceiraResponse atualizada.
     */
    TransacaoFinanceiraResponse atualizar(Long id, TransacaoFinanceiraRequest request);

    /**
     * Busca uma transação pelo seu ID.
     *
     * @param id Identificador da transação.
     * @return Optional contendo a TransacaoFinanceiraResponse, se encontrada.
     */
    Optional<TransacaoFinanceiraResponse> buscarPorId(Long id);

    /**
     * Lista todas as transações cadastradas.
     *
     * @return Lista de TransacaoFinanceiraResponse.
     */
    List<TransacaoFinanceiraResponse> listarTodas();

    /**
     * Exclui uma transação com base no seu ID.
     *
     * @param id Identificador da transação.
     */
    void deletar(Long id);

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Busca uma transação pelo código identificador único.
     *
     * @param codigoTransacao Código da transação (ex: hash de gateway).
     * @return Optional contendo a transação, se encontrada.
     */
    Optional<TransacaoFinanceiraResponse> buscarPorCodigo(String codigoTransacao);

    /**
     * Lista transações filtradas por status.
     *
     * @param status Status da transação (PROCESSANDO, CONFIRMADA, FALHA).
     * @return Lista de transações com o status informado.
     */
    List<TransacaoFinanceiraResponse> listarPorStatus(StatusTransacao status);

    /**
     * Lista transações por tipo de pagamento.
     *
     * @param tipo Tipo de pagamento (PIX, CARTÃO, BOLETO, etc).
     * @return Lista de transações do tipo informado.
     */
    List<TransacaoFinanceiraResponse> listarPorTipo(TipoFormaPagamento tipo);

    /**
     * Lista transações realizadas entre duas datas e horários.
     *
     * @param inicio Data/hora inicial.
     * @param fim    Data/hora final.
     * @return Lista de transações dentro do período informado.
     */
    List<TransacaoFinanceiraResponse> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);

    // ==================================
    // 💼 OPERAÇÕES DE NEGÓCIO
    // ==================================

    /**
     * Atualiza o status de uma transação financeira.
     *
     * @param id     ID da transação.
     * @param status Novo status da transação.
     * @return TransacaoFinanceiraResponse atualizada.
     */
    TransacaoFinanceiraResponse atualizarStatus(Long id, StatusTransacao status);

    /**
     * Conta quantas transações apresentaram falha.
     *
     * @return Quantidade de transações com status FALHA.
     */
    Long contarTransacoesFalhas();
}
