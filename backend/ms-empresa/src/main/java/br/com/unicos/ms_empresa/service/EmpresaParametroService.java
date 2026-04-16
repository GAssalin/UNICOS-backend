package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroResponse;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroUpdateRequest;
import br.com.unicos.ms_empresa.mapper.EmpresaParametroMapper;
import br.com.unicos.ms_empresa.model.Empresa;
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

    @CircuitBreaker(name = "empresa-parametro-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaParametroResponse criar(EmpresaParametroCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();
        Empresa empresa = empresaRef(request.empresaRefId());

        validarChaveDuplicada(empresa, request.chave(), empresaId);

        EmpresaParametro parametro = mapper.toEntity(request);
        parametro.setEmpresaId(empresaId);

        return mapper.toResponse(repository.save(parametro));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "empresa-parametro-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaParametroResponse atualizar(
            Long empresaRefId,
            String chave,
            EmpresaParametroUpdateRequest request
    ) {
        Long empresaId = TenantContext.getEmpresaId();
        EmpresaParametro parametro = buscarParametroPorChave(empresaRefId, chave, empresaId);

        mapper.updateEntity(request, parametro);

        return mapper.toResponse(repository.save(parametro));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-parametro-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaParametroResponse buscarPorChave(Long empresaRefId, String chave) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarParametroPorChave(empresaRefId, chave, empresaId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-parametro-admin", fallbackMethod = "fallbackAdminPage")
    public Page<EmpresaParametroResumoResponse> listar(Long empresaRefId, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();
        Empresa empresa = empresaRef(empresaRefId);

        return repository.findByEmpresaAndEmpresaId(empresa, empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "empresa-parametro-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long empresaRefId, String chave) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarParametroPorChave(empresaRefId, chave, empresaId));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EmpresaParametroResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de parâmetros da empresa temporariamente indisponível"
        );
    }

    private Page<EmpresaParametroResumoResponse> fallbackAdminPage(
            Long empresaRefId,
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de parâmetros da empresa temporariamente indisponível"
        );
    }

    private void fallbackAdminVoid(Long empresaRefId, String chave, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de parâmetros da empresa temporariamente indisponível"
        );
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private EmpresaParametro buscarParametroPorChave(Long empresaRefId, String chave, Long empresaId) {
        Empresa empresa = empresaRef(empresaRefId);

        return repository.findByEmpresaAndChaveAndEmpresaId(empresa, chave, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Parâmetro não encontrado para a chave: " + chave));
    }

    private void validarChaveDuplicada(Empresa empresa, String chave, Long empresaId) {
        if (repository.existsByEmpresaAndChaveAndEmpresaId(empresa, chave, empresaId)) {
            throw new IllegalArgumentException(
                    "Já existe um parâmetro cadastrado com a chave informada para esta empresa."
            );
        }
    }

    private Empresa empresaRef(Long empresaRefId) {
        return Empresa.builder()
                .id(empresaRefId)
                .build();
    }
}