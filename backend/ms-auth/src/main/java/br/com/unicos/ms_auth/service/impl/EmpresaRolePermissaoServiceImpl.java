package br.com.unicos.ms_auth.service.impl;

import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoListDTO;
import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoRequest;
import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoResponse;
import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoResumoDTO;
import br.com.unicos.ms_auth.model.EmpresaRolePermissao;
import br.com.unicos.ms_auth.model.Permissao;
import br.com.unicos.ms_auth.model.Role;
import br.com.unicos.ms_auth.repository.EmpresaRolePermissaoRepository;
import br.com.unicos.ms_auth.repository.PermissaoRepository;
import br.com.unicos.ms_auth.repository.RoleRepository;
import br.com.unicos.ms_auth.service.interfaces.EmpresaRolePermissaoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação do serviço responsável pelas regras de negócio
 * relacionadas à entidade EmpresaRolePermissao.
 */
@Service
@RequiredArgsConstructor
public class EmpresaRolePermissaoServiceImpl implements EmpresaRolePermissaoService {

    private final EmpresaRolePermissaoRepository repository;
    private final RoleRepository roleRepository;
    private final PermissaoRepository permissaoRepository;

    // ============================================================
    // CREATE
    // ============================================================

    @Override
    public EmpresaRolePermissaoResponse criar(EmpresaRolePermissaoRequest request) {

        if (repository.existsByEmpresaIdAndRole_IdAndPermissao_Id(
                request.empresaId(),
                request.roleId(),
                request.permissaoId())) {

            throw new IllegalArgumentException(
                    "Já existe vínculo entre empresa, role e permissão informado."
            );
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

        repository.save(entity);
        return toResponse(entity);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Override
    public EmpresaRolePermissaoResponse alterarStatus(Long id, Boolean ativo) {

        EmpresaRolePermissao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Vínculo Empresa-Role-Permissão não encontrado"
                ));

        entity.setAtivo(ativo);
        repository.save(entity);

        return toResponse(entity);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Override
    public void remover(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Vínculo Empresa-Role-Permissão não encontrado"
            );
        }
        repository.deleteById(id);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Override
    public List<EmpresaRolePermissaoListDTO> listarPorEmpresa(Long empresaId) {
        return repository.listarComRoleEPermissao(empresaId)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    public List<EmpresaRolePermissaoListDTO> listarAtivosPorEmpresa(Long empresaId) {
        return repository.findByEmpresaIdAndAtivoTrue(empresaId)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    // ============================================================
    // MAPPERS
    // ============================================================

    private EmpresaRolePermissaoResponse toResponse(EmpresaRolePermissao e) {
        return new EmpresaRolePermissaoResponse(
                e.getId(),
                e.getEmpresaId(),
                e.getRole().getId(),
                e.getRole().getNome(),
                e.getPermissao().getId(),
                e.getPermissao().getNome(),
                e.getAtivo(),
                e.getCriadoEm(),
                e.getAtualizadoEm()
        );
    }

    private EmpresaRolePermissaoListDTO toListDTO(EmpresaRolePermissao e) {
        return new EmpresaRolePermissaoListDTO(
                e.getId(),
                e.getEmpresaId(),
                e.getRole().getId(),
                e.getRole().getNome(),
                e.getPermissao().getId(),
                e.getPermissao().getNome(),
                e.getAtivo()
        );
    }
}
