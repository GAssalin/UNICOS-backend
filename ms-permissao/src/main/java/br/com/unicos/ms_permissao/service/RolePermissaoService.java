package br.com.unicos.ms_permissao.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_permissao.dto.role_permissao.RolePermissaoListDTO;
import br.com.unicos.ms_permissao.dto.role_permissao.RolePermissaoRequest;
import br.com.unicos.ms_permissao.dto.role_permissao.RolePermissaoResponse;
import br.com.unicos.ms_permissao.mapper.RolePermissaoMapper;
import br.com.unicos.ms_permissao.model.Permissao;
import br.com.unicos.ms_permissao.model.Role;
import br.com.unicos.ms_permissao.model.RolePermissao;
import br.com.unicos.ms_permissao.repository.PermissaoRepository;
import br.com.unicos.ms_permissao.repository.RolePermissaoRepository;
import br.com.unicos.ms_permissao.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Vínculos entre roles e permissões.
 *
 * <p>
 * A empresa do vínculo é sempre a do usuário autenticado: o {@code empresaId} enviado pelo
 * cliente é aceito apenas por compatibilidade e precisa coincidir com ela.
 * </p>
 */
@Service
public class RolePermissaoService extends BaseTenantService<RolePermissao, Long> {

    private final RolePermissaoRepository rolePermissaoRepository;
    private final RoleRepository roleRepository;
    private final PermissaoRepository permissaoRepository;
    private final RolePermissaoMapper mapper;

    public RolePermissaoService(RolePermissaoRepository rolePermissaoRepository, RoleRepository roleRepository, PermissaoRepository permissaoRepository, RolePermissaoMapper mapper) {
        super(rolePermissaoRepository);
        this.rolePermissaoRepository = rolePermissaoRepository;
        this.roleRepository = roleRepository;
        this.permissaoRepository = permissaoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public RolePermissaoResponse criar(RolePermissaoRequest request) {
        Long empresaId = resolverEmpresa(request.empresaId());

        if (rolePermissaoRepository.existsByRoleIdAndPermissaoIdAndEmpresaId(request.roleId(), request.permissaoId(), empresaId))
            throw new IllegalArgumentException("Já existe vínculo entre empresa, role e permissão informados.");

        Role role = roleRepository.findByIdAndEmpresaId(request.roleId(), empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada"));

        // Catálogo global de permissões.
        Permissao permissao = permissaoRepository.findById(request.permissaoId())
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada"));

        RolePermissao entity = RolePermissao.builder()
                .empresaId(empresaId)
                .role(role)
                .permissao(permissao)
                .ativo(request.ativo() == null || request.ativo())
                .build();

        return mapper.toResponse(save(entity));
    }

    @Transactional
    public RolePermissaoResponse alterarStatus(Long id, boolean ativo) {
        RolePermissao entity = buscarVinculo(id);
        entity.setAtivo(ativo);
        return mapper.toResponse(save(entity));
    }

    @Transactional
    public void remover(Long id) {
        rolePermissaoRepository.delete(buscarVinculo(id));
    }

    @Transactional(readOnly = true)
    public Page<RolePermissaoListDTO> listarPorEmpresa(Long empresaId, Pageable pageable) {
        return rolePermissaoRepository
                .findAllByEmpresaId(resolverEmpresa(empresaId), pageable)
                .map(mapper::toListDTO);
    }

    @Transactional(readOnly = true)
    public Page<RolePermissaoListDTO> listarAtivosPorEmpresa(Long empresaId, Pageable pageable) {
        return rolePermissaoRepository
                .findByAtivoTrueAndEmpresaId(resolverEmpresa(empresaId), pageable)
                .map(mapper::toListDTO);
    }

    private RolePermissao buscarVinculo(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo Empresa-Role-Permissão não encontrado"));
    }

    private static Long resolverEmpresa(Long empresaIdInformado) {
        Long empresaId = TenantContext.getEmpresaId();

        if (empresaIdInformado != null && !empresaIdInformado.equals(empresaId))
            throw new AccessDeniedException("Acesso negado aos dados de outra empresa.");

        return empresaId;
    }
}
