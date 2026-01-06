package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoListDTO;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoRequest;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoResponse;
import br.com.unicos.ms_produtos.service.interfaces.FornecedorProdutoService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/fornecedores-produtos")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Fornecedor-Produto",
        description = "Gerencia vínculos entre fornecedores e produtos, com atualizações, consultas e regras específicas."
)
public class FornecedorProdutoController {

    private final FornecedorProdutoService fornecedorProdutoService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Criar vínculo fornecedor-produto",
            description = "Registra um novo vínculo entre fornecedor e produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Vínculo criado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = FornecedorProdutoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'FORNECEDOR_PRODUTO_CRIAR')")
    @PostMapping
    public ResponseEntity<FornecedorProdutoResponse> salvar(
            @Valid @RequestBody FornecedorProdutoRequest request
    ) {
        FornecedorProdutoResponse response =
                fornecedorProdutoService.salvar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar vínculo fornecedor-produto",
            description = "Atualiza os dados completos de um vínculo existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Vínculo atualizado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = FornecedorProdutoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'FORNECEDOR_PRODUTO_ATUALIZAR')")
    @PutMapping("/{id}")
    public ResponseEntity<FornecedorProdutoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FornecedorProdutoRequest request
    ) {
        return ResponseEntity.ok(fornecedorProdutoService.atualizar(id, request));
    }

    // =============================================================
    // PATCH – PREÇO DE CUSTO
    // =============================================================

    @Operation(
            summary = "Atualizar preço de custo",
            description = "Atualiza exclusivamente o preço de custo do vínculo fornecedor-produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Preço de custo atualizado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = FornecedorProdutoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'FORNECEDOR_PRODUTO_ATUALIZAR')")
    @PatchMapping("/{id}/preco-custo")
    public ResponseEntity<FornecedorProdutoResponse> atualizarPrecoCusto(
            @PathVariable Long id,
            @RequestParam BigDecimal novoPrecoCusto
    ) {
        FornecedorProdutoResponse response =
                fornecedorProdutoService.atualizarPrecoCusto(id, novoPrecoCusto);
        return ResponseEntity.ok(response);
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar vínculo por ID",
            description = "Retorna os dados completos de um vínculo fornecedor-produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = FornecedorProdutoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'FORNECEDOR_PRODUTO_VISUALIZAR')")
    @GetMapping("/{id}")
    public ResponseEntity<FornecedorProdutoResponse> buscarPorId(@PathVariable Long id) {
        Optional<FornecedorProdutoResponse> resultado =
                fornecedorProdutoService.buscarPorId(id);
        return resultado
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar todos os vínculos fornecedor-produto",
            description = "Retorna todos os vínculos cadastrados.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = FornecedorProdutoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'FORNECEDOR_PRODUTO_LISTAR')")
    @GetMapping
    public ResponseEntity<List<FornecedorProdutoResponse>> listarTodos() {
        return ResponseEntity.ok(fornecedorProdutoService.listarTodos());
    }

    @Operation(
            summary = "Listar vínculos por produto",
            description = "Retorna os vínculos associados ao produto informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = FornecedorProdutoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'FORNECEDOR_PRODUTO_LISTAR')")
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<FornecedorProdutoListDTO>> listarPorProduto(
            @PathVariable Long produtoId
    ) {
        return ResponseEntity.ok(fornecedorProdutoService.listarPorProduto(produtoId));
    }

    @Operation(
            summary = "Listar vínculos por fornecedor",
            description = "Retorna os vínculos associados ao fornecedor informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = FornecedorProdutoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'FORNECEDOR_PRODUTO_LISTAR')")
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<List<FornecedorProdutoListDTO>> listarPorFornecedor(
            @PathVariable Long fornecedorId
    ) {
        return ResponseEntity.ok(fornecedorProdutoService.listarPorFornecedor(fornecedorId));
    }

    @Operation(
            summary = "Verificar existência de vínculo fornecedor-produto",
            description = "Retorna true se existir vínculo entre fornecedor e produto.",
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
    @PreAuthorize("hasPermission(null, 'FORNECEDOR_PRODUTO_LISTAR')")
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existeVinculo(
            @RequestParam Long fornecedorId,
            @RequestParam Long produtoId
    ) {
        return ResponseEntity.ok(fornecedorProdutoService.existeVinculo(fornecedorId, produtoId));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover vínculo fornecedor-produto",
            description = "Remove um vínculo pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vínculo removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'FORNECEDOR_PRODUTO_REMOVER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        fornecedorProdutoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
