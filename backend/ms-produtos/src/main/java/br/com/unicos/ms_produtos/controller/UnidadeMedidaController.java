package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaListDTO;
import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaRequest;
import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaResponse;
import br.com.unicos.ms_produtos.service.UnidadeMedidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento de unidades de medida.
 */
@RestController
@RequestMapping("/v1/unidades-medida")
@RequiredArgsConstructor
@Tag(
        name = "Unidades de Medida",
        description = "Gerencia unidades de medida utilizadas no cadastro de produtos."
)
public class UnidadeMedidaController {

    private final UnidadeMedidaService unidadeMedidaService;

    // ============================================================
    // CRIAR
    // ============================================================

    @Operation(
            summary = "Criar unidade de medida",
            description = "Cadastra uma nova unidade de medida no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Unidade criada",
                            content = @Content(schema = @Schema(implementation = UnidadeMedidaResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
            }
    )
    @PostMapping
    public ResponseEntity<UnidadeMedidaResponse> salvar(
            @Valid @RequestBody UnidadeMedidaRequest request) {

        UnidadeMedidaResponse response = unidadeMedidaService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // ATUALIZAR
    // ============================================================

    @Operation(
            summary = "Atualizar unidade de medida",
            description = "Atualiza os dados de uma unidade de medida existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Unidade atualizada",
                            content = @Content(schema = @Schema(implementation = UnidadeMedidaResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeMedidaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UnidadeMedidaRequest request) {

        UnidadeMedidaResponse response = unidadeMedidaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // DELETAR
    // ============================================================

    @Operation(
            summary = "Remover unidade de medida",
            description = "Exclui uma unidade de medida pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Unidade removida"),
                    @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        unidadeMedidaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // BUSCAR POR ID
    // ============================================================

    @Operation(
            summary = "Buscar unidade por ID",
            description = "Retorna os dados completos de uma unidade de medida.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Unidade encontrada",
                            content = @Content(schema = @Schema(implementation = UnidadeMedidaResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeMedidaResponse> buscarPorId(@PathVariable Long id) {

        Optional<UnidadeMedidaResponse> resultado =
                unidadeMedidaService.buscarPorId(id);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // LISTAGEM DETALHADA
    // ============================================================

    @Operation(
            summary = "Listar todas as unidades",
            description = "Retorna todas as unidades cadastradas com informações detalhadas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UnidadeMedidaResponse.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<UnidadeMedidaResponse>> listarTodas() {
        return ResponseEntity.ok(unidadeMedidaService.listarTodas());
    }

    // ============================================================
    // LISTAGEM SIMPLES
    // ============================================================

    @Operation(
            summary = "Listar unidades (simples)",
            description = "Retorna lista simplificada contendo ID, nome e sigla.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UnidadeMedidaListDTO.class)))
                    )
            }
    )
    @GetMapping("/simples")
    public ResponseEntity<List<UnidadeMedidaListDTO>> listarSimples() {
        return ResponseEntity.ok(unidadeMedidaService.listarSimples());
    }

    // ============================================================
    // BUSCAR POR NOME EXATO
    // ============================================================

    @Operation(
            summary = "Buscar unidade por nome exato",
            description = "Retorna unidade cujo nome seja exatamente igual ao informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Unidade encontrada",
                            content = @Content(schema = @Schema(implementation = UnidadeMedidaResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Nenhuma unidade corresponde ao nome informado")
            }
    )
    @GetMapping("/nome")
    public ResponseEntity<UnidadeMedidaResponse> buscarPorNome(
            @RequestParam String nome) {

        Optional<UnidadeMedidaResponse> resultado =
                unidadeMedidaService.buscarPorNome(nome);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // BUSCAR POR SIGLA EXATA
    // ============================================================

    @Operation(
            summary = "Buscar unidade por sigla",
            description = "Retorna unidade cuja sigla seja exatamente igual à informada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Unidade encontrada",
                            content = @Content(schema = @Schema(implementation = UnidadeMedidaResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Nenhuma unidade corresponde à sigla informada")
            }
    )
    @GetMapping("/sigla")
    public ResponseEntity<UnidadeMedidaResponse> buscarPorSigla(
            @RequestParam String sigla) {

        Optional<UnidadeMedidaResponse> resultado =
                unidadeMedidaService.buscarPorSigla(sigla);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // BUSCA POR NOME CONTENDO
    // ============================================================

    @Operation(
            summary = "Buscar unidades pelo nome (contém)",
            description = "Retorna unidades cujo nome contenha o termo informado (ignore case).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UnidadeMedidaListDTO.class)))
                    )
            }
    )
    @GetMapping("/buscar")
    public ResponseEntity<List<UnidadeMedidaListDTO>> buscarPorNomeContendo(
            @RequestParam String nome) {

        return ResponseEntity.ok(
                unidadeMedidaService.buscarPorNomeContendo(nome)
        );
    }

    // ============================================================
    // VERIFICAR SIGLA EXISTENTE
    // ============================================================

    @Operation(
            summary = "Verificar existência da sigla",
            description = "Retorna true se já existir uma unidade com a sigla informada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resultado retornado",
                            content = @Content(schema = @Schema(implementation = Boolean.class))
                    )
            }
    )
    @GetMapping("/sigla/existe")
    public ResponseEntity<Boolean> verificarSiglaExistente(
            @RequestParam String sigla) {

        return ResponseEntity.ok(
                unidadeMedidaService.verificarSiglaExistente(sigla)
        );
    }
}
