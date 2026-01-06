package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoListDTO;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoRequest;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoResponse;
import br.com.unicos.ms_produtos.service.interfaces.AtributoPersonalizadoService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/atributos-personalizados")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Atributos Personalizados",
        description = "Gerencia atributos customizados associados às categorias de produtos do UniCoS."
)
public class AtributoPersonalizadoController {

    private final AtributoPersonalizadoService atributoPersonalizadoService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar atributo personalizado",
            description = "Cria um atributo personalizado vinculado a uma categoria de produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Atributo criado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = AtributoPersonalizadoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'ATRIBUTO_PERSONALIZADO_CRIAR')")
    @PostMapping
    public ResponseEntity<AtributoPersonalizadoResponse> criar(
            @Valid @RequestBody AtributoPersonalizadoRequest request
    ) {
        AtributoPersonalizadoResponse response =
                atributoPersonalizadoService.criar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar atributo personalizado",
            description = "Atualiza os dados de um atributo personalizado existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Atributo atualizado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = AtributoPersonalizadoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Atributo não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'ATRIBUTO_PERSONALIZADO_ATUALIZAR')")
    @PutMapping("/{id}")
    public ResponseEntity<AtributoPersonalizadoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtributoPersonalizadoRequest request
    ) {
        return ResponseEntity.ok(atributoPersonalizadoService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar atributo personalizado por ID",
            description = "Retorna os dados completos de um atributo personalizado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = AtributoPersonalizadoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Atributo não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'ATRIBUTO_PERSONALIZADO_VISUALIZAR')")
    @GetMapping("/{id}")
    public ResponseEntity<AtributoPersonalizadoResponse> buscarPorId(
            @PathVariable Long id
    ) {
        Optional<AtributoPersonalizadoResponse> resultado =
                atributoPersonalizadoService.buscarPorId(id);
        return resultado
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar todos os atributos personalizados",
            description = "Retorna todos os atributos personalizados cadastrados.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = AtributoPersonalizadoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'ATRIBUTO_PERSONALIZADO_LISTAR')")
    @GetMapping
    public ResponseEntity<List<AtributoPersonalizadoListDTO>> listarTodos() {
        return ResponseEntity.ok(atributoPersonalizadoService.listarTodos());
    }

    @Operation(
            summary = "Listar atributos por categoria",
            description = "Retorna os atributos personalizados associados a uma categoria de produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = AtributoPersonalizadoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada ou sem atributos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'ATRIBUTO_PERSONALIZADO_LISTAR')")
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<AtributoPersonalizadoListDTO>> listarPorCategoria(
            @PathVariable Long categoriaId
    ) {
        return ResponseEntity.ok(atributoPersonalizadoService.listarPorCategoria(categoriaId));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover atributo personalizado",
            description = "Remove um atributo personalizado pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Atributo removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Atributo não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'ATRIBUTO_PERSONALIZADO_REMOVER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        atributoPersonalizadoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
