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
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @Transactional
    @CircuitBreaker(name = "role-permissao-admin", fallbackMethod = "fallbackAdmin")
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

    @Transactional
    @CircuitBreaker(name = "role-permissao-admin", fallbackMethod = "fallbackAdmin")
    public RolePermissaoResponse alterarStatus(Long id, boolean ativo) {
        RolePermissao entity = findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo Empresa-Role-Permissão não encontrado"));

        entity.setAtivo(ativo);
        return mapper.toResponse(save(entity));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "role-permissao-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long id) {
        if (!rolePermissaoRepository.existsById(id))
            throw new EntityNotFoundException("Vínculo Empresa-Role-Permissão não encontrado");
        rolePermissaoRepository.deleteById(id);
    }

    // ============================================================
    // LISTAGENS ADMINISTRATIVAS
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "role-permissao-admin", fallbackMethod = "fallbackAdminPage")
    public Page<RolePermissaoListDTO> listarPorEmpresa(Long empresaId, Pageable pageable) {
        Page<RolePermissao> entidades =
                rolePermissaoRepository.findAllByEmpresaId(
                        TenantContext.getEmpresaId(),
                        Pageable.unpaged()
                );

        return new PageImpl<>(
                entidades.stream().map(mapper::toListDTO).toList(),
                pageable,
                entidades.getTotalElements()
        );
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "role-permissao-admin", fallbackMethod = "fallbackAdminPage")
    public Page<RolePermissaoListDTO> listarAtivosPorEmpresa(Long empresaId, Pageable pageable) {
        List<RolePermissao> ativos =
                rolePermissaoRepository.findByAtivoTrueAndEmpresaId(empresaId);

        return new PageImpl<>(
                ativos.stream().map(mapper::toListDTO).toList(),
                pageable,
                ativos.size()
        );
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private RolePermissaoResponse fallbackAdmin(Object request, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de vínculo Role-Permissão temporariamente indisponível");
    }

    private Page<RolePermissaoListDTO> fallbackAdminPage(Long empresaId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de vínculo Role-Permissão temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de vínculo Role-Permissão temporariamente indisponível");
    }
}
