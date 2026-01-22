package br.com.unicos.ms_departamento.controller;

import br.com.unicos.ms_departamento.dto.vinculo.VinculoDepartamentoFilialCreateRequestDto;
import br.com.unicos.ms_departamento.dto.vinculo.VinculoDepartamentoFilialResponseDto;
import br.com.unicos.ms_departamento.dto.vinculo.VinculoDepartamentoFilialUpdateRequestDto;
import br.com.unicos.ms_departamento.enums.StatusVinculoDepartamentoFilial;
import br.com.unicos.ms_departamento.service.UtilsService;
import br.com.unicos.ms_departamento.service.VinculoDepartamentoFilialService;
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
 * Controller responsável pelos endpoints de Vínculos Departamento x Filial.
 */
@RestController
@RequestMapping("/v1/vinculos-departamento-filial")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Vínculos Departamento x Filial",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de vínculos entre departamentos e filiais."
)
public class VinculoDepartamentoFilialController {

    private final UtilsService utilsService;
    private final VinculoDepartamentoFilialService vinculoService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar vínculo Departamento x Filial",
            description = "Cria um novo vínculo entre um departamento e uma filial.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Vínculo criado com sucesso",
                            content = @Content(schema = @Schema(implementation = VinculoDepartamentoFilialResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<VinculoDepartamentoFilialResponseDto> salvar(
            @Valid @RequestBody VinculoDepartamentoFilialCreateRequestDto request
    ) {
        if (utilsService.verificarPermissao("DEPARTAMENTO_VINCULO_FILIAL_CRIAR")) {
            VinculoDepartamentoFilialResponseDto response = vinculoService.salvar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar vínculo Departamento x Filial",
            description = "Atualiza os dados de um vínculo existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Vínculo atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = VinculoDepartamentoFilialResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<VinculoDepartamentoFilialResponseDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody VinculoDepartamentoFilialUpdateRequestDto request
    ) {
        if (utilsService.verificarPermissao("DEPARTAMENTO_VINCULO_FILIAL_EDITAR"))
            return ResponseEntity.ok(vinculoService.atualizar(id, request));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar vínculo por ID",
            description = "Retorna os dados de um vínculo Departamento x Filial específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = VinculoDepartamentoFilialResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<VinculoDepartamentoFilialResponseDto> buscarPorId(@PathVariable Long id) {
        if (utilsService.verificarPermissao("DEPARTAMENTO_VINCULO_FILIAL_LISTAR"))
            return ResponseEntity.ok(vinculoService.buscarPorId(id));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar vínculos por filial",
            description = "Lista vínculos Departamento x Filial vinculados a uma filial de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = VinculoDepartamentoFilialResponseDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/filial/{filialId}")
    public ResponseEntity<Page<VinculoDepartamentoFilialResponseDto>> listarPorFilial(
            @PathVariable Long filialId,
            @ParameterObject Pageable pageable
    ) {
        if (utilsService.verificarPermissao("DEPARTAMENTO_VINCULO_FILIAL_LISTAR"))
            return ResponseEntity.ok(vinculoService.listarPorFilial(filialId, pageable));
        else
            return ResponseEntity.status(403).build();
    }

    @Operation(
            summary = "Listar vínculos por filial e status",
            description = "Lista vínculos vinculados a uma filial filtrando por status, de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = VinculoDepartamentoFilialResponseDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/filial/{filialId}/status/{status}")
    public ResponseEntity<Page<VinculoDepartamentoFilialResponseDto>> listarPorFilialEStatus(
            @PathVariable Long filialId,
            @PathVariable StatusVinculoDepartamentoFilial status,
            @ParameterObject Pageable pageable
    ) {
        if (utilsService.verificarPermissao("DEPARTAMENTO_VINCULO_FILIAL_LISTAR"))
            return ResponseEntity.ok(vinculoService.listarPorFilialEStatus(filialId, status, pageable));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover vínculo Departamento x Filial",
            description = "Remove um vínculo pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vínculo removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (utilsService.verificarPermissao("DEPARTAMENTO_VINCULO_FILIAL_EXCLUIR")) {
            vinculoService.deletar(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
