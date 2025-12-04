package br.com.unicos.ms_produtos.service;

import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoListDTO;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoRequest;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas aos registros de histórico de preço dos produtos.
 */
public interface HistoricoPrecoService {

    /**
     * Registra um novo histórico de preço para um produto.
     *
     * @param produtoId ID do produto ao qual o histórico pertence.
     * @param request   Dados do histórico a ser registrado.
     * @return HistoricoPrecoResponse com os dados do registro criado.
     */
    HistoricoPrecoResponse salvar(Long produtoId, HistoricoPrecoRequest request);

    /**
     * Busca um registro de histórico de preço pelo ID.
     *
     * @param id Identificador do histórico.
     * @return HistoricoPrecoResponse encapsulado em Optional.
     */
    Optional<HistoricoPrecoResponse> buscarPorId(Long id);

    /**
     * Lista todos os históricos de preço cadastrados,
     * ordenados pela data de alteração (mais recentes primeiro).
     *
     * @return Lista de registros completos.
     */
    List<HistoricoPrecoResponse> listarTodos();

    /**
     * Lista todos os históricos de preço vinculados a um produto,
     * ordenados pela data de alteração (mais recentes primeiro).
     *
     * @param produtoId ID do produto.
     * @return Lista de registros completos.
     */
    List<HistoricoPrecoResponse> listarPorProduto(Long produtoId);

    /**
     * Lista os últimos 10 registros de histórico de preço de um produto.
     *
     * @param produtoId ID do produto.
     * @return Lista resumida dos últimos registros.
     */
    List<HistoricoPrecoListDTO> listarUltimosPorProduto(Long produtoId);

    /**
     * Exclui um registro de histórico de preço.
     *
     * @param id Identificador do histórico.
     */
    void deletar(Long id);
}
