package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.ItemCotacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ItemCotacao}.
 * <p>
 * Centraliza consultas relacionadas aos itens das cotações de compra,
 * respeitando o contexto multi-tenant (empresa/tenant).
 * </p>
 */
@Repository
public interface ItemCotacaoRepository extends BaseTenantRepository<ItemCotacao, Long> {

    /**
     * Recupera item por ID dentro do tenant.
     */
    Optional<ItemCotacao> findByIdAndEmpresaId(
            Long id,
            Long tenantId
    );

    /**
     * Lista itens de uma cotação dentro do tenant.
     */
    Page<ItemCotacao> findByCotacaoCompraIdAndEmpresaId(
            Long cotacaoCompraId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista todos os itens de uma cotação ordenados por ID.
     */
    List<ItemCotacao> findByCotacaoCompraIdAndEmpresaIdOrderByIdAsc(
            Long cotacaoCompraId,
            Long tenantId
    );

    /**
     * Lista itens de uma cotação filtrando por produto dentro do tenant.
     */
    Page<ItemCotacao> findByCotacaoCompraIdAndProdutoIdAndEmpresaId(
            Long cotacaoCompraId,
            Long produtoId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Verifica se existe item para um produto dentro da cotação no tenant.
     */
    boolean existsByCotacaoCompraIdAndProdutoIdAndEmpresaId(
            Long cotacaoCompraId,
            Long produtoId,
            Long tenantId
    );

    /**
     * Remove todos os itens de uma cotação dentro do tenant.
     */
    void deleteByCotacaoCompraIdAndEmpresaId(
            Long cotacaoCompraId,
            Long tenantId
    );
}