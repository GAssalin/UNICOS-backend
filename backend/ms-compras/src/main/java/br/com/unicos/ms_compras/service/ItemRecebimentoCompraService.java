package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.ItemRecebimentoCompraDto;
import br.com.unicos.ms_compras.mapper.ItemRecebimentoCompraMapper;
import br.com.unicos.ms_compras.model.ItemPedidoCompra;
import br.com.unicos.ms_compras.model.ItemRecebimentoCompra;
import br.com.unicos.ms_compras.model.RecebimentoCompra;
import br.com.unicos.ms_compras.repository.ItemPedidoCompraRepository;
import br.com.unicos.ms_compras.repository.ItemRecebimentoCompraRepository;
import br.com.unicos.ms_compras.repository.RecebimentoCompraRepository;
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
 * Service responsável por regras de negócio e operações do agregado {@link ItemRecebimentoCompra}.
 *
 * <p>
 * Padrões UniCoS:
 * - Relacionamentos com {@link RecebimentoCompra} e {@link ItemPedidoCompra} resolvidos no service
 * - Proteção tenant usando métodos tenant-aware
 * - Regra: 1 recebimento por itemPedidoCompra no tenant
 * - Regra básica de consistência: aprovada + recusada = recebida (normalização/validação)
 * - Coleção {@code divergencias} não é manipulada aqui (use service específico/endpoint de divergências)
 * </p>
 */
@Service
@Transactional
public class ItemRecebimentoCompraService extends BaseTenantService<ItemRecebimentoCompra, Long> {

    private final ItemRecebimentoCompraRepository itemRecebimentoRepository;
    private final RecebimentoCompraRepository recebimentoCompraRepository;
    private final ItemPedidoCompraRepository itemPedidoCompraRepository;
    private final ItemRecebimentoCompraMapper itemRecebimentoMapper;

