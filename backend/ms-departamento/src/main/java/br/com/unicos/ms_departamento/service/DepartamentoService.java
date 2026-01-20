package br.com.unicos.ms_departamento.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_departamento.client.PermissaoClient;
import br.com.unicos.ms_departamento.dto.departamento.DepartamentoCreateRequestDto;
import br.com.unicos.ms_departamento.dto.departamento.DepartamentoResponseDto;
import br.com.unicos.ms_departamento.dto.departamento.DepartamentoUpdateRequestDto;
import br.com.unicos.ms_departamento.enums.StatusDepartamento;
import br.com.unicos.ms_departamento.mapper.DepartamentoMapper;
import br.com.unicos.ms_departamento.model.Departamento;
import br.com.unicos.ms_departamento.repository.DepartamentoRepository;
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
 * Service responsável por regras de negócio e operações do agregado {@link Departamento}.
 */
@Service
@Transactional
public class DepartamentoService extends BaseTenantService<Departamento, Long> {

    private final DepartamentoRepository departamentoRepository;
    private final DepartamentoMapper departamentoMapper;
    private final PermissaoClient permissaoClient;

    public DepartamentoService(
            DepartamentoRepository departamentoRepository,
            DepartamentoMapper departamentoMapper,
            PermissaoClient permissaoClient
    ) {
        super(departamentoRepository);
        this.departamentoRepository = departamentoRepository;
        this.departamentoMapper = departamentoMapper;
        this.permissaoClient = permissaoClient;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "departamento-admin", fallbackMethod = "fallbackAdmin")
    public DepartamentoResponseDto salvar(DepartamentoCreateRequestDto request) {
        if (!permissaoClient.usuarioPossuiPermissao("DEPARTAMENTO_CRIAR"))
            throw new AccessDeniedException("Usuário não possui permissão para criar departamentos.");

        validarCodigoDuplicado(request.codigo());

        if (request.departamentoPaiId() != null)
            // garante que o pai exista no tenant
            buscarDepartamento(request.departamentoPaiId());

        Departamento entity = departamentoMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId()); // tenant

        return departamentoMapper.toResponse(departamentoRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "departamento-admin", fallbackMethod = "fallbackAdminIdReq")
    public DepartamentoResponseDto atualizar(Long id, DepartamentoUpdateRequestDto request) {
        if (!permissaoClient.usuarioPossuiPermissao("DEPARTAMENTO_EDITAR"))
            throw new AccessDeniedException("Usuário não possui permissão para editar departamentos.");

        Departamento entity = buscarDepartamento(id);

        if (!entity.getCodigo().equalsIgnoreCase(request.codigo()))
            validarCodigoDuplicado(request.codigo());

        if (request.departamentoPaiId() != null) {
            if (id.equals(request.departamentoPaiId()))
                throw new IllegalArgumentException("Um departamento não pode ser pai de si mesmo.");

            buscarDepartamento(request.departamentoPaiId());
        }

        departamentoMapper.updateEntity(request, entity);

        return departamentoMapper.toResponse(departamentoRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "departamento-admin", fallbackMethod = "fallbackAdminId")
    public DepartamentoResponseDto buscarPorId(Long id) {
        if (!permissaoClient.usuarioPossuiPermissao("DEPARTAMENTO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar departamentos.");
        return departamentoMapper.toResponse(buscarDepartamento(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "departamento-admin", fallbackMethod = "fallbackAdminPage")
    public Page<DepartamentoResponseDto> listar(Pageable pageable) {
        if (!permissaoClient.usuarioPossuiPermissao("DEPARTAMENTO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar departamentos.");
        return departamentoRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(departamentoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "departamento-admin", fallbackMethod = "fallbackAdminPageStatus")
    public Page<DepartamentoResponseDto> listarPorStatus(StatusDepartamento status, Pageable pageable) {
        if (!permissaoClient.usuarioPossuiPermissao("DEPARTAMENTO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar departamentos.");
        return departamentoRepository
                .findByStatusDepartamentoAndEmpresaId(status, TenantContext.getEmpresaId(), pageable)
                .map(departamentoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "departamento-admin", fallbackMethod = "fallbackAdminPagePai")
    public Page<DepartamentoResponseDto> listarFilhos(Long departamentoPaiId, Pageable pageable) {
        if (!permissaoClient.usuarioPossuiPermissao("DEPARTAMENTO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar departamentos.");

        // garante que o pai exista no tenant
        buscarDepartamento(departamentoPaiId);

        return departamentoRepository
                .findByDepartamentoPaiIdAndEmpresaId(departamentoPaiId, TenantContext.getEmpresaId(), pageable)
                .map(departamentoMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "departamento-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        if (!permissaoClient.usuarioPossuiPermissao("DEPARTAMENTO_EXCLUIR"))
            throw new AccessDeniedException("Usuário não possui permissão para excluir departamentos.");
        departamentoRepository.delete(buscarDepartamento(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private DepartamentoResponseDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private DepartamentoResponseDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private DepartamentoResponseDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private Page<DepartamentoResponseDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private Page<DepartamentoResponseDto> fallbackAdminPageStatus(StatusDepartamento status, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private Page<DepartamentoResponseDto> fallbackAdminPagePai(Long departamentoPaiId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Departamento buscarDepartamento(Long id) {
        Departamento entity = departamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado: " + id));

        // proteção adicional: garante tenant correto (caso findById não esteja tenant-aware)
        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao departamento fora do tenant.");

        return entity;
    }

    private void validarCodigoDuplicado(String codigo) {
        if (departamentoRepository.existsByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe um departamento com o código informado neste tenant.");
    }
}
