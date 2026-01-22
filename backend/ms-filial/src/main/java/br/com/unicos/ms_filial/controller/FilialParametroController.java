package br.com.unicos.ms_filial.controller;

import br.com.unicos.ms_filial.dto.parametro.FilialParametroCreateRequest;
import br.com.unicos.ms_filial.dto.parametro.FilialParametroResponse;
import br.com.unicos.ms_filial.dto.parametro.FilialParametroUpdateRequest;
import br.com.unicos.ms_filial.service.FilialParametroService;
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
@RequestMapping("/v1/filiais-parametros")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Parâmetros de Filial",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de parâmetros configuráveis por filial."
)
public class FilialParametroController {

    private final FilialParametroService parametroService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar parâmetro de filial",
            description = "Cria um novo parâmetro (chave/valor) para uma filial.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Parâmetro criado com sucesso",
                            content = @Content(schema = @Schema(implementation = FilialParametroResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<FilialParametroResponse> salvar(@Valid @RequestBody FilialParametroCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parametroService.salvar(request));
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar parâmetro de filial",
            description = "Atualiza os dados de um parâmetro existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Parâmetro atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = FilialParametroResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Parâmetro não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<FilialParametroResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FilialParametroUpdateRequest request
    ) {
        return ResponseEntity.ok(parametroService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar parâmetro por ID",
            description = "Retorna os dados de um parâmetro específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = FilialParametroResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Parâmetro não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<FilialParametroResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(parametroService.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar parâmetros por filial",
            description = "Lista os parâmetros vinculados a uma filial de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = FilialParametroResponse.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/filial/{filialId}")
    public ResponseEntity<Page<FilialParametroResponse>> listarPorFilial(
            @PathVariable Long filialId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(parametroService.listarPorFilial(filialId, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover parâmetro",
            description = "Remove um parâmetro pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Parâmetro removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Parâmetro não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        parametroService.deletar(id);
        return ResponseEntity.ok().build();
    }
}
