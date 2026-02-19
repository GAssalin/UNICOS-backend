package br.com.unicos.ms_vendas.controller;

import br.com.unicos.ms_vendas.dto.produtoatributo.ProdutoAtributoCreateRequest;
import br.com.unicos.ms_vendas.dto.produtoatributo.ProdutoAtributoResponse;
import br.com.unicos.ms_vendas.dto.produtoatributo.ProdutoAtributoResumoResponse;
import br.com.unicos.ms_vendas.dto.produtoatributo.ProdutoAtributoUpdateRequest;
import br.com.unicos.ms_vendas.service.ProdutoAtributoService;
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
@RequestMapping("/v1/vendas/atributos")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Atributos de Produto",
        description = "Endpoints para gerenciamento de atributos de produto (tenant)."
)
public class ProdutoAtributoController {

    private final ProdutoAtributoService produtoAtributoService;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar atributo de produto",
            description = "Cria um novo atributo de produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Atributo criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoAtributoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ProdutoAtributoResponse> criar(
            @RequestBody @Validated ProdutoAtributoCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoAtributoService.criar(request));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar atributo de produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Atributo atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoAtributoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Atributo não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoAtributoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Validated ProdutoAtributoUpdateRequest request
    ) {
        return ResponseEntity.ok(produtoAtributoService.atualizar(id, request));
    }

    // ============================================================
    // GET
    // ============================================================

    @Operation(
            summary = "Buscar atributo de produto por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoAtributoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Atributo não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoAtributoResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(produtoAtributoService.buscarPorId(id));
    }

    // ============================================================
    // LIST (PAGINATED)
    // ============================================================

    @Operation(
            summary = "Listar atributos de produto",
            description = "Lista atributos de produto de forma paginada.",
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
    public ResponseEntity<Page<ProdutoAtributoResumoResponse>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(produtoAtributoService.listar(pageable));
    }

    @Operation(
            summary = "Listar atributos de produto por status",
            description = "Lista atributos filtrando pelo status (ativo/inativo) de forma paginada.",
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
    public ResponseEntity<Page<ProdutoAtributoResumoResponse>> listarPorAtivo(
            @PathVariable Boolean ativo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(produtoAtributoService.listarPorAtivo(ativo, pageable));
    }

    // ============================================================
    // STATUS
    // ============================================================

    @Operation(
            summary = "Ativar atributo de produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Atributo ativado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoAtributoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para ativar"),
                    @ApiResponse(responseCode = "404", description = "Atributo não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}/ativar")
    public ResponseEntity<ProdutoAtributoResponse> ativar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(produtoAtributoService.ativar(id));
    }

    @Operation(
            summary = "Inativar atributo de produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Atributo inativado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoAtributoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para inativar"),
                    @ApiResponse(responseCode = "404", description = "Atributo não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}/inativar")
    public ResponseEntity<ProdutoAtributoResponse> inativar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(produtoAtributoService.inativar(id));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover atributo de produto",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Atributo removido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Atributo não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id
    ) {
        produtoAtributoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
