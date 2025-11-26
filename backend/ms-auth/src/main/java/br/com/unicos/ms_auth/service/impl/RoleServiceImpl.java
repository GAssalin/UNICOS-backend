package br.com.unicos.ms_auth.service.impl;

import br.com.unicos.ms_auth.dto.permissao.PermissaoResponse;
import br.com.unicos.ms_auth.dto.role.RoleRequest;
import br.com.unicos.ms_auth.dto.role.RoleResponse;
import br.com.unicos.ms_auth.model.Permissao;
import br.com.unicos.ms_auth.model.Role;
import br.com.unicos.ms_auth.repository.PermissaoRepository;
import br.com.unicos.ms_auth.repository.RoleRepository;
import br.com.unicos.ms_auth.service.interfaces.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementação do serviço responsável pelas regras de negócio
 * relacionadas à entidade Role.
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissaoRepository permissaoRepository;

    /**
     * Cria um novo papel no sistema.
     */
    @Override
    public RoleResponse salvar(RoleRequest request) {

        if (roleRepository.existsByNome(request.nome())) {
            throw new IllegalArgumentException("Já existe um papel cadastrado com o código informado.");
        }

        Role role = Role.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .permissoes(buscarPermissoes(request.permissoesIds()))
                .build();

        roleRepository.save(role);
        return toResponse(role);
    }

    /**
     * Atualiza um papel existente.
     */
    @Override
    public RoleResponse atualizar(Long id, RoleRequest request) {

        Role entity = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada: " + id));

        // Verificar duplicidade caso o código seja alterado
        if (!entity.getNome().equals(request.nome()) &&
                roleRepository.existsByNome(request.nome())) {

            throw new IllegalArgumentException("Já existe um papel cadastrado com o código informado.");
        }

        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
        entity.setPermissoes(buscarPermissoes(request.permissoesIds()));

        roleRepository.save(entity);
        return toResponse(entity);
    }

    /**
     * Busca um papel pelo ID informado.
     */
    @Override
    public RoleResponse buscarPorId(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada: " + id));

        return toResponse(role);
    }

    /**
     * Lista todos os papéis cadastrados.
     */
    @Override
    public List<RoleResponse> listarTodos() {
        return roleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Remove um papel existente.
     */
    @Override
    public void deletar(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Role não encontrada: " + id);
        }

        roleRepository.deleteById(id);
    }

    /**
     * Verifica se existe um papel com o nome informado.
     */
    @Override
    public boolean existePorNome(String nome) {
        return roleRepository.existsByNome(nome);
    }

    /**
     * Converte entidade Role em RoleResponse.
     */
    private RoleResponse toResponse(Role entity) {
        return new RoleResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getPermissoes()
                        .stream()
                        .map(perm -> new PermissaoResponse(
                                perm.getId(),
                                perm.getNome(),
                                perm.getDescricao()
                        ))
                        .collect(Collectors.toSet())
        );
    }

    /**
     * Busca permissões pelo conjunto de IDs e retorna o Set de entidades.
     */
    private Set<Permissao> buscarPermissoes(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Set.of();
        }

        return ids.stream()
                .map(id -> permissaoRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada: " + id)))
                .collect(Collectors.toSet());
    }
}
