package br.com.unicos.ms_estoque.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_estoque.model.EstoqueProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link EstoqueProduto}.
 * <p>
 * Centraliza consultas relacionadas ao saldo de produtos por estoque,
 * respeitando o contexto multi-tenant.
 */
@Repository
public interface EstoqueProdutoRepository extends BaseTenantRepository<EstoqueProduto, Long> {

    /**
     * Lista os produtos de um estoque dentro do tenant.
     *
     * @param estoqueId identificador do estoque
     * @param tenantId identificador do tenant
     * @param pageable paginação
     * @return página de saldos de produtos do estoque
     */
    Page<EstoqueProduto> findByEstoqueIdAndEmpresaId(
            Long estoqueId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista as ocorrências de um produto em todos os estoques do tenant.
     *
     * @param produtoId identificador do produto
     * @param tenantId identificador do tenant
     * @param pageable paginação
     * @return página de saldos do produto
     */
    Page<EstoqueProduto> findByProdutoIdAndEmpresaId(
            Long produtoId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Busca o saldo de um produto em um estoque específico.
     *
     * @param estoqueId identificador do estoque
     * @param produtoId identificador do produto
     * @param tenantId identificador do tenant
     * @return saldo encontrado
     */
    Optional<EstoqueProduto> findByEstoqueIdAndProdutoIdAndEmpresaId(
            Long estoqueId,
            Long produtoId,
            Long tenantId
    );

    /**
     * Verifica se já existe saldo cadastrado para um produto em um estoque.
     *
     * @param estoqueId identificador do estoque
     * @param produtoId identificador do produto
     * @param tenantId identificador do tenant
     * @return true se existir
     */
    boolean existsByEstoqueIdAndProdutoIdAndEmpresaId(
            Long estoqueId,
            Long produtoId,
            Long tenantId
    );

    /**
     * Busca o saldo de um produto em um estoque com lock pessimista para atualização.
     * Útil em operações de entrada, saída e transferência com concorrência.
     *
     * @param estoqueId identificador do estoque
     * @param produtoId identificador do produto
     * @param tenantId identificador do tenant
     * @return saldo encontrado com lock
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<EstoqueProduto> findWithLockByEstoqueIdAndProdutoIdAndEmpresaId(
            Long estoqueId,
            Long produtoId,
            Long tenantId
    );
}