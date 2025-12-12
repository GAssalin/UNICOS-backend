package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoListDTO;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoRequest;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoResponse;
import br.com.unicos.ms_produtos.service.HistoricoPrecoService;
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

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento
 * do histórico de alterações de preços dos produtos.
 */
@RestController
@RequestMapping("/v1/historico-precos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Histórico de Preços",
        description = "Gerencia registros de alterações de preço dos produtos."
)
public class HistoricoPrecoController {

    private final HistoricoPrecoService historicoPrecoService;

    // ============================================================
    // Criar registro
    // ============================================================

    @PreAuthorize("hasAuthority('HISTORICO_PRECO_CRIAR')")
    @Operation(
            summary = "Registrar alteração de preço",
            description = "Cria um novo registro de histórico de alteração de preço para um produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Registro criado com sucesso",
                            content = @Content(schema = @Schema(implementation = HistoricoPrecoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    @PostMapping("/produto/{produtoId}")
    public ResponseEntity<HistoricoPrecoResponse> salvar(
            @PathVariable Long produtoId,
            @Valid @RequestBody HistoricoPrecoRequest request) {

        HistoricoPrecoResponse response = historicoPrecoService.salvar(produtoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @PreAuthorize("hasAuthority('HISTORICO_PRECO_LISTAR')")
    @Operation(
            summary = "Buscar registro de histórico",
            description = "Retorna os dados de um registro específico de histórico de preço pelo ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Registro encontrado",
                            content = @Content(schema = @Schema(implementation = HistoricoPrecoResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Registro não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<HistoricoPrecoResponse> buscarPorId(@PathVariable Long id) {

        Optional<HistoricoPrecoResponse> resultado = historicoPrecoService.buscarPorId(id);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar todos
    // ============================================================

    @PreAuthorize("hasAuthority('HISTORICO_PRECO_LISTAR')")
    @Operation(
            summary = "Listar todo o histórico",
            description = "Retorna todos os registros de histórico de preço, ordenados por data DESC.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HistoricoPrecoResponse.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<HistoricoPrecoResponse>> listarTodos() {
        return ResponseEntity.ok(historicoPrecoService.listarTodos());
    }

    // ============================================================
    // Listar por produto
    // ============================================================

    @PreAuthorize("hasAuthority('HISTORICO_PRECO_LISTAR')")
    @Operation(
            summary = "Listar histórico por produto",
            description = "Retorna o histórico completo de alterações de preço para um produto específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HistoricoPrecoResponse.class)))
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<HistoricoPrecoResponse>> listarPorProduto(
            @PathVariable Long produtoId) {

        return ResponseEntity.ok(historicoPrecoService.listarPorProduto(produtoId));
    }

    // ============================================================
    // Listar últimos registros (listagem reduzida)
    // ============================================================

    @PreAuthorize("hasAuthority('HISTORICO_PRECO_LISTAR')")
    @Operation(
            summary = "Listar últimos registros de histórico por produto",
            description = "Retorna os últimos 10 registros de histórico de preço do produto, em formato simplificado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HistoricoPrecoListDTO.class)))
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    @GetMapping("/produto/{produtoId}/ultimos")
    public ResponseEntity<List<HistoricoPrecoListDTO>> listarUltimosPorProduto(
            @PathVariable Long produtoId) {

        return ResponseEntity.ok(historicoPrecoService.listarUltimosPorProduto(produtoId));
    }

    // ============================================================
    // Deletar registro
    // ============================================================

    @PreAuthorize("hasAuthority('HISTORICO_PRECO_EXCLUIR')")
    @Operation(
            summary = "Excluir registro de histórico",
            description = "Remove um registro de histórico de preço pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Registro removido"),
                    @ApiResponse(responseCode = "404", description = "Registro não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        historicoPrecoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
