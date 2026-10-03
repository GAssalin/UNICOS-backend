package br.com.unicos.ms_permissao.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_permissao.dto.role.RoleRequest;
import br.com.unicos.ms_permissao.dto.role.RoleResponse;
import br.com.unicos.ms_permissao.mapper.RoleMapper;
import br.com.unicos.ms_permissao.model.Role;
import br.com.unicos.ms_permissao.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Regras de negócio das roles (papéis), sempre restritas à empresa do usuário autenticado.
 */
@Service
public class RoleService extends BaseTenantService<Role, Long> {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        super(roleRepository);
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Transactional
    public RoleResponse salvar(RoleRequest request) {
        validarNomeDisponivel(request.nome());

        Role role = Role.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .empresaId(TenantContext.getEmpresaId())
                .build();

        return roleMapper.toResponse(roleRepository.save(role));
    }

    @Transactional
    public RoleResponse atualizar(Long id, RoleRequest request) {
        Role entity = buscarEntidadePorId(id);

        if (!entity.getNome().equalsIgnoreCase(request.nome()))
            validarNomeDisponivel(request.nome());

        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());

        return roleMapper.toResponse(roleRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public RoleResponse buscarPorId(Long id) {
        return roleMapper.toResponse(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> listarTodos() {
        return roleRepository.findByEmpresaIdOrderByNomeAsc(TenantContext.getEmpresaId())
                .stream()
                .map(roleMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        roleRepository.delete(buscarEntidadePorId(id));
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    @Transactional(readOnly = true)
    public boolean existePorNome(String nome) {
        return roleRepository.existsByNomeIgnoreCaseAndEmpresaId(nome, TenantContext.getEmpresaId());
    }

    private Role buscarEntidadePorId(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada: " + id));
    }

    private void validarNomeDisponivel(String nome) {
        if (existePorNome(nome))
            throw new IllegalArgumentException("Já existe um papel cadastrado com o nome informado.");
    }
}
