package br.com.unicos.ms_filial.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_filial.dto.filial.FilialCreateRequest;
import br.com.unicos.ms_filial.dto.filial.FilialResponse;
import br.com.unicos.ms_filial.dto.filial.FilialUpdateRequest;
import br.com.unicos.ms_filial.enums.StatusFilial;
import br.com.unicos.ms_filial.mapper.FilialMapper;
import br.com.unicos.ms_filial.model.Filial;
import br.com.unicos.ms_filial.repository.FilialRepository;
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
public class FilialService extends BaseTenantService<Filial, Long> {

    private final FilialRepository filialRepository;
    private final FilialMapper filialMapper;
    private final PermissionCheckService permissionCheckService;

    public FilialService(
            FilialRepository filialRepository,
            FilialMapper filialMapper,
            PermissionCheckService permissionCheckService
    ) {
        super(filialRepository);
        this.filialRepository = filialRepository;
        this.filialMapper = filialMapper;
        this.permissionCheckService = permissionCheckService;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "filial-admin", fallbackMethod = "fallbackAdmin")
    public FilialResponse salvar(FilialCreateRequest request) {
        if (!permissionCheckService.hasPermission("FILIAL_CRIAR"))
            throw new AccessDeniedException("Usuário não possui permissão para criar filiais.");

        validarCodigoDuplicado(request.codigo());
        validarCnpjDuplicado(request.cnpj());

        Filial filial = filialMapper.toEntity(request);
        filial.setEmpresaId(TenantContext.getEmpresaId()); // tenant
        // request.empresaId() = empresa proprietária (ms-empresa) — permanece no campo empresaId (domínio)
        filial.setEmpresaId(request.empresaId()); // empresa proprietária (domínio)

        // atenção: BaseTenantEntity geralmente tem empresaId (tenant). Se for o seu caso,
        // você pode ter um conflito de nomes. Ajuste o setter conforme seu BaseTenantEntity real.

        return filialMapper.toResponse(filialRepository.save(filial));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "filial-admin", fallbackMethod = "fallbackAdmin")
    public FilialResponse atualizar(Long id, FilialUpdateRequest request) {
        if (!permissionCheckService.hasPermission("FILIAL_EDITAR"))
            throw new AccessDeniedException("Usuário não possui permissão para editar filiais.");

        Filial filial = buscarFilial(id);

        if (!filial.getCodigo().equalsIgnoreCase(request.codigo()))
            validarCodigoDuplicado(request.codigo());

        if (!filial.getCnpj().equals(request.cnpj()))
            validarCnpjDuplicado(request.cnpj());

        filialMapper.updateEntity(request, filial);

        return filialMapper.toResponse(filialRepository.save(filial));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "filial-admin", fallbackMethod = "fallbackAdminId")
    public FilialResponse buscarPorId(Long id) {
        if (!permissionCheckService.hasPermission("FILIAL_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar filiais.");
        return filialMapper.toResponse(buscarFilial(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "filial-admin", fallbackMethod = "fallbackAdminPage")
    public Page<FilialResponse> listarPorEmpresa(Long empresaId, Pageable pageable) {
        if (!permissionCheckService.hasPermission("FILIAL_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar filiais.");
        return filialRepository
                .findByEmpresaIdAndEmpresaId(empresaId, TenantContext.getEmpresaId(), pageable)
                .map(filialMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "filial-admin", fallbackMethod = "fallbackAdminPageStatus")
    public Page<FilialResponse> listarPorEmpresaEStatus(
            Long empresaId,
            StatusFilial status,
            Pageable pageable
    ) {
        if (!permissionCheckService.hasPermission("FILIAL_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar filiais.");
        return filialRepository
                .findByEmpresaIdAndStatusFilialAndEmpresaId(
                        empresaId,
                        status,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(filialMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "filial-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        if (!permissionCheckService.hasPermission("FILIAL_EXCLUIR"))
            throw new AccessDeniedException("Usuário não possui permissão para excluir filiais.");
        filialRepository.delete(buscarFilial(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private FilialResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de filiais temporariamente indisponível");
    }

    private FilialResponse fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de filiais temporariamente indisponível");
    }

    private Page<FilialResponse> fallbackAdminPage(Long empresaId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de filiais temporariamente indisponível");
    }

    private Page<FilialResponse> fallbackAdminPageStatus(Long empresaId, StatusFilial status, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de filiais temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de filiais temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Filial buscarFilial(Long id) {
        return filialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filial não encontrada: " + id));
    }

    private void validarCodigoDuplicado(String codigo) {
        if (filialRepository.existsByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe uma filial com o código informado neste tenant.");
    }

    private void validarCnpjDuplicado(String cnpj) {
        if (filialRepository.existsByCnpjAndEmpresaId(cnpj, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe uma filial com o CNPJ informado neste tenant.");
    }
}
