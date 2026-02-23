package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.CotacaoCompraDto;
import br.com.unicos.ms_compras.mapper.CotacaoCompraMapper;
import br.com.unicos.ms_compras.model.CotacaoCompra;
import br.com.unicos.ms_compras.model.PedidoCompra;
import br.com.unicos.ms_compras.repository.CotacaoCompraRepository;
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

import java.time.LocalDate;

/**
 * Service responsável por regras de negócio e operações do agregado {@link CotacaoCompra}.
 *
 * <p>
 * Padrões UniCoS:
 * - Proteção tenant usando métodos tenant-aware do repository
 * - Validação de código duplicado por tenant
 * - Relacionamento com {@link PedidoCompra} é resolvido no service (quando aplicável)
 * - Itens ({@code itens}) não são manipulados aqui (MVP); trate em service/endpoint específico
 * </p>
 */
@Service
@Transactional
public class CotacaoCompraService extends BaseTenantService<CotacaoCompra, Long> {

    private final CotacaoCompraRepository cotacaoRepository;
    private final CotacaoCompraMapper cotacaoMapper;
    private final PedidoCompraRepository pedidoCompraRepository;

    public CotacaoCompraService(
            CotacaoCompraRepository cotacaoRepository,
            CotacaoCompraMapper cotacaoMapper,
            PedidoCompraRepository pedidoCompraRepository
    ) {
        super(cotacaoRepository);
        this.cotacaoRepository = cotacaoRepository;
        this.cotacaoMapper = cotacaoMapper;
        this.pedidoCompraRepository = pedidoCompraRepository;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-cotacao-compra-admin", fallbackMethod = "fallbackAdmin")
    public CotacaoCompraDto salvar(CotacaoCompraDto request) {
        validarCodigoDuplicado(request.codigo());
        validarDatas(request.dataAbertura(), request.dataValidade());

        CotacaoCompra entity = cotacaoMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        // pedidoCompra (opcional): se vier no DTO, resolve e associa
        if (request.pedidoCompraId() != null)
            entity.setPedidoCompra(buscarPedidoCompra(request.pedidoCompraId()));

        return cotacaoMapper.toResponse(cotacaoRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-cotacao-compra-admin", fallbackMethod = "fallbackAdminIdReq")
    public CotacaoCompraDto atualizar(Long id, CotacaoCompraDto request) {
        CotacaoCompra entity = buscarCotacao(id);

        if (!entity.getCodigo().equalsIgnoreCase(request.codigo()))
            validarCodigoDuplicado(request.codigo());

        validarDatas(request.dataAbertura(), request.dataValidade());

        // pedidoCompra: caso de uso controlado (vincular/alterar) — permitido aqui por DTO, mas resolvido no service
        if (request.pedidoCompraId() != null) {
            PedidoCompra pedido = buscarPedidoCompra(request.pedidoCompraId());
            entity.setPedidoCompra(pedido);
        }

        cotacaoMapper.updateEntity(request, entity);

        return cotacaoMapper.toResponse(cotacaoRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-cotacao-compra-admin", fallbackMethod = "fallbackAdminId")
    public CotacaoCompraDto buscarPorId(Long id) {
        return cotacaoMapper.toResponse(buscarCotacao(id));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-cotacao-compra-admin", fallbackMethod = "fallbackAdminCodigo")
    public CotacaoCompraDto buscarPorCodigo(String codigo) {
        CotacaoCompra entity = cotacaoRepository
                .findByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Cotação não encontrada para o código: " + codigo));

        return cotacaoMapper.toResponse(entity);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-cotacao-compra-admin", fallbackMethod = "fallbackAdminPage")
    public Page<CotacaoCompraDto> listar(Pageable pageable) {
        return cotacaoRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(cotacaoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-cotacao-compra-admin", fallbackMethod = "fallbackAdminPageStatus")
    public Page<CotacaoCompraDto> listarPorStatus(String status, Pageable pageable) {
        return cotacaoRepository
                .findByStatusAndEmpresaId(status, TenantContext.getEmpresaId(), pageable)
                .map(cotacaoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-cotacao-compra-admin", fallbackMethod = "fallbackAdminPagePeriodo")
    public Page<CotacaoCompraDto> listarPorPeriodoAbertura(LocalDate dataInicial, LocalDate dataFinal, Pageable pageable) {
        validarPeriodo(dataInicial, dataFinal);

        return cotacaoRepository
                .findByDataAberturaBetweenAndEmpresaId(dataInicial, dataFinal, TenantContext.getEmpresaId(), pageable)
                .map(cotacaoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-cotacao-compra-admin", fallbackMethod = "fallbackAdminPageVencimento")
    public Page<CotacaoCompraDto> listarProximasDoVencimento(LocalDate dataLimite, Pageable pageable) {
        if (dataLimite == null)
            throw new IllegalArgumentException("dataLimite é obrigatório.");

        return cotacaoRepository
                .findByDataValidadeLessThanEqualAndEmpresaId(dataLimite, TenantContext.getEmpresaId(), pageable)
                .map(cotacaoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-cotacao-compra-admin", fallbackMethod = "fallbackAdminPagePedido")
    public Page<CotacaoCompraDto> listarPorPedidoCompra(Long pedidoCompraId, Pageable pageable) {
        // garante que pedido exista no tenant
        buscarPedidoCompra(pedidoCompraId);

        return cotacaoRepository
                .findByPedidoCompraIdAndEmpresaId(pedidoCompraId, TenantContext.getEmpresaId(), pageable)
                .map(cotacaoMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-cotacao-compra-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        cotacaoRepository.delete(buscarCotacao(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private CotacaoCompraDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de cotações de compra temporariamente indisponível");
    }

    private CotacaoCompraDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de cotações de compra temporariamente indisponível");
    }

    private CotacaoCompraDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de cotações de compra temporariamente indisponível");
    }

    private CotacaoCompraDto fallbackAdminCodigo(String codigo, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de cotações de compra temporariamente indisponível");
    }

    private Page<CotacaoCompraDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de cotações de compra temporariamente indisponível");
    }

    private Page<CotacaoCompraDto> fallbackAdminPageStatus(String status, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de cotações de compra temporariamente indisponível");
    }

    private Page<CotacaoCompraDto> fallbackAdminPagePeriodo(LocalDate dataInicial, LocalDate dataFinal, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de cotações de compra temporariamente indisponível");
    }

    private Page<CotacaoCompraDto> fallbackAdminPageVencimento(LocalDate dataLimite, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de cotações de compra temporariamente indisponível");
    }

    private Page<CotacaoCompraDto> fallbackAdminPagePedido(Long pedidoCompraId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de cotações de compra temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de cotações de compra temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private CotacaoCompra buscarCotacao(Long id) {
        CotacaoCompra entity = cotacaoRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Cotação de compra não encontrada: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado à cotação fora do tenant.");

        return entity;
    }

    private PedidoCompra buscarPedidoCompra(Long id) {
        // Mantém a mesma abordagem: tenant-aware.
        return pedidoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de compra não encontrado: " + id));
    }

    private void validarCodigoDuplicado(String codigo) {
        if (codigo == null || codigo.isBlank())
            throw new IllegalArgumentException("codigo é obrigatório.");
        if (cotacaoRepository.existsByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe uma cotação com o código informado neste tenant.");
    }

    private void validarDatas(LocalDate dataAbertura, LocalDate dataValidade) {
        if (dataAbertura == null)
            throw new IllegalArgumentException("dataAbertura é obrigatório.");
        if (dataValidade == null)
            throw new IllegalArgumentException("dataValidade é obrigatório.");
        if (dataValidade.isBefore(dataAbertura))
            throw new IllegalArgumentException("dataValidade não pode ser anterior à dataAbertura.");
    }

    private void validarPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        if (dataInicial == null || dataFinal == null)
            throw new IllegalArgumentException("dataInicial e dataFinal são obrigatórios.");
        if (dataFinal.isBefore(dataInicial))
            throw new IllegalArgumentException("dataFinal não pode ser anterior à dataInicial.");
    }
}