package br.com.unicos.ms_usuario.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.core.usuario.auth.dto.UsuarioAuthResponse;
import br.com.unicos.ms_usuario.client.PermissaoClient;
import br.com.unicos.ms_usuario.dto.permissao.RoleResumoResponse;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService extends BaseTenantService<Usuario, Long> {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final PermissaoClient permissaoClient;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            UsuarioMapper usuarioMapper,
            PermissaoClient permissaoClient
    ) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.permissaoClient = permissaoClient;
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAuth")
    public UsuarioAuthResponse buscarParaAutenticacao(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return new UsuarioAuthResponse(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getPassword(),
                usuario.getEmpresaId()
        );
    }

    @Transactional
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdmin")
    public UsuarioResponse salvar(UsuarioRequest request) {
        validarLoginDuplicado(request.login());
        validarEmailDuplicado(request.email());

        RoleResumoResponse role = buscarRoleObrigatoria(request.roleId());

        Usuario usuario = Usuario.builder()
                .login(request.login())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .pessoaId(request.pessoaId())
                .roleId(role.id())
                .ativo(request.ativo() != null ? request.ativo() : true)
                .empresaId(TenantContext.getEmpresaId())
                .emailVerificado(false)
                .build();

        return usuarioMapper.toResponse(usuarioRepository.save(usuario), role.nome());
    }

    @Transactional
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdmin")
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
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

        RoleResumoResponse role = buscarRoleObrigatoria(request.roleId());
        usuario.setRoleId(role.id());

        if (request.password() != null && !request.password().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.password()));
        }

        return usuarioMapper.toResponse(usuarioRepository.save(usuario), role.nome());
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdminById")
    public UsuarioResponse buscarPorId(Long id) {
        return toResponseComRole(buscarUsuario(id));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdminByLogin")
    public UsuarioResponse buscarPorLogin(String login) {
        Usuario usuario = usuarioRepository
                .findByLoginIgnoreCaseAndEmailVerificadoTrueAndEmpresaId(login, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + login));

        return toResponseComRole(usuario);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdminPage")
    public Page<UsuarioResponse> listarTodos(Pageable pageable) {
        return usuarioRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(this::toResponseComRole);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdminPage")
    public Page<UsuarioResponse> listarAtivos(Pageable pageable) {
        return usuarioRepository
                .findByAtivoTrueAndEmailVerificadoTrueAndEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(this::toResponseComRole);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdminPage")
    public Page<UsuarioResponse> listarInativos(Pageable pageable) {
        return usuarioRepository
                .findByAtivoFalseAndEmailVerificadoTrueAndEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(this::toResponseComRole);
    }

    @Transactional
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdminEntity")
    public Usuario desativar(Long id) {
        Usuario usuario = buscarUsuario(id);
        usuario.setAtivo(false);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    @CircuitBreaker(name = "usuario-admin", fallbackMethod = "fallbackAdminEntity")
    public Usuario deletar(Long id) {
        usuarioRepository.delete(buscarUsuario(id));
        return usuarioRepository.findById(id).orElseGet(Usuario::new);
    }

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

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));
    }

    private UsuarioResponse toResponseComRole(Usuario usuario) {
        String roleNome = null;
        if (usuario.getRoleId() != null) {
            try {
                roleNome = permissaoClient.buscarRolePorId(usuario.getRoleId()).nome();
            } catch (Exception ignored) {
                roleNome = null;
            }
        }
        return usuarioMapper.toResponse(usuario, roleNome);
    }

    private RoleResumoResponse buscarRoleObrigatoria(Long roleId) {
        try {
            RoleResumoResponse role = permissaoClient.buscarRolePorId(roleId);
            if (role == null || role.id() == null) {
                throw new EntityNotFoundException("Role não encontrada: " + roleId);
            }
            return role;
        } catch (Exception ex) {
            throw new EntityNotFoundException("Role não encontrada: " + roleId);
        }
    }

    private void validarLoginDuplicado(String login) {
        if (usuarioRepository
                .findByLoginIgnoreCaseAndEmailVerificadoTrueAndEmpresaId(login, TenantContext.getEmpresaId())
                .isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário com o login informado.");
        }
    }

    private void validarEmailDuplicado(String email) {
        if (email == null) {
            return;
        }

        if (usuarioRepository
                .findByEmailIgnoreCaseAndEmailVerificadoTrueAndEmpresaId(email, TenantContext.getEmpresaId())
                .isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário com o e-mail informado.");
        }
    }
}
