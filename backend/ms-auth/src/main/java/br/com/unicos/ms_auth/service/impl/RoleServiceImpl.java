package br.com.unicos.ms_auth.service.impl;

import br.com.unicos.ms_auth.dto.role.RoleRequest;
import br.com.unicos.ms_auth.dto.role.RoleResponse;
import br.com.unicos.ms_auth.model.Role;
import br.com.unicos.ms_auth.repository.RoleRepository;
import br.com.unicos.ms_auth.service.interfaces.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação do serviço responsável pelas regras de negócio
 * relacionadas à entidade Role.
 *
 * Role representa apenas identidade funcional.
 * Permissões são tratadas exclusivamente por empresa.
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleResponse salvar(RoleRequest request) {

        if (roleRepository.existsByNome(request.nome())) {
            throw new IllegalArgumentException("Já existe um papel cadastrado com o nome informado.");
        }

        Role role = Role.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .build();

        roleRepository.save(role);
        return toResponse(role);
    }

    @Override
    public RoleResponse atualizar(Long id, RoleRequest request) {

        Role entity = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada: " + id));

        if (!entity.getNome().equals(request.nome())
                && roleRepository.existsByNome(request.nome())) {

            throw new IllegalArgumentException("Já existe um papel cadastrado com o nome informado.");
        }

        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());

        roleRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public RoleResponse buscarPorId(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada: " + id));

        return toResponse(role);
    }

    @Override
    public List<RoleResponse> listarTodos() {
        return roleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {

        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Role não encontrada: " + id);
        }

        roleRepository.deleteById(id);
    }

    @Override
    public boolean existePorNome(String nome) {
        return roleRepository.existsByNome(nome);
    }

    private RoleResponse toResponse(Role entity) {
        return new RoleResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao()
        );
    }
}
