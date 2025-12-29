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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PermissionCheckService permissionCheckService;

    public EmpresaUsuarioService(
            EmpresaUsuarioRepository repository,
            EmpresaUsuarioMapper mapper,
            PermissionCheckService permissionCheckService
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
        this.permissionCheckService = permissionCheckService;
    }

    // ============================================================
    // CRUD
    // ============================================================

    /**
     * Vincula um usuário a uma empresa.
     */
    public EmpresaUsuarioResponse criar(EmpresaUsuarioCreateRequest request) {
        if (!permissionCheckService.hasPermission("EMPRESA_USUARIO_CRIAR"))
            throw new AccessDeniedException("Usuário não possui permissão para vincular usuários à empresa.");

        Empresa empresa = empresaRef(request.empresaRefId());

        validarUsuarioNaoVinculado(empresa, request.usuarioId());

        EmpresaUsuario entity = mapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(entity));
    }

    /**
     * Atualiza o perfil de um usuário dentro da empresa.
     */
    public EmpresaUsuarioResponse atualizarPerfil(Long empresaRefId, Long usuarioId, EmpresaUsuarioUpdateRequest request) {
        if (!permissionCheckService.hasPermission("EMPRESA_USUARIO_EDITAR"))
            throw new AccessDeniedException("Usuário não possui permissão para alterar perfis.");

        EmpresaUsuario vinculo = buscarVinculo(empresaRefId, usuarioId);

        protegerUltimoAdmin(vinculo, request.perfil());

        mapper.updateEntity(request, vinculo);

        return mapper.toResponse(repository.save(vinculo));
    }

    /**
     * Busca o vínculo de um usuário com a empresa.
     */
    @Transactional(readOnly = true)
    public EmpresaUsuarioResponse buscar(Long empresaRefId, Long usuarioId) {
        if (!permissionCheckService.hasPermission("EMPRESA_USUARIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar vínculos.");

        return mapper.toResponse(buscarVinculo(empresaRefId, usuarioId));
    }

    // ============================================================
    // LISTAGENS
    // ============================================================

    /**
     * Lista os usuários vinculados à empresa.
     */
    @Transactional(readOnly = true)
    public Page<EmpresaUsuarioResumoResponse> listar(Long empresaRefId, Pageable pageable) {
        if (!permissionCheckService.hasPermission("EMPRESA_USUARIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar usuários.");

        Empresa empresa = empresaRef(empresaRefId);

        return repository
                .findByEmpresaAndEmpresaId(
                        empresa,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(mapper::toResumoResponse);
    }

    /**
     * Lista os usuários vinculados à empresa filtrando por perfil.
     */
    @Transactional(readOnly = true)
    public Page<EmpresaUsuarioResumoResponse> listarPorPerfil(Long empresaRefId, PerfilEmpresaUsuario perfil, Pageable pageable) {
        if (!permissionCheckService.hasPermission("EMPRESA_USUARIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar usuários.");

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
    // EXCLUSÃO
    // ============================================================

    /**
     * Remove o vínculo de um usuário com a empresa.
     */
    public void remover(Long empresaRefId, Long usuarioId) {
        if (!permissionCheckService.hasPermission("EMPRESA_USUARIO_EXCLUIR"))
            throw new AccessDeniedException("Usuário não possui permissão para remover usuários da empresa.");

        EmpresaUsuario vinculo = buscarVinculo(empresaRefId, usuarioId);

        protegerUltimoAdmin(vinculo, null);

        repository.delete(vinculo);
    }

    // ============================================================
    // MÉTODOS AUXILIARES
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
        if (repository.existsByEmpresaAndUsuarioIdAndEmpresaId(empresa, usuarioId, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("O usuário já está vinculado a esta empresa.");
    }

    /**
     * Impede a remoção ou downgrade do último ADMIN da empresa.
     */
    private void protegerUltimoAdmin(EmpresaUsuario vinculo, PerfilEmpresaUsuario novoPerfil) {
        if (vinculo.getPerfil() == PerfilEmpresaUsuario.ADMIN && (novoPerfil == null || novoPerfil != PerfilEmpresaUsuario.ADMIN)) {
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
