package br.com.unicos.ms_auth.service;

import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_auth.dto.usuario.UsuarioRequest;
import br.com.unicos.ms_auth.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_auth.mapper.UsuarioMapper;
import br.com.unicos.ms_auth.model.Role;
import br.com.unicos.ms_auth.model.Usuario;
import br.com.unicos.ms_auth.repository.RoleRepository;
import br.com.unicos.ms_auth.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela gestão de usuários do sistema.
 *
 * <p>
 * Todas as operações são isoladas por empresa (tenant).
 * Verificação de e-mail é tratada em serviço específico.
 * </p>
 */
@Service
public class UsuarioService extends BaseTenantService<Usuario, Long> implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UsuarioMapper usuarioMapper) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    // ============================================================
    // CRUD
    // ============================================================
    @Transactional
    public UsuarioResponse salvar(UsuarioRequest request, Long empresaId) {

        validarLoginDuplicado(request.login(), empresaId);
        validarEmailDuplicado(request.email(), empresaId);

        Usuario usuario = Usuario.builder()
                .login(request.login())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .pessoaId(request.pessoaId())
                .ativo(request.ativo() != null ? request.ativo() : true)
                .roles(buscarRolesByIds(request.rolesIds()))
                .empresaId(empresaId)
                .emailVerificado(false)
                .build();

        Usuario salvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(salvo);
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request, Long empresaId) {
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
        usuario.setRoles(buscarRolesByIds(request.rolesIds()));

        Usuario atualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(atualizado);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        return usuarioMapper.toResponse(buscarUsuario(id));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPeloEmailAndVerificado(String email) {
        return usuarioRepository
                .findByEmailIgnoreCaseAndEmailVerificadoTrue(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorLogin(String login, Long empresaId) {
        Usuario usuario = usuarioRepository
                .findByLoginIgnoreCaseAndEmailVerificadoTrueAndEmpresaId(login, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + login));

        return usuarioMapper.toResponse(usuario);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarTodos(Long empresaId, Pageable pageable) {
        return usuarioRepository
                .findByEmpresaId(empresaId, pageable)
                .map(usuarioMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarAtivos(Long empresaId, Pageable pageable) {
        return usuarioRepository
                .findByAtivoTrueAndEmailVerificadoTrueAndEmpresaId(empresaId, pageable)
                .map(usuarioMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarInativos(Long empresaId, Pageable pageable) {
        return usuarioRepository
                .findByAtivoFalseAndEmailVerificadoTrueAndEmpresaId(empresaId, pageable)
                .map(usuarioMapper::toResponse);
    }

    @Transactional
    public void desativar(Long id) {
        Usuario usuario = buscarUsuario(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        Usuario usuario = buscarUsuario(id);
        usuarioRepository.delete(usuario);
    }

    // ============================================================
    // Spring Security
    // ============================================================

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("Autenticação deve ser realizada com tenant informado.");
    }

    // ============================================================
    // Métodos auxiliares
    // ============================================================

    @Transactional(readOnly = true)
    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado no tenant informado: " + id));
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

    private Set<Role> buscarRolesByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty())
            return Set.of();

        return ids.stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Role não encontrada: " + id)))
                .collect(Collectors.toSet());
    }
}
