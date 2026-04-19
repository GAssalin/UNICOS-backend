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

    private static final String CIRCUIT_BREAKER_NAME = "empresa-usuario-admin";
    private static final String MSG_SERVICO_INDISPONIVEL =
            "Serviço de usuários da empresa temporariamente indisponível";

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

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackCriar")
    public EmpresaUsuarioResponse criar(EmpresaUsuarioCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        validarUsuarioNaoVinculado(request.usuarioId(), empresaId);

        EmpresaUsuario entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);

        return mapper.toResponse(repository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAtualizarPerfil")
    public EmpresaUsuarioResponse atualizarPerfil(
            Long usuarioId,
            EmpresaUsuarioUpdateRequest request
    ) {
        Long empresaId = TenantContext.getEmpresaId();
        EmpresaUsuario vinculo = buscarVinculo(usuarioId, empresaId);

        protegerUltimoAdmin(vinculo, request.perfil(), empresaId);

        mapper.updateEntity(request, vinculo);

        return mapper.toResponse(repository.save(vinculo));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackBuscar")
    public EmpresaUsuarioResponse buscar(Long usuarioId) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarVinculo(usuarioId, empresaId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackListar")
    public Page<EmpresaUsuarioResumoResponse> listar(Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackListarPorPerfil")
    public Page<EmpresaUsuarioResumoResponse> listarPorPerfil(
            PerfilEmpresaUsuario perfil,
            Pageable pageable
    ) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByPerfilAndEmpresaId(perfil, empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackRemover")
    public void remover(Long usuarioId) {
        Long empresaId = TenantContext.getEmpresaId();
        EmpresaUsuario vinculo = buscarVinculo(usuarioId, empresaId);

        protegerUltimoAdmin(vinculo, null, empresaId);

        repository.deleteByUsuarioIdAndEmpresaId(usuarioId, empresaId);
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EmpresaUsuarioResponse fallbackCriar(
            EmpresaUsuarioCreateRequest request,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                MSG_SERVICO_INDISPONIVEL,
                ex
        );
    }

    private EmpresaUsuarioResponse fallbackAtualizarPerfil(
            Long usuarioId,
            EmpresaUsuarioUpdateRequest request,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                MSG_SERVICO_INDISPONIVEL,
                ex
        );
    }

    private EmpresaUsuarioResponse fallbackBuscar(Long usuarioId, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                MSG_SERVICO_INDISPONIVEL,
                ex
        );
    }

    private Page<EmpresaUsuarioResumoResponse> fallbackListar(
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                MSG_SERVICO_INDISPONIVEL,
                ex
        );
    }

    private Page<EmpresaUsuarioResumoResponse> fallbackListarPorPerfil(
            PerfilEmpresaUsuario perfil,
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                MSG_SERVICO_INDISPONIVEL,
                ex
        );
    }

    private void fallbackRemover(Long usuarioId, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                MSG_SERVICO_INDISPONIVEL,
                ex
        );
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
        if (repository.existsByUsuarioIdAndEmpresaId(usuarioId, empresaId)) {
            throw new IllegalArgumentException("O usuário já está vinculado a esta empresa.");
        }
    }

    private void protegerUltimoAdmin(
            EmpresaUsuario vinculo,
            PerfilEmpresaUsuario novoPerfil,
            Long empresaId
    ) {
        if (vinculo.getPerfil() == PerfilEmpresaUsuario.ADMIN
                && novoPerfil != PerfilEmpresaUsuario.ADMIN) {

            long totalAdmins = repository.findByPerfilAndEmpresaId(
                    PerfilEmpresaUsuario.ADMIN,
                    empresaId,
                    Pageable.unpaged()
            ).getTotalElements();

            if (totalAdmins <= 1) {
                throw new IllegalStateException(
                        "Não é permitido remover ou alterar o perfil do último ADMIN da empresa."
                );
            }
        }
    }
}