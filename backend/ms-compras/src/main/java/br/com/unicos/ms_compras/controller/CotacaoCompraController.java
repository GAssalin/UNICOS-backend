package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.CotacaoCompraDto;
import br.com.unicos.ms_compras.service.CotacaoCompraService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller responsável pelos endpoints de Cotações de Compra.
 */
@RestController
@RequestMapping("/v1/cotacoes-compra")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Cotações de Compra",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de cotações de compra."
)
public class CotacaoCompraController {

    private final CotacaoCompraService service;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar cotação de compra",
            description = "Cria uma nova cotação de compra.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Cotação criada com sucesso",
                            content = @Content(schema = @Schema(implementation = CotacaoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<CotacaoCompraDto> salvar(@Valid @RequestBody CotacaoCompraDto request) {
        CotacaoCompraDto response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar cotação de compra",
            description = "Atualiza os dados de uma cotação de compra existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cotação atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = CotacaoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Cotação não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CotacaoCompraDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CotacaoCompraDto request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar cotação por ID",
            description = "Retorna os dados de uma cotação de compra específica.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = CotacaoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Cotação não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CotacaoCompraDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar cotações de compra",
            description = "Lista cotações de compra de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = CotacaoCompraDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<CotacaoCompraDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @Operation(
            summary = "Buscar cotação por código",
            description = "Retorna os dados de uma cotação de compra pelo código (dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = CotacaoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Cotação não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CotacaoCompraDto> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(service.buscarPorCodigo(codigo));
    }

    @Operation(
            summary = "Listar cotações por status",
            description = "Lista cotações por status de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = CotacaoCompraDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<CotacaoCompraDto>> listarPorStatus(
            @PathVariable String status,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorStatus(status, pageable));
    }

    @Operation(
            summary = "Listar cotações por período de abertura",
            description = "Lista cotações por período (dataAbertura) de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = CotacaoCompraDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/periodo-abertura")
    public ResponseEntity<Page<CotacaoCompraDto>> listarPorPeriodoAbertura(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorPeriodoAbertura(dataInicial, dataFinal, pageable));
    }

    @Operation(
            summary = "Listar cotações próximas do vencimento",
            description = "Lista cotações com dataValidade menor ou igual a um limite informado (paginado).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = CotacaoCompraDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/proximas-vencimento")
    public ResponseEntity<Page<CotacaoCompraDto>> listarProximasDoVencimento(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataLimite,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarProximasDoVencimento(dataLimite, pageable));
    }

    @Operation(
            summary = "Listar cotações por pedido de compra",
            description = "Lista cotações vinculadas a um pedido de compra específico de forma paginada (dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = CotacaoCompraDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Pedido de compra não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pedido-compra/{pedidoCompraId}")
    public ResponseEntity<Page<CotacaoCompraDto>> listarPorPedidoCompra(
            @PathVariable Long pedidoCompraId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorPedidoCompra(pedidoCompraId, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover cotação de compra",
            description = "Remove uma cotação de compra pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cotação removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cotação não encontrada"),
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