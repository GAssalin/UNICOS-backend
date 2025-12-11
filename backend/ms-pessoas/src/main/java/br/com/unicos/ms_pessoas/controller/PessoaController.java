package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaResponse;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.service.interfaces.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller responsável pelas operações genéricas relacionadas à entidade {@link Pessoa},
 * contemplando consultas independentes do tipo (Física ou Jurídica).
 * <p>
 * Este controller atende buscas por ID, nome, nome exato e tipo de pessoa,
 * servindo como ponto central de consulta para diversas integrações do UniCoS.
 */
@RestController
@RequestMapping("/v1/pessoas")
@RequiredArgsConstructor
@Tag(
        name = "Pessoas",
        description = "Consultas gerais de pessoas (físicas e jurídicas) por ID, nome, nome exato e tipo."
)
public class PessoaController {

    private final PessoaService service;

    // ============================================================
    // Buscar por ID
    // ============================================================

    @Operation(
            summary = "Buscar pessoa por ID",
            description = "Retorna os dados completos de uma pessoa com base no seu identificador único.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pessoa encontrada",
                            content = @Content(schema = @Schema(implementation = PessoaResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Pessoa não encontrada"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar todas
    // ============================================================

    @Operation(
            summary = "Listar todas as pessoas",
            description = "Retorna todas as pessoas cadastradas (físicas e jurídicas).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaListDTO.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<PessoaListDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // ============================================================
    // Listar por nome (contains)
    // ============================================================

    @Operation(
            summary = "Listar pessoas por nome contendo termo",
            description = "Retorna todas as pessoas cujo nome contenha o termo informado (busca parcial, ignore case).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista filtrada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaListDTO.class)))
                    )
            }
    )
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<PessoaListDTO>> listarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorNome(nome));
    }

    // ============================================================
    // Listar por nome exato
    // ============================================================

    @Operation(
            summary = "Listar pessoas por nome exato",
            description = "Retorna todas as pessoas cujo nome seja exatamente igual ao informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaListDTO.class)))
                    )
            }
    )
    @GetMapping("/nome-exato/{nome}")
    public ResponseEntity<List<PessoaListDTO>> listarPorNomeExato(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorNomeExato(nome));
    }

    // ============================================================
    // Listar por tipo (FISICA / JURIDICA)
    // ============================================================

    @Operation(
            summary = "Listar pessoas por tipo (Física ou Jurídica)",
            description = """
                    Retorna todas as pessoas do tipo especificado. \
                    Valores aceitos: FISICA, JURIDICA (case insensitive).
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaListDTO.class)))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Tipo de pessoa inválido"
                    )
            }
    )
    @GetMapping("/tipo/{tipoPessoa}")
    public ResponseEntity<List<PessoaListDTO>> listarPorTipo(@PathVariable String tipoPessoa) {
        return ResponseEntity.ok(service.listarPorTipo(tipoPessoa));
    }
}
