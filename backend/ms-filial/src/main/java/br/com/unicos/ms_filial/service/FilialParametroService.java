package br.com.unicos.ms_filial.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_filial.dto.parametro.FilialParametroCreateRequest;
import br.com.unicos.ms_filial.dto.parametro.FilialParametroResponse;
import br.com.unicos.ms_filial.dto.parametro.FilialParametroUpdateRequest;
import br.com.unicos.ms_filial.mapper.FilialParametroMapper;
import br.com.unicos.ms_filial.model.FilialParametro;
import br.com.unicos.ms_filial.repository.FilialParametroRepository;
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
public class FilialParametroService extends BaseTenantService<FilialParametro, Long> {

    private final FilialParametroRepository parametroRepository;
    private final FilialParametroMapper parametroMapper;
    private final PermissionCheckService permissionCheckService;

    public FilialParametroService(
            FilialParametroRepository parametroRepository,
            FilialParametroMapper parametroMapper,
            PermissionCheckService permissionCheckService
    ) {
        super(parametroRepository);
        this.parametroRepository = parametroRepository;
        this.parametroMapper = parametroMapper;
        this.permissionCheckService = permissionCheckService;
    }

    @CircuitBreaker(name = "filial-parametro-admin", fallbackMethod = "fallbackAdmin")
    public FilialParametroResponse salvar(FilialParametroCreateRequest request) {
        if (!permissionCheckService.hasPermission("FILIAL_PARAMETRO_CRIAR"))
            throw new AccessDeniedException("Usuário não possui permissão para criar parâmetros de filial.");

        validarChaveDuplicada(request.filialId(), request.chave());

        FilialParametro entity = parametroMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return parametroMapper.toResponse(parametroRepository.save(entity));
    }

    @CircuitBreaker(name = "filial-parametro-admin", fallbackMethod = "fallbackAdmin")
    public FilialParametroResponse atualizar(Long id, FilialParametroUpdateRequest request) {
        if (!permissionCheckService.hasPermission("FILIAL_PARAMETRO_EDITAR"))
            throw new AccessDeniedException("Usuário não possui permissão para editar parâmetros de filial.");

        FilialParametro entity = buscarParametro(id);

        if (!entity.getChave().equalsIgnoreCase(request.chave()))
            validarChaveDuplicada(request.filialId(), request.chave());

        parametroMapper.updateEntity(request, entity);

        return parametroMapper.toResponse(parametroRepository.save(entity));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "filial-parametro-admin", fallbackMethod = "fallbackAdminId")
    public FilialParametroResponse buscarPorId(Long id) {
        if (!permissionCheckService.hasPermission("FILIAL_PARAMETRO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar parâmetros de filial.");
        return parametroMapper.toResponse(buscarParametro(id));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "filial-parametro-admin", fallbackMethod = "fallbackAdminPage")
    public Page<FilialParametroResponse> listarPorFilial(Long filialId, Pageable pageable) {
        if (!permissionCheckService.hasPermission("FILIAL_PARAMETRO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar parâmetros de filial.");
        return parametroRepository
                .findByFilialIdAndEmpresaId(filialId, TenantContext.getEmpresaId(), pageable)
                .map(parametroMapper::toResponse);
    }

    @CircuitBreaker(name = "filial-parametro-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        if (!permissionCheckService.hasPermission("FILIAL_PARAMETRO_EXCLUIR"))
            throw new AccessDeniedException("Usuário não possui permissão para excluir parâmetros de filial.");
        parametroRepository.delete(buscarParametro(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private FilialParametroResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de parâmetros de filial temporariamente indisponível");
    }

    private FilialParametroResponse fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de parâmetros de filial temporariamente indisponível");
    }

    private Page<FilialParametroResponse> fallbackAdminPage(Long filialId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de parâmetros de filial temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de parâmetros de filial temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private FilialParametro buscarParametro(Long id) {
        return parametroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parâmetro de filial não encontrado: " + id));
    }

    private void validarChaveDuplicada(Long filialId, String chave) {
        if (parametroRepository.existsByFilialIdAndChaveAndEmpresaId(
                filialId,
                chave,
                TenantContext.getEmpresaId()
        )) {
            throw new IllegalArgumentException("Já existe parâmetro com a chave informada para esta filial.");
        }
    }
}
