package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoResponse;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoUpdateRequest;
import br.com.unicos.ms_empresa.enums.TipoContatoEmpresa;
import br.com.unicos.ms_empresa.mapper.EmpresaContatoMapper;
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

    @CircuitBreaker(name = "empresa-contato-admin", fallbackMethod = "fallbackCriar")
    public EmpresaContatoResponse criar(EmpresaContatoCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        validarContatoDuplicado(request.valor(), empresaId);

        if (request.principal()) {
            removerContatoPrincipalAtual(empresaId);
        }

        EmpresaContato contato = mapper.toEntity(request);
        contato.setEmpresaId(empresaId);

        return mapper.toResponse(repository.save(contato));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "empresa-contato-admin", fallbackMethod = "fallbackAtualizar")
    public EmpresaContatoResponse atualizar(Long id, EmpresaContatoUpdateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();
        EmpresaContato contato = buscarContato(id, empresaId);

        if (!contato.getValor().equalsIgnoreCase(request.valor())) {
            validarContatoDuplicado(request.valor(), empresaId);
        }

        if (request.principal()) {
            removerContatoPrincipalAtual(empresaId, id);
        }

        mapper.updateEntity(request, contato);

        return mapper.toResponse(repository.save(contato));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-contato-admin", fallbackMethod = "fallbackBuscarPorId")
    public EmpresaContatoResponse buscarPorId(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarContato(id, empresaId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-contato-admin", fallbackMethod = "fallbackListar")
    public Page<EmpresaContatoResumoResponse> listar(Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-contato-admin", fallbackMethod = "fallbackListarPorTipo")
    public Page<EmpresaContatoResumoResponse> listarPorTipo(
            TipoContatoEmpresa tipo,
            Pageable pageable
    ) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByTipoContatoAndEmpresaId(tipo, empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "empresa-contato-admin", fallbackMethod = "fallbackRemover")
    public void remover(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarContato(id, empresaId));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EmpresaContatoResponse fallbackCriar(EmpresaContatoCreateRequest request, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de contatos da empresa temporariamente indisponível"
        );
    }

    private EmpresaContatoResponse fallbackAtualizar(
            Long id,
            EmpresaContatoUpdateRequest request,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de contatos da empresa temporariamente indisponível"
        );
    }

    private EmpresaContatoResponse fallbackBuscarPorId(Long id, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de contatos da empresa temporariamente indisponível"
        );
    }

    private Page<EmpresaContatoResumoResponse> fallbackListar(
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de contatos da empresa temporariamente indisponível"
        );
    }

    private Page<EmpresaContatoResumoResponse> fallbackListarPorTipo(
            TipoContatoEmpresa tipo,
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de contatos da empresa temporariamente indisponível"
        );
    }

    private void fallbackRemover(Long id, Throwable ex) {
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
                .orElseThrow(() ->
                        new EntityNotFoundException("Contato institucional não encontrado: " + id));
    }

    private void validarContatoDuplicado(String valor, Long empresaId) {
        if (repository.existsByValorAndEmpresaId(valor, empresaId)) {
            throw new IllegalArgumentException(
                    "Já existe um contato institucional com o valor informado para esta empresa."
            );
        }
    }

    private void removerContatoPrincipalAtual(Long empresaId) {
        repository.findByPrincipalTrueAndEmpresaId(empresaId)
                .ifPresent(contato -> {
                    contato.setPrincipal(false);
                    repository.save(contato);
                });
    }

    private void removerContatoPrincipalAtual(Long empresaId, Long contatoAtualId) {
        repository.findByPrincipalTrueAndEmpresaId(empresaId)
                .filter(contato -> !contato.getId().equals(contatoAtualId))
                .ifPresent(contato -> {
                    contato.setPrincipal(false);
                    repository.save(contato);
                });
    }
}