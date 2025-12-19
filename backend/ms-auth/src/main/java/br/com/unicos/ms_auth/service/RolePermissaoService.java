package br.com.unicos.ms_auth.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_auth.client.AuthUsuarioClient;
import br.com.unicos.ms_auth.dto.role_permissao.RolePermissaoListDTO;
import br.com.unicos.ms_auth.dto.role_permissao.RolePermissaoRequest;
import br.com.unicos.ms_auth.dto.role_permissao.RolePermissaoResponse;
import br.com.unicos.ms_auth.mapper.RolePermissaoMapper;
import br.com.unicos.ms_auth.model.Permissao;
import br.com.unicos.ms_auth.model.Role;
import br.com.unicos.ms_auth.model.RolePermissao;
import br.com.unicos.ms_auth.repository.PermissaoRepository;
import br.com.unicos.ms_auth.repository.RolePermissaoRepository;
import br.com.unicos.ms_auth.repository.RoleRepository;
import br.com.unicos.ms_auth.repository.RoleUsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável pelas regras de negócio
 * relacionadas ao vínculo Role-Permissão.
 *
 * <p>
 * As operações administrativas utilizam paginação.
 * As operações técnicas (autorização) são otimizadas
 * para execução em tempo de autenticação.
 * </p>
 */
@Service
public class RolePermissaoService extends BaseTenantService<RolePermissao, Long> {

    private final RolePermissaoRepository rolePermissaoRepository;
    private final RoleRepository roleRepository;
    private final PermissaoRepository permissaoRepository;
    private final RolePermissaoMapper mapper;

    public RolePermissaoService(
            RolePermissaoRepository rolePermissaoRepository,
            RoleRepository roleRepository,
            PermissaoRepository permissaoRepository,
            AuthUsuarioClient authUsuarioClient,
            RolePermissaoMapper mapper
    ) {
        super(rolePermissaoRepository);
        this.rolePermissaoRepository = rolePermissaoRepository;
        this.roleRepository = roleRepository;
        this.permissaoRepository = permissaoRepository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    public RolePermissaoResponse criar(RolePermissaoRequest request) {

        if (rolePermissaoRepository.existsByRoleIdAndPermissaoIdAndEmpresaId(
                request.roleId(),
                request.permissaoId(),
                request.empresaId()
        )) {
            throw new IllegalArgumentException("Já existe vínculo entre empresa, role e permissão informados.");
        }

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada"));

        Permissao permissao = permissaoRepository.findById(request.permissaoId())
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada"));

        RolePermissao entity = RolePermissao.builder()
                .empresaId(request.empresaId())
                .role(role)
                .permissao(permissao)
                .ativo(true)
                .build();

        return mapper.toResponse(save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    public RolePermissaoResponse alterarStatus(Long id, boolean ativo) {

        RolePermissao entity = findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo Empresa-Role-Permissão não encontrado"));

        entity.setAtivo(ativo);
        return mapper.toResponse(save(entity));
    }

    // ============================================================
    // DELETE
    // ============================================================

    public void remover(Long id) {
        if (!rolePermissaoRepository.existsById(id))
            throw new EntityNotFoundException("Vínculo Empresa-Role-Permissão não encontrado");
        rolePermissaoRepository.deleteById(id);
    }

    // ============================================================
    // LISTAGENS ADMINISTRATIVAS (PAGINADAS)
    // ============================================================

    public Page<RolePermissaoListDTO> listarPorEmpresa(Long empresaId, Pageable pageable) {
        Page<RolePermissao> entidades = rolePermissaoRepository.findAllByEmpresaId(TenantContext.getEmpresaId(), Pageable.unpaged());

        return new PageImpl<>(
                entidades.stream()
                        .map(mapper::toListDTO)
                        .toList(),
                pageable,
                entidades.getTotalElements()
        );
    }

    @Transactional(readOnly = true)
    public Page<RolePermissaoListDTO> listarAtivosPorEmpresa(Long empresaId, Pageable pageable) {
        final List<RolePermissao> ativos = rolePermissaoRepository.findByAtivoTrueAndEmpresaId(empresaId);

        return new PageImpl<>(
                ativos.stream()
                        .map(mapper::toListDTO)
                        .toList(),
                pageable,
                ativos.size()
        );
    }

    @Transactional(readOnly = true)
    public Page<RolePermissaoListDTO> listarInativosPorEmpresa(Long empresaId, Pageable pageable) {
        final List<RolePermissao> inativos = rolePermissaoRepository.findByAtivoFalseAndEmpresaId(empresaId);

        return new PageImpl<>(
                inativos.stream()
                        .map(mapper::toListDTO)
                        .toList(),
                pageable,
                inativos.size()
        );
    }

}
