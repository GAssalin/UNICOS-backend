package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.marca.MarcaProdutoCreateRequest;
import br.com.unicos.ms_compras.dto.marca.MarcaProdutoResponse;
import br.com.unicos.ms_compras.dto.marca.MarcaProdutoResumoResponse;
import br.com.unicos.ms_compras.dto.marca.MarcaProdutoUpdateRequest;
import br.com.unicos.ms_compras.service.MarcaProdutoService;
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
@RequestMapping("/v1/vendas/marcas-produto")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Marcas de Produto",
        description = "Endpoints para gerenciamento de marcas de produto (tenant)."
)
public class MarcaProdutoController {

    private final MarcaProdutoService marcaProdutoService;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar marca de produto",
            description = "Cria uma nova marca de produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Marca criada com sucesso",
                            content = @Content(schema = @Schema(implementation = MarcaProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<MarcaProdutoResponse> criar(
            @RequestBody @Validated MarcaProdutoCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(marcaProdutoService.criar(request));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar marca de produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Marca atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = MarcaProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Marca não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<MarcaProdutoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Validated MarcaProdutoUpdateRequest request
    ) {
        return ResponseEntity.ok(marcaProdutoService.atualizar(id, request));
    }

    // ============================================================
    // GET
    // ============================================================

    @Operation(
            summary = "Buscar marca de produto por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = MarcaProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Marca não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MarcaProdutoResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(marcaProdutoService.buscarPorId(id));
    }

    // ============================================================
    // LIST (PAGINATED)
    // ============================================================

    @Operation(
            summary = "Listar marcas de produto",
            description = "Lista marcas de produto de forma paginada.",
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
    public ResponseEntity<Page<MarcaProdutoResumoResponse>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(marcaProdutoService.listar(pageable));
    }

    @Operation(
            summary = "Listar marcas de produto por status",
            description = "Lista marcas filtrando pelo status (ativo/inativo) de forma paginada.",
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
    @GetMapping("/status/{ativo}")
    public ResponseEntity<Page<MarcaProdutoResumoResponse>> listarPorAtivo(
            @PathVariable Boolean ativo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(marcaProdutoService.listarPorAtivo(ativo, pageable));
    }

    // ============================================================
    // STATUS
    // ============================================================

    @Operation(
            summary = "Ativar marca de produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Marca ativada com sucesso",
                            content = @Content(schema = @Schema(implementation = MarcaProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para ativar"),
                    @ApiResponse(responseCode = "404", description = "Marca não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}/ativar")
    public ResponseEntity<MarcaProdutoResponse> ativar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(marcaProdutoService.ativar(id));
    }

    @Operation(
            summary = "Inativar marca de produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Marca inativada com sucesso",
                            content = @Content(schema = @Schema(implementation = MarcaProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para inativar"),
                    @ApiResponse(responseCode = "404", description = "Marca não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}/inativar")
    public ResponseEntity<MarcaProdutoResponse> inativar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(marcaProdutoService.inativar(id));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover marca de produto",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Marca removida"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Marca não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id
    ) {
        marcaProdutoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
