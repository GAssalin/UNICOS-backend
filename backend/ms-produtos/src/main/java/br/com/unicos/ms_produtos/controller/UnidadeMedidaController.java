package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaListDTO;
import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaRequest;
import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaResponse;
import br.com.unicos.ms_produtos.service.interfaces.UnidadeMedidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/unidades-medida")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Unidades de Medida",
        description = "Gerencia unidades de medida utilizadas no cadastro de produtos."
)
public class UnidadeMedidaController {

    private final UnidadeMedidaService unidadeMedidaService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar unidade de medida",
            description = "Cadastra uma nova unidade de medida no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Unidade criada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = UnidadeMedidaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'UNIDADE_MEDIDA_CRIAR')")
    @PostMapping
    public ResponseEntity<UnidadeMedidaResponse> salvar(
            @Valid @RequestBody UnidadeMedidaRequest request
    ) {
        UnidadeMedidaResponse response = unidadeMedidaService.salvar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar unidade de medida",
            description = "Atualiza os dados de uma unidade de medida existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Unidade atualizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = UnidadeMedidaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'UNIDADE_MEDIDA_ATUALIZAR')")
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeMedidaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UnidadeMedidaRequest request
    ) {
        return ResponseEntity.ok(unidadeMedidaService.atualizar(id, request));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover unidade de medida",
            description = "Remove uma unidade de medida pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Unidade removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'UNIDADE_MEDIDA_REMOVER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        unidadeMedidaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar unidade de medida por ID",
            description = "Retorna os dados completos de uma unidade de medida.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = UnidadeMedidaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'UNIDADE_MEDIDA_VISUALIZAR')")
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeMedidaResponse> buscarPorId(@PathVariable Long id) {
        Optional<UnidadeMedidaResponse> resultado =
                unidadeMedidaService.buscarPorId(id);
        return resultado
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar todas as unidades de medida",
            description = "Retorna todas as unidades de medida cadastradas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = UnidadeMedidaResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'UNIDADE_MEDIDA_LISTAR')")
    @GetMapping
    public ResponseEntity<List<UnidadeMedidaResponse>> listarTodas() {
        return ResponseEntity.ok(unidadeMedidaService.listarTodas());
    }

    @Operation(
            summary = "Listar unidades de medida (simples)",
            description = "Retorna lista simplificada contendo ID, nome e sigla.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = UnidadeMedidaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'UNIDADE_MEDIDA_LISTAR')")
    @GetMapping("/simples")
    public ResponseEntity<List<UnidadeMedidaListDTO>> listarSimples() {
        return ResponseEntity.ok(unidadeMedidaService.listarSimples());
    }

    // =============================================================
    // BUSCAS
    // =============================================================

    @Operation(
            summary = "Buscar unidade por nome exato",
            description = "Retorna unidade cujo nome seja exatamente igual ao informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Unidade encontrada",
                            content = @Content(
                                    schema = @Schema(implementation = UnidadeMedidaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão")
            }
    )
    @PreAuthorize("hasPermission(null, 'UNIDADE_MEDIDA_VISUALIZAR')")
    @GetMapping("/nome")
    public ResponseEntity<UnidadeMedidaResponse> buscarPorNome(
            @RequestParam String nome
    ) {
        return unidadeMedidaService.buscarPorNome(nome)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Buscar unidade por sigla",
            description = "Retorna unidade cuja sigla seja exatamente igual à informada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Unidade encontrada",
                            content = @Content(
                                    schema = @Schema(implementation = UnidadeMedidaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão")
            }
    )
    @PreAuthorize("hasPermission(null, 'UNIDADE_MEDIDA_VISUALIZAR')")
    @GetMapping("/sigla")
    public ResponseEntity<UnidadeMedidaResponse> buscarPorSigla(
            @RequestParam String sigla
    ) {
        return unidadeMedidaService.buscarPorSigla(sigla)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Buscar unidades pelo nome (contém)",
            description = "Retorna unidades cujo nome contenha o termo informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = UnidadeMedidaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão")
            }
    )
    @PreAuthorize("hasPermission(null, 'UNIDADE_MEDIDA_LISTAR')")
    @GetMapping("/buscar")
    public ResponseEntity<List<UnidadeMedidaListDTO>> buscarPorNomeContendo(
            @RequestParam String nome
    ) {
        return ResponseEntity.ok(unidadeMedidaService.buscarPorNomeContendo(nome));
    }

    // =============================================================
    // VALIDAÇÃO
    // =============================================================

    @Operation(
            summary = "Verificar existência de sigla",
            description = "Retorna true se já existir uma unidade de medida com a sigla informada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resultado retornado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = Boolean.class)
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão")
            }
    )
    @PreAuthorize("hasPermission(null, 'UNIDADE_MEDIDA_LISTAR')")
    @GetMapping("/sigla/existe")
    public ResponseEntity<Boolean> verificarSiglaExistente(
            @RequestParam String sigla
    ) {
        return ResponseEntity.ok(unidadeMedidaService.verificarSiglaExistente(sigla));
    }
}
