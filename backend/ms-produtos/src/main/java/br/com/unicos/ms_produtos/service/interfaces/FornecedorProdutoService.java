package br.com.unicos.ms_produtos.service.interfaces;

import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoListDTO;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoRequest;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas regras de negócio relacionadas
 * ao vínculo entre fornecedores e produtos.
 *
 * <p>
 * Um fornecedor pode fornecer múltiplos produtos e um produto
 * pode possuir múltiplos fornecedores, cada um com condições
 * comerciais próprias.
 * </p>
 */
public interface FornecedorProdutoService {

    /**
     * Cria um novo vínculo entre fornecedor e produto.
     *
     * @param request Dados para criação do vínculo.
     * @return DTO representando o vínculo criado.
     */
    FornecedorProdutoResponse salvar(FornecedorProdutoRequest request);

    /**
     * Atualiza os dados de um vínculo existente.
     *
     * @param id      Identificador do vínculo.
     * @param request Dados atualizados.
     * @return DTO representando o vínculo atualizado.
     */
    FornecedorProdutoResponse atualizar(Long id, FornecedorProdutoRequest request);

    /**
     * Remove um vínculo entre fornecedor e produto.
     *
     * @param id Identificador do vínculo.
     */
    void deletar(Long id);

    /**
     * Busca um vínculo específico pelo seu identificador.
     *
     * @param id Identificador do vínculo.
     * @return DTO do vínculo, se encontrado.
     */
    Optional<FornecedorProdutoResponse> buscarPorId(Long id);

    /**
     * Lista todos os vínculos cadastrados.
     *
     * @return Lista completa de vínculos.
     */
    List<FornecedorProdutoResponse> listarTodos();

    /**
     * Lista todos os fornecedores associados a um produto.
     *
     * @param produtoId ID do produto.
     * @return Lista simplificada de vínculos.
     */
    List<FornecedorProdutoListDTO> listarPorProduto(Long produtoId);

    /**
     * Lista todos os produtos associados a um fornecedor.
     *
     * @param fornecedorId ID do fornecedor.
     * @return Lista simplificada de vínculos.
     */
    List<FornecedorProdutoListDTO> listarPorFornecedor(Long fornecedorId);

    /**
     * Atualiza apenas o preço de custo do vínculo.
     *
     * @param id             ID do vínculo.
     * @param novoPrecoCusto Novo preço de custo.
     * @return DTO atualizado.
     */
    FornecedorProdutoResponse atualizarPrecoCusto(Long id, BigDecimal novoPrecoCusto);

    /**
     * Verifica se já existe vínculo entre fornecedor e produto.
     *
     * @param fornecedorId ID do fornecedor.
     * @param produtoId    ID do produto.
     * @return true se já existir vínculo, false caso contrário.
     */
    boolean existeVinculo(Long fornecedorId, Long produtoId);
}
