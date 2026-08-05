package br.com.unicos.ms_produto.controller;

import br.com.unicos.ms_produto.dto.produtoatributovalor.ProdutoAtributoValorCreateRequest;
import br.com.unicos.ms_produto.dto.produtoatributovalor.ProdutoAtributoValorResponse;
import br.com.unicos.ms_produto.dto.produtoatributovalor.ProdutoAtributoValorUpdateRequest;
import br.com.unicos.ms_produto.service.ProdutoAtributoValorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import java.util.List;

@RestController
@RequestMapping("/v1/produtos/atributos-valores")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Valores de Atributos do Produto",
        description = "Endpoints para gerenciamento de valores de atributos vinculados a produtos (tenant)."
)
public class ProdutoAtributoValorController {

    private final ProdutoAtributoValorService produtoAtributoValorService;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar valor de atributo do produto",
            description = "Cria um novo valor para um atributo em um produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Valor criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoAtributoValorResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ProdutoAtributoValorResponse> criar(
            @RequestBody @Validated ProdutoAtributoValorCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoAtributoValorService.criar(request));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar valor de atributo do produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Valor atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoAtributoValorResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Valor não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoAtributoValorResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Validated ProdutoAtributoValorUpdateRequest request
    ) {
        return ResponseEntity.ok(produtoAtributoValorService.atualizar(id, request));
    }

    // ============================================================
    // GET
    // ============================================================

    @Operation(
            summary = "Buscar valor de atributo por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoAtributoValorResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Valor não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoAtributoValorResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(produtoAtributoValorService.buscarPorId(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Operation(
            summary = "Listar valores de atributos por produto",
            description = "Lista todos os valores de atributos cadastrados para um produto no tenant atual.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ProdutoAtributoValorResponse.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<ProdutoAtributoValorResponse>> listarPorProduto(
            @PathVariable Long produtoId
    ) {
        return ResponseEntity.ok(produtoAtributoValorService.listarPorProduto(produtoId));
    }

    @Operation(
            summary = "Listar valores de atributos (paginado)",
            description = "Lista todos os valores de atributos do tenant atual de forma paginada.",
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
    public ResponseEntity<Page<ProdutoAtributoValorResponse>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(produtoAtributoValorService.listar(pageable));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover valor de atributo por ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Valor removido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Valor não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id
    ) {
        produtoAtributoValorService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Remover valores de atributos por produto",
            description = "Remove todos os valores de atributos vinculados a um produto no tenant atual.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Valores removidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/produto/{produtoId}")
    public ResponseEntity<Void> removerPorProduto(
            @PathVariable Long produtoId
    ) {
        produtoAtributoValorService.removerPorProduto(produtoId);
        return ResponseEntity.noContent().build();
    }
}
