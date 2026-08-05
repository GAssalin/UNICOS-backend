package br.com.unicos.ms_estoque.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_estoque.model.MovimentacaoEstoqueItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link MovimentacaoEstoqueItem}.
 * <p>
 * Centraliza consultas relacionadas aos itens das movimentações de estoque,
 * respeitando o contexto multi-tenant.
 */
@Repository
public interface MovimentacaoEstoqueItemRepository extends BaseTenantRepository<MovimentacaoEstoqueItem, Long> {

    /**
     * Lista os itens de uma movimentação.
     *
     * @param movimentacaoId identificador da movimentação
     * @param tenantId identificador do tenant
     * @return lista de itens
     */
    List<MovimentacaoEstoqueItem> findByMovimentacaoIdAndEmpresaId(
            Long movimentacaoId,
            Long tenantId
    );

    /**
     * Lista itens de movimentação por produto.
     *
     * @param produtoId identificador do produto
     * @param tenantId identificador do tenant
     * @param pageable paginação
     * @return página de itens
     */
    Page<MovimentacaoEstoqueItem> findByProdutoIdAndEmpresaId(
            Long produtoId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Busca item por movimentação e produto.
     *
     * @param movimentacaoId identificador da movimentação
     * @param produtoId identificador do produto
     * @param tenantId identificador do tenant
     * @return item encontrado
     */
    Optional<MovimentacaoEstoqueItem> findByMovimentacaoIdAndProdutoIdAndEmpresaId(
            Long movimentacaoId,
            Long produtoId,
            Long tenantId
    );

    /**
     * Verifica se existe item para uma movimentação e produto.
     *
     * @param movimentacaoId identificador da movimentação
     * @param produtoId identificador do produto
     * @param tenantId identificador do tenant
     * @return true se existir
     */
    boolean existsByMovimentacaoIdAndProdutoIdAndEmpresaId(
            Long movimentacaoId,
            Long produtoId,
            Long tenantId
    );
}