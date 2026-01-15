package br.com.unicos.ms_departamento.controller;

import br.com.unicos.ms_departamento.dto.departamento.DepartamentoCreateRequestDto;
import br.com.unicos.ms_departamento.dto.departamento.DepartamentoResponseDto;
import br.com.unicos.ms_departamento.dto.departamento.DepartamentoUpdateRequestDto;
import br.com.unicos.ms_departamento.enums.StatusDepartamento;
import br.com.unicos.ms_departamento.service.DepartamentoService;
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
 * Controller responsável pelos endpoints de Departamentos.
 */
@RestController
@RequestMapping("/v1/departamentos")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Departamentos",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de departamentos."
)
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar departamento",
            description = "Cria um novo departamento.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Departamento criado com sucesso",
                            content = @Content(schema = @Schema(implementation = DepartamentoResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<DepartamentoResponseDto> salvar(@Valid @RequestBody DepartamentoCreateRequestDto request) {
        DepartamentoResponseDto response = departamentoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar departamento",
            description = "Atualiza os dados de um departamento existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Departamento atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = DepartamentoResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoResponseDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DepartamentoUpdateRequestDto request
    ) {
        return ResponseEntity.ok(departamentoService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar departamento por ID",
            description = "Retorna os dados de um departamento específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = DepartamentoResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(departamentoService.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar departamentos",
            description = "Lista departamentos de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = DepartamentoResponseDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<DepartamentoResponseDto>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(departamentoService.listar(pageable));
    }

    @Operation(
            summary = "Listar departamentos por status",
            description = "Lista departamentos por status de forma paginada dentro do tenant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = DepartamentoResponseDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<DepartamentoResponseDto>> listarPorStatus(
            @PathVariable StatusDepartamento status,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(departamentoService.listarPorStatus(status, pageable));
    }

    @Operation(
            summary = "Listar departamentos filhos",
            description = "Lista departamentos filhos de um departamento pai de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = DepartamentoResponseDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Departamento pai não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{departamentoPaiId}/filhos")
    public ResponseEntity<Page<DepartamentoResponseDto>> listarFilhos(
            @PathVariable Long departamentoPaiId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(departamentoService.listarFilhos(departamentoPaiId, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover departamento",
            description = "Remove um departamento pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Departamento removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        departamentoService.deletar(id);
        return ResponseEntity.ok().build();
    }
}
