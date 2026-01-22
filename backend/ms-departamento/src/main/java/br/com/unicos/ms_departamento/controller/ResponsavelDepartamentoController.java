package br.com.unicos.ms_departamento.controller;

import br.com.unicos.ms_departamento.dto.responsavel.ResponsavelDepartamentoCreateRequestDto;
import br.com.unicos.ms_departamento.dto.responsavel.ResponsavelDepartamentoResponseDto;
import br.com.unicos.ms_departamento.dto.responsavel.ResponsavelDepartamentoUpdateRequestDto;
import br.com.unicos.ms_departamento.enums.StatusResponsavelDepartamento;
import br.com.unicos.ms_departamento.service.ResponsavelDepartamentoService;
import br.com.unicos.ms_departamento.service.UtilsService;
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
 * Controller responsável pelos endpoints de Responsáveis por Departamento.
 */
@RestController
@RequestMapping("/v1/responsaveis-departamento")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Responsáveis de Departamento",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de responsáveis por departamento."
)
public class ResponsavelDepartamentoController {

    private final UtilsService utilsService;
    private final ResponsavelDepartamentoService responsavelService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar responsável por departamento",
            description = "Cria um novo responsável (gestor/ponto focal) para um departamento.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Responsável criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ResponsavelDepartamentoResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ResponsavelDepartamentoResponseDto> salvar(
            @Valid @RequestBody ResponsavelDepartamentoCreateRequestDto request
    ) {
        if (utilsService.verificarPermissao("DEPARTAMENTO_RESPONSAVEL_CRIAR")) {
            ResponsavelDepartamentoResponseDto response = responsavelService.salvar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar responsável por departamento",
            description = "Atualiza os dados de um responsável por departamento existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Responsável atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ResponsavelDepartamentoResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Responsável não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ResponsavelDepartamentoResponseDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResponsavelDepartamentoUpdateRequestDto request
    ) {
        if (utilsService.verificarPermissao("DEPARTAMENTO_RESPONSAVEL_EDITAR"))
            return ResponseEntity.ok(responsavelService.atualizar(id, request));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar responsável por ID",
            description = "Retorna os dados de um responsável por departamento específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ResponsavelDepartamentoResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Responsável não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResponsavelDepartamentoResponseDto> buscarPorId(@PathVariable Long id) {
        if (utilsService.verificarPermissao("DEPARTAMENTO_RESPONSAVEL_LISTAR"))
            return ResponseEntity.ok(responsavelService.buscarPorId(id));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar responsáveis por departamento",
            description = "Lista responsáveis vinculados a um departamento de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ResponsavelDepartamentoResponseDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/departamento/{departamentoId}")
    public ResponseEntity<Page<ResponsavelDepartamentoResponseDto>> listarPorDepartamento(
            @PathVariable Long departamentoId,
            @ParameterObject Pageable pageable
    ) {
        if (utilsService.verificarPermissao("DEPARTAMENTO_RESPONSAVEL_LISTAR"))
            return ResponseEntity.ok(responsavelService.listarPorDepartamento(departamentoId, pageable));
        else
            return ResponseEntity.status(403).build();
    }

    @Operation(
            summary = "Listar responsáveis por departamento e status",
            description = "Lista responsáveis vinculados a um departamento filtrando por status, de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ResponsavelDepartamentoResponseDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/departamento/{departamentoId}/status/{status}")
    public ResponseEntity<Page<ResponsavelDepartamentoResponseDto>> listarPorDepartamentoEStatus(
            @PathVariable Long departamentoId,
            @PathVariable StatusResponsavelDepartamento status,
            @ParameterObject Pageable pageable
    ) {
        if (utilsService.verificarPermissao("DEPARTAMENTO_RESPONSAVEL_LISTAR"))
            return ResponseEntity.ok(responsavelService.listarPorDepartamentoEStatus(departamentoId, status, pageable));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover responsável por departamento",
            description = "Remove um responsável por departamento pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responsável removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Responsável não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (utilsService.verificarPermissao("DEPARTAMENTO_RESPONSAVEL_EXCLUIR")) {
            responsavelService.deletar(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
