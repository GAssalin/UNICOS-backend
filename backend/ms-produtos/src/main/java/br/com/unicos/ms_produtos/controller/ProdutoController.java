package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.produto.ProdutoRequest;
import br.com.unicos.ms_produtos.dto.produto.ProdutoResponse;
import br.com.unicos.ms_produtos.service.interfaces.ProdutoService;
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
@RequestMapping("/v1/produtos")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Produtos",
        description = "Gerencia o cadastro, atualização, filtros, ativação, inativação e alteração de preço de produtos."
)
public class ProdutoController {

    private final ProdutoService produtoService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar produto",
            description = "Cadastra um novo produto com informações completas.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Produto criado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = ProdutoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PRODUTO_CRIAR')")
    @PostMapping
    public ResponseEntity<ProdutoResponse> salvar(
            @Valid @RequestBody ProdutoRequest request
    ) {
        ProdutoResponse response = produtoService.salvar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza completamente os dados de um produto existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto atualizado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = ProdutoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PRODUTO_ATUALIZAR')")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request
    ) {
        return ResponseEntity.ok(produtoService.atualizar(id, request));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover produto",
            description = "Remove um produto pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PRODUTO_REMOVER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna os dados de um produto específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = ProdutoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PRODUTO_VISUALIZAR')")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        Optional<ProdutoResponse> resultado = produtoService.buscarPorId(id);
        return resultado
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar todos os produtos",
            description = "Retorna todos os produtos cadastrados.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ProdutoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PRODUTO_LISTAR')")
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @Operation(
            summary = "Buscar produtos por nome",
            description = "Busca produtos cujo nome contenha o termo informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ProdutoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PRODUTO_LISTAR')")
    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoResponse>> buscarPorNome(
            @RequestParam String nome
    ) {
        return ResponseEntity.ok(produtoService.buscarPorNome(nome));
    }

    @Operation(
            summary = "Buscar produto por SKU",
            description = "Retorna um produto a partir do seu código SKU.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = ProdutoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PRODUTO_VISUALIZAR')")
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProdutoResponse> buscarPorSku(@PathVariable String sku) {
        Optional<ProdutoResponse> resultado = produtoService.buscarPorSku(sku);
        return resultado
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Listar produtos por categoria",
            description = "Retorna produtos associados à categoria informada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ProdutoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PRODUTO_LISTAR')")
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProdutoResponse>> listarPorCategoria(
            @PathVariable Long categoriaId
    ) {
        return ResponseEntity.ok(produtoService.listarPorCategoria(categoriaId));
    }

    @Operation(
            summary = "Listar produtos por marca",
            description = "Retorna produtos associados à marca informada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ProdutoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PRODUTO_LISTAR')")
    @GetMapping("/marca/{marcaId}")
    public ResponseEntity<List<ProdutoResponse>> listarPorMarca(
            @PathVariable Long marcaId
    ) {
        return ResponseEntity.ok(produtoService.listarPorMarca(marcaId));
    }

    @Operation(
            summary = "Listar produtos ativos",
            description = "Retorna apenas produtos ativos.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ProdutoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PRODUTO_LISTAR')")
    @GetMapping("/ativos")
    public ResponseEntity<List<ProdutoResponse>> listarAtivos() {
        return ResponseEntity.ok(produtoService.listarAtivos());
    }

    @Operation(
            summary = "Listar produtos inativos",
            description = "Retorna apenas produtos inativos.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ProdutoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PRODUTO_LISTAR')")
    @GetMapping("/inativos")
    public ResponseEntity<List<ProdutoResponse>> listarInativos() {
        return ResponseEntity.ok(produtoService.listarInativos());
    }

    @Operation(
            summary = "Listar produtos por faixa de preço",
            description = "Retorna produtos dentro da faixa de preço informada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ProdutoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PRODUTO_LISTAR')")
    @GetMapping("/preco")
    public ResponseEntity<List<ProdutoResponse>> listarPorFaixaPreco(
            @RequestParam BigDecimal minimo,
            @RequestParam BigDecimal maximo
    ) {
        return ResponseEntity.ok(produtoService.listarPorFaixaDePreco(minimo, maximo));
    }

    // =============================================================
    // STATUS
    // =============================================================

    @Operation(
            summary = "Ativar produto",
            description = "Ativa um produto inativo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto ativado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = ProdutoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PRODUTO_ATUALIZAR')")
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<ProdutoResponse> ativarProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.ativarProduto(id));
    }

    @Operation(
            summary = "Inativar produto",
            description = "Inativa um produto ativo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto inativado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = ProdutoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PRODUTO_ATUALIZAR')")
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<ProdutoResponse> inativarProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.inativarProduto(id));
    }

    // =============================================================
    // PREÇO
    // =============================================================

    @Operation(
            summary = "Atualizar preço de venda",
            description = "Atualiza apenas o preço de venda do produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Preço atualizado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = ProdutoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PRODUTO_ATUALIZAR_PRECO')")
    @PatchMapping("/{id}/preco")
    public ResponseEntity<ProdutoResponse> atualizarPreco(
            @PathVariable Long id,
            @RequestParam BigDecimal novoPreco
    ) {
        return ResponseEntity.ok(produtoService.atualizarPreco(id, novoPreco));
    }

    // =============================================================
    // SKU
    // =============================================================

    @Operation(
            summary = "Verificar disponibilidade de SKU",
            description = "Retorna true se o SKU estiver disponível para uso.",
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
    @PreAuthorize("hasPermission(null, 'PRODUTO_LISTAR')")
    @GetMapping("/sku/{sku}/disponivel")
    public ResponseEntity<Boolean> verificarDisponibilidadeSku(@PathVariable String sku) {
        return ResponseEntity.ok(produtoService.verificarDisponibilidadeSku(sku));
    }
}
