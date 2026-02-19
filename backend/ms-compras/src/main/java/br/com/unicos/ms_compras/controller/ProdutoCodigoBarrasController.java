package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.produtocodigobarras.ProdutoCodigoBarrasCreateRequest;
import br.com.unicos.ms_compras.dto.produtocodigobarras.ProdutoCodigoBarrasResponse;
import br.com.unicos.ms_compras.dto.produtocodigobarras.ProdutoCodigoBarrasUpdateRequest;
import br.com.unicos.ms_compras.service.ProdutoCodigoBarrasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/vendas/codigo-barras")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Códigos de Barras do Produto",
        description = "Endpoints para gerenciamento de códigos de barras vinculados a produtos (tenant)."
)
public class ProdutoCodigoBarrasController {

    private final ProdutoCodigoBarrasService produtoCodigoBarrasService;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar código de barras do produto",
            description = "Cria um novo código de barras para um produto. Se marcado como principal, remove o principal atual do produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Código de barras criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoCodigoBarrasResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ProdutoCodigoBarrasResponse> criar(
            @RequestBody @Validated ProdutoCodigoBarrasCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoCodigoBarrasService.criar(request));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar código de barras do produto",
            description = "Atualiza um código de barras existente. Se marcado como principal, remove o principal atual do produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Código de barras atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoCodigoBarrasResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Código de barras não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoCodigoBarrasResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Validated ProdutoCodigoBarrasUpdateRequest request
    ) {
        return ResponseEntity.ok(produtoCodigoBarrasService.atualizar(id, request));
    }

    // ============================================================
    // GET BY ID
    // ============================================================

    @Operation(
            summary = "Buscar código de barras por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoCodigoBarrasResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Código de barras não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoCodigoBarrasResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(produtoCodigoBarrasService.buscarPorId(id));
    }

    // ============================================================
    // GET BY BARCODE
    // ============================================================

    @Operation(
            summary = "Buscar código de barras pelo valor do código",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoCodigoBarrasResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Código de barras não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/codigo/{codigoBarras}")
    public ResponseEntity<ProdutoCodigoBarrasResponse> buscarPorCodigoBarras(
            @PathVariable String codigoBarras
    ) {
        return ResponseEntity.ok(produtoCodigoBarrasService.buscarPorCodigoBarras(codigoBarras));
    }

    // ============================================================
    // LIST BY PRODUCT
    // ============================================================

    @Operation(
            summary = "Listar códigos de barras por produto",
            description = "Lista todos os códigos de barras cadastrados para um produto no tenant atual.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ProdutoCodigoBarrasResponse.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<ProdutoCodigoBarrasResponse>> listarPorProduto(
            @PathVariable Long produtoId
    ) {
        return ResponseEntity.ok(produtoCodigoBarrasService.listarPorProduto(produtoId));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover código de barras por ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Código de barras removido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Código de barras não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id
    ) {
        produtoCodigoBarrasService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Remover códigos de barras por produto",
            description = "Remove todos os códigos de barras vinculados a um produto no tenant atual.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Códigos de barras removidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/produto/{produtoId}")
    public ResponseEntity<Void> removerPorProduto(
            @PathVariable Long produtoId
    ) {
        produtoCodigoBarrasService.removerPorProduto(produtoId);
        return ResponseEntity.noContent().build();
    }
}
