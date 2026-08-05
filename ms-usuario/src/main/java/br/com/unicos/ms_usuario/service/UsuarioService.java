package br.com.unicos.ms_usuario.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.core.usuario.auth.dto.UsuarioAuthResponse;
import br.com.unicos.ms_usuario.client.PermissaoClient;
import br.com.unicos.ms_usuario.client.PermissaoService;
import br.com.unicos.ms_usuario.dto.permissao.RoleResumoResponse;
import br.com.unicos.ms_usuario.dto.usuario.UsuarioRequest;
import br.com.unicos.ms_usuario.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_usuario.mapper.UsuarioMapper;
import br.com.unicos.ms_usuario.model.Usuario;
import br.com.unicos.ms_usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UsuarioService extends BaseTenantService<Usuario, Long> {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final PermissaoService permissaoService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, UsuarioMapper usuarioMapper, PermissaoService permissaoService) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.permissaoService = permissaoService;
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

        RoleResumoResponse role = permissaoService.buscarRolePorId(request.roleId());

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

        RoleResumoResponse role = permissaoService.buscarRolePorId(request.roleId());
        usuario.setRoleId(role.id());

        if (request.password() != null && !request.password().isBlank())
            usuario.setPassword(passwordEncoder.encode(request.password()));

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

        if (Boolean.TRUE.equals(usuario.getAtivo()))
            return;

        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void desativar(Long id) {
        Usuario usuario = buscarUsuario(id);

        if (Boolean.FALSE.equals(usuario.getAtivo()))
            return;

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = buscarUsuario(id);
        usuarioRepository.delete(usuario);
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));
    }

    private UsuarioResponse toResponseComRole(Usuario usuario) {
        String roleNome = permissaoService.buscarRolePorId(usuario.getRoleId()).nome();
        return usuarioMapper.toResponse(usuario, roleNome);
    }

    private void validarLoginDuplicado(String login) {
        if (usuarioRepository
                .findByLoginIgnoreCaseAndEmailVerificadoTrueAndEmpresaId(login, TenantContext.getEmpresaId())
                .isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário com o login informado.");
        }
    }

    private void validarEmailDuplicado(String email) {
        if (email == null)
            return;

        if (usuarioRepository
                .findByEmailIgnoreCaseAndEmailVerificadoTrueAndEmpresaId(email, TenantContext.getEmpresaId())
                .isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário com o e-mail informado.");
        }
    }
}