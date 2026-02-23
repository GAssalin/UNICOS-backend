package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.ItemCotacaoDto;
import br.com.unicos.ms_compras.service.ItemCotacaoService;
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
 * Controller responsável pelos endpoints de Itens de Cotação.
 */
@RestController
@RequestMapping("/v1/itens-cotacao")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Cotações de Compra - Itens",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de itens de cotação de compra."
)
public class ItemCotacaoController {

    private final ItemCotacaoService service;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar item de cotação",
            description = "Cria um novo item vinculado a uma cotação de compra.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Item criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ItemCotacaoDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ItemCotacaoDto> salvar(@Valid @RequestBody ItemCotacaoDto request) {
        ItemCotacaoDto response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar item de cotação",
            description = "Atualiza os dados de um item de cotação existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Item atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ItemCotacaoDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Item não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ItemCotacaoDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ItemCotacaoDto request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar item de cotação por ID",
            description = "Retorna os dados de um item de cotação específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ItemCotacaoDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Item não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ItemCotacaoDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar itens de cotação",
            description = "Lista itens de cotação de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ItemCotacaoDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<ItemCotacaoDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @Operation(
            summary = "Listar itens por cotação",
            description = "Lista itens de uma cotação de compra específica de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ItemCotacaoDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Cotação não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/cotacao/{cotacaoCompraId}")
    public ResponseEntity<Page<ItemCotacaoDto>> listarPorCotacao(
            @PathVariable Long cotacaoCompraId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorCotacao(cotacaoCompraId, pageable));
    }

    @Operation(
            summary = "Listar itens por cotação e produto",
            description = "Lista itens filtrando por cotação e produto (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ItemCotacaoDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "404", description = "Cotação não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/cotacao/{cotacaoCompraId}/produto/{produtoId}")
    public ResponseEntity<Page<ItemCotacaoDto>> listarPorCotacaoEProduto(
            @PathVariable Long cotacaoCompraId,
            @PathVariable Long produtoId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorCotacaoEProduto(cotacaoCompraId, produtoId, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover item de cotação",
            description = "Remove um item de cotação pelo identificador.",
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
            summary = "Remover itens por cotação",
            description = "Remove todos os itens vinculados a uma cotação de compra (dentro do tenant).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Itens removidos com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cotação não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/cotacao/{cotacaoCompraId}")
    public ResponseEntity<Void> deletarPorCotacao(@PathVariable Long cotacaoCompraId) {
        service.deletarPorCotacao(cotacaoCompraId);
        return ResponseEntity.ok().build();
    }
}