package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.ItemCotacaoDto;
import br.com.unicos.ms_compras.mapper.ItemCotacaoMapper;
import br.com.unicos.ms_compras.model.CotacaoCompra;
import br.com.unicos.ms_compras.model.ItemCotacao;
import br.com.unicos.ms_compras.repository.CotacaoCompraRepository;
import br.com.unicos.ms_compras.repository.ItemCotacaoRepository;
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
 * Service responsável por regras de negócio e operações do agregado {@link ItemCotacao}.
 *
 * <p>
 * Padrões UniCoS:
 * - Relacionamento com {@link CotacaoCompra} é resolvido no service
 * - Proteção tenant usando métodos tenant-aware do repository
 * - Regra: não permitir produto duplicado dentro da mesma cotação (tenant)
 * </p>
 */
@Service
@Transactional
public class ItemCotacaoService extends BaseTenantService<ItemCotacao, Long> {

    private final ItemCotacaoRepository itemRepository;
    private final CotacaoCompraRepository cotacaoCompraRepository;
    private final ItemCotacaoMapper itemMapper;

    public ItemCotacaoService(
            ItemCotacaoRepository itemRepository,
            CotacaoCompraRepository cotacaoCompraRepository,
            ItemCotacaoMapper itemMapper
    ) {
        super(itemRepository);
        this.itemRepository = itemRepository;
        this.cotacaoCompraRepository = cotacaoCompraRepository;
        this.itemMapper = itemMapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-item-cotacao-admin", fallbackMethod = "fallbackAdmin")
    public ItemCotacaoDto salvar(ItemCotacaoDto request) {
        if (request.cotacaoCompraId() == null)
            throw new IllegalArgumentException("cotacaoCompraId é obrigatório para criar um item de cotação.");
        if (request.produtoId() == null)
            throw new IllegalArgumentException("produtoId é obrigatório para criar um item de cotação.");

        CotacaoCompra cotacao = buscarCotacaoCompra(request.cotacaoCompraId());

        validarProdutoDuplicadoAoSalvar(cotacao.getId(), request.produtoId());

        ItemCotacao entity = itemMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());
        entity.setCotacaoCompra(cotacao);

        return itemMapper.toResponse(itemRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-item-cotacao-admin", fallbackMethod = "fallbackAdminIdReq")
    public ItemCotacaoDto atualizar(Long id, ItemCotacaoDto request) {
        ItemCotacao entity = buscarItem(id);

        // não permite trocar a cotação por este endpoint
        if (request.cotacaoCompraId() != null
                && entity.getCotacaoCompra() != null
                && !entity.getCotacaoCompra().getId().equals(request.cotacaoCompraId())) {
            throw new IllegalArgumentException("Não é permitido alterar cotacaoCompraId deste item por este endpoint.");
        }

        Long cotacaoId = entity.getCotacaoCompra().getId();

        // se mudou produtoId, valida duplicidade dentro da mesma cotação/tenant
        if (request.produtoId() != null && !request.produtoId().equals(entity.getProdutoId()))
            validarProdutoDuplicadoAoSalvar(cotacaoId, request.produtoId());

        itemMapper.updateEntity(request, entity);

        return itemMapper.toResponse(itemRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-cotacao-admin", fallbackMethod = "fallbackAdminId")
    public ItemCotacaoDto buscarPorId(Long id) {
        return itemMapper.toResponse(buscarItem(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-cotacao-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ItemCotacaoDto> listar(Pageable pageable) {
        return itemRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(itemMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-cotacao-admin", fallbackMethod = "fallbackAdminPageCotacao")
    public Page<ItemCotacaoDto> listarPorCotacao(Long cotacaoCompraId, Pageable pageable) {
        buscarCotacaoCompra(cotacaoCompraId);

        return itemRepository
                .findByCotacaoCompraIdAndEmpresaId(cotacaoCompraId, TenantContext.getEmpresaId(), pageable)
                .map(itemMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-cotacao-admin", fallbackMethod = "fallbackAdminPageCotacaoProduto")
    public Page<ItemCotacaoDto> listarPorCotacaoEProduto(Long cotacaoCompraId, Long produtoId, Pageable pageable) {
        if (produtoId == null)
            throw new IllegalArgumentException("produtoId é obrigatório.");

        buscarCotacaoCompra(cotacaoCompraId);

        return itemRepository
                .findByCotacaoCompraIdAndProdutoIdAndEmpresaId(cotacaoCompraId, produtoId, TenantContext.getEmpresaId(), pageable)
                .map(itemMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-item-cotacao-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        itemRepository.delete(buscarItem(id));
    }

    @CircuitBreaker(name = "compras-item-cotacao-admin", fallbackMethod = "fallbackAdminVoidCotacao")
    public void deletarPorCotacao(Long cotacaoCompraId) {
        buscarCotacaoCompra(cotacaoCompraId);

        itemRepository.deleteByCotacaoCompraIdAndEmpresaId(
                cotacaoCompraId,
                TenantContext.getEmpresaId()
        );
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private ItemCotacaoDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de cotação temporariamente indisponível");
    }

    private ItemCotacaoDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de cotação temporariamente indisponível");
    }

    private ItemCotacaoDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de cotação temporariamente indisponível");
    }

    private Page<ItemCotacaoDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de cotação temporariamente indisponível");
    }

    private Page<ItemCotacaoDto> fallbackAdminPageCotacao(Long cotacaoCompraId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de cotação temporariamente indisponível");
    }

    private Page<ItemCotacaoDto> fallbackAdminPageCotacaoProduto(Long cotacaoCompraId, Long produtoId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de cotação temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de cotação temporariamente indisponível");
    }

    private void fallbackAdminVoidCotacao(Long cotacaoCompraId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de cotação temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ItemCotacao buscarItem(Long id) {
        ItemCotacao entity = itemRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Item de cotação não encontrado: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao item de cotação fora do tenant.");

        return entity;
    }

    private CotacaoCompra buscarCotacaoCompra(Long id) {
        CotacaoCompra entity = cotacaoCompraRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Cotação de compra não encontrada: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado à cotação fora do tenant.");

        return entity;
    }

    private void validarProdutoDuplicadoAoSalvar(Long cotacaoCompraId, Long produtoId) {
        if (itemRepository.existsByCotacaoCompraIdAndProdutoIdAndEmpresaId(
                cotacaoCompraId,
                produtoId,
                TenantContext.getEmpresaId()
        )) {
            throw new IllegalArgumentException("Já existe item para este produto nesta cotação (tenant).");
        }
    }
}