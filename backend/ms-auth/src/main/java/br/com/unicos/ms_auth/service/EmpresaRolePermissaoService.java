package br.com.unicos.ms_auth.service;

import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoListDTO;
import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoRequest;
import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoResponse;
import br.com.unicos.ms_auth.mapper.EmpresaRolePermissaoMapper;
import br.com.unicos.ms_auth.model.EmpresaRolePermissao;
import br.com.unicos.ms_auth.model.Permissao;
import br.com.unicos.ms_auth.model.Role;
import br.com.unicos.ms_auth.repository.EmpresaRolePermissaoRepository;
import br.com.unicos.ms_auth.repository.PermissaoRepository;
import br.com.unicos.ms_auth.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pelas regras de negócio
 * relacionadas ao vínculo Empresa-Role-Permissão.
 *
 * <p>
 * As operações administrativas utilizam paginação.
 * As operações técnicas (autorização) são otimizadas
 * para execução em tempo de autenticação.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class EmpresaRolePermissaoService {

    private final EmpresaRolePermissaoRepository repository;
    private final RoleRepository roleRepository;
    private final PermissaoRepository permissaoRepository;
    private final EmpresaRolePermissaoMapper mapper;

    // ============================================================
    // CREATE
    // ============================================================

    public EmpresaRolePermissaoResponse criar(EmpresaRolePermissaoRequest request) {

        if (repository.existsByEmpresaIdAndRole_IdAndPermissao_Id(
                request.empresaId(),
                request.roleId(),
                request.permissaoId()
        )) {
            throw new IllegalArgumentException("Já existe vínculo entre empresa, role e permissão informados.");
        }

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada"));

        Permissao permissao = permissaoRepository.findById(request.permissaoId())
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada"));

        EmpresaRolePermissao entity = EmpresaRolePermissao.builder()
                .empresaId(request.empresaId())
                .role(role)
                .permissao(permissao)
                .ativo(true)
                .build();

        return mapper.toResponse(repository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    public EmpresaRolePermissaoResponse alterarStatus(Long id, boolean ativo) {

        EmpresaRolePermissao entity = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Vínculo Empresa-Role-Permissão não encontrado")
                );

        entity.setAtivo(ativo);
        return mapper.toResponse(repository.save(entity));
    }

    // ============================================================
    // DELETE
    // ============================================================

    public void remover(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Vínculo Empresa-Role-Permissão não encontrado");
        repository.deleteById(id);
    }

    // ============================================================
    // LISTAGENS ADMINISTRATIVAS (PAGINADAS)
    // ============================================================

    public Page<EmpresaRolePermissaoListDTO> listarPorEmpresa(Long empresaId, Pageable pageable) {
        Page<Long> pageIds = repository.listarIdsPorEmpresa(empresaId, pageable);

        if (pageIds.isEmpty())
            return Page.empty(pageable);

        List<EmpresaRolePermissao> entidades = repository.buscarComRoleEPermissaoPorIds(pageIds.getContent());

        return new PageImpl<>(
                entidades.stream()
                        .map(mapper::toListDTO)
                        .toList(),
                pageable,
                pageIds.getTotalElements()
        );
    }

    public Page<EmpresaRolePermissaoListDTO> listarAtivosPorEmpresa(Long empresaId, Pageable pageable) {
        Page<Long> pageIds = repository.listarIdsAtivosPorEmpresa(empresaId, pageable);

        if (pageIds.isEmpty())
            return Page.empty(pageable);

        List<EmpresaRolePermissao> entidades = repository.buscarComRoleEPermissaoPorIds(pageIds.getContent());

        return new PageImpl<>(
                entidades.stream()
                        .map(mapper::toListDTO)
                        .toList(),
                pageable,
                pageIds.getTotalElements()
        );
    }
}
