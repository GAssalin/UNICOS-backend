package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.CondicaoPagamentoParcelaDto;
import br.com.unicos.ms_compras.mapper.CondicaoPagamentoParcelaMapper;
import br.com.unicos.ms_compras.model.CondicaoPagamento;
import br.com.unicos.ms_compras.model.CondicaoPagamentoParcela;
import br.com.unicos.ms_compras.repository.CondicaoPagamentoParcelaRepository;
import br.com.unicos.ms_compras.repository.CondicaoPagamentoRepository;
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
import java.math.RoundingMode;
import java.util.List;

/**
 * Service responsável por regras de negócio e operações do agregado {@link CondicaoPagamentoParcela}.
 *
 * <p>
 * Padrões:
 * - Resolve {@link CondicaoPagamento} no service
 * - Protege tenant ao buscar entidades
 * - Valida duplicidade de ordem por condição/tenant
 * - (Opcional/recomendado) valida soma de percentuais = 100.00
 * </p>
 */
@Service
@Transactional
public class CondicaoPagamentoParcelaService extends BaseTenantService<CondicaoPagamentoParcela, Long> {

    private static final BigDecimal CEM = new BigDecimal("100.00");

    private final CondicaoPagamentoParcelaRepository parcelaRepository;
    private final CondicaoPagamentoRepository condicaoPagamentoRepository;
    private final CondicaoPagamentoParcelaMapper parcelaMapper;

    public CondicaoPagamentoParcelaService(
            CondicaoPagamentoParcelaRepository parcelaRepository,
            CondicaoPagamentoRepository condicaoPagamentoRepository,
            CondicaoPagamentoParcelaMapper parcelaMapper
    ) {
        super(parcelaRepository);
        this.parcelaRepository = parcelaRepository;
        this.condicaoPagamentoRepository = condicaoPagamentoRepository;
        this.parcelaMapper = parcelaMapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-condicao-pagamento-parcela-admin", fallbackMethod = "fallbackAdmin")
    public CondicaoPagamentoParcelaDto salvar(CondicaoPagamentoParcelaDto request) {
        if (request.condicaoPagamentoId() == null)
            throw new IllegalArgumentException("condicaoPagamentoId é obrigatório para criar uma parcela.");

        CondicaoPagamento condicao = buscarCondicaoPagamento(request.condicaoPagamentoId());

        validarOrdemDuplicada(condicao.getId(), request.ordem());

        CondicaoPagamentoParcela entity = parcelaMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());
        entity.setCondicaoPagamento(condicao);

        CondicaoPagamentoParcela salvo = parcelaRepository.save(entity);

        validarSomaPercentuais(condicao.getId());

        return parcelaMapper.toResponse(salvo);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-condicao-pagamento-parcela-admin", fallbackMethod = "fallbackAdminIdReq")
    public CondicaoPagamentoParcelaDto atualizar(Long id, CondicaoPagamentoParcelaDto request) {
        CondicaoPagamentoParcela entity = buscarParcela(id);

        // não permite trocar o vínculo de condicaoPagamento aqui
        if (request.condicaoPagamentoId() != null
                && entity.getCondicaoPagamento() != null
                && !entity.getCondicaoPagamento().getId().equals(request.condicaoPagamentoId())) {
            throw new IllegalArgumentException("Não é permitido alterar condicaoPagamentoId desta parcela por este endpoint.");
        }

        Long condicaoId = entity.getCondicaoPagamento().getId();

        // valida duplicidade de ordem apenas se a ordem mudou
        if (request.ordem() != null && !request.ordem().equals(entity.getOrdem()))
            validarOrdemDuplicada(condicaoId, request.ordem());

        parcelaMapper.updateEntity(request, entity);

        CondicaoPagamentoParcela atualizado = parcelaRepository.save(entity);

        validarSomaPercentuais(condicaoId);

        return parcelaMapper.toResponse(atualizado);
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-condicao-pagamento-parcela-admin", fallbackMethod = "fallbackAdminId")
    public CondicaoPagamentoParcelaDto buscarPorId(Long id) {
        return parcelaMapper.toResponse(buscarParcela(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-condicao-pagamento-parcela-admin", fallbackMethod = "fallbackAdminPage")
    public Page<CondicaoPagamentoParcelaDto> listar(Pageable pageable) {
        return parcelaRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(parcelaMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-condicao-pagamento-parcela-admin", fallbackMethod = "fallbackAdminPageCondicao")
    public Page<CondicaoPagamentoParcelaDto> listarPorCondicaoPagamento(Long condicaoPagamentoId, Pageable pageable) {
        buscarCondicaoPagamento(condicaoPagamentoId);

        return parcelaRepository
                .findByCondicaoPagamentoIdAndEmpresaId(condicaoPagamentoId, TenantContext.getEmpresaId(), pageable)
                .map(parcelaMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-condicao-pagamento-parcela-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        CondicaoPagamentoParcela parcela = buscarParcela(id);
        Long condicaoId = parcela.getCondicaoPagamento().getId();

        parcelaRepository.delete(parcela);

        validarSomaPercentuais(condicaoId);
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private CondicaoPagamentoParcelaDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de parcelas de condição de pagamento temporariamente indisponível");
    }

    private CondicaoPagamentoParcelaDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de parcelas de condição de pagamento temporariamente indisponível");
    }

    private CondicaoPagamentoParcelaDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de parcelas de condição de pagamento temporariamente indisponível");
    }

    private Page<CondicaoPagamentoParcelaDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de parcelas de condição de pagamento temporariamente indisponível");
    }

    private Page<CondicaoPagamentoParcelaDto> fallbackAdminPageCondicao(Long condicaoPagamentoId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de parcelas de condição de pagamento temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de parcelas de condição de pagamento temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private CondicaoPagamentoParcela buscarParcela(Long id) {
        CondicaoPagamentoParcela entity = parcelaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parcela da condição de pagamento não encontrada: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado à parcela fora do tenant.");

        return entity;
    }

    private CondicaoPagamento buscarCondicaoPagamento(Long id) {
        CondicaoPagamento entity = condicaoPagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Condição de pagamento não encontrada: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado à condição de pagamento fora do tenant.");

        return entity;
    }

    private void validarOrdemDuplicada(Long condicaoPagamentoId, Integer ordem) {
        if (ordem == null) return;

        if (parcelaRepository.existsByCondicaoPagamentoIdAndOrdemAndEmpresaId(
                condicaoPagamentoId,
                ordem,
                TenantContext.getEmpresaId()
        )) {
            throw new IllegalArgumentException("Já existe uma parcela com a ordem informada nesta condição de pagamento (tenant).");
        }
    }

    /**
     * Valida (recomendado) se a soma dos percentuais das parcelas da condição = 100.00.
     *
     * <p>Se você quiser permitir "rascunho", torne essa validação condicional.</p>
     */
    private void validarSomaPercentuais(Long condicaoPagamentoId) {
        List<CondicaoPagamentoParcela> parcelas = parcelaRepository
                .findByCondicaoPagamentoIdAndEmpresaIdOrderByOrdemAsc(condicaoPagamentoId, TenantContext.getEmpresaId());

        BigDecimal soma = parcelas.stream()
                .map(CondicaoPagamentoParcela::getPercentual)
                .filter(p -> p != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        if (!CEM.equals(soma))
            throw new IllegalArgumentException("A soma dos percentuais das parcelas deve ser 100.00. Soma atual: " + soma);
    }
}