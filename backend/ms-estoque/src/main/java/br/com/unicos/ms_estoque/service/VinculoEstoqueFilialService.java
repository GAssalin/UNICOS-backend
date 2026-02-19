package br.com.unicos.ms_estoque.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_estoque.client.PermissaoClient;
import br.com.unicos.ms_estoque.dto.vinculo.VinculoEstoqueFilialCreateRequestDto;
import br.com.unicos.ms_estoque.dto.vinculo.VinculoEstoqueFilialResponseDto;
import br.com.unicos.ms_estoque.dto.vinculo.VinculoEstoqueFilialUpdateRequestDto;
import br.com.unicos.ms_estoque.enums.StatusVinculoEstoqueFilial;
import br.com.unicos.ms_estoque.mapper.VinculoEstoqueFilialMapper;
import br.com.unicos.ms_estoque.model.VinculoEstoqueFilial;
import br.com.unicos.ms_estoque.repository.VinculoEstoqueFilialRepository;
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
 * Service responsável por regras de negócio e operações de {@link VinculoEstoqueFilial}.
 */
@Service
@Transactional
public class VinculoEstoqueFilialService extends BaseTenantService<VinculoEstoqueFilial, Long> {

    private final VinculoEstoqueFilialRepository vinculoRepository;
    private final VinculoEstoqueFilialMapper vinculoMapper;
    private final PermissaoClient permissaoClient;

    public VinculoEstoqueFilialService(
            VinculoEstoqueFilialRepository vinculoRepository,
            VinculoEstoqueFilialMapper vinculoMapper,
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

    @CircuitBreaker(name = "vinculo-estoque-admin", fallbackMethod = "fallbackAdmin")
    public VinculoEstoqueFilialResponseDto salvar(VinculoEstoqueFilialCreateRequestDto request) {
        validarDuplicidadeParEstoqueFilial(request.estoqueId(), request.filialId());

        VinculoEstoqueFilial entity = vinculoMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId()); // tenant

        return vinculoMapper.toResponse(vinculoRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "vinculo-estoque-admin", fallbackMethod = "fallbackAdminIdReq")
    public VinculoEstoqueFilialResponseDto atualizar(Long id, VinculoEstoqueFilialUpdateRequestDto request) {
        VinculoEstoqueFilial entity = buscarVinculo(id);

        // se mudou o par, revalidar duplicidade
        if (!entity.getEstoqueId().equals(request.estoqueId()) || !entity.getFilialId().equals(request.filialId()))
            validarDuplicidadeParEstoqueFilial(request.estoqueId(), request.filialId(), id);

        vinculoMapper.updateEntity(request, entity);

        return vinculoMapper.toResponse(vinculoRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "vinculo-estoque-admin", fallbackMethod = "fallbackAdminId")
    public VinculoEstoqueFilialResponseDto buscarPorId(Long id) {
        return vinculoMapper.toResponse(buscarVinculo(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "vinculo-estoque-admin", fallbackMethod = "fallbackAdminPageFilial")
    public Page<VinculoEstoqueFilialResponseDto> listarPorFilial(Long filialId, Pageable pageable) {
        return vinculoRepository
                .findByFilialIdAndEmpresaId(filialId, TenantContext.getEmpresaId(), pageable)
                .map(vinculoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "vinculo-estoque-admin", fallbackMethod = "fallbackAdminPageFilialStatus")
    public Page<VinculoEstoqueFilialResponseDto> listarPorFilialEStatus(
            Long filialId,
            StatusVinculoEstoqueFilial status,
            Pageable pageable
    ) {
        return vinculoRepository
                .findByFilialIdAndStatusVinculoEstoqueFilialAndEmpresaId(
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

    @CircuitBreaker(name = "vinculo-estoque-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        vinculoRepository.delete(buscarVinculo(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private VinculoEstoqueFilialResponseDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private VinculoEstoqueFilialResponseDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private VinculoEstoqueFilialResponseDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private Page<VinculoEstoqueFilialResponseDto> fallbackAdminPageFilial(Long filialId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private Page<VinculoEstoqueFilialResponseDto> fallbackAdminPageFilialStatus(Long filialId, StatusVinculoEstoqueFilial status, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private VinculoEstoqueFilial buscarVinculo(Long id) {
        VinculoEstoqueFilial entity = vinculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo estoque x filial não encontrado: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao vínculo fora do tenant.");

        return entity;
    }

    private void validarDuplicidadeParEstoqueFilial(Long estoqueId, Long filialId) {
        validarDuplicidadeParEstoqueFilial(estoqueId, filialId, null);
    }

    private void validarDuplicidadeParEstoqueFilial(Long estoqueId, Long filialId, Long ignorarId) {
        boolean existe = vinculoRepository
                .findByEstoqueIdAndFilialIdAndEmpresaId(estoqueId, filialId, TenantContext.getEmpresaId())
                .filter(v -> !v.getId().equals(ignorarId))
                .isPresent();

        if (existe)
            throw new IllegalArgumentException("Já existe vínculo para este estoque e filial neste tenant.");
    }
}
