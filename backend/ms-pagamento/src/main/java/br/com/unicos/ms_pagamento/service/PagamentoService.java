package br.com.unicos.ms_pagamento.service;

import br.com.unicos.ms_pagamento.dto.PagamentoRequest;
import br.com.unicos.ms_pagamento.dto.PagamentoResponse;
import br.com.unicos.ms_pagamento.enums.StatusPagamento;
import br.com.unicos.ms_pagamento.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade Pagamento.
 *
 * Define métodos para criação, atualização, exclusão e consultas específicas,
 * além de operações financeiras e de status.
 */
public interface PagamentoService {

    /**
     * Registra um novo pagamento a partir dos dados fornecidos.
     *
     * @param request Dados do pagamento a ser criado.
     * @return PagamentoResponse representando o pagamento criado.
     */
    PagamentoResponse salvar(PagamentoRequest request);

    /**
     * Atualiza os dados de um pagamento existente.
     *
     * @param id      Identificador do pagamento.
     * @param request Dados atualizados do pagamento.
     * @return PagamentoResponse atualizado.
     */
    PagamentoResponse atualizar(Long id, PagamentoRequest request);

    /**
     * Busca um pagamento pelo seu ID.
     *
     * @param id Identificador do pagamento.
     * @return Optional contendo o PagamentoResponse, se encontrado.
     */
    Optional<PagamentoResponse> buscarPorId(Long id);

    /**
     * Lista todos os pagamentos cadastrados.
     *
     * @return Lista de PagamentoResponse.
     */
    List<PagamentoResponse> listarTodos();

    /**
     * Exclui um pagamento com base no seu ID.
     *
     * @param id Identificador do pagamento.
     */
    void deletar(Long id);

    // ==================================
    // 🔹 MÉTODOS DE CONSULTA
    // ==================================

    /**
     * Lista pagamentos com base no status informado.
     *
     * @param status Status do pagamento (PENDENTE, PAGO, CANCELADO, etc.).
     * @return Lista de PagamentoResponse com o status informado.
     */
    List<PagamentoResponse> listarPorStatus(StatusPagamento status);

    /**
     * Lista pagamentos realizados entre duas datas.
     *
     * @param inicio Data inicial.
     * @param fim    Data final.
     * @return Lista de pagamentos realizados no período.
     */
    List<PagamentoResponse> listarPorPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Lista pagamentos filtrados por tipo de transação.
     *
     * @param tipo Tipo da transação (VENDA, COMPRA, OUTROS).
     * @return Lista de pagamentos correspondentes ao tipo informado.
     */
    List<PagamentoResponse> listarPorTipoTransacao(TipoTransacao tipo);

    /**
     * Lista pagamentos que estão atrasados.
     *
     * @return Lista de pagamentos com vencimento anterior à data atual e não pagos.
     */
    List<PagamentoResponse> listarAtrasados();

    // ==================================
    // 💼 OPERAÇÕES FINANCEIRAS
    // ==================================

    /**
     * Atualiza o status de um pagamento.
     *
     * @param id     ID do pagamento.
     * @param status Novo status do pagamento.
     * @return PagamentoResponse atualizado.
     */
    PagamentoResponse atualizarStatus(Long id, StatusPagamento status);

    /**
     * Calcula o valor total de todos os pagamentos quitados.
     *
     * @return Valor total de pagamentos pagos.
     */
    BigDecimal calcularTotalPago();

    /**
     * Calcula o valor total de todos os pagamentos pendentes.
     *
     * @return Valor total de pagamentos pendentes.
     */
    BigDecimal calcularTotalPendente();
}
