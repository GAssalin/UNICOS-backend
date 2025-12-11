package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.marca.MarcaListDTO;
import br.com.unicos.ms_produtos.dto.marca.MarcaRequest;
import br.com.unicos.ms_produtos.dto.marca.MarcaResponse;
import br.com.unicos.ms_produtos.service.MarcaService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento de marcas de produtos.
 */
@RestController
@RequestMapping("/v1/marcas")
@RequiredArgsConstructor
@Tag(
        name = "Marcas",
        description = "Gerencia o cadastro e as consultas de marcas de produtos."
)
public class MarcaController {

    private final MarcaService marcaService;

    // ============================================================
    // Criar marca
    // ============================================================

    @PreAuthorize("hasAuthority('MARCA_CRIAR')")
    @Operation(
            summary = "Criar nova marca",
            description = "Cadastra uma nova marca no sistema UniCoS.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Marca criada com sucesso",
                            content = @Content(schema = @Schema(implementation = MarcaResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
            }
    )
    @PostMapping
    public ResponseEntity<MarcaResponse> salvar(
            @Valid @RequestBody MarcaRequest request) {

        MarcaResponse response = marcaService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // Atualizar marca
    // ============================================================

    @PreAuthorize("hasAuthority('MARCA_ATUALIZAR')")
    @Operation(
            summary = "Atualizar marca",
            description = "Atualiza os dados de uma marca existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Marca atualizada",
                            content = @Content(schema = @Schema(implementation = MarcaResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Marca não encontrada")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<MarcaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MarcaRequest request) {

        MarcaResponse response = marcaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @PreAuthorize("hasAuthority('MARCA_LISTAR')")
    @Operation(
            summary = "Buscar marca por ID",
            description = "Retorna os dados completos de uma marca.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Marca encontrada",
                            content = @Content(schema = @Schema(implementation = MarcaResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Marca não encontrada")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponse> buscarPorId(@PathVariable Long id) {

        Optional<MarcaResponse> resultado = marcaService.buscarPorId(id);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar todas
    // ============================================================

    @PreAuthorize("hasAuthority('MARCA_LISTAR')")
    @Operation(
            summary = "Listar todas as marcas",
            description = "Retorna a lista completa de marcas cadastradas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = MarcaResponse.class))
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<MarcaResponse>> listarTodas() {
        return ResponseEntity.ok(marcaService.listarTodas());
    }

    // ============================================================
    // Listar simples
    // ============================================================

    @PreAuthorize("hasAuthority('MARCA_LISTAR')")
    @Operation(
            summary = "Listar marcas em formato simplificado",
            description = "Retorna lista reduzida contendo apenas informações básicas da marca.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista simplificada retornada",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = MarcaListDTO.class))
                            )
                    )
            }
    )
    @GetMapping("/simples")
    public ResponseEntity<List<MarcaListDTO>> listarSimples() {
        return ResponseEntity.ok(marcaService.listarSimples());
    }

    // ============================================================
    // Buscar por nome
    // ============================================================

    @PreAuthorize("hasAuthority('MARCA_LISTAR')")
    @Operation(
            summary = "Buscar marcas por nome",
            description = "Busca marcas cujo nome contenha o valor informado (ignore case).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = MarcaResponse.class))
                            )
                    )
            }
    )
    @GetMapping("/buscar")
    public ResponseEntity<List<MarcaResponse>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(marcaService.buscarPorNome(nome));
    }

    // ============================================================
    // Deletar marca
    // ============================================================

    @PreAuthorize("hasAuthority('MARCA_EXCLUIR')")
    @Operation(
            summary = "Excluir marca",
            description = "Remove uma marca do sistema.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Marca removida"),
                    @ApiResponse(responseCode = "404", description = "Marca não encontrada")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        marcaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // Verificar existência por nome
    // ============================================================

    @PreAuthorize("hasAuthority('MARCA_LISTAR')")
    @Operation(
            summary = "Verificar existência de marca",
            description = "Retorna true/false indicando se já existe uma marca com o nome informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resultado retornado",
                            content = @Content(schema = @Schema(implementation = Boolean.class))
                    )
            }
    )
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existePorNome(@RequestParam String nome) {
        return ResponseEntity.ok(marcaService.existePorNome(nome));
    }
}
