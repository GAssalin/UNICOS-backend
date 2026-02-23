package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.DivergenciaRecebimentoDto;
import br.com.unicos.ms_compras.service.DivergenciaRecebimentoService;
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
 * Controller responsável pelos endpoints de Divergências de Recebimento.
 */
@RestController
@RequestMapping("/v1/divergencias-recebimento")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Divergências de Recebimento",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de divergências de recebimento."
)
public class DivergenciaRecebimentoController {

    private final DivergenciaRecebimentoService service;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar divergência de recebimento",
            description = "Cria uma nova divergência vinculada a um item de recebimento de compra.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Divergência criada com sucesso",
                            content = @Content(schema = @Schema(implementation = DivergenciaRecebimentoDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<DivergenciaRecebimentoDto> salvar(@Valid @RequestBody DivergenciaRecebimentoDto request) {
        DivergenciaRecebimentoDto response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar divergência de recebimento",
            description = "Atualiza os dados de uma divergência existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Divergência atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = DivergenciaRecebimentoDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Divergência não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<DivergenciaRecebimentoDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DivergenciaRecebimentoDto request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar divergência por ID",
            description = "Retorna os dados de uma divergência específica.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = DivergenciaRecebimentoDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Divergência não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DivergenciaRecebimentoDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar divergências de recebimento",
            description = "Lista divergências de recebimento de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = DivergenciaRecebimentoDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<DivergenciaRecebimentoDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @Operation(
            summary = "Listar divergências por item de recebimento",
            description = "Lista divergências vinculadas a um item de recebimento de compra, de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = DivergenciaRecebimentoDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Item de recebimento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/item-recebimento/{itemRecebimentoCompraId}")
    public ResponseEntity<Page<DivergenciaRecebimentoDto>> listarPorItemRecebimento(
            @PathVariable Long itemRecebimentoCompraId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorItemRecebimento(itemRecebimentoCompraId, pageable));
    }

    @Operation(
            summary = "Listar divergências por tipo",
            description = "Lista divergências por tipo de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = DivergenciaRecebimentoDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Page<DivergenciaRecebimentoDto>> listarPorTipo(
            @PathVariable String tipo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorTipo(tipo, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover divergência",
            description = "Remove uma divergência pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Divergência removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Divergência não encontrada"),
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
            summary = "Remover divergências por item de recebimento",
            description = "Remove todas as divergências vinculadas a um item de recebimento de compra (dentro do tenant).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Divergências removidas com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Item de recebimento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/item-recebimento/{itemRecebimentoCompraId}")
    public ResponseEntity<Void> deletarPorItemRecebimento(@PathVariable Long itemRecebimentoCompraId) {
        service.deletarPorItemRecebimento(itemRecebimentoCompraId);
        return ResponseEntity.ok().build();
    }
}