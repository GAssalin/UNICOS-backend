package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.categoria.CategoriaListDTO;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaRequest;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaResponse;
import br.com.unicos.ms_produtos.service.interfaces.CategoriaService;
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
@RequestMapping("/v1/categorias")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Categorias de Produto",
        description = "Gerencia categorias, incluindo criação, atualização, consultas e hierarquia de categorias."
)
public class CategoriaController {

    private final CategoriaService categoriaService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar categoria",
            description = "Cria uma nova categoria, com possibilidade de vincular uma categoria pai.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Categoria criada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = CategoriaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'CATEGORIA_CRIAR')")
    @PostMapping
    public ResponseEntity<CategoriaResponse> salvar(
            @Valid @RequestBody CategoriaRequest request
    ) {
        CategoriaResponse response = categoriaService.salvar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar categoria",
            description = "Atualiza os dados de uma categoria existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Categoria atualizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = CategoriaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'CATEGORIA_ATUALIZAR')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request
    ) {
        return ResponseEntity.ok(categoriaService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar categoria por ID",
            description = "Retorna as informações completas de uma categoria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = CategoriaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'CATEGORIA_VISUALIZAR')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Long id) {
        Optional<CategoriaResponse> categoria = categoriaService.buscarPorId(id);
        return categoria
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar todas as categorias (detalhado)",
            description = "Retorna todas as categorias com informações completas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = CategoriaResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'CATEGORIA_LISTAR')")
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @Operation(
            summary = "Listar categorias (modo simples)",
            description = "Retorna categorias com informações resumidas (ID e nome).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = CategoriaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'CATEGORIA_LISTAR')")
    @GetMapping("/simples")
    public ResponseEntity<List<CategoriaListDTO>> listarSimples() {
        return ResponseEntity.ok(categoriaService.listarSimples());
    }

    @Operation(
            summary = "Buscar categorias por nome",
            description = "Pesquisa categorias cujo nome contenha o texto informado (ignore case).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = CategoriaResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'CATEGORIA_LISTAR')")
    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaResponse>> buscarPorNome(
            @RequestParam String nome
    ) {
        return ResponseEntity.ok(categoriaService.buscarPorNome(nome));
    }

    @Operation(
            summary = "Verificar existência de categoria por nome",
            description = "Retorna true se existir uma categoria com o nome informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resultado retornado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = Boolean.class)
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'CATEGORIA_LISTAR')")
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existePorNome(@RequestParam String nome) {
        return ResponseEntity.ok(categoriaService.existePorNome(nome));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover categoria",
            description = "Remove uma categoria pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Categoria removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'CATEGORIA_REMOVER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
