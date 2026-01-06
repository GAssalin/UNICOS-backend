package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.marca.MarcaListDTO;
import br.com.unicos.ms_produtos.dto.marca.MarcaRequest;
import br.com.unicos.ms_produtos.dto.marca.MarcaResponse;
import br.com.unicos.ms_produtos.service.interfaces.MarcaService;
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
@RequestMapping("/v1/marcas")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Marcas",
        description = "Gerencia o cadastro e as consultas de marcas de produtos."
)
public class MarcaController {

    private final MarcaService marcaService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar marca",
            description = "Cria uma nova marca no sistema UniCoS.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Marca criada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = MarcaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'MARCA_CRIAR')")
    @PostMapping
    public ResponseEntity<MarcaResponse> salvar(
            @Valid @RequestBody MarcaRequest request
    ) {
        MarcaResponse response = marcaService.salvar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar marca",
            description = "Atualiza os dados de uma marca existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Marca atualizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = MarcaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Marca não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'MARCA_ATUALIZAR')")
    @PutMapping("/{id}")
    public ResponseEntity<MarcaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MarcaRequest request
    ) {
        return ResponseEntity.ok(marcaService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar marca por ID",
            description = "Retorna os dados completos de uma marca.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = MarcaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Marca não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'MARCA_VISUALIZAR')")
    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponse> buscarPorId(@PathVariable Long id) {
        Optional<MarcaResponse> resultado = marcaService.buscarPorId(id);
        return resultado
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar todas as marcas",
            description = "Retorna a lista completa de marcas cadastradas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = MarcaResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'MARCA_LISTAR')")
    @GetMapping
    public ResponseEntity<List<MarcaResponse>> listarTodas() {
        return ResponseEntity.ok(marcaService.listarTodas());
    }

    @Operation(
            summary = "Listar marcas (modo simples)",
            description = "Retorna marcas com informações resumidas (ID e nome).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = MarcaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'MARCA_LISTAR')")
    @GetMapping("/simples")
    public ResponseEntity<List<MarcaListDTO>> listarSimples() {
        return ResponseEntity.ok(marcaService.listarSimples());
    }

    @Operation(
            summary = "Buscar marcas por nome",
            description = "Busca marcas cujo nome contenha o valor informado (ignore case).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = MarcaResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'MARCA_LISTAR')")
    @GetMapping("/buscar")
    public ResponseEntity<List<MarcaResponse>> buscarPorNome(
            @RequestParam String nome
    ) {
        return ResponseEntity.ok(marcaService.buscarPorNome(nome));
    }

    @Operation(
            summary = "Verificar existência de marca por nome",
            description = "Retorna true se existir uma marca com o nome informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resultado retornado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = Boolean.class)
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'MARCA_LISTAR')")
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existePorNome(@RequestParam String nome) {
        return ResponseEntity.ok(marcaService.existePorNome(nome));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover marca",
            description = "Remove uma marca pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Marca removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Marca não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'MARCA_REMOVER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        marcaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
