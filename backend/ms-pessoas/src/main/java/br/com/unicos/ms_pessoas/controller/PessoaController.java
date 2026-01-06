package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaResponse;
import br.com.unicos.ms_pessoas.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/pessoas")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Pessoas",
        description = "Endpoints de consulta genérica para pessoas físicas e jurídicas."
)
public class PessoaController {

    private final PessoaService pessoaService;

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar pessoa por ID",
            description = "Retorna os dados de uma pessoa (física ou jurídica) pelo identificador.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = PessoaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PESSOA_VISUALIZAR')")
    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponse> buscarPorId(@PathVariable Long id) {
        Optional<PessoaResponse> response = pessoaService.buscarPorId(id);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar todas as pessoas",
            description = "Retorna todas as pessoas cadastradas (PF e PJ).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PESSOA_LISTAR')")
    @GetMapping
    public ResponseEntity<List<PessoaListDTO>> listarTodas() {
        return ResponseEntity.ok(pessoaService.listarTodas());
    }

    @Operation(
            summary = "Listar pessoas por nome parcial",
            description = "Retorna pessoas cujo nome contenha o valor informado (ignore case).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PESSOA_LISTAR')")
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<PessoaListDTO>> listarPorNome(
            @PathVariable String nome
    ) {
        return ResponseEntity.ok(pessoaService.listarPorNome(nome));
    }

    @Operation(
            summary = "Listar pessoas por nome exato",
            description = "Retorna pessoas cujo nome seja exatamente igual ao informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PESSOA_LISTAR')")
    @GetMapping("/nome-exato/{nome}")
    public ResponseEntity<List<PessoaListDTO>> listarPorNomeExato(
            @PathVariable String nome
    ) {
        return ResponseEntity.ok(pessoaService.listarPorNomeExato(nome));
    }

    @Operation(
            summary = "Listar pessoas por tipo",
            description = "Retorna pessoas filtrando pelo tipo (FISICA ou JURIDICA).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PESSOA_LISTAR')")
    @GetMapping("/tipo/{tipoPessoa}")
    public ResponseEntity<List<PessoaListDTO>> listarPorTipo(
            @PathVariable String tipoPessoa
    ) {
        return ResponseEntity.ok(pessoaService.listarPorTipo(tipoPessoa));
    }
}
