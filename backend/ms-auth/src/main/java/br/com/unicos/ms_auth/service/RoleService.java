package br.com.unicos.ms_auth.service;

import br.com.unicos.ms_auth.dto.role.RoleRequest;
import br.com.unicos.ms_auth.dto.role.RoleResponse;
import br.com.unicos.ms_auth.mapper.RoleMapper;
import br.com.unicos.ms_auth.model.Role;
import br.com.unicos.ms_auth.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementação do serviço responsável pelas regras de negócio
 * relacionadas à entidade {@link Role}.
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Transactional
    public RoleResponse salvar(RoleRequest request) {
        validarNomeDuplicado(request.nome());

        Role role = Role.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .build();

        Role salvo = roleRepository.save(role);
        return roleMapper.toResponse(salvo);
    }

    @Transactional
    public RoleResponse atualizar(Long id, RoleRequest request) {
        Role entity = buscarEntidadePorId(id);

        if (!entity.getNome().equalsIgnoreCase(request.nome())) {
            validarNomeDuplicado(request.nome());
            entity.setNome(request.nome());
        }

        entity.setDescricao(request.descricao());
        Role atualizado = roleRepository.save(entity);
        return roleMapper.toResponse(atualizado);
    }

    @Transactional(readOnly = true)
    public RoleResponse buscarPorId(Long id) {
        return roleMapper.toResponse(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> listarTodos() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toResponse)
                .toList();
    }

    public void deletar(Long id) {
        if (!roleRepository.existsById(id))
            throw new EntityNotFoundException("Role não encontrada: " + id);

        roleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existePorNome(String nome) {
        return roleRepository.existsByNome(nome);
    }

    private Role buscarEntidadePorId(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada: " + id));
    }

    private void validarNomeDuplicado(String nome) {
        if (roleRepository.existsByNome(nome))
            throw new IllegalArgumentException("Já existe um papel cadastrado com o nome informado.");
    }
}
