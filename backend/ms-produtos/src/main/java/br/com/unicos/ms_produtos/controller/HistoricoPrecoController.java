package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoListDTO;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoRequest;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoResponse;
import br.com.unicos.ms_produtos.service.interfaces.HistoricoPrecoService;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/historico-precos")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Histórico de Preços",
        description = "Gerencia registros de alterações de preço dos produtos."
)
public class HistoricoPrecoController {

    private final HistoricoPrecoService historicoPrecoService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Registrar alteração de preço",
            description = "Cria um novo registro de histórico de alteração de preço para um produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Registro criado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = HistoricoPrecoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'HISTORICO_PRECO_CRIAR')")
    @PostMapping("/produto/{produtoId}")
    public ResponseEntity<HistoricoPrecoResponse> salvar(
            @PathVariable Long produtoId,
            @Valid @RequestBody HistoricoPrecoRequest request
    ) {
        HistoricoPrecoResponse response =
                historicoPrecoService.salvar(produtoId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar registro de histórico por ID",
            description = "Retorna os dados de um registro específico de histórico de preço.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = HistoricoPrecoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'HISTORICO_PRECO_VISUALIZAR')")
    @GetMapping("/{id}")
    public ResponseEntity<HistoricoPrecoResponse> buscarPorId(
            @PathVariable Long id
    ) {
        Optional<HistoricoPrecoResponse> resultado =
                historicoPrecoService.buscarPorId(id);
        return resultado
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar todo o histórico de preços",
            description = "Retorna todos os registros de histórico de preço, ordenados por data decrescente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = HistoricoPrecoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'HISTORICO_PRECO_LISTAR')")
    @GetMapping
    public ResponseEntity<List<HistoricoPrecoResponse>> listarTodos() {
        return ResponseEntity.ok(historicoPrecoService.listarTodos());
    }

    @Operation(
            summary = "Listar histórico por produto",
            description = "Retorna o histórico completo de alterações de preço de um produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = HistoricoPrecoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'HISTORICO_PRECO_LISTAR')")
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<HistoricoPrecoResponse>> listarPorProduto(
            @PathVariable Long produtoId
    ) {
        return ResponseEntity.ok(historicoPrecoService.listarPorProduto(produtoId));
    }

    @Operation(
            summary = "Listar últimos registros de histórico por produto",
            description = "Retorna os últimos registros de histórico de preço do produto, em formato simplificado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = HistoricoPrecoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'HISTORICO_PRECO_LISTAR')")
    @GetMapping("/produto/{produtoId}/ultimos")
    public ResponseEntity<List<HistoricoPrecoListDTO>> listarUltimosPorProduto(
            @PathVariable Long produtoId
    ) {
        return ResponseEntity.ok(historicoPrecoService.listarUltimosPorProduto(produtoId));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover registro de histórico de preço",
            description = "Remove um registro de histórico de preço pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Registro removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'HISTORICO_PRECO_REMOVER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        historicoPrecoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
