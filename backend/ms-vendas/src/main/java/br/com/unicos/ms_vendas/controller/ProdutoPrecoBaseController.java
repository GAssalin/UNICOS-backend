package br.com.unicos.ms_vendas.controller;

import br.com.unicos.ms_vendas.dto.produtoprecobase.ProdutoPrecoBaseCreateRequest;
import br.com.unicos.ms_vendas.dto.produtoprecobase.ProdutoPrecoBaseResponse;
import br.com.unicos.ms_vendas.dto.produtoprecobase.ProdutoPrecoBaseUpdateRequest;
import br.com.unicos.ms_vendas.service.ProdutoPrecoBaseService;
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
@RequestMapping("/v1/vendas/precos-base")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Preço Base do Produto",
        description = "Endpoints para gerenciamento do preço base de produtos (tenant)."
)
public class ProdutoPrecoBaseController {

    private final ProdutoPrecoBaseService produtoPrecoBaseService;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar preço base do produto",
            description = "Cria o preço base de um produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Preço base criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoPrecoBaseResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ProdutoPrecoBaseResponse> criar(
            @RequestBody @Validated ProdutoPrecoBaseCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoPrecoBaseService.criar(request));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar preço base do produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Preço atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoPrecoBaseResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Preço não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoPrecoBaseResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Validated ProdutoPrecoBaseUpdateRequest request
    ) {
        return ResponseEntity.ok(produtoPrecoBaseService.atualizar(id, request));
    }

    // ============================================================
    // GET
    // ============================================================

    @Operation(
            summary = "Buscar preço base por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoPrecoBaseResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Preço não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoPrecoBaseResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(produtoPrecoBaseService.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar preço base por produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoPrecoBaseResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Preço não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<ProdutoPrecoBaseResponse> buscarPorProduto(
            @PathVariable Long produtoId
    ) {
        return ResponseEntity.ok(produtoPrecoBaseService.buscarPorProduto(produtoId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Operation(
            summary = "Listar preços base",
            description = "Lista todos os preços base do tenant de forma paginada.",
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
    public ResponseEntity<Page<ProdutoPrecoBaseResponse>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(produtoPrecoBaseService.listar(pageable));
    }

    @Operation(
            summary = "Listar preços base por status",
            description = "Lista preços base filtrando pelo status ativo/inativo.",
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
    public ResponseEntity<Page<ProdutoPrecoBaseResponse>> listarPorAtivo(
            @PathVariable Boolean ativo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(produtoPrecoBaseService.listarPorAtivo(ativo, pageable));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover preço base por ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Preço removido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Preço não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id
    ) {
        produtoPrecoBaseService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Remover preço base por produto",
            description = "Remove o preço base vinculado ao produto.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Preço removido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/produto/{produtoId}")
    public ResponseEntity<Void> removerPorProduto(
            @PathVariable Long produtoId
    ) {
        produtoPrecoBaseService.removerPorProduto(produtoId);
        return ResponseEntity.noContent().build();
    }
}
