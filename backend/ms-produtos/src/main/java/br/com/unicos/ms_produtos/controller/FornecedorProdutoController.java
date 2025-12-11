package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoListDTO;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoRequest;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoResponse;
import br.com.unicos.ms_produtos.service.FornecedorProdutoService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento dos vínculos
 * entre fornecedores e produtos.
 */
@RestController
@RequestMapping("/v1/fornecedores-produtos")
@RequiredArgsConstructor
@Tag(
        name = "Fornecedor-Produto",
        description = "Gerencia vínculos entre fornecedores e produtos, com atualizações, consultas e regras específicas."
)
public class FornecedorProdutoController {

    private final FornecedorProdutoService fornecedorProdutoService;

    // ============================================================
    // Criar vínculo
    // ============================================================

    @PreAuthorize("hasAuthority('FORNECEDOR_PRODUTO_CRIAR')")
    @Operation(
            summary = "Criar vínculo fornecedor-produto",
            description = "Registra um novo vínculo entre fornecedor e produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Vínculo criado com sucesso",
                            content = @Content(schema = @Schema(implementation = FornecedorProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
            }
    )
    @PostMapping
    public ResponseEntity<FornecedorProdutoResponse> salvar(
            @Valid @RequestBody FornecedorProdutoRequest request) {

        FornecedorProdutoResponse response = fornecedorProdutoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // Atualizar vínculo
    // ============================================================

    @PreAuthorize("hasAuthority('FORNECEDOR_PRODUTO_EDITAR')")
    @Operation(
            summary = "Atualizar vínculo fornecedor-produto",
            description = "Atualiza informações completas do vínculo entre fornecedor e produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Vínculo atualizado",
                            content = @Content(schema = @Schema(implementation = FornecedorProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<FornecedorProdutoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FornecedorProdutoRequest request) {

        FornecedorProdutoResponse response = fornecedorProdutoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Atualizar preço de custo
    // ============================================================

    @PreAuthorize("hasAuthority('FORNECEDOR_PRODUTO_EDITAR')")
    @Operation(
            summary = "Atualizar preço de custo",
            description = "Modifica exclusivamente o preço de custo do vínculo fornecedor-produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Preço atualizado",
                            content = @Content(schema = @Schema(implementation = FornecedorProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
            }
    )
    @PatchMapping("/{id}/preco-custo")
    public ResponseEntity<FornecedorProdutoResponse> atualizarPrecoCusto(
            @PathVariable Long id,
            @RequestParam BigDecimal novoPrecoCusto) {

        FornecedorProdutoResponse response =
                fornecedorProdutoService.atualizarPrecoCusto(id, novoPrecoCusto);

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Deletar vínculo
    // ============================================================

    @PreAuthorize("hasAuthority('FORNECEDOR_PRODUTO_EXCLUIR')")
    @Operation(
            summary = "Excluir vínculo fornecedor-produto",
            description = "Remove o vínculo pelo ID informado.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vínculo removido"),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        fornecedorProdutoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @PreAuthorize("hasAuthority('FORNECEDOR_PRODUTO_LISTAR')")
    @Operation(
            summary = "Buscar vínculo por ID",
            description = "Retorna os dados completos de um vínculo fornecedor-produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Vínculo encontrado",
                            content = @Content(schema = @Schema(implementation = FornecedorProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<FornecedorProdutoResponse> buscarPorId(@PathVariable Long id) {

        Optional<FornecedorProdutoResponse> resultado =
                fornecedorProdutoService.buscarPorId(id);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar todos
    // ============================================================

    @PreAuthorize("hasAuthority('FORNECEDOR_PRODUTO_LISTAR')")
    @Operation(
            summary = "Listar todos os vínculos fornecedor-produto",
            description = "Retorna todos os registros cadastrados.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = FornecedorProdutoResponse.class))
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<FornecedorProdutoResponse>> listarTodos() {
        return ResponseEntity.ok(fornecedorProdutoService.listarTodos());
    }

    // ============================================================
    // Listar por produto
    // ============================================================

    @PreAuthorize("hasAuthority('FORNECEDOR_PRODUTO_LISTAR')")
    @Operation(
            summary = "Listar vínculos por produto",
            description = "Busca todos os vínculos pertencentes ao produto informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = FornecedorProdutoListDTO.class))
                            )
                    )
            }
    )
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<FornecedorProdutoListDTO>> listarPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(fornecedorProdutoService.listarPorProduto(produtoId));
    }

    // ============================================================
    // Listar por fornecedor
    // ============================================================

    @PreAuthorize("hasAuthority('FORNECEDOR_PRODUTO_LISTAR')")
    @Operation(
            summary = "Listar vínculos por fornecedor",
            description = "Busca todos os vínculos pertencentes ao fornecedor informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = FornecedorProdutoListDTO.class))
                            )
                    )
            }
    )
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<List<FornecedorProdutoListDTO>> listarPorFornecedor(@PathVariable Long fornecedorId) {
        return ResponseEntity.ok(fornecedorProdutoService.listarPorFornecedor(fornecedorId));
    }

    // ============================================================
    // Verificar existência
    // ============================================================

    @PreAuthorize("hasAuthority('FORNECEDOR_PRODUTO_LISTAR')")
    @Operation(
            summary = "Verificar existência de vínculo",
            description = "Retorna true/false indicando se há vínculo entre fornecedor e produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resultado encontrado",
                            content = @Content(schema = @Schema(implementation = Boolean.class))
                    )
            }
    )
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existeVinculo(
            @RequestParam Long fornecedorId,
            @RequestParam Long produtoId) {

        return ResponseEntity.ok(
                fornecedorProdutoService.existeVinculo(fornecedorId, produtoId)
        );
    }
}
