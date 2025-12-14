package br.com.unicos.ms_produtos.service.interfaces;

import br.com.unicos.ms_produtos.dto.produto.ProdutoRequest;
import br.com.unicos.ms_produtos.dto.produto.ProdutoResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade Produto.
 *
 * <p>
 * Este serviço opera em um contexto <strong>multi-tenant</strong>.
 * O isolamento por empresa (tenant) é tratado internamente pela camada
 * de serviço, utilizando o {@code TenantContext}.
 * </p>
 *
 * <p>
 * Nenhum método expõe ou recebe {@code empresaId} como parâmetro,
 * garantindo encapsulamento, segurança e padronização da arquitetura.
 * </p>
 */
public interface ProdutoService {

    /**
     * Cria um novo produto no contexto da empresa atual.
     *
     * @param request Dados do produto a ser criado.
     * @return ProdutoResponse representando o produto criado.
     */
    ProdutoResponse salvar(ProdutoRequest request);

    /**
     * Atualiza os dados de um produto existente da empresa atual.
     *
     * @param id      Identificador do produto.
     * @param request Dados atualizados do produto.
     * @return ProdutoResponse atualizado.
     */
    ProdutoResponse atualizar(Long id, ProdutoRequest request);

    /**
     * Busca um produto pelo seu ID, restrito à empresa atual.
     *
     * @param id Identificador do produto.
     * @return Optional contendo o ProdutoResponse, se encontrado.
     */
    Optional<ProdutoResponse> buscarPorId(Long id);

    /**
     * Lista todos os produtos pertencentes à empresa atual.
     *
     * @return Lista de ProdutoResponse.
     */
    List<ProdutoResponse> listarTodos();

    /**
     * Exclui um produto da empresa atual.
     *
     * @param id Identificador do produto.
     */
    void deletar(Long id);

    // ============================================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ============================================================

    /**
     * Busca um produto pelo SKU no contexto da empresa atual.
     *
     * @param sku Código SKU do produto.
     * @return Optional contendo o ProdutoResponse, se encontrado.
     */
    Optional<ProdutoResponse> buscarPorSku(String sku);

    /**
     * Busca produtos cujo nome contenha o termo informado,
     * restrito à empresa atual.
     *
     * @param nome Termo de busca.
     * @return Lista de ProdutoResponse correspondentes.
     */
    List<ProdutoResponse> buscarPorNome(String nome);

    /**
     * Lista todos os produtos de uma categoria específica
     * pertencentes à empresa atual.
     *
     * @param categoriaId ID da categoria.
     * @return Lista de ProdutoResponse.
     */
    List<ProdutoResponse> listarPorCategoria(Long categoriaId);

    /**
     * Lista todos os produtos de uma marca específica
     * pertencentes à empresa atual.
     *
     * @param marcaId ID da marca.
     * @return Lista de ProdutoResponse.
     */
    List<ProdutoResponse> listarPorMarca(Long marcaId);

    /**
     * Lista todos os produtos ativos da empresa atual.
     *
     * @return Lista de ProdutoResponse ativos.
     */
    List<ProdutoResponse> listarAtivos();

    /**
     * Lista todos os produtos inativos da empresa atual.
     *
     * @return Lista de ProdutoResponse inativos.
     */
    List<ProdutoResponse> listarInativos();

    /**
     * Lista produtos cujo preço esteja dentro de uma faixa específica,
     * restrito à empresa atual.
     *
     * @param precoMin Valor mínimo.
     * @param precoMax Valor máximo.
     * @return Lista de ProdutoResponse.
     */
    List<ProdutoResponse> listarPorFaixaDePreco(
            BigDecimal precoMin,
            BigDecimal precoMax
    );

    // ============================================================
    // 🔹 OPERAÇÕES DE ESTADO E PREÇO
    // ============================================================

    /**
     * Ativa um produto da empresa atual.
     *
     * @param id ID do produto.
     * @return ProdutoResponse atualizado.
     */
    ProdutoResponse ativarProduto(Long id);

    /**
     * Inativa um produto da empresa atual.
     *
     * @param id ID do produto.
     * @return ProdutoResponse atualizado.
     */
    ProdutoResponse inativarProduto(Long id);

    /**
     * Atualiza o preço de um produto da empresa atual
     * e registra automaticamente o histórico de preços.
     *
     * @param id        ID do produto.
     * @param novoPreco Novo valor do preço.
     * @return ProdutoResponse atualizado.
     */
    ProdutoResponse atualizarPreco(Long id, BigDecimal novoPreco);

    /**
     * Verifica se um SKU está disponível para uso
     * dentro da empresa atual.
     *
     * @param sku Código SKU do produto.
     * @return {@code true} se disponível, {@code false} caso contrário.
     */
    boolean verificarDisponibilidadeSku(String sku);
}
