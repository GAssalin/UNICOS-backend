package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroResponse;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroUpdateRequest;
import br.com.unicos.ms_empresa.mapper.EmpresaParametroMapper;
import br.com.unicos.ms_empresa.model.EmpresaParametro;
import br.com.unicos.ms_empresa.repository.EmpresaParametroRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class EmpresaParametroService extends BaseTenantService<EmpresaParametro, Long> {

    private static final String CB = "empresa-parametro-admin";
    private static final String MSG = "Serviço de parâmetros da empresa temporariamente indisponível";

    private final EmpresaParametroRepository repository;
    private final EmpresaParametroMapper mapper;

    public EmpresaParametroService(
            EmpresaParametroRepository repository,
            EmpresaParametroMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = CB, fallbackMethod = "fallback")
    public EmpresaParametroResponse criar(EmpresaParametroCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        validarChaveDuplicada(request.chave(), empresaId);

        EmpresaParametro entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);

        return mapper.toResponse(repository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = CB, fallbackMethod = "fallbackUpdate")
    public EmpresaParametroResponse atualizar(String chave, EmpresaParametroUpdateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        EmpresaParametro entity = buscarPorChaveInterno(chave, empresaId);

        mapper.updateEntity(request, entity);

        return mapper.toResponse(repository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = CB, fallbackMethod = "fallbackGet")
    public EmpresaParametroResponse buscarPorChave(String chave) {
        Long empresaId = TenantContext.getEmpresaId();

        return mapper.toResponse(buscarPorChaveInterno(chave, empresaId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = CB, fallbackMethod = "fallbackPage")
    public Page<EmpresaParametroResumoResponse> listar(Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = CB, fallbackMethod = "fallbackVoid")
    public void remover(String chave) {
        Long empresaId = TenantContext.getEmpresaId();

        repository.deleteByChaveAndEmpresaId(chave, empresaId);
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EmpresaParametroResponse fallback(EmpresaParametroCreateRequest req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, MSG, ex);
    }

    private EmpresaParametroResponse fallbackUpdate(String chave, EmpresaParametroUpdateRequest req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, MSG, ex);
    }

    private EmpresaParametroResponse fallbackGet(String chave, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, MSG, ex);
    }

    private Page<EmpresaParametroResumoResponse> fallbackPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, MSG, ex);
    }

    private void fallbackVoid(String chave, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, MSG, ex);
    }

    // ============================================================
    // AUX
    // ============================================================

    private EmpresaParametro buscarPorChaveInterno(String chave, Long empresaId) {
        return repository.findByChaveAndEmpresaId(chave, empresaId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Parâmetro não encontrado para a chave: " + chave)
                );
    }

    private void validarChaveDuplicada(String chave, Long empresaId) {
        if (repository.existsByChaveAndEmpresaId(chave, empresaId)) {
            throw new IllegalArgumentException("Já existe um parâmetro com essa chave.");
        }
    }
}