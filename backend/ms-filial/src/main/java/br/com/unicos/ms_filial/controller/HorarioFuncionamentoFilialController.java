package br.com.unicos.ms_filial.controller;

import br.com.unicos.ms_filial.dto.horario.HorarioFuncionamentoFilialCreateRequest;
import br.com.unicos.ms_filial.dto.horario.HorarioFuncionamentoFilialResponse;
import br.com.unicos.ms_filial.dto.horario.HorarioFuncionamentoFilialUpdateRequest;
import br.com.unicos.ms_filial.service.HorarioFuncionamentoFilialService;
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
@RequestMapping("/v1/horarios-filial")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Horários de Filial",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de horários de funcionamento de filiais."
)
public class HorarioFuncionamentoFilialController {

    private final UtilsService utilsService;
    private final HorarioFuncionamentoFilialService horarioService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar horário de filial",
            description = "Cria um novo horário de funcionamento para uma filial (por dia da semana).",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Horário criado com sucesso",
                            content = @Content(schema = @Schema(implementation = HorarioFuncionamentoFilialResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<HorarioFuncionamentoFilialResponse> salvar(
            @Valid @RequestBody HorarioFuncionamentoFilialCreateRequest request
    ) {
        if (utilsService.verificarPermissao("FILIAL_HORARIO_CRIAR")) {
            HorarioFuncionamentoFilialResponse response = horarioService.salvar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar horário de filial",
            description = "Atualiza os dados de um horário de funcionamento existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Horário atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = HorarioFuncionamentoFilialResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Horário não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<HorarioFuncionamentoFilialResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody HorarioFuncionamentoFilialUpdateRequest request
    ) {
        if (utilsService.verificarPermissao("FILIAL_HORARIO_EDITAR"))
            return ResponseEntity.ok(horarioService.atualizar(id, request));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar horário de filial por ID",
            description = "Retorna os dados de um horário de funcionamento específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = HorarioFuncionamentoFilialResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Horário não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<HorarioFuncionamentoFilialResponse> buscarPorId(@PathVariable Long id) {
        if (utilsService.verificarPermissao("FILIAL_HORARIO_LISTAR"))
            return ResponseEntity.ok(horarioService.buscarPorId(id));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar horários por filial",
            description = "Lista os horários de funcionamento vinculados a uma filial de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = HorarioFuncionamentoFilialResponse.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/filial/{filialId}")
    public ResponseEntity<Page<HorarioFuncionamentoFilialResponse>> listarPorFilial(
            @PathVariable Long filialId,
            @ParameterObject Pageable pageable
    ) {
        if (utilsService.verificarPermissao("FILIAL_HORARIO_LISTAR"))
            return ResponseEntity.ok(horarioService.listarPorFilial(filialId, pageable));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover horário de filial",
            description = "Remove um horário de funcionamento pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Horário removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Horário não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (utilsService.verificarPermissao("FILIAL_HORARIO_EXCLUIR")) {
            horarioService.deletar(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
