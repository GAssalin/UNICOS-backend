package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoListDTO;
import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoRequest;
import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoResponse;
import br.com.unicos.ms_auth.service.interfaces.EmpresaRolePermissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelos endpoints relacionados à entidade EmpresaRolePermissao.
 */
@RestController
@RequestMapping("/v1/empresa-role-permissao")
@RequiredArgsConstructor
@Tag(name = "EmpresaRolePermissao", description = "Endpoints relacionados aos vínculos entre empresa, role e permissões.")
public class EmpresaRolePermissaoController {

    private final EmpresaRolePermissaoService empresaRolePermissaoService;

    // ============================================================
    // CREATE: Criar vínculo entre empresa, role e permissão
    // ============================================================
    @Operation(
            summary = "Criar vínculo entre empresa, role e permissão",
            description = "Cria um vínculo entre uma empresa, um papel (role) e uma permissão.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Vínculo criado com sucesso", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<EmpresaRolePermissaoResponse> criar(
            @Valid @RequestBody EmpresaRolePermissaoRequest request
    ) {
        EmpresaRolePermissaoResponse response = empresaRolePermissaoService.criar(request);
        return ResponseEntity.status(201).body(response);
    }

    // ============================================================
    // UPDATE: Alterar status do vínculo
    // ============================================================
    @Operation(
            summary = "Alterar status do vínculo",
            description = "Altera o status (ativo/inativo) de um vínculo entre empresa, role e permissão.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status alterado com sucesso", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaRolePermissaoResponse> alterarStatus(
            @PathVariable Long id,
            @RequestParam Boolean ativo
    ) {
        EmpresaRolePermissaoResponse response = empresaRolePermissaoService.alterarStatus(id, ativo);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // DELETE: Remover vínculo
    // ============================================================
    @Operation(
            summary = "Remover vínculo",
            description = "Remove o vínculo entre uma empresa, um papel (role) e uma permissão.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vínculo removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        empresaRolePermissaoService.remover(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // LISTAR: Listar todos os vínculos ativos de uma empresa
    // ============================================================
    @Operation(
            summary = "Listar vínculos ativos por empresa",
            description = "Retorna todos os vínculos ativos entre empresas, roles e permissões.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content)
            }
    )
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<EmpresaRolePermissaoListDTO>> listarAtivosPorEmpresa(@PathVariable Long empresaId) {
        List<EmpresaRolePermissaoListDTO> response = empresaRolePermissaoService.listarAtivosPorEmpresa(empresaId);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // LISTAR: Listar todos os vínculos de uma empresa
    // ============================================================
    @Operation(
            summary = "Listar todos os vínculos de uma empresa",
            description = "Retorna todos os vínculos entre empresas, roles e permissões.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content)
            }
    )
    @GetMapping("/empresa/{empresaId}/todos")
    public ResponseEntity<List<EmpresaRolePermissaoListDTO>> listarPorEmpresa(@PathVariable Long empresaId) {
        List<EmpresaRolePermissaoListDTO> response = empresaRolePermissaoService.listarPorEmpresa(empresaId);
        return ResponseEntity.ok(response);
    }
}
