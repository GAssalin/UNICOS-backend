package br.com.unicos.ms_estoque.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_estoque.enums.StatusMovimentacaoEstoque;
import br.com.unicos.ms_estoque.enums.TipoMovimentacaoEstoque;
import br.com.unicos.ms_estoque.model.MovimentacaoEstoque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link MovimentacaoEstoque}.
 * <p>
 * Centraliza consultas relacionadas ao histórico de movimentações de estoque,
 * respeitando o contexto multi-tenant.
 */
@Repository
public interface MovimentacaoEstoqueRepository extends BaseTenantRepository<MovimentacaoEstoque, Long> {

    /**
     * Lista movimentações por tipo dentro do tenant.
     *
     * @param tipoMovimentacao tipo da movimentação
     * @param tenantId identificador do tenant
     * @param pageable paginação
     * @return página de movimentações
     */
    Page<MovimentacaoEstoque> findByTipoMovimentacaoAndEmpresaId(
            TipoMovimentacaoEstoque tipoMovimentacao,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista movimentações por status dentro do tenant.
     *
     * @param statusMovimentacao status da movimentação
     * @param tenantId identificador do tenant
     * @param pageable paginação
     * @return página de movimentações
     */
    Page<MovimentacaoEstoque> findByStatusMovimentacaoAndEmpresaId(
            StatusMovimentacaoEstoque statusMovimentacao,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista movimentações em que o estoque aparece como origem.
     *
     * @param estoqueOrigemId identificador do estoque de origem
     * @param tenantId identificador do tenant
     * @param pageable paginação
     * @return página de movimentações
     */
    Page<MovimentacaoEstoque> findByEstoqueOrigemIdAndEmpresaId(
            Long estoqueOrigemId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista movimentações em que o estoque aparece como destino.
     *
     * @param estoqueDestinoId identificador do estoque de destino
     * @param tenantId identificador do tenant
     * @param pageable paginação
     * @return página de movimentações
     */
    Page<MovimentacaoEstoque> findByEstoqueDestinoIdAndEmpresaId(
            Long estoqueDestinoId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista movimentações por estoque de origem e tipo.
     *
     * @param estoqueOrigemId identificador do estoque de origem
     * @param tipoMovimentacao tipo da movimentação
     * @param tenantId identificador do tenant
     * @param pageable paginação
     * @return página de movimentações
     */
    Page<MovimentacaoEstoque> findByEstoqueOrigemIdAndTipoMovimentacaoAndEmpresaId(
            Long estoqueOrigemId,
            TipoMovimentacaoEstoque tipoMovimentacao,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista movimentações por estoque de destino e tipo.
     *
     * @param estoqueDestinoId identificador do estoque de destino
     * @param tipoMovimentacao tipo da movimentação
     * @param tenantId identificador do tenant
     * @param pageable paginação
     * @return página de movimentações
     */
    Page<MovimentacaoEstoque> findByEstoqueDestinoIdAndTipoMovimentacaoAndEmpresaId(
            Long estoqueDestinoId,
            TipoMovimentacaoEstoque tipoMovimentacao,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Busca movimentação pelo documento de referência dentro do tenant.
     *
     * @param documentoReferencia documento de referência
     * @param tenantId identificador do tenant
     * @return movimentação encontrada
     */
    Optional<MovimentacaoEstoque> findByDocumentoReferenciaAndEmpresaId(
            String documentoReferencia,
            Long tenantId
    );

    /**
     * Verifica se já existe movimentação para o documento de referência dentro do tenant.
     *
     * @param documentoReferencia documento de referência
     * @param tenantId identificador do tenant
     * @return true se existir
     */
    boolean existsByDocumentoReferenciaAndEmpresaId(
            String documentoReferencia,
            Long tenantId
    );

    /**
     * Lista movimentações dentro de um intervalo de datas no tenant.
     *
     * @param dataInicial data/hora inicial
     * @param dataFinal data/hora final
     * @param tenantId identificador do tenant
     * @param pageable paginação
     * @return página de movimentações
     */
    Page<MovimentacaoEstoque> findByDataMovimentacaoBetweenAndEmpresaId(
            LocalDateTime dataInicial,
            LocalDateTime dataFinal,
            Long tenantId,
            Pageable pageable
    );
}