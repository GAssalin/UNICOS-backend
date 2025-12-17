package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoListDTO;
import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoRequest;
import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoResponse;
import br.com.unicos.ms_auth.service.EmpresaRolePermissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelos endpoints relacionados à entidade EmpresaRolePermissao.
 */
@RestController
@RequestMapping("/v1/empresa-role-permissao")
@RequiredArgsConstructor
@Tag(
        name = "EmpresaRolePermissao",
        description = "Endpoints relacionados aos vínculos entre empresa, role e permissões."
)
public class EmpresaRolePermissaoController {

    private final EmpresaRolePermissaoService service;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar vínculo entre empresa, role e permissão"
    )
    @PostMapping
    public ResponseEntity<EmpresaRolePermissaoResponse> criar(
            @Valid @RequestBody EmpresaRolePermissaoRequest request
    ) {
        return ResponseEntity
                .status(201)
                .body(service.criar(request));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Alterar status do vínculo"
    )
    @PutMapping("/{id}/status")
    public ResponseEntity<EmpresaRolePermissaoResponse> alterarStatus(
            @PathVariable Long id,
            @RequestParam boolean ativo
    ) {
        return ResponseEntity.ok(service.alterarStatus(id, ativo));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover vínculo"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // LISTAGENS ADMINISTRATIVAS (PAGINADAS)
    // ============================================================

    @Operation(
            summary = "Listar vínculos ativos por empresa (paginado)"
    )
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<Page<EmpresaRolePermissaoListDTO>> listarAtivosPorEmpresa(
            @PathVariable Long empresaId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                service.listarAtivosPorEmpresa(empresaId, pageable)
        );
    }

    @Operation(
            summary = "Listar todos os vínculos por empresa (paginado)"
    )
    @GetMapping("/empresa/{empresaId}/todos")
    public ResponseEntity<Page<EmpresaRolePermissaoListDTO>> listarPorEmpresa(
            @PathVariable Long empresaId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                service.listarPorEmpresa(empresaId, pageable)
        );
    }
}
