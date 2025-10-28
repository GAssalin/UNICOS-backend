package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.dto.ProdutoRequest;
import br.com.erp.ms_produtos.dto.ProdutoResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade Produto.
 *
 * Define os métodos para criação, atualização, exclusão e consultas específicas,
 * além de operações de ativação, inativação e atualização de preço.
 */
public interface ProdutoService {

    /**
     * Cria um novo produto a partir dos dados fornecidos.
     *
     * @param request Dados do produto a ser criado.
     * @return ProdutoResponse representando o produto criado.
     */
    ProdutoResponse salvar(ProdutoRequest request);

    /**
     * Atualiza os dados de um produto existente.
     *
     * @param id      Identificador do produto.
     * @param request Dados atualizados do produto.
     * @return ProdutoResponse atualizado.
     */
    ProdutoResponse atualizar(Long id, ProdutoRequest request);

    /**
     * Busca um produto pelo seu ID.
     *
     * @param id Identificador do produto.
     * @return Optional contendo o ProdutoResponse, se encontrado.
     */
    Optional<ProdutoResponse> buscarPorId(Long id);

    /**
     * Lista todos os produtos cadastrados.
     *
     * @return Lista de ProdutoResponse.
     */
    List<ProdutoResponse> listarTodos();

    /**
     * Exclui um produto com base no seu ID.
     *
     * @param id Identificador do produto.
     */
    void deletar(Long id);

    // ==================================
    // 🔹 MÉTODOS ESPECÍFICOS DE CONSULTA
    // ==================================

    /**
     * Busca um produto pelo seu SKU.
     *
     * @param sku Código SKU do produto.
     * @return Optional contendo o ProdutoResponse, se encontrado.
     */
    Optional<ProdutoResponse> buscarPorSku(String sku);

    /**
     * Busca produtos cujo nome contenha o termo informado.
     *
     * @param nome Termo de busca.
     * @return Lista de ProdutoResponse correspondentes.
     */
    List<ProdutoResponse> buscarPorNome(String nome);

    /**
     * Lista todos os produtos de uma determinada categoria.
     *
     * @param categoriaId ID da categoria.
     * @return Lista de ProdutoResponse pertencentes à categoria.
     */
    List<ProdutoResponse> listarPorCategoria(Long categoriaId);

    /**
     * Lista todos os produtos de uma determinada marca.
     *
     * @param marcaId ID da marca.
     * @return Lista de ProdutoResponse pertencentes à marca.
     */
    List<ProdutoResponse> listarPorMarca(Long marcaId);

    /**
     * Lista todos os produtos que estão ativos.
     *
     * @return Lista de ProdutoResponse ativos.
     */
    List<ProdutoResponse> listarAtivos();

    /**
     * Lista todos os produtos que estão inativos.
     *
     * @return Lista de ProdutoResponse inativos.
     */
    List<ProdutoResponse> listarInativos();

    /**
     * Lista produtos cujo preço esteja dentro de uma faixa específica.
     *
     * @param precoMin Valor mínimo.
     * @param precoMax Valor máximo.
     * @return Lista de ProdutoResponse dentro da faixa informada.
     */
    List<ProdutoResponse> listarPorFaixaDePreco(BigDecimal precoMin, BigDecimal precoMax);

    // ==================================
    // 🔹 OPERAÇÕES DE ESTADO E PREÇO
    // ==================================

    /**
     * Ativa um produto, definindo o campo "ativo" como true.
     *
     * @param id ID do produto.
     * @return ProdutoResponse atualizado.
     */
    ProdutoResponse ativarProduto(Long id);

    /**
     * Inativa um produto, definindo o campo "ativo" como false.
     *
     * @param id ID do produto.
     * @return ProdutoResponse atualizado.
     */
    ProdutoResponse inativarProduto(Long id);

    /**
     * Atualiza o preço de um produto e registra a alteração no histórico de preços.
     *
     * @param id         ID do produto.
     * @param novoPreco  Novo valor do preço.
     * @return ProdutoResponse com o preço atualizado.
     */
    ProdutoResponse atualizarPreco(Long id, BigDecimal novoPreco);

    /**
     * Verifica se um SKU já está em uso por outro produto.
     *
     * @param sku Código SKU do produto.
     * @return true se o SKU estiver disponível, false caso contrário.
     */
    boolean verificarDisponibilidadeSku(String sku);
}