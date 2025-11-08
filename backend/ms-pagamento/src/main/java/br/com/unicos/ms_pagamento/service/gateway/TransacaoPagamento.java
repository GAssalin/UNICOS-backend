package br.com.unicos.ms_pagamento.service.gateway;

import br.com.unicos.ms_pagamento.enums.TipoFormaPagamento;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade FormaPagamento.
 *
 * Define métodos para criação, atualização, exclusão e consultas específicas,
 * além de operações de ativação e inativação de formas de pagamento.
 */
public interface TransacaoPagamento {

    /**
     * Cria uma nova forma de pagamento a partir dos dados fornecidos.
     *
     * @param request Dados da forma de pagamento a ser criada.
     * @return FormaPagamentoResponse representando a forma criada.
     */
    FormaPagamentoResponse salvar(FormaPagamentoRequest request);

    /**
     * Atualiza uma forma de pagamento existente.
     *
     * @param id      Identificador da forma de pagamento.
     * @param request Dados atualizados da forma de pagamento.
     * @return FormaPagamentoResponse atualizada.
     */
    FormaPagamentoResponse atualizar(Long id, FormaPagamentoRequest request);

    /**
     * Busca uma forma de pagamento pelo seu ID.
     *
     * @param id Identificador da forma de pagamento.
     * @return Optional contendo a FormaPagamentoResponse, se encontrada.
     */
    Optional<FormaPagamentoResponse> buscarPorId(Long id);

    /**
     * Lista todas as formas de pagamento cadastradas.
     *
     * @return Lista de FormaPagamentoResponse.
     */
    List<FormaPagamentoResponse> listarTodas();

    /**
     * Exclui uma forma de pagamento com base no seu ID.
     *
     * @param id Identificador da forma de pagamento.
     */
    void deletar(Long id);

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Lista todas as formas de pagamento ativas.
     *
     * @return Lista de formas de pagamento com o campo "ativo" igual a true.
     */
    List<FormaPagamentoResponse> listarAtivas();

    /**
     * Lista todas as formas de pagamento de um tipo específico.
     *
     * @param tipo Tipo de forma de pagamento (DINHEIRO, PIX, CARTÃO, etc).
     * @return Lista de formas de pagamento do tipo informado.
     */
    List<FormaPagamentoResponse> listarPorTipo(TipoFormaPagamento tipo);

    /**
     * Busca formas de pagamento cuja descrição contenha o termo informado.
     *
     * @param descricao Termo parcial da descrição.
     * @return Lista de formas de pagamento correspondentes ao termo informado.
     */
    List<FormaPagamentoResponse> buscarPorDescricao(String descricao);

    // ==================================
    // 💼 OPERAÇÕES DE NEGÓCIO
    // ==================================

    /**
     * Ativa uma forma de pagamento, definindo o campo "ativo" como true.
     *
     * @param id ID da forma de pagamento.
     * @return FormaPagamentoResponse atualizada.
     */
    FormaPagamentoResponse ativar(Long id);

    /**
     * Inativa uma forma de pagamento, definindo o campo "ativo" como false.
     *
     * @param id ID da forma de pagamento.
     * @return FormaPagamentoResponse atualizada.
     */
    FormaPagamentoResponse inativar(Long id);
}
