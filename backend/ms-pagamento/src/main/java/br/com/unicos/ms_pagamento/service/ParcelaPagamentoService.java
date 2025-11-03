package br.com.unicos.ms_pagamento.service;

import br.com.unicos.ms_pagamento.dto.ParcelaPagamentoRequest;
import br.com.unicos.ms_pagamento.dto.ParcelaPagamentoResponse;
import br.com.unicos.ms_pagamento.enums.StatusParcela;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade ParcelaPagamento.
 *
 * Define métodos para criação, atualização, exclusão e consultas específicas,
 * além de operações de quitação e identificação de parcelas em atraso.
 */
public interface ParcelaPagamentoService {

    /**
     * Cria uma nova parcela de pagamento.
     *
     * @param request Dados da parcela a ser criada.
     * @return ParcelaPagamentoResponse representando a parcela criada.
     */
    ParcelaPagamentoResponse salvar(ParcelaPagamentoRequest request);

    /**
     * Atualiza os dados de uma parcela existente.
     *
     * @param id      Identificador da parcela.
     * @param request Dados atualizados da parcela.
     * @return ParcelaPagamentoResponse atualizada.
     */
    ParcelaPagamentoResponse atualizar(Long id, ParcelaPagamentoRequest request);

    /**
     * Busca uma parcela pelo seu ID.
     *
     * @param id Identificador da parcela.
     * @return Optional contendo a ParcelaPagamentoResponse, se encontrada.
     */
    Optional<ParcelaPagamentoResponse> buscarPorId(Long id);

    /**
     * Lista todas as parcelas cadastradas.
     *
     * @return Lista de ParcelaPagamentoResponse.
     */
    List<ParcelaPagamentoResponse> listarTodas();

    /**
     * Exclui uma parcela com base no seu ID.
     *
     * @param id Identificador da parcela.
     */
    void deletar(Long id);

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Lista todas as parcelas associadas a um pagamento específico.
     *
     * @param pagamentoId ID do pagamento.
     * @return Lista de parcelas vinculadas ao pagamento informado.
     */
    List<ParcelaPagamentoResponse> listarPorPagamento(Long pagamentoId);

    /**
     * Lista parcelas com um determinado status.
     *
     * @param status Status da parcela (PENDENTE, QUITADA, ATRASADA).
     * @return Lista de parcelas com o status informado.
     */
    List<ParcelaPagamentoResponse> listarPorStatus(StatusParcela status);

    /**
     * Lista parcelas com vencimento anterior à data informada.
     *
     * @param data Data limite de vencimento.
     * @return Lista de parcelas vencidas antes da data informada.
     */
    List<ParcelaPagamentoResponse> listarVencidasAntesDe(LocalDate data);

    /**
     * Lista parcelas em atraso (vencidas e não quitadas).
     *
     * @return Lista de parcelas em atraso.
     */
    List<ParcelaPagamentoResponse> listarAtrasadas();

    // ==================================
    // 💼 OPERAÇÕES DE NEGÓCIO
    // ==================================

    /**
     * Marca uma parcela como quitada.
     *
     * @param id ID da parcela a ser quitada.
     * @return ParcelaPagamentoResponse atualizada com o status QUITADA.
     */
    ParcelaPagamentoResponse quitarParcela(Long id);

    /**
     * Atualiza o status de uma parcela.
     *
     * @param id     ID da parcela.
     * @param status Novo status.
     * @return ParcelaPagamentoResponse atualizada.
     */
    ParcelaPagamentoResponse atualizarStatus(Long id, StatusParcela status);
}
