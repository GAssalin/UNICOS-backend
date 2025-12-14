package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelas operações de persistência
 * da entidade {@link Produto}.
 *
 * <p>
 * Todas as consultas são restritas ao contexto da empresa (tenant),
 * identificado pelo {@code empresaId}.
 * </p>
 *
 * <p>
 * Este é um dos repositories mais críticos do domínio, pois garante
 * o isolamento total do catálogo de produtos entre empresas.
 * </p>
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // ============================================================
    // 🔐 Consultas básicas (ISOLAMENTO POR TENANT)
    // ============================================================

    /**
     * Busca um produto pelo ID, restrito ao contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param id        ID do produto.
     * @return {@link Optional} contendo o produto, se existir.
     */
    Optional<Produto> findByEmpresaIdAndId(
            Long empresaId,
            Long id
    );

    /**
     * Retorna todos os produtos de uma empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @return Lista de produtos.
     */
    List<Produto> findByEmpresaId(
            Long empresaId
    );

    /**
     * Verifica se um produto existe dentro do contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param id        ID do produto.
     * @return {@code true} se existir, {@code false} caso contrário.
     */
    boolean existsByEmpresaIdAndId(
            Long empresaId,
            Long id
    );

    // ============================================================
    // 🔍 Consultas por dados básicos (Embedded: dadosBasicos)
    // ============================================================

    /**
     * Busca produtos cujo nome contenha o termo informado,
     * restringindo a busca à empresa.
     */
    List<Produto> findByEmpresaIdAndDadosBasicosNomeContainingIgnoreCase(
            Long empresaId,
            String nome
    );

    /**
     * Busca um produto pelo SKU global definido no core-produto,
     * dentro do contexto de uma empresa.
     */
    Optional<Produto> findByEmpresaIdAndDadosBasicosSku(
            Long empresaId,
            String sku
    );

    /**
     * Verifica se já existe um produto com o SKU informado
     * dentro do contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param sku       Código SKU.
     * @return {@code true} se existir, {@code false} caso contrário.
     */
    boolean existsByEmpresaIdAndDadosBasicosSku(
            Long empresaId,
            String sku
    );

    // ============================================================
    // 🔍 Consultas por atributos operacionais
    // ============================================================

    /**
     * Retorna todos os produtos ativos de uma empresa.
     */
    List<Produto> findByEmpresaIdAndAtivoTrue(
            Long empresaId
    );

    /**
     * Retorna todos os produtos inativos de uma empresa.
     */
    List<Produto> findByEmpresaIdAndAtivoFalse(
            Long empresaId
    );

    /**
     * Busca produtos pertencentes a uma categoria específica
     * dentro do contexto de uma empresa.
     */
    List<Produto> findByEmpresaIdAndCategoriaId(
            Long empresaId,
            Long categoriaId
    );

    /**
     * Busca produtos vinculados a uma marca específica
     * dentro do contexto de uma empresa.
     */
    List<Produto> findByEmpresaIdAndMarcaId(
            Long empresaId,
            Long marcaId
    );

    // ============================================================
    // 🔍 Consultas por preço (Embedded: precoAtual)
    // ============================================================

    /**
     * Busca produtos cujo preço de venda esteja dentro
     * da faixa informada, restringindo a busca à empresa.
     */
    List<Produto> findByEmpresaIdAndPrecoAtualPrecoVendaBetween(
            Long empresaId,
            BigDecimal precoMin,
            BigDecimal precoMax
    );
}
