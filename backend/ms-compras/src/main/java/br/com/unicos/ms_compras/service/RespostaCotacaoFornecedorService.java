package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.RespostaCotacaoFornecedorDto;
import br.com.unicos.ms_compras.mapper.RespostaCotacaoFornecedorMapper;
import br.com.unicos.ms_compras.model.CondicaoPagamento;
import br.com.unicos.ms_compras.model.CotacaoCompra;
import br.com.unicos.ms_compras.model.Fornecedor;
import br.com.unicos.ms_compras.model.RespostaCotacaoFornecedor;
import br.com.unicos.ms_compras.repository.CondicaoPagamentoRepository;
import br.com.unicos.ms_compras.repository.CotacaoCompraRepository;
import br.com.unicos.ms_compras.repository.FornecedorRepository;
import br.com.unicos.ms_compras.repository.RespostaCotacaoFornecedorRepository;
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
 * Service responsável por regras de negócio e operações do agregado {@link RespostaCotacaoFornecedor}.
 *
 * <p>
 * Regras aplicadas:
 * - Unicidade de resposta por (cotacaoCompra, fornecedor) no tenant
 * - Relacionamentos (CotacaoCompra/Fornecedor/CondicaoPagamento) resolvidos no service
 * - Totais normalizados (totalProposto/frete) para evitar null/negativos
 * - Coleção de itens não é manipulada aqui (fluxo específico em ItemRespostaCotacaoFornecedorService)
 * </p>
 */
@Service
@Transactional
public class RespostaCotacaoFornecedorService extends BaseTenantService<RespostaCotacaoFornecedor, Long> {

    private final RespostaCotacaoFornecedorRepository respostaRepository;
    private final CotacaoCompraRepository cotacaoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final CondicaoPagamentoRepository condicaoPagamentoRepository;
    private final RespostaCotacaoFornecedorMapper respostaMapper;

