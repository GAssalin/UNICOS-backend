package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioResponse;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioUpdateRequest;
import br.com.unicos.ms_empresa.enums.PerfilEmpresaUsuario;
import br.com.unicos.ms_empresa.mapper.EmpresaUsuarioMapper;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaUsuario;
import br.com.unicos.ms_empresa.repository.EmpresaUsuarioRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Implementação das regras de negócio relacionadas
 * ao vínculo entre usuários e empresas.
 *
 * <p>
 * Responsável por garantir:
 * <ul>
 *     <li>Isolamento multi-tenant</li>
 *     <li>Controle de permissões por perfil</li>
 *     <li>Vínculo único usuário-empresa</li>
 *     <li>Proteção contra downgrade do último ADMIN</li>
 * </ul>
 * </p>
 */
@Service
@Transactional
public class EmpresaUsuarioService extends BaseTenantService<EmpresaUsuario, Long> {

    private final EmpresaUsuarioRepository repository;
    private final EmpresaUsuarioMapper mapper;

    public EmpresaUsuarioService(
            EmpresaUsuarioRepository repository,
            EmpresaUsuarioMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "empresa-usuario-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaUsuarioResponse criar(EmpresaUsuarioCreateRequest request) {
        Empresa empresa = empresaRef(request.empresaRefId());

        validarUsuarioNaoVinculado(empresa, request.usuarioId());

        EmpresaUsuario entity = mapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(entity));
    }

    // ============================================================
    // UPDATE PERFIL
    // ============================================================

    @CircuitBreaker(name = "empresa-usuario-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaUsuarioResponse atualizarPerfil(
            Long empresaRefId,
            Long usuarioId,
            EmpresaUsuarioUpdateRequest request
    ) {
        EmpresaUsuario vinculo = buscarVinculo(empresaRefId, usuarioId);

        protegerUltimoAdmin(vinculo, request.perfil());

        mapper.updateEntity(request, vinculo);

        return mapper.toResponse(repository.save(vinculo));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-usuario-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaUsuarioResponse buscar(Long empresaRefId, Long usuarioId) {
        return mapper.toResponse(buscarVinculo(empresaRefId, usuarioId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-usuario-admin", fallbackMethod = "fallbackAdminPage")
    public Page<EmpresaUsuarioResumoResponse> listar(Long empresaRefId, Pageable pageable) {
        Empresa empresa = empresaRef(empresaRefId);

        return repository
                .findByEmpresaAndEmpresaId(
                        empresa,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-usuario-admin", fallbackMethod = "fallbackAdminPage")
    public Page<EmpresaUsuarioResumoResponse> listarPorPerfil(
            Long empresaRefId,
            PerfilEmpresaUsuario perfil,
            Pageable pageable
    ) {
        Empresa empresa = empresaRef(empresaRefId);

        return repository
                .findByEmpresaAndPerfilAndEmpresaId(
                        empresa,
                        perfil,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "empresa-usuario-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long empresaRefId, Long usuarioId) {
        EmpresaUsuario vinculo = buscarVinculo(empresaRefId, usuarioId);

        protegerUltimoAdmin(vinculo, null);

        repository.delete(vinculo);
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EmpresaUsuarioResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de usuários da empresa temporariamente indisponível");
    }

    private Page<EmpresaUsuarioResumoResponse> fallbackAdminPage(
            Long empresaRefId,
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de usuários da empresa temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long empresaRefId, Long usuarioId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de usuários da empresa temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES (SEM CB)
    // ============================================================

    private EmpresaUsuario buscarVinculo(Long empresaRefId, Long usuarioId) {
        Empresa empresa = empresaRef(empresaRefId);

        return repository.findByEmpresaAndUsuarioIdAndEmpresaId(
                        empresa,
                        usuarioId,
                        TenantContext.getEmpresaId()
                )
                .orElseThrow(() -> new EntityNotFoundException("Vínculo usuário-empresa não encontrado."));
    }

    private void validarUsuarioNaoVinculado(Empresa empresa, Long usuarioId) {
        if (repository.existsByEmpresaAndUsuarioIdAndEmpresaId(
                empresa,
                usuarioId,
                TenantContext.getEmpresaId()
        ))
            throw new IllegalArgumentException("O usuário já está vinculado a esta empresa.");
    }

    private void protegerUltimoAdmin(EmpresaUsuario vinculo, PerfilEmpresaUsuario novoPerfil) {
        if (vinculo.getPerfil() == PerfilEmpresaUsuario.ADMIN &&
                (novoPerfil != PerfilEmpresaUsuario.ADMIN)) {

            long totalAdmins = repository.findByEmpresaAndPerfilAndEmpresaId(
                    vinculo.getEmpresa(),
                    PerfilEmpresaUsuario.ADMIN,
                    TenantContext.getEmpresaId(),
                    Pageable.unpaged()
            ).getTotalElements();

            if (totalAdmins <= 1)
                throw new IllegalStateException("Não é permitido remover ou alterar o perfil do último ADMIN da empresa.");
        }
    }

    private Empresa empresaRef(Long empresaRefId) {
        return Empresa.builder()
                .id(empresaRefId)
                .build();
    }
}
