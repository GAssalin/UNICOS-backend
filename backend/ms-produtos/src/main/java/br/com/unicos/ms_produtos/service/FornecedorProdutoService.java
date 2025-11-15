package br.com.unicos.ms_produtos.service;

import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoRequest;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoResponse;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoListDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas regras de negócio relacionadas ao vínculo
 * entre fornecedores e produtos.
 *
 * <p>
 * Um fornecedor pode fornecer múltiplos produtos e um produto pode possuir
 * diversos fornecedores, cada um com preços, códigos internos e prazos
 * específicos.
 * </p>
 */
public interface FornecedorProdutoService {

    // ============================================================
    // 🔹 CRUD padrão
    // ============================================================

    /**
     * Cria um novo vínculo entre fornecedor e produto.
     *
     * @param request DTO contendo os dados para criação.
     * @return DTO da entidade criada.
     */
    FornecedorProdutoResponse salvar(FornecedorProdutoRequest request);

    /**
     * Atualiza os dados de um vínculo existente.
     *
     * @param id ID do vínculo.
     * @param request DTO contendo os dados atualizados.
     * @return DTO da entidade atualizada.
     */
    FornecedorProdutoResponse atualizar(Long id, FornecedorProdutoRequest request);

    /**
     * Remove um vínculo entre fornecedor e produto.
     *
     * @param id Identificador do vínculo.
     */
    void deletar(Long id);

    // ============================================================
    // 🔹 Consultas
    // ============================================================

    /**
     * Busca um vínculo específico pelo seu identificador.
     *
     * @param id ID do vínculo.
     * @return FornecedorProdutoResponse, se encontrado.
     */
    Optional<FornecedorProdutoResponse> buscarPorId(Long id);

    /**
     * Lista todos os vínculos cadastrados no sistema.
     *
     * @return Lista de FornecedorProdutoResponse.
     */
    List<FornecedorProdutoResponse> listarTodos();

    /**
     * Lista todos os fornecedores associados a um determinado produto.
     *
     * @param produtoId ID do produto.
     * @return Lista simplificada de vínculos.
     */
    List<FornecedorProdutoListDTO> listarPorProduto(Long produtoId);

    /**
     * Lista todos os produtos associados a um determinado fornecedor.
     *
     * @param fornecedorId ID do fornecedor.
     * @return Lista simplificada de vínculos.
     */
    List<FornecedorProdutoListDTO> listarPorFornecedor(Long fornecedorId);

    // ============================================================
    // 🔹 Operações específicas
    // ============================================================

    /**
     * Atualiza apenas o preço de custo do vínculo.
     *
     * @param id ID do vínculo.
     * @param novoPrecoCusto Novo preço de custo.
     * @return DTO atualizado.
     */
    FornecedorProdutoResponse atualizarPrecoCusto(Long id, BigDecimal novoPrecoCusto);

    // ============================================================
    // 🔹 Validações
    // ============================================================

    /**
     * Verifica se já existe um vínculo entre um fornecedor e um produto.
     *
     * @param fornecedorId ID do fornecedor.
     * @param produtoId ID do produto.
     * @return true se já houver vínculo, false caso contrário.
     */
    boolean existeVinculo(Long fornecedorId, Long produtoId);
}
