package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioResponse;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioUpdateRequest;
import br.com.unicos.ms_empresa.enums.PerfilEmpresaUsuario;
import br.com.unicos.ms_empresa.mapper.EmpresaUsuarioMapper;
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

@Service
@Transactional
public class EmpresaUsuarioService extends BaseTenantService<EmpresaUsuario, Long> {

    private final EmpresaUsuarioRepository repository;
    private final EmpresaUsuarioMapper mapper;

    public EmpresaUsuarioService(EmpresaUsuarioRepository repository, EmpresaUsuarioMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    public EmpresaUsuarioResponse criar(EmpresaUsuarioCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        validarUsuarioNaoVinculado(request.usuarioId(), empresaId);

        EmpresaUsuario entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);

        return mapper.toResponse(repository.save(entity));
    }

    public EmpresaUsuarioResponse atualizarPerfil(Long usuarioId, EmpresaUsuarioUpdateRequest request) {
        EmpresaUsuario vinculo = buscarVinculo(usuarioId, TenantContext.getEmpresaId());

        protegerUltimoAdmin(vinculo, request.perfil(), TenantContext.getEmpresaId());

        mapper.updateEntity(request, vinculo);

        return mapper.toResponse(repository.save(vinculo));
    }

    @Transactional(readOnly = true)
    public EmpresaUsuarioResponse buscar(Long usuarioId) {
        return mapper.toResponse(buscarVinculo(usuarioId, TenantContext.getEmpresaId()));
    }

    @Transactional(readOnly = true)
    public Page<EmpresaUsuarioResumoResponse> listar(Pageable pageable) {
        return repository.findByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    public Page<EmpresaUsuarioResumoResponse> listarPorPerfil(PerfilEmpresaUsuario perfil, Pageable pageable) {
        return repository.findByPerfilAndEmpresaId(perfil, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    public void remover(Long usuarioId) {
        EmpresaUsuario vinculo = buscarVinculo(usuarioId, TenantContext.getEmpresaId());

        protegerUltimoAdmin(vinculo, null, TenantContext.getEmpresaId());

        repository.deleteByUsuarioIdAndEmpresaId(usuarioId, TenantContext.getEmpresaId());
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private EmpresaUsuario buscarVinculo(Long usuarioId, Long empresaId) {
        return repository.findByUsuarioIdAndEmpresaId(usuarioId, empresaId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Vínculo usuário-empresa não encontrado.")
                );
    }

    private void validarUsuarioNaoVinculado(Long usuarioId, Long empresaId) {
        if (repository.existsByUsuarioIdAndEmpresaId(usuarioId, empresaId))
            throw new IllegalArgumentException("O usuário já está vinculado a esta empresa.");
    }

    private void protegerUltimoAdmin(EmpresaUsuario vinculo, PerfilEmpresaUsuario novoPerfil, Long empresaId) {
        if (vinculo.getPerfil() == PerfilEmpresaUsuario.ADMIN && novoPerfil != PerfilEmpresaUsuario.ADMIN) {
            long totalAdmins = repository.findByPerfilAndEmpresaId(
                    PerfilEmpresaUsuario.ADMIN,
                    empresaId,
                    Pageable.unpaged()
            ).getTotalElements();

            if (totalAdmins <= 1)
                throw new IllegalStateException("Não é permitido remover ou alterar o perfil do último ADMIN da empresa.");
        }
    }
}