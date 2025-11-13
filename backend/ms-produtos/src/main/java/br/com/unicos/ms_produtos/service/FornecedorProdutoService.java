package br.com.unicos.ms_produtos.service;

import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoRequest;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoResponse;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoListDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade FornecedorProduto.
 */
public interface FornecedorProdutoService {

    /**
     * Cria um novo vínculo entre fornecedor e produto.
     *
     * @param request Dados para criação do vínculo.
     * @return FornecedorProdutoResponse criado.
     */
    FornecedorProdutoResponse salvar(FornecedorProdutoRequest request);

    /**
     * Atualiza um vínculo existente entre fornecedor e produto.
     *
     * @param id ID do vínculo.
     * @param request Dados atualizados.
     * @return FornecedorProdutoResponse atualizado.
     */
    FornecedorProdutoResponse atualizar(Long id, FornecedorProdutoRequest request);

    /**
     * Busca um vínculo específico por ID.
     *
     * @param id ID do vínculo.
     * @return FornecedorProdutoResponse correspondente.
     */
    Optional<FornecedorProdutoResponse> buscarPorId(Long id);

    /**
     * Lista todos os vínculos registrados.
     *
     * @return Lista de FornecedorProdutoResponse.
     */
    List<FornecedorProdutoResponse> listarTodos();

    /**
     * Lista vínculos de um determinado produto.
     *
     * @param produtoId ID do produto.
     * @return Lista de FornecedorProdutoListDTO.
     */
    List<FornecedorProdutoListDTO> listarPorProduto(Long produtoId);

    /**
     * Lista vínculos de um determinado fornecedor.
     *
     * @param fornecedorId ID do fornecedor.
     * @return Lista de FornecedorProdutoListDTO.
     */
    List<FornecedorProdutoListDTO> listarPorFornecedor(Long fornecedorId);

    /**
     * Atualiza apenas o preço de custo de um vínculo existente.
     *
     * @param id ID do vínculo.
     * @param novoPrecoCusto Novo valor.
     * @return FornecedorProdutoResponse atualizado.
     */
    FornecedorProdutoResponse atualizarPrecoCusto(Long id, BigDecimal novoPrecoCusto);

    /**
     * Remove um vínculo entre fornecedor e produto.
     *
     * @param id ID do vínculo.
     */
    void deletar(Long id);

    /**
     * Verifica se já existe um vínculo entre o fornecedor e o produto.
     *
     * @param fornecedorId ID do fornecedor.
     * @param produtoId ID do produto.
     * @return true se já existir, false caso contrário.
     */
    boolean existeVinculo(Long fornecedorId, Long produtoId);
}