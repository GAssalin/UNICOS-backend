package br.com.unicos.ms_auth.service.impl;

import br.com.unicos.ms_auth.dto.usuario.UsuarioRequest;
import br.com.unicos.ms_auth.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_auth.model.Role;
import br.com.unicos.ms_auth.model.Usuario;
import br.com.unicos.ms_auth.repository.RoleRepository;
import br.com.unicos.ms_auth.repository.UsuarioRepository;
import br.com.unicos.ms_auth.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementação do serviço responsável pelas regras de negócio
 * relacionadas à entidade Usuario.
 */
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    /**
     * Cria um novo usuário autenticável no sistema.
     */
    @Override
    public UsuarioResponse salvar(UsuarioRequest request) {

        if (usuarioRepository.findByLogin(request.login()).isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário com o login informado.");
        }

        if (request.email() != null &&
                usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário com o e-mail informado.");
        }

        Usuario usuario = Usuario.builder()
                .login(request.login())
                .password(request.password()) // futuramente criptografado
                .email(request.email())
                .pessoaId(request.pessoaId())
                .ativo(request.ativo() != null ? request.ativo() : true)
                .roles(buscarRoles(request.rolesIds()))
                .build();

        usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    /**
     * Atualiza um usuário existente.
     */
    @Override
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));

        // Verifica se o login está sendo alterado e se é único
        if (!usuario.getLogin().equals(request.login()) &&
                usuarioRepository.findByLogin(request.login()).isPresent()) {

            throw new IllegalArgumentException("Já existe um usuário com o login informado.");
        }

        // Verifica se o email está sendo alterado e se é único
        if (request.email() != null &&
                !request.email().equals(usuario.getEmail()) &&
                usuarioRepository.findByEmail(request.email()).isPresent()) {

            throw new IllegalArgumentException("Já existe um usuário com o e-mail informado.");
        }

        usuario.setLogin(request.login());
        usuario.setEmail(request.email());
        usuario.setPessoaId(request.pessoaId());
        usuario.setAtivo(request.ativo() != null ? request.ativo() : usuario.isAtivo());

        // Atualiza roles
        usuario.setRoles(buscarRoles(request.rolesIds()));

        usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    /**
     * Busca um usuário pelo ID.
     */
    @Override
    public UsuarioResponse buscarPorId(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));

        return toResponse(usuario);
    }

    /**
     * Busca um usuário pelo login.
     */
    @Override
    public UsuarioResponse buscarPorLogin(String login) {

        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + login));

        return toResponse(usuario);
    }

    /**
     * Lista todos os usuários cadastrados.
     */
    @Override
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Lista todos os usuários ativos.
     */
    @Override
    public List<UsuarioResponse> listarAtivos() {
        return usuarioRepository.findByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Lista todos os usuários inativos.
     */
    @Override
    public List<UsuarioResponse> listarInativos() {
        return usuarioRepository.findByAtivoFalse()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Desativa um usuário.
     */
    @Override
    public void desativar(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    /**
     * Remove um usuário permanentemente.
     */
    @Override
    public void deletar(Long id) {

        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado: " + id);
        }

        usuarioRepository.deleteById(id);
    }

    /**
     * Converte entidade Usuario em UsuarioResponse.
     */
    private UsuarioResponse toResponse(Usuario entity) {
        return new UsuarioResponse(
                entity.getId(),
                entity.getLogin(),
                entity.getPessoaId(),
                entity.getEmail(),
                entity.isAtivo(),
                entity.getRoles()
                        .stream()
                        .map(Role::getNome)
                        .collect(Collectors.toSet()),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    /**
     * Retorna o conjunto de roles baseado nos IDs recebidos.
     */
    private Set<Role> buscarRoles(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Set.of();
        }

        return ids.stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Role não encontrada: " + id)))
                .collect(Collectors.toSet());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("O usuário não foi encontrado!"));
    }
}
