package br.com.unicos.ms_departamento.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_departamento.client.PermissaoClient;
import br.com.unicos.ms_departamento.dto.responsavel.ResponsavelDepartamentoCreateRequestDto;
import br.com.unicos.ms_departamento.dto.responsavel.ResponsavelDepartamentoResponseDto;
import br.com.unicos.ms_departamento.dto.responsavel.ResponsavelDepartamentoUpdateRequestDto;
import br.com.unicos.ms_departamento.enums.StatusResponsavelDepartamento;
import br.com.unicos.ms_departamento.mapper.ResponsavelDepartamentoMapper;
import br.com.unicos.ms_departamento.model.ResponsavelDepartamento;
import br.com.unicos.ms_departamento.repository.ResponsavelDepartamentoRepository;
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
 * Service responsável por regras de negócio e operações de {@link ResponsavelDepartamento}.
 */
@Service
@Transactional
public class ResponsavelDepartamentoService extends BaseTenantService<ResponsavelDepartamento, Long> {

    private final ResponsavelDepartamentoRepository responsavelRepository;
    private final ResponsavelDepartamentoMapper responsavelMapper;

    public ResponsavelDepartamentoService(
            ResponsavelDepartamentoRepository responsavelRepository,
            ResponsavelDepartamentoMapper responsavelMapper,
            PermissaoClient permissaoClient
    ) {
        super(responsavelRepository);
        this.responsavelRepository = responsavelRepository;
        this.responsavelMapper = responsavelMapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "responsavel-departamento-admin", fallbackMethod = "fallbackAdmin")
    public ResponsavelDepartamentoResponseDto salvar(ResponsavelDepartamentoCreateRequestDto request) {
        validarPrincipalUnicoAtivo(request.departamentoId(), request.principal(), request.statusResponsavelDepartamento());

        ResponsavelDepartamento entity = responsavelMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId()); // tenant

        return responsavelMapper.toResponse(responsavelRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "responsavel-departamento-admin", fallbackMethod = "fallbackAdminIdReq")
    public ResponsavelDepartamentoResponseDto atualizar(Long id, ResponsavelDepartamentoUpdateRequestDto request) {
        ResponsavelDepartamento entity = buscarResponsavel(id);

        // regra: se estiver marcando como principal+ativo, garantir unicidade
        validarPrincipalUnicoAtivo(request.departamentoId(), request.principal(), request.statusResponsavelDepartamento(), id);

        responsavelMapper.updateEntity(request, entity);

        return responsavelMapper.toResponse(responsavelRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "responsavel-departamento-admin", fallbackMethod = "fallbackAdminId")
    public ResponsavelDepartamentoResponseDto buscarPorId(Long id) {
        return responsavelMapper.toResponse(buscarResponsavel(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "responsavel-departamento-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ResponsavelDepartamentoResponseDto> listarPorDepartamento(Long departamentoId, Pageable pageable) {
        return responsavelRepository
                .findByDepartamentoIdAndEmpresaId(departamentoId, TenantContext.getEmpresaId(), pageable)
                .map(responsavelMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "responsavel-departamento-admin", fallbackMethod = "fallbackAdminPageStatus")
    public Page<ResponsavelDepartamentoResponseDto> listarPorDepartamentoEStatus(
            Long departamentoId,
            StatusResponsavelDepartamento status,
            Pageable pageable
    ) {
        return responsavelRepository
                .findByDepartamentoIdAndStatusResponsavelDepartamentoAndEmpresaId(
                        departamentoId,
                        status,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(responsavelMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "responsavel-departamento-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        responsavelRepository.delete(buscarResponsavel(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private ResponsavelDepartamentoResponseDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private ResponsavelDepartamentoResponseDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private ResponsavelDepartamentoResponseDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private Page<ResponsavelDepartamentoResponseDto> fallbackAdminPage(Long departamentoId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private Page<ResponsavelDepartamentoResponseDto> fallbackAdminPageStatus(Long departamentoId, StatusResponsavelDepartamento status, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ResponsavelDepartamento buscarResponsavel(Long id) {
        ResponsavelDepartamento entity = responsavelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Responsável do departamento não encontrado: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao responsável fora do tenant.");

        return entity;
    }

    private void validarPrincipalUnicoAtivo(
            Long departamentoId,
            Boolean principal,
            StatusResponsavelDepartamento status
    ) {
        validarPrincipalUnicoAtivo(departamentoId, principal, status, null);
    }

    private void validarPrincipalUnicoAtivo(
            Long departamentoId,
            Boolean principal,
            StatusResponsavelDepartamento status,
            Long ignorarId
    ) {
        if (!Boolean.TRUE.equals(principal)) return;
        if (status != StatusResponsavelDepartamento.ATIVO) return;

        boolean existeOutroPrincipalAtivo = responsavelRepository
                .findByDepartamentoIdAndPrincipalAndEmpresaId(departamentoId, true, TenantContext.getEmpresaId())
                .filter(r -> r.getStatusResponsavelDepartamento() == StatusResponsavelDepartamento.ATIVO)
                .filter(r -> !r.getId().equals(ignorarId))
                .isPresent();

        if (existeOutroPrincipalAtivo)
            throw new IllegalArgumentException("Já existe um responsável principal ativo para este departamento neste tenant.");
    }

}
