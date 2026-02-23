package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.CondicaoPagamentoDto;
import br.com.unicos.ms_compras.mapper.CondicaoPagamentoMapper;
import br.com.unicos.ms_compras.model.CondicaoPagamento;
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

/**
 * Service responsável por regras de negócio e operações do agregado {@link CondicaoPagamento}.
 */
@Service
@Transactional
public class CondicaoPagamentoService extends BaseTenantService<CondicaoPagamento, Long> {

    private final CondicaoPagamentoRepository condicaoPagamentoRepository;
    private final CondicaoPagamentoMapper condicaoPagamentoMapper;

    public CondicaoPagamentoService(
            CondicaoPagamentoRepository condicaoPagamentoRepository,
            CondicaoPagamentoMapper condicaoPagamentoMapper
    ) {
        super(condicaoPagamentoRepository);
        this.condicaoPagamentoRepository = condicaoPagamentoRepository;
        this.condicaoPagamentoMapper = condicaoPagamentoMapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdmin")
    public CondicaoPagamentoDto salvar(CondicaoPagamentoDto request) {
        validarCodigoDuplicado(request.codigo());

        CondicaoPagamento entity = condicaoPagamentoMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId()); // tenant

        return condicaoPagamentoMapper.toResponse(condicaoPagamentoRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminIdReq")
    public CondicaoPagamentoDto atualizar(Long id, CondicaoPagamentoDto request) {
        CondicaoPagamento entity = buscarCondicaoPagamento(id);

        if (!entity.getCodigo().equalsIgnoreCase(request.codigo()))
            validarCodigoDuplicado(request.codigo());

        condicaoPagamentoMapper.updateEntity(request, entity);

        return condicaoPagamentoMapper.toResponse(condicaoPagamentoRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminId")
    public CondicaoPagamentoDto buscarPorId(Long id) {
        return condicaoPagamentoMapper.toResponse(buscarCondicaoPagamento(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminPage")
    public Page<CondicaoPagamentoDto> listar(Pageable pageable) {
        return condicaoPagamentoRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(condicaoPagamentoMapper::toResponse);
    }

    // (opcional, mas útil) buscar por código dentro do tenant
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminCodigo")
    public CondicaoPagamentoDto buscarPorCodigo(String codigo) {
        CondicaoPagamento entity = condicaoPagamentoRepository
                .findByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Condição de pagamento não encontrada para o código: " + codigo));

        return condicaoPagamentoMapper.toResponse(entity);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        // Se você tiver um PermissaoClient no ms_compras, pluga aqui (igual no EstoqueService).
        // Caso contrário, mantenho a proteção tenant + not found.
        condicaoPagamentoRepository.delete(buscarCondicaoPagamento(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private CondicaoPagamentoDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de condições de pagamento temporariamente indisponível");
    }

    private CondicaoPagamentoDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de condições de pagamento temporariamente indisponível");
    }

    private CondicaoPagamentoDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de condições de pagamento temporariamente indisponível");
    }

    private Page<CondicaoPagamentoDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de condições de pagamento temporariamente indisponível");
    }

    private CondicaoPagamentoDto fallbackAdminCodigo(String codigo, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de condições de pagamento temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de condições de pagamento temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private CondicaoPagamento buscarCondicaoPagamento(Long id) {
        CondicaoPagamento entity = condicaoPagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Condição de pagamento não encontrada: " + id));

        // proteção adicional: garante tenant correto (caso findById não esteja tenant-aware)
        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado à condição de pagamento fora do tenant.");

        return entity;
    }

    private void validarCodigoDuplicado(String codigo) {
        if (condicaoPagamentoRepository.existsByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe uma condição de pagamento com o código informado neste tenant.");
    }
}