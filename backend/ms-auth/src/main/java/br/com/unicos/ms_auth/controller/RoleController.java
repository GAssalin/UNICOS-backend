package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.role.RoleRequest;
import br.com.unicos.ms_auth.dto.role.RoleResponse;
import br.com.unicos.ms_auth.service.RoleService;
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
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final RoleService roleService;

    @Operation(
            summary = "Criar novo papel (Role)",
            description = "Cria um novo papel no sistema, que poderá conter permissões e ser associado a usuários.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Role criado com sucesso", content = @Content(schema = @Schema(implementation = RoleResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos enviados", content = @Content)
            }
    )
    @PreAuthorize("hasPermission(null, 'ROLE_CRIAR')")
    @PostMapping
    public ResponseEntity<RoleResponse> criar(@Valid @RequestBody RoleRequest request) {
        RoleResponse response = roleService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Atualizar papel existente",
            description = "Atualiza os dados de um papel já cadastrado no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role atualizado com sucesso", content = @Content(schema = @Schema(implementation = RoleResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Role não encontrado", content = @Content)
            }
    )
    @PreAuthorize("hasPermission(null, 'ROLE_EDITAR')")
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> atualizar(@PathVariable Long id, @Valid @RequestBody RoleRequest request) {
        RoleResponse response = roleService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Buscar papel por ID",
            description = "Consulta os dados de um papel específico com base no ID informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role encontrado", content = @Content(schema = @Schema(implementation = RoleResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Role não encontrado", content = @Content)
            }
    )
    @PreAuthorize("hasPermission(null, 'ROLE_LISTAR')")
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> buscarPorId(@PathVariable Long id) {
        RoleResponse response = roleService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Listar todos os papéis",
            description = "Retorna todos os papéis cadastrados no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoleResponse.class))))
            }
    )
    @PreAuthorize("hasPermission(null, 'ROLE_LISTAR')")
    @GetMapping
    public ResponseEntity<List<RoleResponse>> listarTodos() {
        List<RoleResponse> lista = roleService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @Operation(
            summary = "Deletar papel",
            description = "Remove um papel do sistema de forma permanente.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Role removido com sucesso", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Role não encontrado", content = @Content)
            }
    )
    @PreAuthorize("hasPermission(null, 'ROLE_EXCLUIR')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        roleService.deletar(id);
    }
}
