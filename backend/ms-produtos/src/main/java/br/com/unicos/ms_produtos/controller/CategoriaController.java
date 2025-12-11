package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.categoria.CategoriaListDTO;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaRequest;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaResponse;
import br.com.unicos.ms_produtos.service.CategoriaService;
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
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento das categorias de produtos.
 */
@RestController
@RequestMapping("/v1/categorias")
@RequiredArgsConstructor
@Tag(
        name = "Categorias de Produto",
        description = "Gerencia categorias, incluindo criação, atualização, consultas e hierarquia de categorias."
)
public class CategoriaController {

    private final CategoriaService categoriaService;

    // ============================================================
    // Criar categoria
    // ============================================================

    @PreAuthorize("hasAuthority('CATEGORIA_CRIAR')")
    @Operation(
            summary = "Criar nova categoria",
            description = "Registra uma nova categoria, com possibilidade de atribuir uma categoria pai.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Categoria criada com sucesso",
                            content = @Content(schema = @Schema(implementation = CategoriaResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos enviados")
            }
    )
    @PostMapping
    public ResponseEntity<CategoriaResponse> salvar(
            @Valid @RequestBody CategoriaRequest request) {

        CategoriaResponse response = categoriaService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // Atualizar categoria
    // ============================================================

    @PreAuthorize("hasAuthority('CATEGORIA_EDITAR')")
    @Operation(
            summary = "Atualizar categoria",
            description = "Atualiza os dados de uma categoria existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Categoria atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = CategoriaResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request) {

        CategoriaResponse response = categoriaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @PreAuthorize("hasAuthority('CATEGORIA_LISTAR')")
    @Operation(
            summary = "Buscar categoria por ID",
            description = "Retorna as informações completas de uma categoria específica.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Categoria encontrada",
                            content = @Content(schema = @Schema(implementation = CategoriaResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Long id) {

        Optional<CategoriaResponse> categoria = categoriaService.buscarPorId(id);

        return categoria
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listagem detalhada
    // ============================================================

    @PreAuthorize("hasAuthority('CATEGORIA_LISTAR')")
    @Operation(
            summary = "Listar todas as categorias (detalhadas)",
            description = "Retorna todas as categorias com informações completas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = CategoriaResponse.class)
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    // ============================================================
    // Listagem simplificada
    // ============================================================

    @PreAuthorize("hasAuthority('CATEGORIA_LISTAR')")
    @Operation(
            summary = "Listar categorias (modo simplificado)",
            description = "Retorna categorias com apenas informações resumidas como ID e nome.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = CategoriaListDTO.class)
                                    )
                            )
                    )
            }
    )
    @GetMapping("/simples")
    public ResponseEntity<List<CategoriaListDTO>> listarSimples() {
        return ResponseEntity.ok(categoriaService.listarSimples());
    }

    // ============================================================
    // Buscar por nome
    // ============================================================

    @PreAuthorize("hasAuthority('CATEGORIA_LISTAR')")
    @Operation(
            summary = "Buscar categorias por nome",
            description = "Pesquisa categorias cujo nome contenha o texto informado (case-insensitive).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Categorias encontradas",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = CategoriaResponse.class))
                            )
                    )
            }
    )
    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaResponse>> buscarPorNome(
            @RequestParam String nome) {

        return ResponseEntity.ok(categoriaService.buscarPorNome(nome));
    }

    // ============================================================
    // Deletar categoria
    // ============================================================

    @PreAuthorize("hasAuthority('CATEGORIA_EXCLUIR')")
    @Operation(
            summary = "Excluir categoria",
            description = "Remove uma categoria pelo ID informado.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Categoria removida"),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // Verificar existência por nome
    // ============================================================

    @PreAuthorize("hasAuthority('CATEGORIA_LISTAR')")
    @Operation(
            summary = "Verificar existência de categoria por nome",
            description = "Retorna true se existir uma categoria com o nome informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resultado retornado com sucesso",
                            content = @Content(schema = @Schema(implementation = Boolean.class))
                    )
            }
    )
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existePorNome(@RequestParam String nome) {
        return ResponseEntity.ok(categoriaService.existePorNome(nome));
    }
}
