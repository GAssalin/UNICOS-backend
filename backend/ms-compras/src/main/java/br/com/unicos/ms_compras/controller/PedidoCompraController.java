package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.PedidoCompraDto;
import br.com.unicos.ms_compras.enums.StatusPedidoCompra;
import br.com.unicos.ms_compras.service.PedidoCompraService;
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
 * Controller responsável pelos endpoints de Pedido de Compra.
 */
@RestController
@RequestMapping("/v1/pedidos-compra")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Pedidos de Compra",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de pedidos de compra."
)
public class PedidoCompraController {

    private final PedidoCompraService service;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar pedido de compra",
            description = "Cria um novo pedido de compra dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Pedido criado com sucesso",
                            content = @Content(schema = @Schema(implementation = PedidoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<PedidoCompraDto> salvar(@Valid @RequestBody PedidoCompraDto request) {
        PedidoCompraDto response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar pedido de compra",
            description = "Atualiza os dados de um pedido de compra existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pedido atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = PedidoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PedidoCompraDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoCompraDto request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // =============================================================
    // GET
    // =============================================================

    @Operation(
            summary = "Buscar pedido por ID",
            description = "Retorna os dados de um pedido de compra específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = PedidoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PedidoCompraDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar pedido por código",
            description = "Retorna o pedido de compra pelo código (único por tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = PedidoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<PedidoCompraDto> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(service.buscarPorCodigo(codigo));
    }

    // =============================================================
    // LIST
    // =============================================================

    @Operation(
            summary = "Listar pedidos de compra",
            description = "Lista pedidos de compra de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PedidoCompraDto.class)))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<PedidoCompraDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @Operation(
            summary = "Listar pedidos por fornecedor",
            description = "Lista pedidos de compra por fornecedor (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PedidoCompraDto.class)))
                    ),
                    @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<Page<PedidoCompraDto>> listarPorFornecedor(
            @PathVariable Long fornecedorId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorFornecedor(fornecedorId, pageable));
    }

    @Operation(
            summary = "Listar pedidos por status",
            description = "Lista pedidos de compra por status (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PedidoCompraDto.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Status inválido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PedidoCompraDto>> listarPorStatus(
            @PathVariable StatusPedidoCompra status,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorStatus(status, pageable));
    }

    @Operation(
            summary = "Listar pedidos por período de emissão",
            description = "Lista pedidos com dataEmissao entre dataInicial e dataFinal (inclusive), paginado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PedidoCompraDto.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/emissao")
    public ResponseEntity<Page<PedidoCompraDto>> listarPorPeriodoEmissao(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorPeriodoEmissao(dataInicial, dataFinal, pageable));
    }

    @Operation(
            summary = "Listar pedidos até a entrega prevista",
            description = "Lista pedidos cuja dataPrevistaEntrega seja menor ou igual a dataLimite (paginado).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PedidoCompraDto.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/entrega-prevista")
    public ResponseEntity<Page<PedidoCompraDto>> listarAteEntregaPrevista(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataLimite,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarAteEntregaPrevista(dataLimite, pageable));
    }

    @Operation(
            summary = "Listar pedidos por condição de pagamento",
            description = "Lista pedidos vinculados a uma condição de pagamento (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PedidoCompraDto.class)))
                    ),
                    @ApiResponse(responseCode = "404", description = "Condição de pagamento não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/condicao-pagamento/{condicaoPagamentoId}")
    public ResponseEntity<Page<PedidoCompraDto>> listarPorCondicaoPagamento(
            @PathVariable Long condicaoPagamentoId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorCondicaoPagamento(condicaoPagamentoId, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover pedido de compra",
            description = "Remove um pedido de compra pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedido removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
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