package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.CondicaoPagamentoParcelaDto;
import br.com.unicos.ms_compras.service.CondicaoPagamentoParcelaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelos endpoints de Parcelas de Condição de Pagamento.
 */
@RestController
@RequestMapping("/v1/condicoes-pagamento-parcelas")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Condições de Pagamento - Parcelas",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de parcelas de condição de pagamento."
)
public class CondicaoPagamentoParcelaController {

    private final CondicaoPagamentoParcelaService service;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar parcela da condição de pagamento",
            description = "Cria uma nova parcela vinculada a uma condição de pagamento.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Parcela criada com sucesso",
                            content = @Content(schema = @Schema(implementation = CondicaoPagamentoParcelaDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<CondicaoPagamentoParcelaDto> salvar(@Valid @RequestBody CondicaoPagamentoParcelaDto request) {
        CondicaoPagamentoParcelaDto response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar parcela da condição de pagamento",
            description = "Atualiza os dados de uma parcela existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Parcela atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = CondicaoPagamentoParcelaDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Parcela não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CondicaoPagamentoParcelaDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CondicaoPagamentoParcelaDto request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar parcela por ID",
            description = "Retorna os dados de uma parcela específica.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = CondicaoPagamentoParcelaDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Parcela não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CondicaoPagamentoParcelaDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar parcelas",
            description = "Lista parcelas de condições de pagamento de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = CondicaoPagamentoParcelaDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<CondicaoPagamentoParcelaDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @Operation(
            summary = "Listar parcelas por condição de pagamento",
            description = "Lista parcelas de uma condição de pagamento específica de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = CondicaoPagamentoParcelaDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Condição de pagamento não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/condicao/{condicaoPagamentoId}")
    public ResponseEntity<Page<CondicaoPagamentoParcelaDto>> listarPorCondicaoPagamento(
            @PathVariable Long condicaoPagamentoId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorCondicaoPagamento(condicaoPagamentoId, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover parcela",
            description = "Remove uma parcela pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Parcela removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Parcela não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok().build();
    }
}