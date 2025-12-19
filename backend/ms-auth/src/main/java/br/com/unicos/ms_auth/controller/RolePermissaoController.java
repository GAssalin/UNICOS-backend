package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.role_permissao.RolePermissaoListDTO;
import br.com.unicos.ms_auth.dto.role_permissao.RolePermissaoRequest;
import br.com.unicos.ms_auth.dto.role_permissao.RolePermissaoResponse;
import br.com.unicos.ms_auth.service.RolePermissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelos endpoints relacionados à entidade RolePermissao.
 */
@RestController
@RequestMapping("/v1/role-permissao")
@RequiredArgsConstructor
@Tag(
        name = "RolePermissao",
        description = "Endpoints relacionados aos vínculos entre role e permissões."
)
public class RolePermissaoController {

    private final RolePermissaoService service;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(summary = "Criar vínculo entre empresa, role e permissão")
    @PreAuthorize("hasPermission(null, 'EMPRESA_ROLE_PERMISSAO_CRIAR')")
    @PostMapping
    public ResponseEntity<RolePermissaoResponse> criar(@Valid @RequestBody RolePermissaoRequest request) {
        return ResponseEntity
                .status(201)
                .body(service.criar(request));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(summary = "Alterar status do vínculo")
    @PreAuthorize("hasPermission(null, 'EMPRESA_ROLE_PERMISSAO_EDITAR')")
    @PutMapping("/{id}/status")
    public ResponseEntity<RolePermissaoResponse> alterarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
        return ResponseEntity.ok(service.alterarStatus(id, ativo));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(summary = "Remover vínculo")
    @PreAuthorize("hasPermission(null, 'EMPRESA_ROLE_PERMISSAO_EXCLUIR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // LISTAGENS ADMINISTRATIVAS (PAGINADAS)
    // ============================================================

    @Operation(summary = "Listar vínculos ativos por empresa (paginado)")
    @PreAuthorize("hasPermission(null, 'EMPRESA_ROLE_PERMISSAO_LISTAR')")
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<Page<RolePermissaoListDTO>> listarAtivosPorEmpresa(@PathVariable Long empresaId, Pageable pageable) {
        return ResponseEntity.ok(service.listarAtivosPorEmpresa(empresaId, pageable));
    }

    @Operation(summary = "Listar todos os vínculos por empresa (paginado)")
    @PreAuthorize("hasPermission(null, 'EMPRESA_ROLE_PERMISSAO_LISTAR')")
    @GetMapping("/empresa/{empresaId}/todos")
    public ResponseEntity<Page<RolePermissaoListDTO>> listarPorEmpresa(@PathVariable Long empresaId, Pageable pageable) {
        return ResponseEntity.ok(service.listarPorEmpresa(empresaId, pageable));
    }
}
