package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.ItemPedidoCompraDto;
import br.com.unicos.ms_compras.service.ItemPedidoCompraService;
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
 * Controller responsável pelos endpoints de Itens do Pedido de Compra.
 */
@RestController
@RequestMapping("/v1/itens-pedido-compra")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Pedidos de Compra - Itens",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de itens do pedido de compra."
)
public class ItemPedidoCompraController {

    private final ItemPedidoCompraService service;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar item do pedido de compra",
            description = "Cria um novo item vinculado a um pedido de compra.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Item criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ItemPedidoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ItemPedidoCompraDto> salvar(@Valid @RequestBody ItemPedidoCompraDto request) {
        ItemPedidoCompraDto response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar item do pedido de compra",
            description = "Atualiza os dados de um item existente do pedido de compra.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Item atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ItemPedidoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Item não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ItemPedidoCompraDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ItemPedidoCompraDto request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar item do pedido por ID",
            description = "Retorna os dados de um item específico do pedido de compra.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ItemPedidoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Item não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ItemPedidoCompraDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar itens do pedido de compra",
            description = "Lista itens de pedido de compra de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ItemPedidoCompraDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<ItemPedidoCompraDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @Operation(
            summary = "Listar itens por pedido",
            description = "Lista itens de um pedido de compra específico (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ItemPedidoCompraDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Pedido de compra não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pedido/{pedidoCompraId}")
    public ResponseEntity<Page<ItemPedidoCompraDto>> listarPorPedido(
            @PathVariable Long pedidoCompraId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorPedido(pedidoCompraId, pageable));
    }

    @Operation(
            summary = "Listar itens por pedido e produto",
            description = "Lista itens filtrando por pedido e produto (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ItemPedidoCompraDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "404", description = "Pedido de compra não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pedido/{pedidoCompraId}/produto/{produtoId}")
    public ResponseEntity<Page<ItemPedidoCompraDto>> listarPorPedidoEProduto(
            @PathVariable Long pedidoCompraId,
            @PathVariable Long produtoId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorPedidoEProduto(pedidoCompraId, produtoId, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover item do pedido",
            description = "Remove um item do pedido de compra pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Item não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Remover itens por pedido",
            description = "Remove todos os itens vinculados a um pedido de compra (dentro do tenant).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Itens removidos com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pedido de compra não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/pedido/{pedidoCompraId}")
    public ResponseEntity<Void> deletarPorPedido(@PathVariable Long pedidoCompraId) {
        service.deletarPorPedido(pedidoCompraId);
        return ResponseEntity.ok().build();
    }
}