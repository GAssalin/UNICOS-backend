package br.com.unicos.ms_auth.service.impl;

import br.com.unicos.ms_auth.dto.usuario.UsuarioRequest;
import br.com.unicos.ms_auth.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_auth.model.Role;
import br.com.unicos.ms_auth.model.Usuario;
import br.com.unicos.ms_auth.repository.RoleRepository;
import br.com.unicos.ms_auth.repository.UsuarioRepository;
import br.com.unicos.ms_auth.service.interfaces.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementação do serviço responsável pelas regras de negócio
 * relacionadas à entidade Usuario.
 * <p>
 * Esta classe trata exclusivamente da gestão dos dados do usuário.
 * Todas as regras relacionadas à verificação de e-mail são tratadas em
 * serviços específicos.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponse salvar(UsuarioRequest request) {
        if (usuarioRepository.findByLoginIgnoreCaseAndEmailVerificadoTrue(request.login()).isPresent())
            throw new IllegalArgumentException("Já existe um usuário com o login informado.");

        if (request.email() != null && usuarioRepository.findByEmailIgnoreCaseAndEmailVerificadoTrue(request.email()).isPresent())
            throw new IllegalArgumentException("Já existe um usuário com o e-mail informado.");

        Usuario usuario = Usuario.builder()
                .login(request.login())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .pessoaId(request.pessoaId())
                .ativo(request.ativo() != null ? request.ativo() : true)
                .roles(buscarRoles(request.rolesIds()))
                .build();

        usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    @Override
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));

        if (!usuario.getLogin().equals(request.login()) && usuarioRepository.findByLoginIgnoreCaseAndEmailVerificadoTrue(request.login()).isPresent())
            throw new IllegalArgumentException("Já existe um usuário com o login informado.");

        if (!usuario.getEmail().equals(request.email()) && usuarioRepository.findByEmailIgnoreCaseAndEmailVerificadoTrue(request.email()).isPresent())
            throw new IllegalArgumentException("Já existe um usuário com o e-mail informado.");

        usuario.setLogin(request.login());
        usuario.setEmail(request.email());
        usuario.setPessoaId(request.pessoaId());
        usuario.setAtivo(request.ativo() != null ? request.ativo() : usuario.isAtivo());

        usuario.setRoles(buscarRoles(request.rolesIds()));

        usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    @Override
    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));

        return toResponse(usuario);
    }

    @Override
    public UsuarioResponse buscarPorLogin(String login) {
        Usuario usuario = usuarioRepository.findByLoginIgnoreCaseAndEmailVerificadoTrue(login)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + login));

        return toResponse(usuario);
    }

    @Override
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<UsuarioResponse> listarAtivos() {
        return usuarioRepository.findByAtivoTrueAndEmailVerificadoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<UsuarioResponse> listarInativos() {
        return usuarioRepository.findByAtivoFalseAndEmailVerificadoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void desativar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public void deletar(Long id) {

        if (!usuarioRepository.existsById(id))
            throw new EntityNotFoundException("Usuário não encontrado: " + id);

        usuarioRepository.deleteById(id);
    }

    private UsuarioResponse toResponse(Usuario entity) {
        return new UsuarioResponse(
                entity.getId(),
                entity.getLogin(),
                entity.getPessoaId(),
                entity.getEmail(),
                entity.isEmailVerificado(),
                entity.isAtivo(),
                entity.getRoles()
                        .stream()
                        .map(Role::getNome)
                        .collect(Collectors.toSet()),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    private Set<Role> buscarRoles(Set<Long> ids) {
        if (ids == null || ids.isEmpty())
            return Set.of();

        return ids.stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Role não encontrada: " + id)))
                .collect(Collectors.toSet());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCaseAndEmailVerificadoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("O usuário não foi encontrado!"));
    }
}
