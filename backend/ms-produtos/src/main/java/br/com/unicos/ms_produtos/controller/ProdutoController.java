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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento de produtos.
 */
@RestController
@RequestMapping("/v1/produtos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Produtos",
        description = "Gerencia o cadastro, atualização, filtro, ativação, inativação e alteração de preço de produtos."
)
public class ProdutoController {

    private final ProdutoService produtoService;

    // ============================================================
    // CRUD PRINCIPAL
    // ============================================================

    @PreAuthorize("hasAuthority('PRODUTO_CRIAR')")
    @Operation(
            summary = "Criar produto",
            description = "Cadastra um novo produto com informações completas.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Produto criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
            }
    )
    @PostMapping
    public ResponseEntity<ProdutoResponse> salvar(
            @Valid @RequestBody ProdutoRequest request) {

        ProdutoResponse response = produtoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('PRODUTO_ATUALIZAR')")
    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza completamente os dados de um produto existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto atualizado",
                            content = @Content(schema = @Schema(implementation = ProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request) {

        ProdutoResponse response = produtoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('PRODUTO_EXCLUIR')")
    @Operation(
            summary = "Remover produto",
            description = "Exclui um produto pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Produto removido"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // CONSULTAS GERAIS
    // ============================================================

    @PreAuthorize("hasAuthority('PRODUTO_LISTAR')")
    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna os dados de um produto específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto encontrado",
                            content = @Content(schema = @Schema(implementation = ProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {

        Optional<ProdutoResponse> resultado = produtoService.buscarPorId(id);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('PRODUTO_LISTAR')")
    @Operation(
            summary = "Listar todos os produtos",
            description = "Retorna uma lista contendo todos os produtos cadastrados.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProdutoResponse.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @PreAuthorize("hasAuthority('PRODUTO_LISTAR')")
    @Operation(
            summary = "Buscar produtos por nome",
            description = "Busca produtos cujo nome contenha o termo informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProdutoResponse.class)))
                    )
            }
    )
    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoResponse>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(produtoService.buscarPorNome(nome));
    }

    @PreAuthorize("hasAuthority('PRODUTO_LISTAR')")
    @Operation(
            summary = "Buscar produto por SKU",
            description = "Retorna um produto a partir do seu código SKU.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto encontrado",
                            content = @Content(schema = @Schema(implementation = ProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProdutoResponse> buscarPorSku(@PathVariable String sku) {

        Optional<ProdutoResponse> resultado = produtoService.buscarPorSku(sku);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // CONSULTAS POR FILTROS
    // ============================================================

    @PreAuthorize("hasAuthority('PRODUTO_LISTAR')")
    @Operation(
            summary = "Listar produtos por categoria",
            description = "Retorna produtos associados à categoria informada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProdutoResponse.class)))
                    )
            }
    )
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProdutoResponse>> listarPorCategoria(
            @PathVariable Long categoriaId) {

        return ResponseEntity.ok(produtoService.listarPorCategoria(categoriaId));
    }

    @PreAuthorize("hasAuthority('PRODUTO_LISTAR')")
    @Operation(
            summary = "Listar produtos por marca",
            description = "Retorna produtos associados à marca informada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProdutoResponse.class)))
                    )
            }
    )
    @GetMapping("/marca/{marcaId}")
    public ResponseEntity<List<ProdutoResponse>> listarPorMarca(@PathVariable Long marcaId) {

        return ResponseEntity.ok(produtoService.listarPorMarca(marcaId));
    }

    @PreAuthorize("hasAuthority('PRODUTO_LISTAR')")
    @Operation(
            summary = "Listar produtos ativos",
            description = "Retorna apenas os produtos com status ativo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProdutoResponse.class)))
                    )
            }
    )
    @GetMapping("/ativos")
    public ResponseEntity<List<ProdutoResponse>> listarAtivos() {
        return ResponseEntity.ok(produtoService.listarAtivos());
    }

    @PreAuthorize("hasAuthority('PRODUTO_LISTAR')")
    @Operation(
            summary = "Listar produtos inativos",
            description = "Retorna apenas os produtos com status inativo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProdutoResponse.class)))
                    )
            }
    )
    @GetMapping("/inativos")
    public ResponseEntity<List<ProdutoResponse>> listarInativos() {
        return ResponseEntity.ok(produtoService.listarInativos());
    }

    @PreAuthorize("hasAuthority('PRODUTO_LISTAR')")
    @Operation(
            summary = "Listar produtos por faixa de preço",
            description = "Retorna todos os produtos dentro da faixa de preço mínima e máxima informada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProdutoResponse.class)))
                    )
            }
    )
    @GetMapping("/preco")
    public ResponseEntity<List<ProdutoResponse>> listarPorFaixaPreco(
            @RequestParam BigDecimal minimo,
            @RequestParam BigDecimal maximo) {

        return ResponseEntity.ok(produtoService.listarPorFaixaDePreco(minimo, maximo));
    }

    // ============================================================
    // ALTERAÇÃO DE ESTADO
    // ============================================================

    @PreAuthorize("hasAuthority('PRODUTO_ATUALIZAR')")
    @Operation(
            summary = "Ativar produto",
            description = "Ativa um produto que esteja inativo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto ativado",
                            content = @Content(schema = @Schema(implementation = ProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<ProdutoResponse> ativarProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.ativarProduto(id));
    }

    @PreAuthorize("hasAuthority('PRODUTO_ATUALIZAR')")
    @Operation(
            summary = "Inativar produto",
            description = "Inativa um produto ativo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto inativado",
                            content = @Content(schema = @Schema(implementation = ProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<ProdutoResponse> inativarProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.inativarProduto(id));
    }

    // ============================================================
    // PREÇO
    // ============================================================

    @PreAuthorize("hasAuthority('PRODUTO_ATUALIZAR_PRECO')")
    @Operation(
            summary = "Atualizar preço de venda",
            description = "Altera apenas o preço de venda do produto, sem afetar outros dados.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Preço atualizado",
                            content = @Content(schema = @Schema(implementation = ProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    @PatchMapping("/{id}/preco")
    public ResponseEntity<ProdutoResponse> atualizarPreco(
            @PathVariable Long id,
            @RequestParam BigDecimal novoPreco) {

        ProdutoResponse response = produtoService.atualizarPreco(id, novoPreco);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // SKU — Validação
    // ============================================================

    @PreAuthorize("hasAuthority('PRODUTO_LISTAR')")
    @Operation(
            summary = "Verificar disponibilidade de SKU",
            description = "Retorna true se o SKU estiver disponível para uso; false se já estiver em uso.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resultado retornado",
                            content = @Content(schema = @Schema(implementation = Boolean.class))
                    )
            }
    )
    @GetMapping("/sku/{sku}/disponivel")
    public ResponseEntity<Boolean> verificarDisponibilidadeSku(@PathVariable String sku) {

        Boolean disponivel = produtoService.verificarDisponibilidadeSku(sku);
        return ResponseEntity.ok(disponivel);
    }
}
