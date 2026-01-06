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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class EmpresaConfiguracaoService extends BaseTenantService<EmpresaConfiguracao, Long> {

    private final EmpresaConfiguracaoRepository repository;
    private final EmpresaConfiguracaoMapper mapper;
    private final PermissionCheckService permissionCheckService;

    public EmpresaConfiguracaoService(
            EmpresaConfiguracaoRepository repository,
            EmpresaConfiguracaoMapper mapper,
            PermissionCheckService permissionCheckService
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
        this.permissionCheckService = permissionCheckService;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "empresa-configuracao-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaConfiguracaoResponse criar(EmpresaConfiguracaoCreateRequest request) {
        if (!permissionCheckService.hasPermission("EMPRESA_CONFIGURACAO_CRIAR"))
            throw new AccessDeniedException("Usuário não possui permissão para criar configurações.");

        Empresa empresaRef = empresaRef(request.empresaRefId());
        validarChaveDuplicada(empresaRef, request.chave());

        EmpresaConfiguracao entity = mapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

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
        if (!permissionCheckService.hasPermission("EMPRESA_CONFIGURACAO_EDITAR"))
            throw new AccessDeniedException("Usuário não possui permissão para editar configurações.");

        EmpresaConfiguracao configuracao =
                buscarPorChaveEntidade(empresaRefId, chave);

        mapper.updateEntity(request, configuracao);

        return mapper.toResponse(repository.save(configuracao));
    }

    // ============================================================
    // GET BY KEY
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-configuracao-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaConfiguracaoResponse buscarPorChave(
            Long empresaRefId,
            String chave
    ) {
        if (!permissionCheckService.hasPermission("EMPRESA_CONFIGURACAO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar configurações.");
        return mapper.toResponse(buscarPorChaveEntidade(empresaRefId, chave));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-configuracao-admin", fallbackMethod = "fallbackAdminPage")
    public Page<EmpresaConfiguracaoResumoResponse> listar(
            Long empresaRefId,
            Pageable pageable
    ) {
        if (!permissionCheckService.hasPermission("EMPRESA_CONFIGURACAO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar configurações.");

        Empresa empresaRef = empresaRef(empresaRefId);

        return repository
                .findByEmpresaAndEmpresaId(
                        empresaRef,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "empresa-configuracao-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long empresaRefId, String chave) {
        if (!permissionCheckService.hasPermission("EMPRESA_CONFIGURACAO_EXCLUIR"))
            throw new AccessDeniedException("Usuário não possui permissão para excluir configurações.");

        EmpresaConfiguracao configuracao =
                buscarPorChaveEntidade(empresaRefId, chave);

        repository.delete(configuracao);
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EmpresaConfiguracaoResponse fallbackAdmin(
            Object request,
            Throwable ex
    ) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de configurações da empresa temporariamente indisponível");
    }

    private Page<EmpresaConfiguracaoResumoResponse> fallbackAdminPage(
            Long empresaRefId,
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de configurações da empresa temporariamente indisponível");
    }

    private void fallbackAdminVoid(
            Long empresaRefId,
            String chave,
            Throwable ex
    ) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de configurações da empresa temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private EmpresaConfiguracao buscarPorChaveEntidade(
            Long empresaRefId,
            String chave
    ) {
        Empresa empresaRef = empresaRef(empresaRefId);

        return repository
                .findByEmpresaAndChaveAndEmpresaId(
                        empresaRef,
                        chave,
                        TenantContext.getEmpresaId()
                )
                .orElseThrow(() -> new EntityNotFoundException("Configuração não encontrada para a chave: " + chave));
    }

    private void validarChaveDuplicada(Empresa empresa, String chave) {
        if (repository.existsByEmpresaAndChaveAndEmpresaId(
                empresa,
                chave,
                TenantContext.getEmpresaId()
        ))
            throw new IllegalArgumentException("Já existe uma configuração cadastrada com a chave informada.");
    }

    private Empresa empresaRef(Long empresaRefId) {
        return Empresa.builder()
                .id(empresaRefId)
                .build();
    }
}
