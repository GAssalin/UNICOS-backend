package br.com.unicos.ms_vendas.controller;

import br.com.unicos.ms_vendas.dto.produtoimagem.ProdutoImagemCreateRequest;
import br.com.unicos.ms_vendas.dto.produtoimagem.ProdutoImagemResponse;
import br.com.unicos.ms_vendas.dto.produtoimagem.ProdutoImagemUpdateRequest;
import br.com.unicos.ms_vendas.service.ProdutoImagemService;
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
@RequestMapping("/v1/vendas/imagens")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Imagens do Produto",
        description = "Endpoints para gerenciamento de imagens vinculadas a produtos (tenant)."
)
public class ProdutoImagemController {

    private final ProdutoImagemService produtoImagemService;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar imagem do produto",
            description = "Cria uma nova imagem para um produto. Se marcada como principal, remove a atual principal.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Imagem criada com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoImagemResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ProdutoImagemResponse> criar(
            @RequestBody @Validated ProdutoImagemCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoImagemService.criar(request));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar imagem do produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Imagem atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoImagemResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Imagem não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoImagemResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Validated ProdutoImagemUpdateRequest request
    ) {
        return ResponseEntity.ok(produtoImagemService.atualizar(id, request));
    }

    // ============================================================
    // GET
    // ============================================================

    @Operation(
            summary = "Buscar imagem por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoImagemResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Imagem não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoImagemResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(produtoImagemService.buscarPorId(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Operation(
            summary = "Listar imagens por produto",
            description = "Lista todas as imagens cadastradas para um produto de forma paginada.",
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
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<Page<ProdutoImagemResponse>> listarPorProduto(
            @PathVariable Long produtoId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(produtoImagemService.listarPorProduto(produtoId, pageable));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover imagem por ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Imagem removida"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Imagem não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id
    ) {
        produtoImagemService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Remover imagens por produto",
            description = "Remove todas as imagens vinculadas a um produto no tenant atual.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Imagens removidas"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/produto/{produtoId}")
    public ResponseEntity<Void> removerPorProduto(
            @PathVariable Long produtoId
    ) {
        produtoImagemService.removerPorProduto(produtoId);
        return ResponseEntity.noContent().build();
    }
}
