package br.com.unicos.ms_filial.controller;

import br.com.unicos.ms_filial.dto.status.FilialStatusHistoricoCreateRequest;
import br.com.unicos.ms_filial.dto.status.FilialStatusHistoricoResponse;
import br.com.unicos.ms_filial.service.FilialStatusHistoricoService;
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

@RestController
@RequestMapping("/v1/filiais-status-historico")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Histórico de Status de Filial",
        description = "Endpoints para registro e consulta do histórico de mudanças de status das filiais."
)
public class FilialStatusHistoricoController {

    private final FilialStatusHistoricoService historicoService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Registrar histórico de status",
            description = "Registra uma mudança de status para uma filial (histórico/auditoria).",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Histórico registrado com sucesso",
                            content = @Content(schema = @Schema(implementation = FilialStatusHistoricoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<FilialStatusHistoricoResponse> salvar(
            @Valid @RequestBody FilialStatusHistoricoCreateRequest request
    ) {
        FilialStatusHistoricoResponse response = historicoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar histórico por ID",
            description = "Retorna os dados de um registro de histórico específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = FilialStatusHistoricoResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Histórico não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<FilialStatusHistoricoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(historicoService.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar histórico por filial",
            description = "Lista o histórico de mudanças de status de uma filial de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = FilialStatusHistoricoResponse.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/filial/{filialId}")
    public ResponseEntity<Page<FilialStatusHistoricoResponse>> listarPorFilial(
            @PathVariable Long filialId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(historicoService.listarPorFilial(filialId, pageable));
    }
}
