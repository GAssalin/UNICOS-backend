package br.com.unicos.ms_estoque.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_estoque.client.PermissaoClient;
import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueCreateRequestDto;
import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueResponseDto;
import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueUpdateRequestDto;
import br.com.unicos.ms_estoque.enums.StatusResponsavelEstoque;
import br.com.unicos.ms_estoque.mapper.ResponsavelEstoqueMapper;
import br.com.unicos.ms_estoque.model.ResponsavelEstoque;
import br.com.unicos.ms_estoque.repository.ResponsavelEstoqueRepository;
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
 * Service responsável por regras de negócio e operações de {@link ResponsavelEstoque}.
 */
@Service
@Transactional
public class ResponsavelEstoqueService extends BaseTenantService<ResponsavelEstoque, Long> {

    private final ResponsavelEstoqueRepository responsavelRepository;
    private final ResponsavelEstoqueMapper responsavelMapper;

    public ResponsavelEstoqueService(
            ResponsavelEstoqueRepository responsavelRepository,
            ResponsavelEstoqueMapper responsavelMapper,
            PermissaoClient permissaoClient
    ) {
        super(responsavelRepository);
        this.responsavelRepository = responsavelRepository;
        this.responsavelMapper = responsavelMapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "responsavel-estoque-admin", fallbackMethod = "fallbackAdmin")
    public ResponsavelEstoqueResponseDto salvar(ResponsavelEstoqueCreateRequestDto request) {
        validarPrincipalUnicoAtivo(request.estoqueId(), request.principal(), request.statusResponsavelEstoque());

        ResponsavelEstoque entity = responsavelMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId()); // tenant

        return responsavelMapper.toResponse(responsavelRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "responsavel-estoque-admin", fallbackMethod = "fallbackAdminIdReq")
    public ResponsavelEstoqueResponseDto atualizar(Long id, ResponsavelEstoqueUpdateRequestDto request) {
        ResponsavelEstoque entity = buscarResponsavel(id);

        // regra: se estiver marcando como principal+ativo, garantir unicidade
        validarPrincipalUnicoAtivo(request.estoqueId(), request.principal(), request.statusResponsavelEstoque(), id);

        responsavelMapper.updateEntity(request, entity);

        return responsavelMapper.toResponse(responsavelRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "responsavel-estoque-admin", fallbackMethod = "fallbackAdminId")
    public ResponsavelEstoqueResponseDto buscarPorId(Long id) {
        return responsavelMapper.toResponse(buscarResponsavel(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "responsavel-estoque-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ResponsavelEstoqueResponseDto> listarPorEstoque(Long estoqueId, Pageable pageable) {
        return responsavelRepository
                .findByEstoqueIdAndEmpresaId(estoqueId, TenantContext.getEmpresaId(), pageable)
                .map(responsavelMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "responsavel-estoque-admin", fallbackMethod = "fallbackAdminPageStatus")
    public Page<ResponsavelEstoqueResponseDto> listarPorEstoqueEStatus(
            Long estoqueId,
            StatusResponsavelEstoque status,
            Pageable pageable
    ) {
        return responsavelRepository
                .findByEstoqueIdAndStatusResponsavelEstoqueAndEmpresaId(
                        estoqueId,
                        status,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(responsavelMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "responsavel-estoque-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        responsavelRepository.delete(buscarResponsavel(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private ResponsavelEstoqueResponseDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private ResponsavelEstoqueResponseDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private ResponsavelEstoqueResponseDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private Page<ResponsavelEstoqueResponseDto> fallbackAdminPage(Long estoqueId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private Page<ResponsavelEstoqueResponseDto> fallbackAdminPageStatus(Long estoqueId, StatusResponsavelEstoque status, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ResponsavelEstoque buscarResponsavel(Long id) {
        ResponsavelEstoque entity = responsavelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Responsável do estoque não encontrado: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao responsável fora do tenant.");

        return entity;
    }

    private void validarPrincipalUnicoAtivo(
            Long estoqueId,
            Boolean principal,
            StatusResponsavelEstoque status
    ) {
        validarPrincipalUnicoAtivo(estoqueId, principal, status, null);
    }

    private void validarPrincipalUnicoAtivo(
            Long estoqueId,
            Boolean principal,
            StatusResponsavelEstoque status,
            Long ignorarId
    ) {
        if (!Boolean.TRUE.equals(principal)) return;
        if (status != StatusResponsavelEstoque.ATIVO) return;

        boolean existeOutroPrincipalAtivo = responsavelRepository
                .findByEstoqueIdAndPrincipalAndEmpresaId(estoqueId, true, TenantContext.getEmpresaId())
                .filter(r -> r.getStatusResponsavelEstoque() == StatusResponsavelEstoque.ATIVO)
                .filter(r -> !r.getId().equals(ignorarId))
                .isPresent();

        if (existeOutroPrincipalAtivo)
            throw new IllegalArgumentException("Já existe um responsável principal ativo para este estoque neste tenant.");
    }

}