    public ItemRecebimentoCompraService(
            ItemRecebimentoCompraRepository itemRecebimentoRepository,
            RecebimentoCompraRepository recebimentoCompraRepository,
            ItemPedidoCompraRepository itemPedidoCompraRepository,
            ItemRecebimentoCompraMapper itemRecebimentoMapper
    ) {
        super(itemRecebimentoRepository);
        this.itemRecebimentoRepository = itemRecebimentoRepository;
        this.recebimentoCompraRepository = recebimentoCompraRepository;
        this.itemPedidoCompraRepository = itemPedidoCompraRepository;
        this.itemRecebimentoMapper = itemRecebimentoMapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-item-recebimento-compra-admin", fallbackMethod = "fallbackAdmin")
    public ItemRecebimentoCompraDto salvar(ItemRecebimentoCompraDto request) {
        if (request.recebimentoCompraId() == null)
            throw new IllegalArgumentException("recebimentoCompraId é obrigatório para criar item de recebimento.");
        if (request.itemPedidoCompraId() == null)
            throw new IllegalArgumentException("itemPedidoCompraId é obrigatório para criar item de recebimento.");

        RecebimentoCompra recebimento = buscarRecebimentoCompra(request.recebimentoCompraId());
        ItemPedidoCompra itemPedido = buscarItemPedidoCompra(request.itemPedidoCompraId());

        validarDuplicidadeItemPedidoAoSalvar(itemPedido.getId());

        ItemRecebimentoCompra entity = itemRecebimentoMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());
        entity.setRecebimentoCompra(recebimento);
        entity.setItemPedidoCompra(itemPedido);

        normalizarEValidarQuantidades(entity);

        return itemRecebimentoMapper.toResponse(itemRecebimentoRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-item-recebimento-compra-admin", fallbackMethod = "fallbackAdminIdReq")
    public ItemRecebimentoCompraDto atualizar(Long id, ItemRecebimentoCompraDto request) {
        ItemRecebimentoCompra entity = buscarItemRecebimento(id);

        // não permite trocar recebimento por este endpoint
        if (request.recebimentoCompraId() != null
                && entity.getRecebimentoCompra() != null
                && !entity.getRecebimentoCompra().getId().equals(request.recebimentoCompraId())) {
            throw new IllegalArgumentException("Não é permitido alterar recebimentoCompraId deste item por este endpoint.");
        }

        // não permite trocar item do pedido por este endpoint
        if (request.itemPedidoCompraId() != null
                && entity.getItemPedidoCompra() != null
                && !entity.getItemPedidoCompra().getId().equals(request.itemPedidoCompraId())) {
            throw new IllegalArgumentException("Não é permitido alterar itemPedidoCompraId deste item por este endpoint.");
        }

        itemRecebimentoMapper.updateEntity(request, entity);

        normalizarEValidarQuantidades(entity);

        return itemRecebimentoMapper.toResponse(itemRecebimentoRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-recebimento-compra-admin", fallbackMethod = "fallbackAdminId")
    public ItemRecebimentoCompraDto buscarPorId(Long id) {
        return itemRecebimentoMapper.toResponse(buscarItemRecebimento(id));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-recebimento-compra-admin", fallbackMethod = "fallbackAdminItemPedido")
    public ItemRecebimentoCompraDto buscarPorItemPedido(Long itemPedidoCompraId) {
        ItemRecebimentoCompra entity = itemRecebimentoRepository
                .findByItemPedidoCompraIdAndEmpresaId(itemPedidoCompraId, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Item de recebimento não encontrado para itemPedidoCompraId: " + itemPedidoCompraId));

        // proteção extra
        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao item de recebimento fora do tenant.");

        return itemRecebimentoMapper.toResponse(entity);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-recebimento-compra-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ItemRecebimentoCompraDto> listar(Pageable pageable) {
        return itemRecebimentoRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(itemRecebimentoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-recebimento-compra-admin", fallbackMethod = "fallbackAdminPageRecebimento")
    public Page<ItemRecebimentoCompraDto> listarPorRecebimento(Long recebimentoCompraId, Pageable pageable) {
        buscarRecebimentoCompra(recebimentoCompraId);

        return itemRecebimentoRepository
                .findByRecebimentoCompraIdAndEmpresaId(recebimentoCompraId, TenantContext.getEmpresaId(), pageable)
                .map(itemRecebimentoMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-item-recebimento-compra-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        itemRecebimentoRepository.delete(buscarItemRecebimento(id));
    }

    @CircuitBreaker(name = "compras-item-recebimento-compra-admin", fallbackMethod = "fallbackAdminVoidRecebimento")
    public void deletarPorRecebimento(Long recebimentoCompraId) {
        buscarRecebimentoCompra(recebimentoCompraId);

        itemRecebimentoRepository.deleteByRecebimentoCompraIdAndEmpresaId(
                recebimentoCompraId,
                TenantContext.getEmpresaId()
        );
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private ItemRecebimentoCompraDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de recebimento temporariamente indisponível");
    }

    private ItemRecebimentoCompraDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de recebimento temporariamente indisponível");
    }

    private ItemRecebimentoCompraDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de recebimento temporariamente indisponível");
    }

    private ItemRecebimentoCompraDto fallbackAdminItemPedido(Long itemPedidoCompraId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de recebimento temporariamente indisponível");
    }

    private Page<ItemRecebimentoCompraDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de recebimento temporariamente indisponível");
    }

    private Page<ItemRecebimentoCompraDto> fallbackAdminPageRecebimento(Long recebimentoCompraId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de recebimento temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de recebimento temporariamente indisponível");
    }

    private void fallbackAdminVoidRecebimento(Long recebimentoCompraId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de recebimento temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ItemRecebimentoCompra buscarItemRecebimento(Long id) {
        ItemRecebimentoCompra entity = itemRecebimentoRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("ItemRecebimentoCompra não encontrado: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao item de recebimento fora do tenant.");

        return entity;
    }

    private RecebimentoCompra buscarRecebimentoCompra(Long id) {
        // Ajuste para tenant-aware se seu RecebimentoCompraRepository tiver findByIdAndEmpresaId(...)
        RecebimentoCompra recebimento = recebimentoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RecebimentoCompra não encontrado: " + id));

        if (recebimento.getEmpresaId() != null && !recebimento.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao recebimento fora do tenant.");

        return recebimento;
    }

    private ItemPedidoCompra buscarItemPedidoCompra(Long id) {
        // Ajuste para tenant-aware se seu ItemPedidoCompraRepository tiver findByIdAndEmpresaId(...)
        ItemPedidoCompra itemPedido = itemPedidoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ItemPedidoCompra não encontrado: " + id));

        if (itemPedido.getEmpresaId() != null && !itemPedido.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao item do pedido fora do tenant.");

        return itemPedido;
    }

    private void validarDuplicidadeItemPedidoAoSalvar(Long itemPedidoCompraId) {
        if (itemRecebimentoRepository.existsByItemPedidoCompraIdAndEmpresaId(
                itemPedidoCompraId,
                TenantContext.getEmpresaId()
        )) {
            throw new IllegalArgumentException("Já existe recebimento para este item do pedido neste tenant.");
        }
    }

    private void normalizarEValidarQuantidades(ItemRecebimentoCompra entity) {
        if (entity.getQuantidadeRecebida() == null)
            throw new IllegalArgumentException("quantidadeRecebida é obrigatória.");

        // mantém compatível com @NotNull do model
        if (entity.getQuantidadeAprovada() == null) entity.setQuantidadeAprovada(BigDecimal.ZERO);
        if (entity.getQuantidadeRecusada() == null) entity.setQuantidadeRecusada(BigDecimal.ZERO);

        if (entity.getQuantidadeRecebida().signum() < 0
                || entity.getQuantidadeAprovada().signum() < 0
                || entity.getQuantidadeRecusada().signum() < 0) {
            throw new IllegalArgumentException("Quantidades não podem ser negativas.");
        }

        BigDecimal soma = entity.getQuantidadeAprovada().add(entity.getQuantidadeRecusada());
        if (soma.compareTo(entity.getQuantidadeRecebida()) != 0)
            throw new IllegalArgumentException("quantidadeAprovada + quantidadeRecusada deve ser igual a quantidadeRecebida.");
    }
}