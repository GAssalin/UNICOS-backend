package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.permissao.PermissaoRequest;
import br.com.unicos.ms_auth.dto.permissao.PermissaoResponse;
import br.com.unicos.ms_auth.service.PermissaoService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/permissoes")
@RequiredArgsConstructor
@Tag(
        name = "Permissões",
        description = "Endpoints para criação, edição, busca e exclusão de permissões do sistema."
)
public class PermissaoController {

    private final PermissaoService service;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar nova permissão",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Permissão criada com sucesso",
                            content = @Content(schema = @Schema(implementation = PermissaoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "401", description = "Não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PERMISSAO_CRIAR')")
    @PostMapping
    public ResponseEntity<PermissaoResponse> criar(@Valid @RequestBody PermissaoRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.salvar(request));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar permissão",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Permissão atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = PermissaoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Permissão não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PERMISSAO_EDITAR')")
    @PutMapping("/{id}")
    public ResponseEntity<PermissaoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody PermissaoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // ============================================================
    // GET BY ID
    // ============================================================

    @Operation(
            summary = "Buscar permissão por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Permissão encontrada",
                            content = @Content(schema = @Schema(implementation = PermissaoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Permissão não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PERMISSAO_LISTAR')")
    @GetMapping("/{id}")
    public ResponseEntity<PermissaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // ============================================================
    // LISTAGEM PAGINADA
    // ============================================================

    @Operation(
            summary = "Listar permissões (paginado)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista paginada de permissões",
                            content = @Content(schema = @Schema(implementation = PermissaoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PERMISSAO_LISTAR')")
    @GetMapping
    public ResponseEntity<Page<PermissaoResponse>> listar(@RequestParam(required = false) String nome, Pageable pageable) {
        return ResponseEntity.ok(service.listar(nome, pageable));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Excluir permissão",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Permissão excluída"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para excluir"),
                    @ApiResponse(responseCode = "404", description = "Permissão não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PERMISSAO_EXCLUIR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
