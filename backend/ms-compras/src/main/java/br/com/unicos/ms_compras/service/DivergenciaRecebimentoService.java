package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.DivergenciaRecebimentoDto;
import br.com.unicos.ms_compras.mapper.DivergenciaRecebimentoMapper;
import br.com.unicos.ms_compras.model.DivergenciaRecebimento;
import br.com.unicos.ms_compras.model.ItemRecebimentoCompra;
import br.com.unicos.ms_compras.repository.DivergenciaRecebimentoRepository;
import br.com.unicos.ms_compras.repository.ItemRecebimentoCompraRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service responsável por regras de negócio e operações do agregado {@link DivergenciaRecebimento}.
 *
 * <p>
 * Padrões UniCoS:
 * - Relacionamento com {@link ItemRecebimentoCompra} é resolvido no service
 * - Proteção tenant usando métodos tenant-aware do repository
 * - Sem manipulação de coleções além do necessário (ex.: listar por item)
 * </p>
 */
@Service
@Transactional
public class DivergenciaRecebimentoService extends BaseTenantService<DivergenciaRecebimento, Long> {

    private final DivergenciaRecebimentoRepository divergenciaRepository;
    private final DivergenciaRecebimentoMapper divergenciaMapper;
    private final ItemRecebimentoCompraRepository itemRecebimentoCompraRepository;

    public DivergenciaRecebimentoService(
            DivergenciaRecebimentoRepository divergenciaRepository,
            DivergenciaRecebimentoMapper divergenciaMapper,
            ItemRecebimentoCompraRepository itemRecebimentoCompraRepository
    ) {
        super(divergenciaRepository);
        this.divergenciaRepository = divergenciaRepository;
        this.divergenciaMapper = divergenciaMapper;
        this.itemRecebimentoCompraRepository = itemRecebimentoCompraRepository;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-divergencia-recebimento-admin", fallbackMethod = "fallbackAdmin")
    public DivergenciaRecebimentoDto salvar(DivergenciaRecebimentoDto request) {
        if (request.itemRecebimentoCompraId() == null)
            throw new IllegalArgumentException("itemRecebimentoCompraId é obrigatório para criar uma divergência.");

        ItemRecebimentoCompra item = buscarItemRecebimentoCompra(request.itemRecebimentoCompraId());

        DivergenciaRecebimento entity = divergenciaMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());
        entity.setItemRecebimentoCompra(item);

        return divergenciaMapper.toResponse(divergenciaRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-divergencia-recebimento-admin", fallbackMethod = "fallbackAdminIdReq")
    public DivergenciaRecebimentoDto atualizar(Long id, DivergenciaRecebimentoDto request) {
        DivergenciaRecebimento entity = buscarDivergencia(id);

        // não permite trocar o vínculo do item por este endpoint (conforme mapper doc)
        if (request.itemRecebimentoCompraId() != null
                && entity.getItemRecebimentoCompra() != null
                && !entity.getItemRecebimentoCompra().getId().equals(request.itemRecebimentoCompraId())) {
            throw new IllegalArgumentException("Não é permitido alterar itemRecebimentoCompraId desta divergência por este endpoint.");
        }

        divergenciaMapper.updateEntity(request, entity);

        return divergenciaMapper.toResponse(divergenciaRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-divergencia-recebimento-admin", fallbackMethod = "fallbackAdminId")
    public DivergenciaRecebimentoDto buscarPorId(Long id) {
        return divergenciaMapper.toResponse(buscarDivergencia(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-divergencia-recebimento-admin", fallbackMethod = "fallbackAdminPage")
    public Page<DivergenciaRecebimentoDto> listar(Pageable pageable) {
        return divergenciaRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(divergenciaMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-divergencia-recebimento-admin", fallbackMethod = "fallbackAdminPageItem")
    public Page<DivergenciaRecebimentoDto> listarPorItemRecebimento(Long itemRecebimentoCompraId, Pageable pageable) {
        // garante que o item exista no tenant
        buscarItemRecebimentoCompra(itemRecebimentoCompraId);

        return divergenciaRepository
                .findByItemRecebimentoCompraIdAndEmpresaId(itemRecebimentoCompraId, TenantContext.getEmpresaId(), pageable)
                .map(divergenciaMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-divergencia-recebimento-admin", fallbackMethod = "fallbackAdminPageTipo")
    public Page<DivergenciaRecebimentoDto> listarPorTipo(String tipo, Pageable pageable) {
        if (tipo == null || tipo.isBlank())
            throw new IllegalArgumentException("tipo é obrigatório.");

        return divergenciaRepository
                .findByTipoAndEmpresaId(tipo, TenantContext.getEmpresaId(), pageable)
                .map(divergenciaMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-divergencia-recebimento-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        divergenciaRepository.delete(buscarDivergencia(id));
    }

    @CircuitBreaker(name = "compras-divergencia-recebimento-admin", fallbackMethod = "fallbackAdminVoidItem")
    public void deletarPorItemRecebimento(Long itemRecebimentoCompraId) {
        // garante que o item exista no tenant
        buscarItemRecebimentoCompra(itemRecebimentoCompraId);

        divergenciaRepository.deleteByItemRecebimentoCompraIdAndEmpresaId(
                itemRecebimentoCompraId,
                TenantContext.getEmpresaId()
        );
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private DivergenciaRecebimentoDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de divergências de recebimento temporariamente indisponível");
    }

    private DivergenciaRecebimentoDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de divergências de recebimento temporariamente indisponível");
    }

    private DivergenciaRecebimentoDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de divergências de recebimento temporariamente indisponível");
    }

    private Page<DivergenciaRecebimentoDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de divergências de recebimento temporariamente indisponível");
    }

    private Page<DivergenciaRecebimentoDto> fallbackAdminPageItem(Long itemRecebimentoCompraId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de divergências de recebimento temporariamente indisponível");
    }

    private Page<DivergenciaRecebimentoDto> fallbackAdminPageTipo(String tipo, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de divergências de recebimento temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de divergências de recebimento temporariamente indisponível");
    }

    private void fallbackAdminVoidItem(Long itemRecebimentoCompraId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de divergências de recebimento temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private DivergenciaRecebimento buscarDivergencia(Long id) {
        DivergenciaRecebimento entity = divergenciaRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Divergência de recebimento não encontrada: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado à divergência fora do tenant.");

        return entity;
    }

    private ItemRecebimentoCompra buscarItemRecebimentoCompra(Long id) {
        // ideal é ter findByIdAndEmpresaId(id, tenantId).
        ItemRecebimentoCompra item = itemRecebimentoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ItemRecebimentoCompra não encontrado: " + id));

        // Se ItemRecebimentoCompra estende BaseTenantEntity, dá pra proteger tenant assim:
        if (item.getEmpresaId() != null && !item.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao item de recebimento fora do tenant.");

        return item;
    }
}