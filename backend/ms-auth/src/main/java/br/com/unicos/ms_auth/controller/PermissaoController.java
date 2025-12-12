package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.permissao.PermissaoRequest;
import br.com.unicos.ms_auth.dto.permissao.PermissaoResponse;
import br.com.unicos.ms_auth.service.interfaces.PermissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento das permissões do sistema.
 * <p>
 * Permite operações de criação, atualização, consulta e remoção
 * de permissões granulares utilizadas na composição de papéis (roles).
 */
@RestController
@RequestMapping("/v1/permissoes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Permissões",
        description = "Endpoints para criação, edição, busca e exclusão de permissões do sistema."
)
public class PermissaoController {

    private final PermissaoService permissaoService;

    // ============================================================
    // 🔹 Criar nova permissão
    // Permissão necessária: PERMISSAO_GERENCIAR
    // ============================================================
    @PreAuthorize("hasAuthority('PERMISSAO_GERENCIAR')")
    @Operation(
            summary = "Criar nova permissão",
            description = "Cria uma nova permissão granular que poderá ser atribuída a papéis (roles).",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Permissão criada com sucesso",
                            content = @Content(schema = @Schema(implementation = PermissaoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados na requisição",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<PermissaoResponse> criar(@Valid @RequestBody PermissaoRequest request) {
        PermissaoResponse response = permissaoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // 🔹 Atualizar permissão existente
    // Permissão necessária: PERMISSAO_GERENCIAR
    // ============================================================
    @PreAuthorize("hasAuthority('PERMISSAO_GERENCIAR')")
    @Operation(
            summary = "Atualizar permissão",
            description = "Atualiza os dados de uma permissão já cadastrada no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Permissão atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = PermissaoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados na requisição",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Permissão não encontrada",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PermissaoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PermissaoRequest request
    ) {
        PermissaoResponse response = permissaoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // 🔹 Buscar permissão por ID
    // Permissão necessária: PERMISSAO_LISTAR
    // ============================================================
    @PreAuthorize("hasAuthority('PERMISSAO_LISTAR')")
    @Operation(
            summary = "Buscar permissão por ID",
            description = "Retorna os detalhes de uma permissão específica com base no ID informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Permissão encontrada",
                            content = @Content(schema = @Schema(implementation = PermissaoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Permissão não encontrada",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PermissaoResponse> buscarPorId(@PathVariable Long id) {
        PermissaoResponse response = permissaoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // 🔹 Listar todas as permissões
    // Permissão necessária: PERMISSAO_LISTAR
    // ============================================================
    @PreAuthorize("hasAuthority('PERMISSAO_LISTAR')")
    @Operation(
            summary = "Listar todas as permissões",
            description = "Retorna todas as permissões cadastradas no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PermissaoResponse.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<PermissaoResponse>> listarTodas() {
        return ResponseEntity.ok(permissaoService.listarTodas());
    }

    // ============================================================
    // 🔹 Deletar permissão
    // Permissão necessária: PERMISSAO_GERENCIAR
    // ============================================================
    @PreAuthorize("hasAuthority('PERMISSAO_GERENCIAR')")
    @Operation(
            summary = "Deletar permissão",
            description = "Remove uma permissão do sistema de forma permanente.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Permissão removida com sucesso",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Permissão não encontrada",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        permissaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
