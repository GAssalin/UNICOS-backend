package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.ItemPedidoCompraDto;
import br.com.unicos.ms_compras.mapper.ItemPedidoCompraMapper;
import br.com.unicos.ms_compras.model.ItemPedidoCompra;
import br.com.unicos.ms_compras.model.PedidoCompra;
import br.com.unicos.ms_compras.repository.ItemPedidoCompraRepository;
import br.com.unicos.ms_compras.repository.PedidoCompraRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

/**
 * Service responsável por regras de negócio e operações do agregado {@link ItemPedidoCompra}.
 *
 * <p>
 * Padrões UniCoS:
 * - Relacionamento com {@link PedidoCompra} é resolvido no service
 * - Proteção tenant usando métodos tenant-aware do repository
 * - Regra: não permitir produto duplicado dentro do mesmo pedido (tenant)
 * - Regra: totalItem = quantidade * precoUnitario - descontoItem (normalização no service)
 * </p>
 */
@Service
@Transactional
public class ItemPedidoCompraService extends BaseTenantService<ItemPedidoCompra, Long> {

    private final ItemPedidoCompraRepository itemRepository;
    private final PedidoCompraRepository pedidoCompraRepository;
    private final ItemPedidoCompraMapper itemMapper;

    public ItemPedidoCompraService(
            ItemPedidoCompraRepository itemRepository,
            PedidoCompraRepository pedidoCompraRepository,
            ItemPedidoCompraMapper itemMapper
    ) {
        super(itemRepository);
        this.itemRepository = itemRepository;
        this.pedidoCompraRepository = pedidoCompraRepository;
        this.itemMapper = itemMapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-item-pedido-compra-admin", fallbackMethod = "fallbackAdmin")
    public ItemPedidoCompraDto salvar(ItemPedidoCompraDto request) {
        if (request.pedidoCompraId() == null)
            throw new IllegalArgumentException("pedidoCompraId é obrigatório para criar um item do pedido.");
        if (request.produtoId() == null)
            throw new IllegalArgumentException("produtoId é obrigatório para criar um item do pedido.");

        PedidoCompra pedido = buscarPedidoCompra(request.pedidoCompraId());

        validarProdutoDuplicadoAoSalvar(pedido.getId(), request.produtoId());

        ItemPedidoCompra entity = itemMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());
        entity.setPedidoCompra(pedido);

        normalizarTotais(entity);

        return itemMapper.toResponse(itemRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-item-pedido-compra-admin", fallbackMethod = "fallbackAdminIdReq")
    public ItemPedidoCompraDto atualizar(Long id, ItemPedidoCompraDto request) {
        ItemPedidoCompra entity = buscarItem(id);

        // não permite trocar o pedido por este endpoint
        if (request.pedidoCompraId() != null
                && entity.getPedidoCompra() != null
                && !entity.getPedidoCompra().getId().equals(request.pedidoCompraId())) {
            throw new IllegalArgumentException("Não é permitido alterar pedidoCompraId deste item por este endpoint.");
        }

        Long pedidoId = entity.getPedidoCompra().getId();

        // se mudou produtoId, valida duplicidade dentro do mesmo pedido/tenant
        if (request.produtoId() != null && !request.produtoId().equals(entity.getProdutoId()))
            validarProdutoDuplicadoAoSalvar(pedidoId, request.produtoId());

        itemMapper.updateEntity(request, entity);

        normalizarTotais(entity);

        return itemMapper.toResponse(itemRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-pedido-compra-admin", fallbackMethod = "fallbackAdminId")
    public ItemPedidoCompraDto buscarPorId(Long id) {
        return itemMapper.toResponse(buscarItem(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-pedido-compra-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ItemPedidoCompraDto> listar(Pageable pageable) {
        return itemRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(itemMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-pedido-compra-admin", fallbackMethod = "fallbackAdminPagePedido")
    public Page<ItemPedidoCompraDto> listarPorPedido(Long pedidoCompraId, Pageable pageable) {
        buscarPedidoCompra(pedidoCompraId);

        return itemRepository
                .findByPedidoCompraIdAndEmpresaId(pedidoCompraId, TenantContext.getEmpresaId(), pageable)
                .map(itemMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-pedido-compra-admin", fallbackMethod = "fallbackAdminPagePedidoProduto")
    public Page<ItemPedidoCompraDto> listarPorPedidoEProduto(Long pedidoCompraId, Long produtoId, Pageable pageable) {
        if (produtoId == null)
            throw new IllegalArgumentException("produtoId é obrigatório.");

        buscarPedidoCompra(pedidoCompraId);

        return itemRepository
                .findByPedidoCompraIdAndProdutoIdAndEmpresaId(pedidoCompraId, produtoId, TenantContext.getEmpresaId(), pageable)
                .map(itemMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-item-pedido-compra-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        itemRepository.delete(buscarItem(id));
    }

    @CircuitBreaker(name = "compras-item-pedido-compra-admin", fallbackMethod = "fallbackAdminVoidPedido")
    public void deletarPorPedido(Long pedidoCompraId) {
        buscarPedidoCompra(pedidoCompraId);

        itemRepository.deleteByPedidoCompraIdAndEmpresaId(
                pedidoCompraId,
                TenantContext.getEmpresaId()
        );
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private ItemPedidoCompraDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens do pedido de compra temporariamente indisponível");
    }

    private ItemPedidoCompraDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens do pedido de compra temporariamente indisponível");
    }

    private ItemPedidoCompraDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens do pedido de compra temporariamente indisponível");
    }

    private Page<ItemPedidoCompraDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens do pedido de compra temporariamente indisponível");
    }

    private Page<ItemPedidoCompraDto> fallbackAdminPagePedido(Long pedidoCompraId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens do pedido de compra temporariamente indisponível");
    }

    private Page<ItemPedidoCompraDto> fallbackAdminPagePedidoProduto(Long pedidoCompraId, Long produtoId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens do pedido de compra temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens do pedido de compra temporariamente indisponível");
    }

    private void fallbackAdminVoidPedido(Long pedidoCompraId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens do pedido de compra temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ItemPedidoCompra buscarItem(Long id) {
        ItemPedidoCompra entity = itemRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Item do pedido de compra não encontrado: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao item do pedido fora do tenant.");

        return entity;
    }

    private PedidoCompra buscarPedidoCompra(Long id) {
        // Ajuste para tenant-aware se seu PedidoCompraRepository tiver findByIdAndEmpresaId(...)
        PedidoCompra pedido = pedidoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de compra não encontrado: " + id));

        if (pedido.getEmpresaId() != null && !pedido.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao pedido fora do tenant.");

        return pedido;
    }

    private void validarProdutoDuplicadoAoSalvar(Long pedidoCompraId, Long produtoId) {
        if (itemRepository.existsByPedidoCompraIdAndProdutoIdAndEmpresaId(
                pedidoCompraId,
                produtoId,
                TenantContext.getEmpresaId()
        )) {
            throw new IllegalArgumentException("Já existe item para este produto neste pedido (tenant).");
        }
    }

    private void normalizarTotais(ItemPedidoCompra entity) {
        if (entity.getQuantidade() == null)
            throw new IllegalArgumentException("quantidade é obrigatória.");
        if (entity.getPrecoUnitario() == null)
            throw new IllegalArgumentException("precoUnitario é obrigatório.");
        if (entity.getDescontoItem() == null)
            // mantém compatível com seu @NotNull no model
            entity.setDescontoItem(BigDecimal.ZERO);

        BigDecimal total = entity.getQuantidade()
                .multiply(entity.getPrecoUnitario())
                .subtract(entity.getDescontoItem());

        // evita total negativo por erro de desconto
        if (total.signum() < 0)
            throw new IllegalArgumentException("descontoItem não pode tornar totalItem negativo.");

        entity.setTotalItem(total);
    }
}