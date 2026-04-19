package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoResponse;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoUpdateRequest;
import br.com.unicos.ms_empresa.mapper.EmpresaConfiguracaoMapper;
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

        validarChaveDuplicada(request.chave(), empresaId);

        EmpresaConfiguracao entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);

        return mapper.toResponse(repository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "empresa-configuracao-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaConfiguracaoResponse atualizar(
            String chave,
            EmpresaConfiguracaoUpdateRequest request
    ) {
        Long empresaId = TenantContext.getEmpresaId();
        EmpresaConfiguracao configuracao = buscarConfiguracaoPorChave(chave, empresaId);

        /*
         * Caso a chave esteja sendo alterada, valida duplicidade para a nova chave.
         */
        if (!configuracao.getChave().equals(request.chave())) {
            validarChaveDuplicada(request.chave(), empresaId);
        }

        mapper.updateEntity(request, configuracao);

        return mapper.toResponse(repository.save(configuracao));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-configuracao-admin", fallbackMethod = "fallbackAdminBuscar")
    public EmpresaConfiguracaoResponse buscarPorChave(String chave) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarConfiguracaoPorChave(chave, empresaId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-configuracao-admin", fallbackMethod = "fallbackAdminPage")
    public Page<EmpresaConfiguracaoResumoResponse> listar(Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "empresa-configuracao-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(String chave) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarConfiguracaoPorChave(chave, empresaId));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EmpresaConfiguracaoResponse fallbackAdmin(
            EmpresaConfiguracaoCreateRequest request,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de configurações da empresa temporariamente indisponível"
        );
    }

    private EmpresaConfiguracaoResponse fallbackAdmin(
            String chave,
            EmpresaConfiguracaoUpdateRequest request,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de configurações da empresa temporariamente indisponível"
        );
    }

    private EmpresaConfiguracaoResponse fallbackAdminBuscar(String chave, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de configurações da empresa temporariamente indisponível"
        );
    }

    private Page<EmpresaConfiguracaoResumoResponse> fallbackAdminPage(
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de configurações da empresa temporariamente indisponível"
        );
    }

    private void fallbackAdminVoid(String chave, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de configurações da empresa temporariamente indisponível"
        );
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private EmpresaConfiguracao buscarConfiguracaoPorChave(String chave, Long empresaId) {
        return repository.findByChaveAndEmpresaId(chave, empresaId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Configuração não encontrada para a chave: " + chave));
    }

    private void validarChaveDuplicada(String chave, Long empresaId) {
        if (repository.existsByChaveAndEmpresaId(chave, empresaId)) {
            throw new IllegalArgumentException("Já existe uma configuração cadastrada com a chave informada.");
        }
    }
}