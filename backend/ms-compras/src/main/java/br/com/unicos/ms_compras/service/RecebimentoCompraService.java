package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.RecebimentoCompraDto;
import br.com.unicos.ms_compras.mapper.RecebimentoCompraMapper;
import br.com.unicos.ms_compras.model.Fornecedor;
import br.com.unicos.ms_compras.model.PedidoCompra;
import br.com.unicos.ms_compras.model.RecebimentoCompra;
import br.com.unicos.ms_compras.repository.FornecedorRepository;
import br.com.unicos.ms_compras.repository.PedidoCompraRepository;
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

import java.time.LocalDate;

/**
 * Service responsável por regras de negócio e operações do agregado {@link RecebimentoCompra}.
 *
 * <p>
 * Padrões UniCoS:
 * - Relacionamentos com {@link PedidoCompra} e {@link Fornecedor} resolvidos no service (mapper exige entidades)
 * - Proteção tenant usando consultas tenant-aware
 * - Coleções {@code itens} e {@code documentosEntrada} não são manipuladas aqui (fluxos específicos)
 * </p>
 */
@Service
@Transactional
public class RecebimentoCompraService extends BaseTenantService<RecebimentoCompra, Long> {

    private final RecebimentoCompraRepository recebimentoRepository;
    private final PedidoCompraRepository pedidoCompraRepository;
    private final FornecedorRepository fornecedorRepository;
    private final RecebimentoCompraMapper recebimentoMapper;

