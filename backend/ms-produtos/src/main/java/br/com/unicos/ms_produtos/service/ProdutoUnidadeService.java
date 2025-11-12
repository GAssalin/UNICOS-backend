package br.com.unicos.ms_produtos.service;

import br.com.unicos.ms_produtos.dto.ProdutoUnidadeRequest;
import br.com.unicos.ms_produtos.dto.ProdutoUnidadeResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade ProdutoUnidade.
 *
 * Define os métodos para criação, atualização, exclusão e consultas
 * de vínculos entre produtos e unidades de medida.
 */
public interface ProdutoUnidadeService {

    /**
     * Cria um novo vínculo entre produto e unidade de medida.
     *
     * @param request Dados do vínculo.
     * @return ProdutoUnidadeResponse criado.
     */
    ProdutoUnidadeResponse salvar(ProdutoUnidadeRequest request);

    /**
     * Atualiza um vínculo existente entre produto e unidade de medida.
     *
     * @param id      ID do vínculo.
     * @param request Dados atualizados.
     * @return ProdutoUnidadeResponse atualizado.
     */
    ProdutoUnidadeResponse atualizar(Long id, ProdutoUnidadeRequest request);

    /**
     * Busca um vínculo pelo seu ID.
     *
     * @param id Identificador do vínculo.
     * @return Optional contendo o ProdutoUnidadeResponse, se encontrado.
     */
    Optional<ProdutoUnidadeResponse> buscarPorId(Long id);

    /**
     * Lista todos os vínculos produto–unidade cadastrados.
     *
     * @return Lista de ProdutoUnidadeResponse.
     */
    List<ProdutoUnidadeResponse> listarTodos();

    /**
     * Lista todos os vínculos associados a um produto específico.
     *
     * @param produtoId ID do produto.
     * @return Lista de ProdutoUnidadeResponse.
     */
    List<ProdutoUnidadeResponse> listarPorProduto(Long produtoId);

    /**
     * Lista todos os vínculos associados a uma unidade de medida específica.
     *
     * @param unidadeMedidaId ID da unidade de medida.
     * @return Lista de ProdutoUnidadeResponse.
     */
    List<ProdutoUnidadeResponse> listarPorUnidadeMedida(Long unidadeMedidaId);

    /**
     * Verifica se já existe um vínculo entre um produto e uma unidade de medida.
     *
     * @param produtoId       ID do produto.
     * @param unidadeMedidaId ID da unidade de medida.
     * @return true se existir, false caso contrário.
     */
    boolean verificarVinculo(Long produtoId, Long unidadeMedidaId);

    /**
     * Exclui um vínculo produto–unidade.
     *
     * @param id ID do vínculo a ser removido.
     */
    void deletar(Long id);
}