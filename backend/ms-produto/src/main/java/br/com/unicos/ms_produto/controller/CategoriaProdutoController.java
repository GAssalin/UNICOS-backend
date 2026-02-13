package br.com.unicos.ms_produto.controller;

import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoCreateRequest;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoResponse;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoResumoResponse;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoUpdateRequest;
import br.com.unicos.ms_produto.service.CategoriaProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/produtos/categorias")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Categorias de Produto",
        description = "Endpoints para gerenciamento de categorias de produto (tenant)."
)
public class CategoriaProdutoController {

    private final CategoriaProdutoService categoriaProdutoService;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar categoria de produto",
            description = "Cria uma nova categoria de produto. O nome deve ser único dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Categoria criada com sucesso",
                            content = @Content(schema = @Schema(implementation = CategoriaProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<CategoriaProdutoResponse> criar(
            @RequestBody @Validated CategoriaProdutoCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoriaProdutoService.criar(request));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar categoria de produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Categoria atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = CategoriaProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProdutoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Validated CategoriaProdutoUpdateRequest request
    ) {
        return ResponseEntity.ok(categoriaProdutoService.atualizar(id, request));
    }

    // ============================================================
    // GET
    // ============================================================

    @Operation(
            summary = "Buscar categoria por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = CategoriaProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaProdutoResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(categoriaProdutoService.buscarPorId(id));
    }

    // ============================================================
    // LIST (PAGINATED)
    // ============================================================

    @Operation(
            summary = "Listar categorias de produto",
            description = "Lista categorias de produto do tenant atual de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<CategoriaProdutoResumoResponse>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(categoriaProdutoService.listar(pageable));
    }

    @Operation(
            summary = "Listar categorias por categoria pai",
            description = "Lista categorias filhas de uma categoria pai (paginado).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pai/{categoriaPaiId}")
    public ResponseEntity<Page<CategoriaProdutoResumoResponse>> listarPorCategoriaPai(
            @PathVariable Long categoriaPaiId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(categoriaProdutoService.listarPorCategoriaPai(categoriaPaiId, pageable));
    }

    // ============================================================
    // STATUS
    // ============================================================

    @Operation(
            summary = "Ativar categoria de produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Categoria ativada com sucesso",
                            content = @Content(schema = @Schema(implementation = CategoriaProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para ativar"),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}/ativar")
    public ResponseEntity<CategoriaProdutoResponse> ativar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(categoriaProdutoService.ativar(id));
    }

    @Operation(
            summary = "Inativar categoria de produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Categoria inativada com sucesso",
                            content = @Content(schema = @Schema(implementation = CategoriaProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para inativar"),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}/inativar")
    public ResponseEntity<CategoriaProdutoResponse> inativar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(categoriaProdutoService.inativar(id));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover categoria de produto",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Categoria removida"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id
    ) {
        categoriaProdutoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
