package br.com.unicos.ms_usuario.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.core.usuario.auth.dto.UsuarioAuthResponse;
import br.com.unicos.ms_usuario.client.AuthClient;
import br.com.unicos.ms_usuario.dto.usuario.UsuarioRequest;
import br.com.unicos.ms_usuario.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_usuario.mapper.UsuarioMapper;
import br.com.unicos.ms_usuario.model.Usuario;
import br.com.unicos.ms_usuario.repository.UsuarioRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Serviço responsável pela gestão de usuários do sistema.
 *
 * <p>
 * Este serviço NÃO gerencia autenticação, roles ou permissões.
 * Essas responsabilidades pertencem exclusivamente ao ms-auth.
 * </p>
 */
@Service
public class UsuarioService extends BaseTenantService<Usuario, Long> {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final AuthClient authClient;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            UsuarioMapper usuarioMapper,
            AuthClient authClient
    ) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.authClient = authClient;
    }

    // ============================================================
    // AUTENTICAÇÃO (USO INTERNO PELO MS-AUTH)
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAuth")
    public UsuarioAuthResponse buscarParaAutenticacao(String email) {
        if (!authClient.usuarioPossuiPermissao("USUARIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para esta operação");
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return new UsuarioAuthResponse(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getPassword(),
                usuario.getEmpresaId()
        );
    }

    // ============================================================
    // CREATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdmin")
    public UsuarioResponse salvar(UsuarioRequest request) {
        if (!authClient.usuarioPossuiPermissao("USUARIO_CRIAR"))
            throw new AccessDeniedException("Usuário não possui permissão para esta operação");

        validarLoginDuplicado(request.login());
        validarEmailDuplicado(request.email());

        Usuario usuario = Usuario.builder()
                .login(request.login())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .pessoaId(request.pessoaId())
                .ativo(request.ativo() != null ? request.ativo() : true)
                .empresaId(TenantContext.getEmpresaId())
                .emailVerificado(false)
                .build();

        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdmin")
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        if (!authClient.usuarioPossuiPermissao("USUARIO_EDITAR"))
            throw new AccessDeniedException("Usuário não possui permissão para esta operação");

        Usuario usuario = buscarUsuario(id);

        if (!usuario.getLogin().equalsIgnoreCase(request.login())) {
            validarLoginDuplicado(request.login());
            usuario.setLogin(request.login());
        }

        if (request.email() != null && !request.email().equalsIgnoreCase(usuario.getEmail())) {
            validarEmailDuplicado(request.email());
            usuario.setEmail(request.email());
        }

        usuario.setPessoaId(request.pessoaId());
        usuario.setAtivo(request.ativo() != null ? request.ativo() : usuario.getAtivo());

        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdminById")
    public UsuarioResponse buscarPorId(Long id) {
        if (!authClient.usuarioPossuiPermissao("USUARIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para esta operação");
        return usuarioMapper.toResponse(buscarUsuario(id));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdminByLogin")
    public UsuarioResponse buscarPorLogin(String login) {
        if (!authClient.usuarioPossuiPermissao("USUARIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para esta operação");

        Usuario usuario = usuarioRepository
                .findByLoginIgnoreCaseAndEmailVerificadoTrueAndEmpresaId(
                        login,
                        TenantContext.getEmpresaId()
                )
                .orElseThrow(() ->
                        new EntityNotFoundException("Usuário não encontrado: " + login)
                );

        return usuarioMapper.toResponse(usuario);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdminPage")
    public Page<UsuarioResponse> listarTodos(Pageable pageable) {
        if (!authClient.usuarioPossuiPermissao("USUARIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para esta operação");
        return usuarioRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(usuarioMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdminPage")
    public Page<UsuarioResponse> listarAtivos(Pageable pageable) {
        if (!authClient.usuarioPossuiPermissao("USUARIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para esta operação");
        return usuarioRepository
                .findByAtivoTrueAndEmailVerificadoTrueAndEmpresaId(
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(usuarioMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdminPage")
    public Page<UsuarioResponse> listarInativos(Pageable pageable) {
        if (!authClient.usuarioPossuiPermissao("USUARIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para esta operação");
        return usuarioRepository
                .findByAtivoFalseAndEmailVerificadoTrueAndEmpresaId(
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(usuarioMapper::toResponse);
    }

    // ============================================================
    // STATUS / DELETE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdminEntity")
    public Usuario desativar(Long id) {
        if (!authClient.usuarioPossuiPermissao("USUARIO_EDITAR"))
            throw new AccessDeniedException("Usuário não possui permissão para esta operação");
        Usuario usuario = buscarUsuario(id);
        usuario.setAtivo(false);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdminEntity")
    public Usuario deletar(Long id) {
        if (!authClient.usuarioPossuiPermissao("USUARIO_EXCLUIR"))
            throw new AccessDeniedException("Usuário não possui permissão para esta operação");
        usuarioRepository.delete(buscarUsuario(id));
        return usuarioRepository.findById(id).orElseGet(Usuario::new);
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private UsuarioAuthResponse fallbackAuth(String email, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de usuários temporariamente indisponível");
    }

    private UsuarioResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de usuários temporariamente indisponível");
    }

    private UsuarioResponse fallbackAdminById(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de usuários temporariamente indisponível");
    }

    private UsuarioResponse fallbackAdminByLogin(String login, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de usuários temporariamente indisponível");
    }

    private Page<UsuarioResponse> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de usuários temporariamente indisponível");
    }

    private Usuario fallbackAdminEntity(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de usuários temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));
    }

    private void validarLoginDuplicado(String login) {
        if (usuarioRepository
                .findByLoginIgnoreCaseAndEmailVerificadoTrueAndEmpresaId(
                        login,
                        TenantContext.getEmpresaId()
                )
                .isPresent()
        ) {
            throw new IllegalArgumentException("Já existe um usuário com o login informado.");
        }
    }

    private void validarEmailDuplicado(String email) {
        if (email == null)
            return;

        if (usuarioRepository
                .findByEmailIgnoreCaseAndEmailVerificadoTrueAndEmpresaId(
                        email,
                        TenantContext.getEmpresaId()
                )
                .isPresent()
        ) {
            throw new IllegalArgumentException("Já existe um usuário com o e-mail informado.");
        }
    }
}
