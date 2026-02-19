package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.unidademedida.UnidadeMedidaCreateRequest;
import br.com.unicos.ms_compras.dto.unidademedida.UnidadeMedidaResponse;
import br.com.unicos.ms_compras.dto.unidademedida.UnidadeMedidaResumoResponse;
import br.com.unicos.ms_compras.dto.unidademedida.UnidadeMedidaUpdateRequest;
import br.com.unicos.ms_compras.service.UnidadeMedidaService;
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
@RequestMapping("/v1/vendas/unidades-medida")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Unidades de Medida",
        description = "Endpoints para gerenciamento de unidades de medida (tenant)."
)
public class UnidadeMedidaController {

    private final UnidadeMedidaService unidadeMedidaService;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar unidade de medida",
            description = "Cria uma nova unidade de medida. O código deve ser único dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Unidade criada com sucesso",
                            content = @Content(schema = @Schema(implementation = UnidadeMedidaResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<UnidadeMedidaResponse> criar(
            @RequestBody @Validated UnidadeMedidaCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(unidadeMedidaService.criar(request));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar unidade de medida",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Unidade atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = UnidadeMedidaResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeMedidaResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Validated UnidadeMedidaUpdateRequest request
    ) {
        return ResponseEntity.ok(unidadeMedidaService.atualizar(id, request));
    }

    // ============================================================
    // GET
    // ============================================================

    @Operation(
            summary = "Buscar unidade de medida por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = UnidadeMedidaResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeMedidaResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(unidadeMedidaService.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar unidade de medida por código",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = UnidadeMedidaResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<UnidadeMedidaResponse> buscarPorCodigo(
            @PathVariable String codigo
    ) {
        return ResponseEntity.ok(unidadeMedidaService.buscarPorCodigo(codigo));
    }

    // ============================================================
    // LIST (PAGINATED)
    // ============================================================

    @Operation(
            summary = "Listar unidades de medida",
            description = "Lista unidades de medida do tenant atual de forma paginada.",
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
    public ResponseEntity<Page<UnidadeMedidaResumoResponse>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(unidadeMedidaService.listar(pageable));
    }

    @Operation(
            summary = "Listar unidades de medida por status",
            description = "Lista unidades de medida filtrando pelo status ativo/inativo de forma paginada.",
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
    public ResponseEntity<Page<UnidadeMedidaResumoResponse>> listarPorAtivo(
            @PathVariable Boolean ativo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(unidadeMedidaService.listarPorAtivo(ativo, pageable));
    }

    // ============================================================
    // STATUS
    // ============================================================

    @Operation(
            summary = "Ativar unidade de medida",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Unidade ativada com sucesso",
                            content = @Content(schema = @Schema(implementation = UnidadeMedidaResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para ativar"),
                    @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}/ativar")
    public ResponseEntity<UnidadeMedidaResponse> ativar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(unidadeMedidaService.ativar(id));
    }

    @Operation(
            summary = "Inativar unidade de medida",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Unidade inativada com sucesso",
                            content = @Content(schema = @Schema(implementation = UnidadeMedidaResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para inativar"),
                    @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}/inativar")
    public ResponseEntity<UnidadeMedidaResponse> inativar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(unidadeMedidaService.inativar(id));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover unidade de medida",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Unidade removida"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id
    ) {
        unidadeMedidaService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
