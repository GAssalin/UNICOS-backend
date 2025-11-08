package br.com.unicos.ms_pagamento.service.caixa;

import br.com.unicos.ms_pagamento.dto.caixa.MovimentoCaixaRequest;
import br.com.unicos.ms_pagamento.dto.caixa.MovimentoCaixaResponse;
import br.com.unicos.ms_pagamento.enums.TipoMovimentoCaixa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade MovimentoCaixa.
 *
 * Define métodos para criação, atualização, exclusão e consultas específicas,
 * além de operações de cálculo de totais e fluxo financeiro.
 */
public interface MovimentoCaixaService {

    /**
     * Registra um novo movimento de caixa (entrada ou saída).
     *
     * @param request Dados do movimento a ser registrado.
     * @return MovimentoCaixaResponse representando o movimento criado.
     */
    MovimentoCaixaResponse salvar(MovimentoCaixaRequest request);

    /**
     * Atualiza os dados de um movimento existente.
     *
     * @param id      Identificador do movimento.
     * @param request Dados atualizados do movimento.
     * @return MovimentoCaixaResponse atualizado.
     */
    MovimentoCaixaResponse atualizar(Long id, MovimentoCaixaRequest request);

    /**
     * Busca um movimento de caixa pelo seu ID.
     *
     * @param id Identificador do movimento.
     * @return Optional contendo o MovimentoCaixaResponse, se encontrado.
     */
    Optional<MovimentoCaixaResponse> buscarPorId(Long id);

    /**
     * Lista todos os movimentos de caixa registrados.
     *
     * @return Lista de MovimentoCaixaResponse.
     */
    List<MovimentoCaixaResponse> listarTodos();

    /**
     * Exclui um movimento de caixa com base no seu ID.
     *
     * @param id Identificador do movimento.
     */
    void deletar(Long id);

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Lista todos os movimentos de um determinado tipo (ENTRADA ou SAIDA).
     *
     * @param tipoMovimento Tipo de movimento.
     * @return Lista de movimentos do tipo informado.
     */
    List<MovimentoCaixaResponse> listarPorTipo(TipoMovimentoCaixa tipoMovimento);

    /**
     * Lista todos os movimentos realizados em uma data específica.
     *
     * @param dataMovimento Data do movimento.
     * @return Lista de movimentos realizados na data informada.
     */
    List<MovimentoCaixaResponse> listarPorData(LocalDate dataMovimento);

    // ==================================
    // 💼 OPERAÇÕES FINANCEIRAS
    // ==================================

    /**
     * Calcula o valor total de todas as entradas registradas.
     *
     * @return Soma dos valores de entrada.
     */
    BigDecimal calcularTotalEntradas();

    /**
     * Calcula o valor total de todas as saídas registradas.
     *
     * @return Soma dos valores de saída.
     */
    BigDecimal calcularTotalSaidas();

    /**
     * Calcula o total de entradas realizadas no dia atual.
     *
     * @return Valor total de entradas do dia.
     */
    BigDecimal totalEntradasHoje();

    /**
     * Calcula o total de saídas realizadas no dia atual.
     *
     * @return Valor total de saídas do dia.
     */
    BigDecimal totalSaidasHoje();
}
