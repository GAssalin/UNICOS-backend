package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.RecebimentoCompraDto;
import br.com.unicos.ms_compras.service.RecebimentoCompraService;
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
 * Controller responsável pelos endpoints de Recebimento de Compra.
 */
@RestController
@RequestMapping("/v1/recebimentos-compra")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Recebimentos de Compra",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de recebimentos de compra."
)
public class RecebimentoCompraController {

    private final RecebimentoCompraService service;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar recebimento de compra",
            description = "Cria um novo recebimento de compra dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Recebimento criado com sucesso",
                            content = @Content(schema = @Schema(implementation = RecebimentoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<RecebimentoCompraDto> salvar(@Valid @RequestBody RecebimentoCompraDto request) {
        RecebimentoCompraDto response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar recebimento de compra",
            description = "Atualiza os dados de um recebimento de compra existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Recebimento atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = RecebimentoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Recebimento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<RecebimentoCompraDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RecebimentoCompraDto request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // =============================================================
    // GET
    // =============================================================

    @Operation(
            summary = "Buscar recebimento por ID",
            description = "Retorna os dados de um recebimento de compra específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = RecebimentoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Recebimento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RecebimentoCompraDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // =============================================================
    // LIST
    // =============================================================

    @Operation(
            summary = "Listar recebimentos de compra",
            description = "Lista recebimentos de compra de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecebimentoCompraDto.class)))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<RecebimentoCompraDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @Operation(
            summary = "Listar recebimentos por pedido de compra",
            description = "Lista recebimentos vinculados a um pedido de compra (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecebimentoCompraDto.class)))
                    ),
                    @ApiResponse(responseCode = "404", description = "Pedido de compra não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pedido/{pedidoCompraId}")
    public ResponseEntity<Page<RecebimentoCompraDto>> listarPorPedidoCompra(
            @PathVariable Long pedidoCompraId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorPedidoCompra(pedidoCompraId, pageable));
    }

    @Operation(
            summary = "Listar recebimentos por fornecedor",
            description = "Lista recebimentos vinculados a um fornecedor (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecebimentoCompraDto.class)))
                    ),
                    @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<Page<RecebimentoCompraDto>> listarPorFornecedor(
            @PathVariable Long fornecedorId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorFornecedor(fornecedorId, pageable));
    }

    @Operation(
            summary = "Listar recebimentos por status",
            description = "Lista recebimentos por status (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecebimentoCompraDto.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<RecebimentoCompraDto>> listarPorStatus(
            @PathVariable String status,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorStatus(status, pageable));
    }

    @Operation(
            summary = "Listar recebimentos por período",
            description = "Lista recebimentos com dataRecebimento entre dataInicial e dataFinal (inclusive), paginado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecebimentoCompraDto.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/periodo")
    public ResponseEntity<Page<RecebimentoCompraDto>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorPeriodo(dataInicial, dataFinal, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover recebimento de compra",
            description = "Remove um recebimento de compra pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recebimento removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Recebimento não encontrado"),
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