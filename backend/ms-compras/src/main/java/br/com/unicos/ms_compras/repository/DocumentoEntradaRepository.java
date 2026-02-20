package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.DocumentoEntrada;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link DocumentoEntrada}.
 * <p>
 * Centraliza consultas relacionadas aos documentos vinculados ao recebimento de compras,
 * respeitando o contexto multi-tenant (empresa/tenant).
 * </p>
 */
@Repository
public interface DocumentoEntradaRepository extends BaseTenantRepository<DocumentoEntrada, Long> {

    /**
     * Recupera documento por ID dentro do tenant.
     */
    Optional<DocumentoEntrada> findByIdAndEmpresaId(
            Long id,
            Long tenantId
    );

    /**
     * Lista documentos de um recebimento dentro do tenant.
     */
    Page<DocumentoEntrada> findByRecebimentoCompraIdAndEmpresaId(
            Long recebimentoCompraId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista todos os documentos de um recebimento ordenados por ID.
     */
    List<DocumentoEntrada> findByRecebimentoCompraIdAndEmpresaIdOrderByIdAsc(
            Long recebimentoCompraId,
            Long tenantId
    );

    /**
     * Recupera documento por tipo e número dentro do tenant.
     */
    Optional<DocumentoEntrada> findByTipoDocumentoAndNumeroAndEmpresaId(
            String tipoDocumento,
            String numero,
            Long tenantId
    );

    /**
     * Verifica se já existe documento com mesmo tipo e número dentro do tenant.
     */
    boolean existsByTipoDocumentoAndNumeroAndEmpresaId(
            String tipoDocumento,
            String numero,
            Long tenantId
    );

    /**
     * Recupera documento por chave de acesso dentro do tenant.
     */
    Optional<DocumentoEntrada> findByChaveAcessoAndEmpresaId(
            String chaveAcesso,
            Long tenantId
    );

    /**
     * Remove todos os documentos de um recebimento dentro do tenant.
     */
    void deleteByRecebimentoCompraIdAndEmpresaId(
            Long recebimentoCompraId,
            Long tenantId
    );
}