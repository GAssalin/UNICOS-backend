package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.CondicaoPagamentoDto;
import br.com.unicos.ms_compras.service.CondicaoPagamentoService;
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
 * Controller responsável pelos endpoints de Condições de Pagamento.
 */
@RestController
@RequestMapping("/v1/condicoes-pagamento")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Condições de Pagamento",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de condições de pagamento."
)
public class CondicaoPagamentoController {

    private final CondicaoPagamentoService service;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar condição de pagamento",
            description = "Cria uma nova condição de pagamento.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Condição de pagamento criada com sucesso",
                            content = @Content(schema = @Schema(implementation = CondicaoPagamentoDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<CondicaoPagamentoDto> salvar(@Valid @RequestBody CondicaoPagamentoDto request) {
        CondicaoPagamentoDto response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar condição de pagamento",
            description = "Atualiza os dados de uma condição de pagamento existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Condição de pagamento atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = CondicaoPagamentoDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Condição de pagamento não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CondicaoPagamentoDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CondicaoPagamentoDto request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar condição de pagamento por ID",
            description = "Retorna os dados de uma condição de pagamento específica.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = CondicaoPagamentoDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Condição de pagamento não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CondicaoPagamentoDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar condições de pagamento",
            description = "Lista condições de pagamento de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = CondicaoPagamentoDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<CondicaoPagamentoDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @Operation(
            summary = "Buscar condição de pagamento por código",
            description = "Retorna os dados de uma condição de pagamento pelo código (dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = CondicaoPagamentoDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Condição de pagamento não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CondicaoPagamentoDto> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(service.buscarPorCodigo(codigo));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover condição de pagamento",
            description = "Remove uma condição de pagamento pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Condição de pagamento removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Condição de pagamento não encontrada"),
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