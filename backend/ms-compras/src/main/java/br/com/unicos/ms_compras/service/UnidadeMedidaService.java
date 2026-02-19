package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.unidademedida.UnidadeMedidaCreateRequest;
import br.com.unicos.ms_compras.dto.unidademedida.UnidadeMedidaResponse;
import br.com.unicos.ms_compras.dto.unidademedida.UnidadeMedidaResumoResponse;
import br.com.unicos.ms_compras.dto.unidademedida.UnidadeMedidaUpdateRequest;
import br.com.unicos.ms_compras.mapper.UnidadeMedidaMapper;
import br.com.unicos.ms_compras.model.UnidadeMedida;
import br.com.unicos.ms_compras.repository.UnidadeMedidaRepository;
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
public class UnidadeMedidaService extends BaseTenantService<UnidadeMedida, Long> {

    private final UnidadeMedidaRepository repository;
    private final UnidadeMedidaMapper mapper;

    public UnidadeMedidaService(
            UnidadeMedidaRepository repository,
            UnidadeMedidaMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-unidade-medida-admin", fallbackMethod = "fallbackAdmin")
    public UnidadeMedidaResponse criar(UnidadeMedidaCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        validarCodigoDuplicado(request.codigo(), empresaId);

        UnidadeMedida unidade = mapper.toEntity(request, empresaId);
        unidade.setAtivo(true);

        return mapper.toResponse(repository.save(unidade));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-unidade-medida-admin", fallbackMethod = "fallbackAdmin")
    public UnidadeMedidaResponse atualizar(Long id, UnidadeMedidaUpdateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        UnidadeMedida unidade = buscarUnidade(id, empresaId);

        if (!unidade.getCodigo().equalsIgnoreCase(request.codigo()))
            validarCodigoDuplicado(request.codigo(), empresaId);

        mapper.updateEntity(request, unidade);

        return mapper.toResponse(repository.save(unidade));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-unidade-medida-admin", fallbackMethod = "fallbackAdmin")
    public UnidadeMedidaResponse buscarPorId(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarUnidade(id, empresaId));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-unidade-medida-admin", fallbackMethod = "fallbackAdmin")
    public UnidadeMedidaResponse buscarPorCodigo(String codigo) {
        Long empresaId = TenantContext.getEmpresaId();

        UnidadeMedida unidade = repository.findByCodigoAndEmpresaId(codigo, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Unidade de medida não encontrada para o código: " + codigo));

        return mapper.toResponse(unidade);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-unidade-medida-admin", fallbackMethod = "fallbackAdminPage")
    public Page<UnidadeMedidaResumoResponse> listar(Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findAllByEmpresaId(empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-unidade-medida-admin", fallbackMethod = "fallbackAdminPage")
    public Page<UnidadeMedidaResumoResponse> listarPorAtivo(Boolean ativo, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByAtivoAndEmpresaId(ativo, empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // STATUS
    // ============================================================

    @CircuitBreaker(name = "compras-unidade-medida-admin", fallbackMethod = "fallbackAdmin")
    public UnidadeMedidaResponse ativar(Long id) {
        Long empresaId = TenantContext.getEmpresaId();

        UnidadeMedida unidade = buscarUnidade(id, empresaId);
        unidade.setAtivo(true);

        return mapper.toResponse(repository.save(unidade));
    }

    @CircuitBreaker(name = "compras-unidade-medida-admin", fallbackMethod = "fallbackAdmin")
    public UnidadeMedidaResponse inativar(Long id) {
        Long empresaId = TenantContext.getEmpresaId();

        UnidadeMedida unidade = buscarUnidade(id, empresaId);
        unidade.setAtivo(false);

        return mapper.toResponse(repository.save(unidade));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-unidade-medida-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarUnidade(id, empresaId));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private UnidadeMedidaResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de unidades de medida temporariamente indisponível");
    }

    private Page<UnidadeMedidaResumoResponse> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de unidades de medida temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de unidades de medida temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private UnidadeMedida buscarUnidade(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Unidade de medida não encontrada: " + id));
    }

    private void validarCodigoDuplicado(String codigo, Long empresaId) {
        if (repository.existsByCodigoAndEmpresaId(codigo, empresaId))
            throw new IllegalArgumentException("Já existe uma unidade de medida com o código informado.");
    }
}
