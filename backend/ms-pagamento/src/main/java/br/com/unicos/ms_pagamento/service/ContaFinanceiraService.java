package br.com.unicos.ms_pagamento.service;

import br.com.unicos.ms_pagamento.dto.ContaFinanceiraRequest;
import br.com.unicos.ms_pagamento.dto.ContaFinanceiraResponse;
import br.com.unicos.ms_pagamento.enums.TipoConta;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade ContaFinanceira.
 *
 * Define métodos para criação, atualização, exclusão e consultas específicas,
 * além de operações financeiras como cálculo de saldos e análise de contas.
 */
public interface ContaFinanceiraService {

    /**
     * Cria uma nova conta financeira.
     *
     * @param request Dados da conta a ser criada.
     * @return ContaFinanceiraResponse representando a conta criada.
     */
    ContaFinanceiraResponse salvar(ContaFinanceiraRequest request);

    /**
     * Atualiza os dados de uma conta existente.
     *
     * @param id      Identificador da conta financeira.
     * @param request Dados atualizados da conta.
     * @return ContaFinanceiraResponse atualizada.
     */
    ContaFinanceiraResponse atualizar(Long id, ContaFinanceiraRequest request);

    /**
     * Busca uma conta financeira pelo seu ID.
     *
     * @param id Identificador da conta.
     * @return Optional contendo a ContaFinanceiraResponse, se encontrada.
     */
    Optional<ContaFinanceiraResponse> buscarPorId(Long id);

    /**
     * Lista todas as contas financeiras cadastradas.
     *
     * @return Lista de ContaFinanceiraResponse.
     */
    List<ContaFinanceiraResponse> listarTodas();

    /**
     * Exclui uma conta financeira com base no seu ID.
     *
     * @param id Identificador da conta.
     */
    void deletar(Long id);

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Lista todas as contas de um tipo específico.
     *
     * @param tipoConta Tipo da conta (CAIXA, CONTA_CORRENTE, CONTA_POUPANCA).
     * @return Lista de contas do tipo informado.
     */
    List<ContaFinanceiraResponse> listarPorTipo(TipoConta tipoConta);

    /**
     * Busca contas cuja descrição contenha o termo informado.
     *
     * @param descricao Termo de busca parcial.
     * @return Lista de contas com a descrição correspondente.
     */
    List<ContaFinanceiraResponse> buscarPorDescricao(String descricao);

    // ==================================
    // 💼 OPERAÇÕES FINANCEIRAS
    // ==================================

    /**
     * Calcula o saldo total consolidado de todas as contas.
     *
     * @return Valor total somado dos saldos das contas.
     */
    BigDecimal calcularSaldoTotal();

    /**
     * Conta quantas contas estão com saldo negativo.
     *
     * @return Quantidade de contas com saldo abaixo de zero.
     */
    Long contarContasNegativas();

    /**
     * Atualiza o saldo de uma conta financeira.
     *
     * @param id     ID da conta.
     * @param valor  Novo valor do saldo.
     * @return ContaFinanceiraResponse com o saldo atualizado.
     */
    ContaFinanceiraResponse atualizarSaldo(Long id, BigDecimal valor);
}
