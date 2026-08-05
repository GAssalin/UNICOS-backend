package br.com.unicos.ms_produto.controller;

import br.com.unicos.ms_produto.dto.produto.ProdutoCreateRequest;
import br.com.unicos.ms_produto.dto.produto.ProdutoResponse;
import br.com.unicos.ms_produto.dto.produto.ProdutoResumoResponse;
import br.com.unicos.ms_produto.dto.produto.ProdutoUpdateRequest;
import br.com.unicos.ms_produto.service.ProdutoService;
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
@RequestMapping("/v1/produtos")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Produtos",
        description = "Endpoints para gerenciamento de produtos (tenant)."
)
public class ProdutoController {

    private final ProdutoService produtoService;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar produto",
            description = "Cria um novo produto. O código (SKU) deve ser único dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Produto criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(
            @RequestBody @Validated ProdutoCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoService.criar(request));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Validated ProdutoUpdateRequest request
    ) {
        return ResponseEntity.ok(produtoService.atualizar(id, request));
    }

    // ============================================================
    // GET
    // ============================================================

    @Operation(
            summary = "Buscar produto por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar produto por código (SKU)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProdutoResponse> buscarPorCodigo(
            @PathVariable String codigo
    ) {
        return ResponseEntity.ok(produtoService.buscarPorCodigo(codigo));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Operation(
            summary = "Listar produtos (paginado)",
            description = "Lista produtos do tenant atual de forma paginada.",
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
    public ResponseEntity<Page<ProdutoResumoResponse>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(produtoService.listar(pageable));
    }

    @Operation(
            summary = "Listar produtos por status (paginado)",
            description = "Lista produtos filtrando pelo status ativo/inativo.",
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
    public ResponseEntity<Page<ProdutoResumoResponse>> listarPorAtivo(
            @PathVariable Boolean ativo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(produtoService.listarPorAtivo(ativo, pageable));
    }

    @Operation(
            summary = "Pesquisar produtos por nome (paginado)",
            description = "Pesquisa produtos por nome (contém / ignore case) no tenant atual.",
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
    @GetMapping("/pesquisa")
    public ResponseEntity<Page<ProdutoResumoResponse>> pesquisarPorNome(
            @RequestParam String nome,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(produtoService.pesquisarPorNome(nome, pageable));
    }

    // ============================================================
    // STATUS
    // ============================================================

    @Operation(
            summary = "Ativar produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto ativado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para ativar"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}/ativar")
    public ResponseEntity<ProdutoResponse> ativar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(produtoService.ativar(id));
    }

    @Operation(
            summary = "Inativar produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto inativado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para inativar"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}/inativar")
    public ResponseEntity<ProdutoResponse> inativar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(produtoService.inativar(id));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover produto",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Produto removido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id
    ) {
        produtoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
