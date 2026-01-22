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

/**
 * Implementação das regras de negócio relacionadas
 * aos parâmetros flexíveis da empresa.
 *
 * <p>
 * Responsável por garantir:
 * <ul>
 *     <li>Isolamento multi-tenant</li>
 *     <li>Validação de duplicidade de chave</li>
 *     <li>Controle de permissões</li>
 * </ul>
 * </p>
 */
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
        Empresa empresa = empresaRef(request.empresaRefId());

        validarChaveDuplicada(empresa, request.chave());

        EmpresaParametro parametro = mapper.toEntity(request);
        parametro.setEmpresaId(TenantContext.getEmpresaId());

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
        EmpresaParametro parametro =
                buscarParametroPorChaveEntidade(empresaRefId, chave);

        mapper.updateEntity(request, parametro);

        return mapper.toResponse(repository.save(parametro));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-parametro-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaParametroResponse buscarPorChave(Long empresaRefId, String chave) {
        return mapper.toResponse(buscarParametroPorChaveEntidade(empresaRefId, chave));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-parametro-admin", fallbackMethod = "fallbackAdminPage")
    public Page<EmpresaParametroResumoResponse> listar(
            Long empresaRefId,
            Pageable pageable
    ) {
        Empresa empresa = empresaRef(empresaRefId);

        return repository
                .findByEmpresaAndEmpresaId(
                        empresa,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "empresa-parametro-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long empresaRefId, String chave) {
        EmpresaParametro parametro =
                buscarParametroPorChaveEntidade(empresaRefId, chave);

        repository.delete(parametro);
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EmpresaParametroResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de parâmetros da empresa temporariamente indisponível");
    }

    private Page<EmpresaParametroResumoResponse> fallbackAdminPage(
            Long empresaRefId,
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de parâmetros da empresa temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long empresaRefId, String chave, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de parâmetros da empresa temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private EmpresaParametro buscarParametroPorChaveEntidade(Long empresaRefId, String chave) {
        Empresa empresa = empresaRef(empresaRefId);

        return repository.findByEmpresaAndChaveAndEmpresaId(
                        empresa,
                        chave,
                        TenantContext.getEmpresaId()
                )
                .orElseThrow(() -> new EntityNotFoundException("Parâmetro não encontrado para a chave: " + chave));
    }

    private void validarChaveDuplicada(Empresa empresa, String chave) {
        if (repository.existsByEmpresaAndChaveAndEmpresaId(
                empresa,
                chave,
                TenantContext.getEmpresaId()
        ))
            throw new IllegalArgumentException("Já existe um parâmetro cadastrado com a chave informada para esta empresa.");
    }

    private Empresa empresaRef(Long empresaRefId) {
        return Empresa.builder()
                .id(empresaRefId)
                .build();
    }
}
