package br.com.unicos.ms_departamento.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_departamento.client.PermissaoClient;
import br.com.unicos.ms_departamento.dto.vinculo.VinculoDepartamentoFilialCreateRequestDto;
import br.com.unicos.ms_departamento.dto.vinculo.VinculoDepartamentoFilialResponseDto;
import br.com.unicos.ms_departamento.dto.vinculo.VinculoDepartamentoFilialUpdateRequestDto;
import br.com.unicos.ms_departamento.enums.StatusVinculoDepartamentoFilial;
import br.com.unicos.ms_departamento.mapper.VinculoDepartamentoFilialMapper;
import br.com.unicos.ms_departamento.model.VinculoDepartamentoFilial;
import br.com.unicos.ms_departamento.repository.VinculoDepartamentoFilialRepository;
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
 * Service responsável por regras de negócio e operações de {@link VinculoDepartamentoFilial}.
 */
@Service
@Transactional
public class VinculoDepartamentoFilialService extends BaseTenantService<VinculoDepartamentoFilial, Long> {

    private final VinculoDepartamentoFilialRepository vinculoRepository;
    private final VinculoDepartamentoFilialMapper vinculoMapper;
    private final PermissaoClient permissaoClient;

    public VinculoDepartamentoFilialService(
            VinculoDepartamentoFilialRepository vinculoRepository,
            VinculoDepartamentoFilialMapper vinculoMapper,
            PermissaoClient permissaoClient
    ) {
        super(vinculoRepository);
        this.vinculoRepository = vinculoRepository;
        this.vinculoMapper = vinculoMapper;
        this.permissaoClient = permissaoClient;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "vinculo-departamento-admin", fallbackMethod = "fallbackAdmin")
    public VinculoDepartamentoFilialResponseDto salvar(VinculoDepartamentoFilialCreateRequestDto request) {
        validarDuplicidadeParDepartamentoFilial(request.departamentoId(), request.filialId());

        VinculoDepartamentoFilial entity = vinculoMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId()); // tenant

        return vinculoMapper.toResponse(vinculoRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "vinculo-departamento-admin", fallbackMethod = "fallbackAdminIdReq")
    public VinculoDepartamentoFilialResponseDto atualizar(Long id, VinculoDepartamentoFilialUpdateRequestDto request) {
        VinculoDepartamentoFilial entity = buscarVinculo(id);

        // se mudou o par, revalidar duplicidade
        if (!entity.getDepartamentoId().equals(request.departamentoId()) || !entity.getFilialId().equals(request.filialId()))
            validarDuplicidadeParDepartamentoFilial(request.departamentoId(), request.filialId(), id);

        vinculoMapper.updateEntity(request, entity);

        return vinculoMapper.toResponse(vinculoRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "vinculo-departamento-admin", fallbackMethod = "fallbackAdminId")
    public VinculoDepartamentoFilialResponseDto buscarPorId(Long id) {
        return vinculoMapper.toResponse(buscarVinculo(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "vinculo-departamento-admin", fallbackMethod = "fallbackAdminPageFilial")
    public Page<VinculoDepartamentoFilialResponseDto> listarPorFilial(Long filialId, Pageable pageable) {
        return vinculoRepository
                .findByFilialIdAndEmpresaId(filialId, TenantContext.getEmpresaId(), pageable)
                .map(vinculoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "vinculo-departamento-admin", fallbackMethod = "fallbackAdminPageFilialStatus")
    public Page<VinculoDepartamentoFilialResponseDto> listarPorFilialEStatus(
            Long filialId,
            StatusVinculoDepartamentoFilial status,
            Pageable pageable
    ) {
        return vinculoRepository
                .findByFilialIdAndStatusVinculoDepartamentoFilialAndEmpresaId(
                        filialId,
                        status,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(vinculoMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "vinculo-departamento-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        vinculoRepository.delete(buscarVinculo(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private VinculoDepartamentoFilialResponseDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private VinculoDepartamentoFilialResponseDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private VinculoDepartamentoFilialResponseDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private Page<VinculoDepartamentoFilialResponseDto> fallbackAdminPageFilial(Long filialId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private Page<VinculoDepartamentoFilialResponseDto> fallbackAdminPageFilialStatus(Long filialId, StatusVinculoDepartamentoFilial status, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de departamentos temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private VinculoDepartamentoFilial buscarVinculo(Long id) {
        VinculoDepartamentoFilial entity = vinculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo departamento x filial não encontrado: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao vínculo fora do tenant.");

        return entity;
    }

    private void validarDuplicidadeParDepartamentoFilial(Long departamentoId, Long filialId) {
        validarDuplicidadeParDepartamentoFilial(departamentoId, filialId, null);
    }

    private void validarDuplicidadeParDepartamentoFilial(Long departamentoId, Long filialId, Long ignorarId) {
        boolean existe = vinculoRepository
                .findByDepartamentoIdAndFilialIdAndEmpresaId(departamentoId, filialId, TenantContext.getEmpresaId())
                .filter(v -> !v.getId().equals(ignorarId))
                .isPresent();

        if (existe)
            throw new IllegalArgumentException("Já existe vínculo para este departamento e filial neste tenant.");
    }
}
