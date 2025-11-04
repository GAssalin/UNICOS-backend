package br.com.unicos.ms_auth.service.impl;

import br.com.unicos.ms_auth.dto.UsuarioRequest;
import br.com.unicos.ms_auth.dto.UsuarioResponse;
import br.com.unicos.ms_auth.model.Usuario;
import br.com.unicos.ms_auth.repository.RoleRepository;
import br.com.unicos.ms_auth.repository.UsuarioRepository;
import br.com.unicos.ms_auth.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link UsuarioService}.
 * <p>
 * Contém as regras de negócio e interações com o repositório de Usuario.
 */
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UsuarioResponse salvar(UsuarioRequest request) {
        if (usuarioRepository.findByUsername(request.username()).isPresent()) {
            throw new DataIntegrityViolationException("Já existe um usuário com este username.");
        }

        Usuario usuario = modelMapper.map(request, Usuario.class);
        if (request.rolesIds() != null && !request.rolesIds().isEmpty()) {
            usuario.setRoles(
                    request.rolesIds().stream()
                            .map(id -> roleRepository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Role não encontrada: ID " + id)))
                            .collect(Collectors.toSet())
            );
        }

        return modelMapper.map(usuarioRepository.save(usuario), UsuarioResponse.class);
    }

    @Override
    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        usuario.setUsername(request.username());
        usuario.setEmail(request.email());
        usuario.setAtivo(request.ativo() != null ? request.ativo() : usuario.isAtivo());

        if (request.rolesIds() != null) {
            usuario.setRoles(
                    request.rolesIds().stream()
                            .map(rid -> roleRepository.findById(rid)
                                    .orElseThrow(() -> new EntityNotFoundException("Role não encontrada: ID " + rid)))
                            .collect(Collectors.toSet())
            );
        }

        return modelMapper.map(usuarioRepository.save(usuario), UsuarioResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioResponse> buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> modelMapper.map(usuario, UsuarioResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(u -> modelMapper.map(u, UsuarioResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarAtivos() {
        return usuarioRepository.findByAtivoTrue().stream()
                .map(u -> modelMapper.map(u, UsuarioResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarInativos() {
        return usuarioRepository.findByAtivoFalse().stream()
                .map(u -> modelMapper.map(u, UsuarioResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void desativar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado para exclusão.");
        }
        usuarioRepository.deleteById(id);
    }
}
