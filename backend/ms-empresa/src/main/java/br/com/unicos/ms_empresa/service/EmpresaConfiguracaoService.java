package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoResponse;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoUpdateRequest;
import br.com.unicos.ms_empresa.mapper.EmpresaConfiguracaoMapper;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaConfiguracao;
import br.com.unicos.ms_empresa.repository.EmpresaConfiguracaoRepository;
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
public class EmpresaConfiguracaoService extends BaseTenantService<EmpresaConfiguracao, Long> {

    private final EmpresaConfiguracaoRepository repository;
    private final EmpresaConfiguracaoMapper mapper;

    public EmpresaConfiguracaoService(
            EmpresaConfiguracaoRepository repository,
            EmpresaConfiguracaoMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "empresa-configuracao-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaConfiguracaoResponse criar(EmpresaConfiguracaoCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();
        Empresa empresa = empresaRef(request.empresaRefId());

        validarChaveDuplicada(empresa, request.chave(), empresaId);

        EmpresaConfiguracao entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);

        return mapper.toResponse(repository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "empresa-configuracao-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaConfiguracaoResponse atualizar(
            Long empresaRefId,
            String chave,
            EmpresaConfiguracaoUpdateRequest request
    ) {
        Long empresaId = TenantContext.getEmpresaId();
        EmpresaConfiguracao configuracao = buscarConfiguracaoPorChave(empresaRefId, chave, empresaId);

        mapper.updateEntity(request, configuracao);

        return mapper.toResponse(repository.save(configuracao));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-configuracao-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaConfiguracaoResponse buscarPorChave(Long empresaRefId, String chave) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarConfiguracaoPorChave(empresaRefId, chave, empresaId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-configuracao-admin", fallbackMethod = "fallbackAdminPage")
    public Page<EmpresaConfiguracaoResumoResponse> listar(Long empresaRefId, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();
        Empresa empresa = empresaRef(empresaRefId);

        return repository.findByEmpresaAndEmpresaId(empresa, empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "empresa-configuracao-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long empresaRefId, String chave) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarConfiguracaoPorChave(empresaRefId, chave, empresaId));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EmpresaConfiguracaoResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de configurações da empresa temporariamente indisponível"
        );
    }

    private Page<EmpresaConfiguracaoResumoResponse> fallbackAdminPage(
            Long empresaRefId,
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de configurações da empresa temporariamente indisponível"
        );
    }

    private void fallbackAdminVoid(Long empresaRefId, String chave, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de configurações da empresa temporariamente indisponível"
        );
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private EmpresaConfiguracao buscarConfiguracaoPorChave(Long empresaRefId, String chave, Long empresaId) {
        Empresa empresa = empresaRef(empresaRefId);

        return repository.findByEmpresaAndChaveAndEmpresaId(empresa, chave, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Configuração não encontrada para a chave: " + chave));
    }

    private void validarChaveDuplicada(Empresa empresa, String chave, Long empresaId) {
        if (repository.existsByEmpresaAndChaveAndEmpresaId(empresa, chave, empresaId)) {
            throw new IllegalArgumentException("Já existe uma configuração cadastrada com a chave informada.");
        }
    }

    private Empresa empresaRef(Long empresaRefId) {
        return Empresa.builder()
                .id(empresaRefId)
                .build();
    }
}