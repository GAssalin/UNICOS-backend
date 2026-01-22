package br.com.unicos.ms_permissao.controller;

import br.com.unicos.ms_permissao.dto.role_permissao.RolePermissaoListDTO;
import br.com.unicos.ms_permissao.dto.role_permissao.RolePermissaoRequest;
import br.com.unicos.ms_permissao.dto.role_permissao.RolePermissaoResponse;
import br.com.unicos.ms_permissao.service.RolePermissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/role-permissao")
@RequiredArgsConstructor
@Tag(
        name = "RolePermissão",
        description = "Endpoints relacionados aos vínculos entre roles e permissões por empresa."
)
public class RolePermissaoController {

    private final RolePermissaoService service;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar vínculo entre empresa, role e permissão",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Vínculo criado com sucesso",
                            content = @Content(schema = @Schema(implementation = RolePermissaoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "404", description = "Role ou permissão não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<RolePermissaoResponse> criar(@Valid @RequestBody RolePermissaoRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.criar(request));
    }

    // ============================================================
    // UPDATE STATUS
    // ============================================================

    @Operation(
            summary = "Alterar status do vínculo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Status alterado com sucesso",
                            content = @Content(schema = @Schema(implementation = RolePermissaoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}/status")
    public ResponseEntity<RolePermissaoResponse> alterarStatus(
            @PathVariable Long id,
            @RequestParam boolean ativo
    ) {
        return ResponseEntity.ok(service.alterarStatus(id, ativo));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover vínculo",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vínculo removido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // LISTAGEM ATIVOS (PAGINADA)
    // ============================================================

    @Operation(
            summary = "Listar vínculos ativos por empresa (paginado)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista paginada de vínculos ativos"
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<Page<RolePermissaoListDTO>> listarAtivosPorEmpresa(
            @PathVariable Long empresaId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarAtivosPorEmpresa(empresaId, pageable));
    }

    // ============================================================
    // LISTAGEM TODOS (PAGINADA)
    // ============================================================

    @Operation(
            summary = "Listar todos os vínculos por empresa (paginado)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista paginada de vínculos"
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/empresa/{empresaId}/todos")
    public ResponseEntity<Page<RolePermissaoListDTO>> listarPorEmpresa(
            @PathVariable Long empresaId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorEmpresa(empresaId, pageable));
    }
}
