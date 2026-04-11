package br.com.unicos.ms_usuario.service;

import br.com.unicos.core.auth.context.AuthContext;
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
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class UsuarioService extends BaseTenantService<Usuario, Long> {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final PermissaoClient permissaoClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            UsuarioMapper usuarioMapper,
            PermissaoClient permissaoClient,
            CircuitBreakerFactory<?, ?> circuitBreakerFactory
    ) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.permissaoClient = permissaoClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Transactional(readOnly = true)
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

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuarioSalvo, role.nome());
    }

    @Transactional
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

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuarioAtualizado, role.nome());
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        return toResponseComRole(buscarUsuario(id));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorLogin(String login) {
        Usuario usuario = usuarioRepository
                .findByLoginIgnoreCaseAndEmailVerificadoTrueAndEmpresaId(login, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + login));

        return toResponseComRole(usuario);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarTodos(Pageable pageable) {
        return usuarioRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(this::toResponseComRole);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarAtivos(Pageable pageable) {
        return usuarioRepository
                .findByAtivoTrueAndEmailVerificadoTrueAndEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(this::toResponseComRole);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarInativos(Pageable pageable) {
        return usuarioRepository
                .findByAtivoFalseAndEmailVerificadoTrueAndEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(this::toResponseComRole);
    }

    @Transactional
    public void ativar(Long id) {
        Usuario usuario = buscarUsuario(id);

        if (Boolean.TRUE.equals(usuario.getAtivo())) {
            return;
        }

        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void desativar(Long id) {
        Usuario usuario = buscarUsuario(id);

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            return;
        }

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = buscarUsuario(id);
        usuarioRepository.delete(usuario);
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));
    }

    private UsuarioResponse toResponseComRole(Usuario usuario) {
        String roleNome = buscarNomeRoleComResiliencia(usuario.getRoleId());
        return usuarioMapper.toResponse(usuario, roleNome);
    }

    private String buscarNomeRoleComResiliencia(Long roleId) {
        if (roleId == null) {
            return null;
        }

        try {
            RoleResumoResponse role = circuitBreakerFactory.create("permissao-client-role").run(
                    () -> permissaoClient.buscarRolePorId(roleId, AuthContext.getToken()),
                    throwable -> fallbackConsultaRole(roleId, throwable)
            );

            return role != null ? role.nome() : null;

        } catch (FeignException.NotFound ex) {
            log.warn("Role não encontrada ao montar resposta do usuário. roleId={}", roleId);
            return null;

        } catch (FeignException ex) {
            log.error(
                    "Falha ao consultar role no ms-permissao durante leitura do usuário. roleId={}, status={}, mensagem={}",
                    roleId,
                    ex.status(),
                    ex.getMessage(),
                    ex
            );
            return null;

        } catch (ResponseStatusException ex) {
            log.error("Circuit breaker acionado ao consultar role {} durante leitura do usuário.", roleId, ex);
            return null;
        }
    }

    private RoleResumoResponse buscarRoleObrigatoria(Long roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException("O roleId é obrigatório.");
        }

        try {
            RoleResumoResponse role = circuitBreakerFactory.create("permissao-client-role").run(
                    () -> permissaoClient.buscarRolePorId(roleId, AuthContext.getToken()),
                    throwable -> fallbackRoleObrigatoria(roleId, throwable)
            );

            if (role == null || role.id() == null) {
                throw new EntityNotFoundException("Role não encontrada: " + roleId);
            }

            return role;

        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException("Role não encontrada: " + roleId);

        } catch (FeignException.BadRequest ex) {
            throw new IllegalArgumentException("Role inválida: " + roleId);

        } catch (FeignException ex) {
            log.error(
                    "Falha ao consultar role obrigatória no ms-permissao. roleId={}, status={}, mensagem={}",
                    roleId,
                    ex.status(),
                    ex.getMessage(),
                    ex
            );

            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Serviço de permissões temporariamente indisponível.",
                    ex
            );
        }
    }

    private RoleResumoResponse fallbackConsultaRole(Long roleId, Throwable throwable) {
        log.error("Circuit breaker acionado na consulta de role para leitura. roleId={}", roleId, throwable);
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de permissões temporariamente indisponível.",
                throwable
        );
    }

    private RoleResumoResponse fallbackRoleObrigatoria(Long roleId, Throwable throwable) {
        if (throwable instanceof FeignException.NotFound) {
            throw new EntityNotFoundException("Role não encontrada: " + roleId);
        }

        if (throwable instanceof FeignException.BadRequest) {
            throw new IllegalArgumentException("Role inválida: " + roleId);
        }

        log.error("Circuit breaker acionado na validação de role obrigatória. roleId={}", roleId, throwable);

        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de permissões temporariamente indisponível.",
                throwable
        );
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