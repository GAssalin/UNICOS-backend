package br.com.unicos.ms_filial.controller;

import br.com.unicos.ms_filial.dto.filial.FilialCreateRequest;
import br.com.unicos.ms_filial.dto.filial.FilialResponse;
import br.com.unicos.ms_filial.dto.filial.FilialUpdateRequest;
import br.com.unicos.ms_filial.enums.StatusFilial;
import br.com.unicos.ms_filial.service.FilialService;
import br.com.unicos.ms_filial.service.UtilsService;
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
@RequestMapping("/v1/filiais")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Filiais",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de filiais."
)
public class FilialController {

    private final UtilsService utilsService;
    private final FilialService filialService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar filial",
            description = "Cria uma nova filial para uma empresa.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Filial criada com sucesso",
                            content = @Content(schema = @Schema(implementation = FilialResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<FilialResponse> salvar(@Valid @RequestBody FilialCreateRequest request) {
        if (utilsService.verificarPermissao("FILIAL_CRIAR")) {
            FilialResponse response = filialService.salvar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar filial",
            description = "Atualiza os dados de uma filial existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Filial atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = FilialResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Filial não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<FilialResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FilialUpdateRequest request
    ) {
        if (utilsService.verificarPermissao("FILIAL_EDITAR"))
            return ResponseEntity.ok(filialService.atualizar(id, request));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar filial por ID",
            description = "Retorna os dados de uma filial específica.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = FilialResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Filial não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<FilialResponse> buscarPorId(@PathVariable Long id) {
        if (utilsService.verificarPermissao("FILIAL_LISTAR"))
            return ResponseEntity.ok(filialService.buscarPorId(id));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar filiais por empresa",
            description = "Lista as filiais vinculadas a uma empresa de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = FilialResponse.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<Page<FilialResponse>> listarPorEmpresa(
            @PathVariable Long empresaId,
            @ParameterObject Pageable pageable
    ) {
        if (utilsService.verificarPermissao("FILIAL_LISTAR"))
            return ResponseEntity.ok(filialService.listarPorEmpresa(empresaId, pageable));
        else
            return ResponseEntity.status(403).build();
    }

    @Operation(
            summary = "Listar filiais por empresa e status",
            description = "Lista as filiais de uma empresa filtrando pelo status operacional.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = FilialResponse.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/empresa/{empresaId}/status/{status}")
    public ResponseEntity<Page<FilialResponse>> listarPorEmpresaEStatus(
            @PathVariable Long empresaId,
            @PathVariable StatusFilial status,
            @ParameterObject Pageable pageable
    ) {
        if (utilsService.verificarPermissao("FILIAL_LISTAR"))
            return ResponseEntity.ok(filialService.listarPorEmpresaEStatus(empresaId, status, pageable));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover filial",
            description = "Remove uma filial pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filial removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Filial não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (utilsService.verificarPermissao("FILIAL_EXCLUIR")) {
            filialService.deletar(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
