package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Produto}.
 * <p>
 * Centraliza consultas relacionadas ao catálogo de produtos e serviços,
 * garantindo isolamento por {@code empresaId}.
 * </p>
 *
 * <p>
 * Este repositório é utilizado em fluxos como:
 * <ul>
 *     <li>Cadastro e manutenção de produtos</li>
 *     <li>Consultas por SKU (código interno) e filtros de catálogo</li>
 *     <li>Integração com módulos como Estoque, Compras e Vendas</li>
 * </ul>
 * </p>
 */
@Repository
public interface ProdutoRepository extends BaseTenantRepository<Produto, Long> {

    /**
     * Recupera um produto dentro do contexto do tenant.
     *
     * @param id        Identificador do produto.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o produto, se encontrado.
     */
    Optional<Produto> findByIdAndEmpresaId(Long id, Long empresaId);

    /**
     * Recupera um produto pelo código interno (SKU),
     * respeitando o contexto multi-tenant.
     *
     * @param codigo    Código interno (SKU) do produto.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o produto, se encontrado.
     */
    Optional<Produto> findByCodigoAndEmpresaId(String codigo, Long empresaId);

    /**
     * Verifica se já existe um produto cadastrado com o mesmo código interno (SKU)
     * dentro do tenant, evitando duplicidade.
     *
     * @param codigo    Código interno (SKU) do produto.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByCodigoAndEmpresaId(String codigo, Long empresaId);

    /**
     * Lista produtos por status (ativo/inativo) dentro do tenant, com paginação.
     *
     * @param ativo     Status do produto.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de produtos.
     */
    Page<Produto> findByAtivoAndEmpresaId(Boolean ativo, Long empresaId, Pageable pageable);

    /**
     * Pesquisa produtos por nome (contendo) dentro do tenant, com paginação.
     *
     * @param nome      Texto para busca no nome do produto.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de produtos.
     */
    Page<Produto> findByNomeContainingIgnoreCaseAndEmpresaId(String nome, Long empresaId, Pageable pageable);

}
