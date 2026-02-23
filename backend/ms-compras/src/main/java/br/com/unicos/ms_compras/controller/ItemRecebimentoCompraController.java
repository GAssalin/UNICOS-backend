package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.ItemRecebimentoCompraDto;
import br.com.unicos.ms_compras.service.ItemRecebimentoCompraService;
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
 * Controller responsável pelos endpoints de Itens de Recebimento de Compra.
 */
@RestController
@RequestMapping("/v1/itens-recebimento-compra")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Recebimentos de Compra - Itens",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de itens de recebimento de compra."
)
public class ItemRecebimentoCompraController {

    private final ItemRecebimentoCompraService service;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar item de recebimento",
            description = "Cria um novo item de recebimento vinculado a um recebimento de compra e a um item do pedido.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Item de recebimento criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ItemRecebimentoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ItemRecebimentoCompraDto> salvar(@Valid @RequestBody ItemRecebimentoCompraDto request) {
        ItemRecebimentoCompraDto response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar item de recebimento",
            description = "Atualiza os dados de um item de recebimento existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Item de recebimento atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ItemRecebimentoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Item de recebimento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ItemRecebimentoCompraDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ItemRecebimentoCompraDto request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar item de recebimento por ID",
            description = "Retorna os dados de um item de recebimento específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ItemRecebimentoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Item de recebimento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ItemRecebimentoCompraDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar item de recebimento por item do pedido",
            description = "Retorna o item de recebimento vinculado a um item do pedido (regra: 1 recebimento por itemPedidoCompra no tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ItemRecebimentoCompraDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Item de recebimento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/item-pedido/{itemPedidoCompraId}")
    public ResponseEntity<ItemRecebimentoCompraDto> buscarPorItemPedido(@PathVariable Long itemPedidoCompraId) {
        return ResponseEntity.ok(service.buscarPorItemPedido(itemPedidoCompraId));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar itens de recebimento",
            description = "Lista itens de recebimento de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ItemRecebimentoCompraDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<ItemRecebimentoCompraDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @Operation(
            summary = "Listar itens por recebimento",
            description = "Lista itens vinculados a um recebimento de compra específico (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ItemRecebimentoCompraDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Recebimento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/recebimento/{recebimentoCompraId}")
    public ResponseEntity<Page<ItemRecebimentoCompraDto>> listarPorRecebimento(
            @PathVariable Long recebimentoCompraId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorRecebimento(recebimentoCompraId, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover item de recebimento",
            description = "Remove um item de recebimento pelo identificador.",
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
            summary = "Remover itens por recebimento",
            description = "Remove todos os itens vinculados a um recebimento de compra (dentro do tenant).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Itens removidos com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Recebimento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/recebimento/{recebimentoCompraId}")
    public ResponseEntity<Void> deletarPorRecebimento(@PathVariable Long recebimentoCompraId) {
        service.deletarPorRecebimento(recebimentoCompraId);
        return ResponseEntity.ok().build();
    }
}