    public RespostaCotacaoFornecedorService(
            RespostaCotacaoFornecedorRepository respostaRepository,
            CotacaoCompraRepository cotacaoRepository,
            FornecedorRepository fornecedorRepository,
            CondicaoPagamentoRepository condicaoPagamentoRepository,
            RespostaCotacaoFornecedorMapper respostaMapper
    ) {
        super(respostaRepository);
        this.respostaRepository = respostaRepository;
        this.cotacaoRepository = cotacaoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.condicaoPagamentoRepository = condicaoPagamentoRepository;
        this.respostaMapper = respostaMapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdmin")
    public RespostaCotacaoFornecedorDto salvar(RespostaCotacaoFornecedorDto request) {
        if (request.cotacaoCompraId() == null)
            throw new IllegalArgumentException("cotacaoCompraId é obrigatório.");
        if (request.fornecedorId() == null)
            throw new IllegalArgumentException("fornecedorId é obrigatório.");

        CotacaoCompra cotacao = buscarCotacao(request.cotacaoCompraId());
        Fornecedor fornecedor = buscarFornecedor(request.fornecedorId());

        validarDuplicidadeCotacaoFornecedor(cotacao.getId(), fornecedor.getId());

        CondicaoPagamento condicaoPagamento = null;
        if (request.condicaoPagamentoId() != null)
            condicaoPagamento = buscarCondicaoPagamento(request.condicaoPagamentoId());

        RespostaCotacaoFornecedor entity = respostaMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());
        entity.setCotacaoCompra(cotacao);
        entity.setFornecedor(fornecedor);
        entity.setCondicaoPagamento(condicaoPagamento);

        normalizarTotais(entity);

        return respostaMapper.toResponse(respostaRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminIdReq")
    public RespostaCotacaoFornecedorDto atualizar(Long id, RespostaCotacaoFornecedorDto request) {
        RespostaCotacaoFornecedor entity = buscarResposta(id);

        // Não permite trocar vínculos (cotacaoCompra/fornecedor) via update
        if (request.cotacaoCompraId() != null
                && entity.getCotacaoCompra() != null
                && !entity.getCotacaoCompra().getId().equals(request.cotacaoCompraId())) {
            throw new IllegalArgumentException("Não é permitido alterar cotacaoCompraId desta resposta por este endpoint.");
        }

        if (request.fornecedorId() != null
                && entity.getFornecedor() != null
                && !entity.getFornecedor().getId().equals(request.fornecedorId())) {
            throw new IllegalArgumentException("Não é permitido alterar fornecedorId desta resposta por este endpoint.");
        }

        CondicaoPagamento condicaoPagamento = null;
        if (request.condicaoPagamentoId() != null)
            condicaoPagamento = buscarCondicaoPagamento(request.condicaoPagamentoId());

        respostaMapper.updateEntity(request, entity);
        entity.setCondicaoPagamento(condicaoPagamento);

        normalizarTotais(entity);

        return respostaMapper.toResponse(respostaRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminId")
    public RespostaCotacaoFornecedorDto buscarPorId(Long id) {
        return respostaMapper.toResponse(buscarResposta(id));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminByCotacaoFornecedor")
    public RespostaCotacaoFornecedorDto buscarPorCotacaoEFornecedor(Long cotacaoCompraId, Long fornecedorId) {
        RespostaCotacaoFornecedor entity = respostaRepository
                .findByCotacaoCompraIdAndFornecedorIdAndEmpresaId(
                        cotacaoCompraId,
                        fornecedorId,
                        TenantContext.getEmpresaId()
                )
                .orElseThrow(() -> new EntityNotFoundException(
                        "Resposta não encontrada para cotacaoCompraId=" + cotacaoCompraId + " e fornecedorId=" + fornecedorId
                ));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado à resposta fora do tenant.");

        return respostaMapper.toResponse(entity);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminPage")
    public Page<RespostaCotacaoFornecedorDto> listar(Pageable pageable) {
        return respostaRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(respostaMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminPageCotacao")
    public Page<RespostaCotacaoFornecedorDto> listarPorCotacao(Long cotacaoCompraId, Pageable pageable) {
        buscarCotacao(cotacaoCompraId);

        return respostaRepository
                .findByCotacaoCompraIdAndEmpresaId(cotacaoCompraId, TenantContext.getEmpresaId(), pageable)
                .map(respostaMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminPageFornecedor")
    public Page<RespostaCotacaoFornecedorDto> listarPorFornecedor(Long fornecedorId, Pageable pageable) {
        buscarFornecedor(fornecedorId);

        return respostaRepository
                .findByFornecedorIdAndEmpresaId(fornecedorId, TenantContext.getEmpresaId(), pageable)
                .map(respostaMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminPageStatus")
    public Page<RespostaCotacaoFornecedorDto> listarPorStatus(String status, Pageable pageable) {
        if (status == null || status.isBlank())
            throw new IllegalArgumentException("status é obrigatório.");

        return respostaRepository
                .findByStatusAndEmpresaId(status, TenantContext.getEmpresaId(), pageable)
                .map(respostaMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminPageCondicao")
    public Page<RespostaCotacaoFornecedorDto> listarPorCondicaoPagamento(Long condicaoPagamentoId, Pageable pageable) {
        buscarCondicaoPagamento(condicaoPagamentoId);

        return respostaRepository
                .findByCondicaoPagamentoIdAndEmpresaId(condicaoPagamentoId, TenantContext.getEmpresaId(), pageable)
                .map(respostaMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        respostaRepository.delete(buscarResposta(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private RespostaCotacaoFornecedorDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de respostas de cotação temporariamente indisponível");
    }

    private RespostaCotacaoFornecedorDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de respostas de cotação temporariamente indisponível");
    }

    private RespostaCotacaoFornecedorDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de respostas de cotação temporariamente indisponível");
    }

    private RespostaCotacaoFornecedorDto fallbackAdminByCotacaoFornecedor(Long cotacaoCompraId, Long fornecedorId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de respostas de cotação temporariamente indisponível");
    }

    private Page<RespostaCotacaoFornecedorDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de respostas de cotação temporariamente indisponível");
    }

    private Page<RespostaCotacaoFornecedorDto> fallbackAdminPageCotacao(Long cotacaoCompraId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de respostas de cotação temporariamente indisponível");
    }

    private Page<RespostaCotacaoFornecedorDto> fallbackAdminPageFornecedor(Long fornecedorId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de respostas de cotação temporariamente indisponível");
    }

    private Page<RespostaCotacaoFornecedorDto> fallbackAdminPageStatus(String status, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de respostas de cotação temporariamente indisponível");
    }

    private Page<RespostaCotacaoFornecedorDto> fallbackAdminPageCondicao(Long condicaoPagamentoId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de respostas de cotação temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de respostas de cotação temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private RespostaCotacaoFornecedor buscarResposta(Long id) {
        RespostaCotacaoFornecedor entity = respostaRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("RespostaCotacaoFornecedor não encontrada: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado à resposta fora do tenant.");

        return entity;
    }

    private CotacaoCompra buscarCotacao(Long id) {
        CotacaoCompra entity = cotacaoRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("CotacaoCompra não encontrada: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado à cotação fora do tenant.");

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
        // se seu repository já tiver findByIdAndEmpresaId(...), prefira esse método
        CondicaoPagamento entity = condicaoPagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Condição de pagamento não encontrada: " + id));

        if (entity.getEmpresaId() != null && !entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado à condição de pagamento fora do tenant.");

        return entity;
    }

    private void validarDuplicidadeCotacaoFornecedor(Long cotacaoCompraId, Long fornecedorId) {
        if (respostaRepository.existsByCotacaoCompraIdAndFornecedorIdAndEmpresaId(
                cotacaoCompraId,
                fornecedorId,
                TenantContext.getEmpresaId()
        )) {
            throw new IllegalArgumentException("Fornecedor já respondeu esta cotação neste tenant.");
        }
    }

    private void normalizarTotais(RespostaCotacaoFornecedor entity) {
        if (entity.getTotalProposto() == null) entity.setTotalProposto(BigDecimal.ZERO);
        if (entity.getFrete() == null) entity.setFrete(BigDecimal.ZERO);

        if (entity.getTotalProposto().signum() < 0 || entity.getFrete().signum() < 0)
            throw new IllegalArgumentException("totalProposto e frete não podem ser negativos.");
    }
}