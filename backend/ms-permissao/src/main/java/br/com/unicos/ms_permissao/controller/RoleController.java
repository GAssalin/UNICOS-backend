package br.com.unicos.ms_permissao.controller;

import br.com.unicos.ms_permissao.dto.role.RoleRequest;
import br.com.unicos.ms_permissao.dto.role.RoleResponse;
import br.com.unicos.ms_permissao.service.PermissaoService;
import br.com.unicos.ms_permissao.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento de papéis (Roles)
 * dentro do módulo de autenticação.
 */
@RestController
@RequestMapping("/v1/roles")
@RequiredArgsConstructor
@Tag(
        name = "Roles",
        description = "Endpoints para criação, edição, listagem e exclusão de papéis do sistema."
)
public class RoleController {

    private final PermissaoService permissaoService;
    private final RoleService roleService;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar novo papel (Role)",
            description = "Cria um novo papel no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Role criado com sucesso",
                            content = @Content(schema = @Schema(implementation = RoleResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<RoleResponse> criar(@Valid @RequestBody RoleRequest request) {
        if (permissaoService.usuarioPossuiPermissao("ROLE_CRIAR")) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(roleService.salvar(request));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar papel existente",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Role atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = RoleResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Role não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequest request
    ) {
        if (permissaoService.usuarioPossuiPermissao("ROLE_EDITAR"))
            return ResponseEntity.ok(roleService.atualizar(id, request));
        else
            return ResponseEntity.status(403).build();
    }

    // ============================================================
    // GET BY ID
    // ============================================================

    @Operation(
            summary = "Buscar papel por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Role encontrado",
                            content = @Content(schema = @Schema(implementation = RoleResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Role não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> buscarPorId(@PathVariable Long id) {
        if (permissaoService.usuarioPossuiPermissao("ROLE_LISTAR"))
            return ResponseEntity.ok(roleService.buscarPorId(id));
        else
            return ResponseEntity.status(403).build();
    }

    // ============================================================
    // LIST ALL
    // ============================================================

    @Operation(
            summary = "Listar todos os papéis",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de roles",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = RoleResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<List<RoleResponse>> listarTodos() {
        if (permissaoService.usuarioPossuiPermissao("ROLE_LISTAR"))
            return ResponseEntity.ok(roleService.listarTodos());
        else
            return ResponseEntity.status(403).build();
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Excluir papel",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Role excluído"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para excluir"),
                    @ApiResponse(responseCode = "404", description = "Role não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (permissaoService.usuarioPossuiPermissao("ROLE_EXCLUIR")) {
            roleService.deletar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
