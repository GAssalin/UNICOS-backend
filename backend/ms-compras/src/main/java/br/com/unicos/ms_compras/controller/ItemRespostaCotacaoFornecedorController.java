package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.ItemRespostaCotacaoFornecedorDto;
import br.com.unicos.ms_compras.service.ItemRespostaCotacaoFornecedorService;
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
 * Controller responsável pelos endpoints de Itens de Resposta de Cotação do Fornecedor.
 */
@RestController
@RequestMapping("/v1/itens-resposta-cotacao-fornecedor")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Cotações - Respostas de Fornecedor (Itens)",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de itens de resposta de cotação do fornecedor."
)
public class ItemRespostaCotacaoFornecedorController {

    private final ItemRespostaCotacaoFornecedorService service;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar item de resposta de cotação",
            description = "Cria um novo item de resposta, vinculado a uma resposta de cotação do fornecedor e a um item da cotação.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Item criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ItemRespostaCotacaoFornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ItemRespostaCotacaoFornecedorDto> salvar(@Valid @RequestBody ItemRespostaCotacaoFornecedorDto request) {
        ItemRespostaCotacaoFornecedorDto response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar item de resposta de cotação",
            description = "Atualiza os dados de um item de resposta de cotação existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Item atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ItemRespostaCotacaoFornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Item não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ItemRespostaCotacaoFornecedorDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ItemRespostaCotacaoFornecedorDto request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar item de resposta por ID",
            description = "Retorna os dados de um item de resposta de cotação do fornecedor específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ItemRespostaCotacaoFornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Item não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ItemRespostaCotacaoFornecedorDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar item por resposta e item de cotação",
            description = "Retorna o item de resposta para uma combinação (respostaCotacaoFornecedorId, itemCotacaoId) dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ItemRespostaCotacaoFornecedorDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Item não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/resposta/{respostaCotacaoFornecedorId}/item-cotacao/{itemCotacaoId}")
    public ResponseEntity<ItemRespostaCotacaoFornecedorDto> buscarPorRespostaEItemCotacao(
            @PathVariable Long respostaCotacaoFornecedorId,
            @PathVariable Long itemCotacaoId
    ) {
        return ResponseEntity.ok(service.buscarPorRespostaEItemCotacao(respostaCotacaoFornecedorId, itemCotacaoId));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar itens de resposta",
            description = "Lista itens de resposta de cotação do fornecedor de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ItemRespostaCotacaoFornecedorDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<ItemRespostaCotacaoFornecedorDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @Operation(
            summary = "Listar itens por resposta",
            description = "Lista itens de resposta vinculados a uma resposta de cotação do fornecedor (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ItemRespostaCotacaoFornecedorDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Resposta não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/resposta/{respostaCotacaoFornecedorId}")
    public ResponseEntity<Page<ItemRespostaCotacaoFornecedorDto>> listarPorResposta(
            @PathVariable Long respostaCotacaoFornecedorId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorResposta(respostaCotacaoFornecedorId, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover item de resposta",
            description = "Remove um item de resposta de cotação do fornecedor pelo identificador.",
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
            summary = "Remover itens por resposta",
            description = "Remove todos os itens vinculados a uma resposta de cotação do fornecedor (dentro do tenant).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Itens removidos com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Resposta não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/resposta/{respostaCotacaoFornecedorId}")
    public ResponseEntity<Void> deletarPorResposta(@PathVariable Long respostaCotacaoFornecedorId) {
        service.deletarPorResposta(respostaCotacaoFornecedorId);
        return ResponseEntity.ok().build();
    }
}