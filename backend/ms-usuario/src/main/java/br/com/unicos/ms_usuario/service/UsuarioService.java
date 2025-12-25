package br.com.unicos.ms_usuario.service;

import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.core.usuario.auth.dto.UsuarioAuthResponse;
import br.com.unicos.ms_usuario.dto.usuario.UsuarioRequest;
import br.com.unicos.ms_usuario.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_usuario.mapper.UsuarioMapper;
import br.com.unicos.ms_usuario.model.Usuario;
import br.com.unicos.ms_usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    private final PermissionCheckService permissionCheckService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            UsuarioMapper usuarioMapper,
            PermissionCheckService permissionCheckService
    ) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.permissionCheckService = permissionCheckService;
    }

    // ============================================================
    // AUTENTICAÇÃO (USO INTERNO PELO MS-AUTH)
    // ============================================================

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

    // ============================================================
    // CRUD
    // ============================================================

    @Transactional
    public UsuarioResponse salvar(UsuarioRequest request, Long empresaId) {
        if (permissionCheckService.hasPermission("USUARIO_CRIAR")) {
            validarLoginDuplicado(request.login(), empresaId);
            validarEmailDuplicado(request.email(), empresaId);

            Usuario usuario = Usuario.builder()
                    .login(request.login())
                    .password(passwordEncoder.encode(request.password()))
                    .email(request.email())
                    .pessoaId(request.pessoaId())
                    .ativo(request.ativo() != null ? request.ativo() : true)
                    .empresaId(empresaId)
                    .emailVerificado(false)
                    .build();
            return usuarioMapper.toResponse(usuarioRepository.save(usuario));
        }
        throw new AccessDeniedException("Usuário não possui permissão para esta operação");
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request, Long empresaId) {
        if (permissionCheckService.hasPermission("USUARIO_EDITAR")) {
            Usuario usuario = buscarUsuario(id);

            if (!usuario.getLogin().equalsIgnoreCase(request.login())) {
                validarLoginDuplicado(request.login(), empresaId);
                usuario.setLogin(request.login());
            }

            if (request.email() != null && !request.email().equalsIgnoreCase(usuario.getEmail())) {
                validarEmailDuplicado(request.email(), empresaId);
                usuario.setEmail(request.email());
            }

            usuario.setPessoaId(request.pessoaId());
            usuario.setAtivo(request.ativo() != null ? request.ativo() : usuario.getAtivo());

            return usuarioMapper.toResponse(usuarioRepository.save(usuario));
        }
        throw new AccessDeniedException("Usuário não possui permissão para esta operação");
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        if (permissionCheckService.hasPermission("USUARIO_LISTAR"))
            return usuarioMapper.toResponse(buscarUsuario(id));
        throw new AccessDeniedException("Usuário não possui permissão para esta operação");
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorLogin(String login, Long empresaId) {
        if (permissionCheckService.hasPermission("USUARIO_LISTAR")) {
            Usuario usuario = usuarioRepository
                    .findByLoginIgnoreCaseAndEmailVerificadoTrueAndEmpresaId(login, empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + login));
            return usuarioMapper.toResponse(usuario);
        }
        throw new AccessDeniedException("Usuário não possui permissão para esta operação");
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarTodos(Long empresaId, Pageable pageable) {
        if (permissionCheckService.hasPermission("USUARIO_LISTAR"))
            return usuarioRepository.findAllByEmpresaId(empresaId, pageable)
                .map(usuarioMapper::toResponse);
        throw new AccessDeniedException("Usuário não possui permissão para esta operação");
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarAtivos(Long empresaId, Pageable pageable) {
        if (permissionCheckService.hasPermission("USUARIO_LISTAR"))
            return usuarioRepository
                .findByAtivoTrueAndEmailVerificadoTrueAndEmpresaId(empresaId, pageable)
                .map(usuarioMapper::toResponse);
        throw new AccessDeniedException("Usuário não possui permissão para esta operação");
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarInativos(Long empresaId, Pageable pageable) {
        if (permissionCheckService.hasPermission("USUARIO_LISTAR"))
            return usuarioRepository
                .findByAtivoFalseAndEmailVerificadoTrueAndEmpresaId(empresaId, pageable)
                .map(usuarioMapper::toResponse);
        throw new AccessDeniedException("Usuário não possui permissão para esta operação");
    }

    @Transactional
    public Usuario desativar(Long id) {
        if (permissionCheckService.hasPermission("USUARIO_EDITAR")) {
            Usuario usuario = buscarUsuario(id);
            usuario.setAtivo(false);
            return usuarioRepository.save(usuario);
        }
        throw new AccessDeniedException("Usuário não possui permissão para esta operação");
    }

    @Transactional
    public Usuario deletar(Long id) {
        if (permissionCheckService.hasPermission("USUARIO_EXCLUIR")) {
            usuarioRepository.delete(buscarUsuario(id));
            Optional<Usuario> byId = usuarioRepository.findById(id);
            return byId.orElseGet(Usuario::new);
        }
        throw new AccessDeniedException("Usuário não possui permissão para esta operação");
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));
    }

    private void validarLoginDuplicado(String login, Long empresaId) {
        if (usuarioRepository.findByLoginIgnoreCaseAndEmailVerificadoTrueAndEmpresaId(login, empresaId).isPresent())
            throw new IllegalArgumentException("Já existe um usuário com o login informado.");
    }

    private void validarEmailDuplicado(String email, Long empresaId) {
        if (email == null)
            return;

        if (usuarioRepository.findByEmailIgnoreCaseAndEmailVerificadoTrueAndEmpresaId(email, empresaId).isPresent())
            throw new IllegalArgumentException("Já existe um usuário com o e-mail informado.");
    }
}
