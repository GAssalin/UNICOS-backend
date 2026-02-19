package br.com.unicos.ms_estoque.controller;

import br.com.unicos.ms_estoque.dto.estoque.EstoqueCreateRequestDto;
import br.com.unicos.ms_estoque.dto.estoque.EstoqueResponseDto;
import br.com.unicos.ms_estoque.dto.estoque.EstoqueUpdateRequestDto;
import br.com.unicos.ms_estoque.enums.StatusEstoque;
import br.com.unicos.ms_estoque.service.EstoqueService;
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
 * Controller responsável pelos endpoints de Estoques.
 */
@RestController
@RequestMapping("/v1/estoques")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Estoques",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de estoques."
)
public class EstoqueController {

    private final EstoqueService estoqueService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar estoque",
            description = "Cria um novo estoque.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Estoque criado com sucesso",
                            content = @Content(schema = @Schema(implementation = EstoqueResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<EstoqueResponseDto> salvar(@Valid @RequestBody EstoqueCreateRequestDto request) {
        EstoqueResponseDto response = estoqueService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar estoque",
            description = "Atualiza os dados de um estoque existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Estoque atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = EstoqueResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Estoque não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EstoqueResponseDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EstoqueUpdateRequestDto request
    ) {
        return ResponseEntity.ok(estoqueService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar estoque por ID",
            description = "Retorna os dados de um estoque específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = EstoqueResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Estoque não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EstoqueResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(estoqueService.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar estoques",
            description = "Lista estoques de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = EstoqueResponseDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<EstoqueResponseDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(estoqueService.listar(pageable));
    }

    @Operation(
            summary = "Listar estoques por status",
            description = "Lista estoques por status de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = EstoqueResponseDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<EstoqueResponseDto>> listarPorStatus(
            @PathVariable StatusEstoque status,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(estoqueService.listarPorStatus(status, pageable));
    }

    @Operation(
            summary = "Listar estoques filhos",
            description = "Lista estoques filhos de um estoque pai de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = EstoqueResponseDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Estoque pai não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{estoquePaiId}/filhos")
    public ResponseEntity<Page<EstoqueResponseDto>> listarFilhos(
            @PathVariable Long estoquePaiId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(estoqueService.listarFilhos(estoquePaiId, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover estoque",
            description = "Remove um estoque pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estoque removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Estoque não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        estoqueService.deletar(id);
        return ResponseEntity.ok().build();
    }
}