    public RecebimentoCompraService(
            RecebimentoCompraRepository recebimentoRepository,
            PedidoCompraRepository pedidoCompraRepository,
            FornecedorRepository fornecedorRepository,
            RecebimentoCompraMapper recebimentoMapper
    ) {
        super(recebimentoRepository);
        this.recebimentoRepository = recebimentoRepository;
        this.pedidoCompraRepository = pedidoCompraRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.recebimentoMapper = recebimentoMapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-recebimento-compra-admin", fallbackMethod = "fallbackAdmin")
    public RecebimentoCompraDto salvar(RecebimentoCompraDto request) {
        if (request.pedidoCompraId() == null)
            throw new IllegalArgumentException("pedidoCompraId é obrigatório.");
        if (request.fornecedorId() == null)
            throw new IllegalArgumentException("fornecedorId é obrigatório.");

        PedidoCompra pedidoCompra = buscarPedidoCompra(request.pedidoCompraId());
        Fornecedor fornecedor = buscarFornecedor(request.fornecedorId());

        // regra simples: impede fornecedor divergente do pedido (se existir no pedido)
        if (pedidoCompra.getFornecedor() != null
                && pedidoCompra.getFornecedor().getId() != null
                && !pedidoCompra.getFornecedor().getId().equals(fornecedor.getId())) {
            throw new IllegalArgumentException("Fornecedor do recebimento deve ser o mesmo fornecedor do pedido de compra.");
        }

        RecebimentoCompra entity = recebimentoMapper.toEntity(request, pedidoCompra, fornecedor);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return recebimentoMapper.toResponse(recebimentoRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-recebimento-compra-admin", fallbackMethod = "fallbackAdminIdReq")
    public RecebimentoCompraDto atualizar(Long id, RecebimentoCompraDto request) {
        RecebimentoCompra entity = buscarRecebimento(id);

        if (request.pedidoCompraId() == null)
            throw new IllegalArgumentException("pedidoCompraId é obrigatório.");
        if (request.fornecedorId() == null)
            throw new IllegalArgumentException("fornecedorId é obrigatório.");

        PedidoCompra pedidoCompra = buscarPedidoCompra(request.pedidoCompraId());
        Fornecedor fornecedor = buscarFornecedor(request.fornecedorId());

        if (pedidoCompra.getFornecedor() != null
                && pedidoCompra.getFornecedor().getId() != null
                && !pedidoCompra.getFornecedor().getId().equals(fornecedor.getId())) {
            throw new IllegalArgumentException("Fornecedor do recebimento deve ser o mesmo fornecedor do pedido de compra.");
        }

        recebimentoMapper.updateEntity(request, entity, pedidoCompra, fornecedor);

        return recebimentoMapper.toResponse(recebimentoRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-recebimento-compra-admin", fallbackMethod = "fallbackAdminId")
    public RecebimentoCompraDto buscarPorId(Long id) {
        return recebimentoMapper.toResponse(buscarRecebimento(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-recebimento-compra-admin", fallbackMethod = "fallbackAdminPage")
    public Page<RecebimentoCompraDto> listar(Pageable pageable) {
        return recebimentoRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(recebimentoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-recebimento-compra-admin", fallbackMethod = "fallbackAdminPagePedido")
    public Page<RecebimentoCompraDto> listarPorPedidoCompra(Long pedidoCompraId, Pageable pageable) {
        buscarPedidoCompra(pedidoCompraId);

        return recebimentoRepository
                .findByPedidoCompraIdAndEmpresaId(pedidoCompraId, TenantContext.getEmpresaId(), pageable)
                .map(recebimentoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-recebimento-compra-admin", fallbackMethod = "fallbackAdminPageFornecedor")
    public Page<RecebimentoCompraDto> listarPorFornecedor(Long fornecedorId, Pageable pageable) {
        buscarFornecedor(fornecedorId);

        return recebimentoRepository
                .findByFornecedorIdAndEmpresaId(fornecedorId, TenantContext.getEmpresaId(), pageable)
                .map(recebimentoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-recebimento-compra-admin", fallbackMethod = "fallbackAdminPageStatus")
    public Page<RecebimentoCompraDto> listarPorStatus(String status, Pageable pageable) {
        if (status == null || status.isBlank())
            throw new IllegalArgumentException("status é obrigatório.");

        return recebimentoRepository
                .findByStatusAndEmpresaId(status, TenantContext.getEmpresaId(), pageable)
                .map(recebimentoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-recebimento-compra-admin", fallbackMethod = "fallbackAdminPagePeriodo")
    public Page<RecebimentoCompraDto> listarPorPeriodo(LocalDate dataInicial, LocalDate dataFinal, Pageable pageable) {
        if (dataInicial == null || dataFinal == null)
            throw new IllegalArgumentException("dataInicial e dataFinal são obrigatórias.");

        return recebimentoRepository
                .findByDataRecebimentoBetweenAndEmpresaId(dataInicial, dataFinal, TenantContext.getEmpresaId(), pageable)
                .map(recebimentoMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-recebimento-compra-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        recebimentoRepository.delete(buscarRecebimento(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private RecebimentoCompraDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de recebimentos temporariamente indisponível");
    }

    private RecebimentoCompraDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de recebimentos temporariamente indisponível");
    }

    private RecebimentoCompraDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de recebimentos temporariamente indisponível");
    }

    private Page<RecebimentoCompraDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de recebimentos temporariamente indisponível");
    }

    private Page<RecebimentoCompraDto> fallbackAdminPagePedido(Long pedidoCompraId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de recebimentos temporariamente indisponível");
    }

    private Page<RecebimentoCompraDto> fallbackAdminPageFornecedor(Long fornecedorId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de recebimentos temporariamente indisponível");
    }

    private Page<RecebimentoCompraDto> fallbackAdminPageStatus(String status, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de recebimentos temporariamente indisponível");
    }

    private Page<RecebimentoCompraDto> fallbackAdminPagePeriodo(LocalDate ini, LocalDate fim, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de recebimentos temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de recebimentos temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private RecebimentoCompra buscarRecebimento(Long id) {
        RecebimentoCompra entity = recebimentoRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("RecebimentoCompra não encontrado: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao recebimento fora do tenant.");

        return entity;
    }

    private PedidoCompra buscarPedidoCompra(Long id) {
        PedidoCompra entity = pedidoCompraRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("PedidoCompra não encontrado: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao pedido fora do tenant.");

        return entity;
    }

    private Fornecedor buscarFornecedor(Long id) {
        Fornecedor entity = fornecedorRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao fornecedor fora do tenant.");

        return entity;
    }
}