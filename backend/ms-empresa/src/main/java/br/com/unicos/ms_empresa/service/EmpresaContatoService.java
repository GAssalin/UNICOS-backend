package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoResponse;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoUpdateRequest;
import br.com.unicos.ms_empresa.enums.TipoContatoEmpresa;
import br.com.unicos.ms_empresa.mapper.EmpresaContatoMapper;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaContato;
import br.com.unicos.ms_empresa.repository.EmpresaContatoRepository;
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
public class EmpresaContatoService extends BaseTenantService<EmpresaContato, Long> {

    private final EmpresaContatoRepository repository;
    private final EmpresaContatoMapper mapper;

    public EmpresaContatoService(
            EmpresaContatoRepository repository,
            EmpresaContatoMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "empresa-contato-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaContatoResponse criar(EmpresaContatoCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();
        Empresa empresa = empresaRef(request.empresaRefId());

        validarContatoDuplicado(empresa, request.valor(), empresaId);

        if (request.principal()) {
            removerContatoPrincipalAtual(empresa, empresaId);
        }

        EmpresaContato contato = mapper.toEntity(request);
        contato.setEmpresaId(empresaId);

        return mapper.toResponse(repository.save(contato));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "empresa-contato-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaContatoResponse atualizar(Long id, EmpresaContatoUpdateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();
        EmpresaContato contato = buscarContato(id, empresaId);

        if (!contato.getValor().equalsIgnoreCase(request.valor())) {
            validarContatoDuplicado(contato.getEmpresa(), request.valor(), empresaId);
        }

        if (request.principal()) {
            removerContatoPrincipalAtual(contato.getEmpresa(), empresaId);
            contato.setPrincipal(true);
        } else {
            contato.setPrincipal(false);
        }

        mapper.updateEntity(request, contato);

        return mapper.toResponse(repository.save(contato));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-contato-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaContatoResponse buscarPorId(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarContato(id, empresaId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-contato-admin", fallbackMethod = "fallbackAdminPage")
    public Page<EmpresaContatoResumoResponse> listar(Long empresaRefId, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();
        Empresa empresa = empresaRef(empresaRefId);

        return repository.findByEmpresaAndEmpresaId(empresa, empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-contato-admin", fallbackMethod = "fallbackAdminPageTipo")
    public Page<EmpresaContatoResumoResponse> listarPorTipo(
            Long empresaRefId,
            TipoContatoEmpresa tipo,
            Pageable pageable
    ) {
        Long empresaId = TenantContext.getEmpresaId();
        Empresa empresa = empresaRef(empresaRefId);

        return repository.findByEmpresaAndTipoContatoAndEmpresaId(
                        empresa,
                        tipo,
                        empresaId,
                        pageable
                )
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "empresa-contato-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarContato(id, empresaId));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EmpresaContatoResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de contatos da empresa temporariamente indisponível"
        );
    }

    private Page<EmpresaContatoResumoResponse> fallbackAdminPage(
            Long empresaRefId,
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de contatos da empresa temporariamente indisponível"
        );
    }

    private Page<EmpresaContatoResumoResponse> fallbackAdminPageTipo(
            Long empresaRefId,
            TipoContatoEmpresa tipo,
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de contatos da empresa temporariamente indisponível"
        );
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de contatos da empresa temporariamente indisponível"
        );
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private EmpresaContato buscarContato(Long id, Long empresaId) {
        return repository.findById(id)
                .filter(contato -> empresaId.equals(contato.getEmpresaId()))
                .orElseThrow(() -> new EntityNotFoundException("Contato institucional não encontrado: " + id));
    }

    private void validarContatoDuplicado(Empresa empresa, String valor, Long empresaId) {
        if (repository.existsByEmpresaAndValorAndEmpresaId(empresa, valor, empresaId)) {
            throw new IllegalArgumentException(
                    "Já existe um contato institucional com o valor informado para esta empresa."
            );
        }
    }

    private void removerContatoPrincipalAtual(Empresa empresa, Long empresaId) {
        repository.findByEmpresaAndPrincipalTrueAndEmpresaId(empresa, empresaId)
                .ifPresent(contato -> {
                    contato.setPrincipal(false);
                    repository.save(contato);
                });
    }

    private Empresa empresaRef(Long empresaRefId) {
        return Empresa.builder()
                .id(empresaRefId)
                .build();
    }
}