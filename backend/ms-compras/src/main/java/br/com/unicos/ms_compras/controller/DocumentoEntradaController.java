package br.com.unicos.ms_compras.controller;

import br.com.unicos.ms_compras.dto.DocumentoEntradaDto;
import br.com.unicos.ms_compras.service.DocumentoEntradaService;
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
 * Controller responsável pelos endpoints de Documentos de Entrada.
 */
@RestController
@RequestMapping("/v1/documentos-entrada")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Documentos de Entrada",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de documentos de entrada."
)
public class DocumentoEntradaController {

    private final DocumentoEntradaService service;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar documento de entrada",
            description = "Cria um novo documento de entrada vinculado a um recebimento de compra.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Documento de entrada criado com sucesso",
                            content = @Content(schema = @Schema(implementation = DocumentoEntradaDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<DocumentoEntradaDto> salvar(@Valid @RequestBody DocumentoEntradaDto request) {
        DocumentoEntradaDto response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar documento de entrada",
            description = "Atualiza os dados de um documento de entrada existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Documento de entrada atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = DocumentoEntradaDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Documento de entrada não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<DocumentoEntradaDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DocumentoEntradaDto request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar documento de entrada por ID",
            description = "Retorna os dados de um documento de entrada específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = DocumentoEntradaDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Documento de entrada não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoEntradaDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar documentos de entrada",
            description = "Lista documentos de entrada de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = DocumentoEntradaDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<DocumentoEntradaDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @Operation(
            summary = "Listar documentos de entrada por recebimento",
            description = "Lista documentos de entrada vinculados a um recebimento de compra específico (paginado, dentro do tenant).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = DocumentoEntradaDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Recebimento de compra não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/recebimento/{recebimentoCompraId}")
    public ResponseEntity<Page<DocumentoEntradaDto>> listarPorRecebimento(
            @PathVariable Long recebimentoCompraId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPorRecebimento(recebimentoCompraId, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover documento de entrada",
            description = "Remove um documento de entrada pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Documento removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Documento de entrada não encontrado"),
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
            summary = "Remover documentos de entrada por recebimento",
            description = "Remove todos os documentos de entrada vinculados a um recebimento de compra (dentro do tenant).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Documentos removidos com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Recebimento de compra não encontrado"),
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