package br.com.unicos.ms_produtos.service;

import br.com.unicos.ms_produtos.dto.HistoricoPrecoListDTO;
import br.com.unicos.ms_produtos.dto.HistoricoPrecoRequest;
import br.com.unicos.ms_produtos.dto.HistoricoPrecoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade HistoricoPreco.
 */
public interface HistoricoPrecoService {

    /**
     * Registra um novo histórico de preço.
     *
     * @param request Dados do histórico.
     * @return HistoricoPrecoResponse criado.
     */
    HistoricoPrecoResponse salvar(HistoricoPrecoRequest request);

    /**
     * Busca um histórico de preço pelo ID.
     *
     * @param id Identificador do histórico.
     * @return HistoricoPrecoResponse, se encontrado.
     */
    Optional<HistoricoPrecoResponse> buscarPorId(Long id);

    /**
     * Lista todos os históricos de preço cadastrados, ordenados por data de alteração (decrescente).
     *
     * @return Lista de HistoricoPrecoResponse.
     */
    List<HistoricoPrecoResponse> listarTodos();

    /**
     * Lista os históricos de preço de um produto específico.
     *
     * @param produtoId ID do produto.
     * @return Lista de HistoricoPrecoResponse ordenada por data (decrescente).
     */
    List<HistoricoPrecoResponse> listarPorProduto(Long produtoId);

    /**
     * Lista os últimos 10 registros de alteração de preço de um produto.
     *
     * @param produtoId ID do produto.
     * @return Lista de HistoricoPrecoListDTO.
     */
    List<HistoricoPrecoListDTO> listarUltimosPorProduto(Long produtoId);

    /**
     * Exclui um histórico de preço pelo ID.
     *
     * @param id Identificador do histórico.
     */
    void deletar(Long id);
}