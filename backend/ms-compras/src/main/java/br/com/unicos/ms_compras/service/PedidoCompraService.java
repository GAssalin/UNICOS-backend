package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.PedidoCompraDto;
import br.com.unicos.ms_compras.enums.StatusPedidoCompra;
import br.com.unicos.ms_compras.mapper.PedidoCompraMapper;
import br.com.unicos.ms_compras.model.CondicaoPagamento;
import br.com.unicos.ms_compras.model.Fornecedor;
import br.com.unicos.ms_compras.model.PedidoCompra;
import br.com.unicos.ms_compras.repository.CondicaoPagamentoRepository;
import br.com.unicos.ms_compras.repository.FornecedorRepository;
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
import java.time.LocalDate;

/**
 * Service responsável por regras de negócio e operações do agregado {@link PedidoCompra}.
 */
@Service
@Transactional
public class PedidoCompraService extends BaseTenantService<PedidoCompra, Long> {

    private final PedidoCompraRepository pedidoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final CondicaoPagamentoRepository condicaoPagamentoRepository;
    private final PedidoCompraMapper pedidoMapper;

    public PedidoCompraService(
            PedidoCompraRepository pedidoRepository,
            FornecedorRepository fornecedorRepository,
            CondicaoPagamentoRepository condicaoPagamentoRepository,
            PedidoCompraMapper pedidoMapper
    ) {
        super(pedidoRepository);
        this.pedidoRepository = pedidoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.condicaoPagamentoRepository = condicaoPagamentoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdmin")
    public PedidoCompraDto salvar(PedidoCompraDto request) {
        validarCodigoDuplicado(request.codigo());

        if (request.fornecedorId() == null)
            throw new IllegalArgumentException("fornecedorId é obrigatório.");

        Fornecedor fornecedor = buscarFornecedor(request.fornecedorId());

        CondicaoPagamento condicaoPagamento = null;
        if (request.condicaoPagamentoId() != null)
            condicaoPagamento = buscarCondicaoPagamento(request.condicaoPagamentoId());

        PedidoCompra entity = pedidoMapper.toEntity(request, fornecedor, condicaoPagamento);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        normalizarTotais(entity);

        return pedidoMapper.toResponse(pedidoRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminIdReq")
    public PedidoCompraDto atualizar(Long id, PedidoCompraDto request) {
        PedidoCompra entity = buscarPedido(id);

        if (request.codigo() != null && !entity.getCodigo().equalsIgnoreCase(request.codigo()))
            validarCodigoDuplicado(request.codigo());

        if (request.fornecedorId() == null)
            throw new IllegalArgumentException("fornecedorId é obrigatório.");

        Fornecedor fornecedor = buscarFornecedor(request.fornecedorId());

        CondicaoPagamento condicaoPagamento = null;
        if (request.condicaoPagamentoId() != null)
            condicaoPagamento = buscarCondicaoPagamento(request.condicaoPagamentoId());

        pedidoMapper.updateEntity(request, entity, fornecedor, condicaoPagamento);

        normalizarTotais(entity);

        return pedidoMapper.toResponse(pedidoRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminId")
    public PedidoCompraDto buscarPorId(Long id) {
        return pedidoMapper.toResponse(buscarPedido(id));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminCodigo")
    public PedidoCompraDto buscarPorCodigo(String codigo) {
        PedidoCompra entity = pedidoRepository
                .findByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Pedido de compra não encontrado para o código: " + codigo));

        // proteção extra (caso o repository não seja 100% tenant-aware em algum cenário)
        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao pedido fora do tenant.");

        return pedidoMapper.toResponse(entity);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminPage")
    public Page<PedidoCompraDto> listar(Pageable pageable) {
        return pedidoRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(pedidoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminPageFornecedor")
    public Page<PedidoCompraDto> listarPorFornecedor(Long fornecedorId, Pageable pageable) {
        buscarFornecedor(fornecedorId);

        return pedidoRepository
                .findByFornecedorIdAndEmpresaId(fornecedorId, TenantContext.getEmpresaId(), pageable)
                .map(pedidoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminPageStatus")
    public Page<PedidoCompraDto> listarPorStatus(StatusPedidoCompra status, Pageable pageable) {
        return pedidoRepository
                .findByStatusPedidoCompraAndEmpresaId(status, TenantContext.getEmpresaId(), pageable)
                .map(pedidoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminPageEmissao")
    public Page<PedidoCompraDto> listarPorPeriodoEmissao(LocalDate dataInicial, LocalDate dataFinal, Pageable pageable) {
        if (dataInicial == null || dataFinal == null)
            throw new IllegalArgumentException("dataInicial e dataFinal são obrigatórias.");

        return pedidoRepository
                .findByDataEmissaoBetweenAndEmpresaId(dataInicial, dataFinal, TenantContext.getEmpresaId(), pageable)
                .map(pedidoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminPageEntrega")
    public Page<PedidoCompraDto> listarAteEntregaPrevista(LocalDate dataLimite, Pageable pageable) {
        if (dataLimite == null)
            throw new IllegalArgumentException("dataLimite é obrigatória.");

        return pedidoRepository
                .findByDataPrevistaEntregaLessThanEqualAndEmpresaId(dataLimite, TenantContext.getEmpresaId(), pageable)
                .map(pedidoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminPageCondicao")
    public Page<PedidoCompraDto> listarPorCondicaoPagamento(Long condicaoPagamentoId, Pageable pageable) {
        buscarCondicaoPagamento(condicaoPagamentoId);

        return pedidoRepository
                .findByCondicaoPagamentoIdAndEmpresaId(condicaoPagamentoId, TenantContext.getEmpresaId(), pageable)
                .map(pedidoMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        pedidoRepository.delete(buscarPedido(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private PedidoCompraDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pedidos de compra temporariamente indisponível");
    }

    private PedidoCompraDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pedidos de compra temporariamente indisponível");
    }

    private PedidoCompraDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pedidos de compra temporariamente indisponível");
    }

    private PedidoCompraDto fallbackAdminCodigo(String codigo, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pedidos de compra temporariamente indisponível");
    }

    private Page<PedidoCompraDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pedidos de compra temporariamente indisponível");
    }

    private Page<PedidoCompraDto> fallbackAdminPageFornecedor(Long fornecedorId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pedidos de compra temporariamente indisponível");
    }

    private Page<PedidoCompraDto> fallbackAdminPageStatus(StatusPedidoCompra status, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pedidos de compra temporariamente indisponível");
    }

    private Page<PedidoCompraDto> fallbackAdminPageEmissao(LocalDate ini, LocalDate fim, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pedidos de compra temporariamente indisponível");
    }

    private Page<PedidoCompraDto> fallbackAdminPageEntrega(LocalDate dataLimite, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pedidos de compra temporariamente indisponível");
    }

    private Page<PedidoCompraDto> fallbackAdminPageCondicao(Long condicaoPagamentoId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pedidos de compra temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pedidos de compra temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private PedidoCompra buscarPedido(Long id) {
        PedidoCompra entity = pedidoRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Pedido de compra não encontrado: " + id));

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

    private CondicaoPagamento buscarCondicaoPagamento(Long id) {
        // se seu repository já for tenant-aware, prefira findByIdAndEmpresaId(...)
        CondicaoPagamento entity = condicaoPagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Condição de pagamento não encontrada: " + id));

        if (entity.getEmpresaId() != null && !entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado à condição de pagamento fora do tenant.");

        return entity;
    }

    private void validarCodigoDuplicado(String codigo) {
        if (codigo == null || codigo.isBlank())
            throw new IllegalArgumentException("codigo é obrigatório.");
        if (pedidoRepository.existsByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe pedido de compra com o código informado neste tenant.");
    }

    private void normalizarTotais(PedidoCompra pedido) {
        // garante não-nulos conforme @NotNull do model
        if (pedido.getSubtotal() == null) pedido.setSubtotal(BigDecimal.ZERO);
        if (pedido.getDesconto() == null) pedido.setDesconto(BigDecimal.ZERO);
        if (pedido.getFrete() == null) pedido.setFrete(BigDecimal.ZERO);

        if (pedido.getSubtotal().signum() < 0 || pedido.getDesconto().signum() < 0 || pedido.getFrete().signum() < 0)
            throw new IllegalArgumentException("subtotal/desconto/frete não podem ser negativos.");

        BigDecimal total = pedido.getSubtotal()
                .subtract(pedido.getDesconto())
                .add(pedido.getFrete());

        if (total.signum() < 0)
            throw new IllegalArgumentException("desconto não pode tornar total negativo.");

        pedido.setTotal(total);
    }
}