package br.com.unicos.ms_auth.service.impl;

import br.com.unicos.ms_auth.dto.RoleRequest;
import br.com.unicos.ms_auth.dto.RoleResponse;
import br.com.unicos.ms_auth.model.Role;
import br.com.unicos.ms_auth.repository.PermissaoRepository;
import br.com.unicos.ms_auth.repository.RoleRepository;
import br.com.unicos.ms_auth.service.RoleService;
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
 * Implementação da interface {@link RoleService}.
 * <p>
 * Contém as regras de negócio e interações com o repositório de Role.
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissaoRepository permissaoRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public RoleResponse salvar(RoleRequest request) {
        if (roleRepository.existsByNome(request.nome())) {
            throw new DataIntegrityViolationException("Já existe um papel com este nome.");
        }

        Role role = modelMapper.map(request, Role.class);
        if (request.permissoesIds() != null && !request.permissoesIds().isEmpty()) {
            role.setPermissoes(
                    request.permissoesIds().stream()
                            .map(id -> permissaoRepository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada: ID " + id)))
                            .collect(Collectors.toSet())
            );
        }

        return modelMapper.map(roleRepository.save(role), RoleResponse.class);
    }

    @Override
    @Transactional
    public RoleResponse atualizar(Long id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Papel não encontrado."));

        role.setNome(request.nome());
        role.setDescricao(request.descricao());

        if (request.permissoesIds() != null) {
            role.setPermissoes(
                    request.permissoesIds().stream()
                            .map(pid -> permissaoRepository.findById(pid)
                                    .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada: ID " + pid)))
                            .collect(Collectors.toSet())
            );
        }

        return modelMapper.map(roleRepository.save(role), RoleResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleResponse> buscarPorId(Long id) {
        return roleRepository.findById(id)
                .map(r -> modelMapper.map(r, RoleResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> listarTodos() {
        return roleRepository.findAll().stream()
                .map(r -> modelMapper.map(r, RoleResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Papel não encontrado para exclusão.");
        }
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNome(String nome) {
        return roleRepository.existsByNome(nome);
    }
}
