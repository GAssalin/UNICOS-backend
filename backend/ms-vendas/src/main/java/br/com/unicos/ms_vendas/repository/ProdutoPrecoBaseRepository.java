package br.com.unicos.ms_vendas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_vendas.model.ProdutoPrecoBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ProdutoPrecoBase}.
 *
 * <p>
 * Centraliza consultas relacionadas à precificação base de produtos,
 * garantindo isolamento por {@code empresaId}.
 * </p>
 *
 * <p>
 * Este repositório é utilizado em fluxos como:
 * <ul>
 *     <li>Consulta e manutenção de custo e preço base</li>
 *     <li>Validações para cálculos de margem</li>
 *     <li>Integrações com módulos de Compras, Vendas e Financeiro</li>
 * </ul>
 * </p>
 */
@Repository
public interface ProdutoPrecoBaseRepository extends BaseTenantRepository<ProdutoPrecoBase, Long> {

    /**
     * Recupera a precificação base de um produto dentro do tenant.
     *
     * @param produtoId Identificador do produto.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo a precificação base, se encontrada.
     */
    Optional<ProdutoPrecoBase> findByProdutoIdAndEmpresaId(Long produtoId, Long empresaId);

    /**
     * Verifica se já existe precificação base cadastrada para o produto dentro do tenant.
     *
     * @param produtoId Identificador do produto.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByProdutoIdAndEmpresaId(Long produtoId, Long empresaId);

    /**
     * Lista precificações base por status (ativo/inativo) dentro do tenant.
     *
     * @param ativo     Status do registro.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de precificações base.
     */
    Page<ProdutoPrecoBase> findByAtivoAndEmpresaId(Boolean ativo, Long empresaId, Pageable pageable);

    /**
     * Remove a precificação base de um produto dentro do tenant.
     *
     * @param produtoId Identificador do produto.
     * @param empresaId Identificador da empresa (tenant).
     */
    void deleteByProdutoIdAndEmpresaId(Long produtoId, Long empresaId);

}
