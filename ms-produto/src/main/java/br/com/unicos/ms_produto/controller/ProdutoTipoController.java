package br.com.unicos.ms_produto.controller;

import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoCreateRequest;
import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoResponse;
import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoResumoResponse;
import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoUpdateRequest;
import br.com.unicos.ms_produto.service.ProdutoTipoService;
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
@RequestMapping("/v1/produtos/tipos")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Tipos de Produto",
        description = "Endpoints para gerenciamento de tipos de produto (tenant)."
)
public class ProdutoTipoController {

    private final ProdutoTipoService produtoTipoService;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar tipo de produto",
            description = "Cria um novo tipo de produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Tipo criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoTipoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ProdutoTipoResponse> criar(
            @RequestBody @Validated ProdutoTipoCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoTipoService.criar(request));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar tipo de produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tipo atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoTipoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Tipo não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoTipoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Validated ProdutoTipoUpdateRequest request
    ) {
        return ResponseEntity.ok(produtoTipoService.atualizar(id, request));
    }

    // ============================================================
    // GET
    // ============================================================

    @Operation(
            summary = "Buscar tipo de produto por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoTipoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Tipo não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoTipoResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(produtoTipoService.buscarPorId(id));
    }

    // ============================================================
    // LIST (PAGINATED)
    // ============================================================

    @Operation(
            summary = "Listar tipos de produto",
            description = "Lista tipos de produto de forma paginada.",
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
    public ResponseEntity<Page<ProdutoTipoResumoResponse>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(produtoTipoService.listar(pageable));
    }

    @Operation(
            summary = "Listar tipos de produto por status",
            description = "Lista tipos filtrando pelo status ativo/inativo de forma paginada.",
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
    public ResponseEntity<Page<ProdutoTipoResumoResponse>> listarPorAtivo(
            @PathVariable Boolean ativo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(produtoTipoService.listarPorAtivo(ativo, pageable));
    }

    // ============================================================
    // STATUS
    // ============================================================

    @Operation(
            summary = "Ativar tipo de produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tipo ativado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoTipoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para ativar"),
                    @ApiResponse(responseCode = "404", description = "Tipo não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}/ativar")
    public ResponseEntity<ProdutoTipoResponse> ativar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(produtoTipoService.ativar(id));
    }

    @Operation(
            summary = "Inativar tipo de produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tipo inativado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoTipoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para inativar"),
                    @ApiResponse(responseCode = "404", description = "Tipo não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}/inativar")
    public ResponseEntity<ProdutoTipoResponse> inativar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(produtoTipoService.inativar(id));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover tipo de produto",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Tipo removido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Tipo não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id
    ) {
        produtoTipoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
