package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.ItemRespostaCotacaoFornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ItemRespostaCotacaoFornecedor}.
 * <p>
 * Centraliza consultas relacionadas aos preços propostos pelos fornecedores
 * para itens das cotações, respeitando o contexto multi-tenant (empresa/tenant).
 * </p>
 */
@Repository
public interface ItemRespostaCotacaoFornecedorRepository extends BaseTenantRepository<ItemRespostaCotacaoFornecedor, Long> {

    /**
     * Recupera item da resposta por ID dentro do tenant.
     */
    Optional<ItemRespostaCotacaoFornecedor> findByIdAndEmpresaId(
            Long id,
            Long tenantId
    );

    /**
     * Lista itens de uma resposta de cotação dentro do tenant.
     */
    Page<ItemRespostaCotacaoFornecedor> findByRespostaCotacaoFornecedorIdAndEmpresaId(
            Long respostaCotacaoFornecedorId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista todos os itens de uma resposta ordenados por ID.
     */
    List<ItemRespostaCotacaoFornecedor> findByRespostaCotacaoFornecedorIdAndEmpresaIdOrderByIdAsc(
            Long respostaCotacaoFornecedorId,
            Long tenantId
    );

    /**
     * Recupera proposta do fornecedor para um item específico da cotação.
     */
    Optional<ItemRespostaCotacaoFornecedor> findByRespostaCotacaoFornecedorIdAndItemCotacaoIdAndEmpresaId(
            Long respostaCotacaoFornecedorId,
            Long itemCotacaoId,
            Long tenantId
    );

    /**
     * Verifica se já existe proposta do fornecedor para um item da cotação.
     */
    boolean existsByRespostaCotacaoFornecedorIdAndItemCotacaoIdAndEmpresaId(
            Long respostaCotacaoFornecedorId,
            Long itemCotacaoId,
            Long tenantId
    );

    /**
     * Remove todos os itens de uma resposta de cotação dentro do tenant.
     */
    void deleteByRespostaCotacaoFornecedorIdAndEmpresaId(
            Long respostaCotacaoFornecedorId,
            Long tenantId
    );
}