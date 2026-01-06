package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaResponse;
import br.com.unicos.ms_pessoas.service.PessoaFisicaService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/pessoas-fisicas")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Pessoas Físicas",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de pessoas físicas."
)
public class PessoaFisicaController {

    private final PessoaFisicaService pessoaFisicaService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar pessoa física",
            description = "Cria uma nova pessoa física no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Pessoa física criada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = PessoaFisicaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PESSOA_FISICA_CRIAR')")
    @PostMapping
    public ResponseEntity<PessoaFisicaResponse> criar(
            @Valid @RequestBody PessoaFisicaRequest request
    ) {
        PessoaFisicaResponse response = pessoaFisicaService.criar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar pessoa física",
            description = "Atualiza os dados de uma pessoa física existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pessoa física atualizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = PessoaFisicaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Pessoa física não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PESSOA_FISICA_ATUALIZAR')")
    @PutMapping("/{id}")
    public ResponseEntity<PessoaFisicaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PessoaFisicaRequest request
    ) {
        return ResponseEntity.ok(pessoaFisicaService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar pessoa física por ID",
            description = "Retorna os dados de uma pessoa física pelo identificador.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = PessoaFisicaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Pessoa física não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PESSOA_FISICA_VISUALIZAR')")
    @GetMapping("/{id}")
    public ResponseEntity<PessoaFisicaResponse> buscarPorId(@PathVariable Long id) {
        Optional<PessoaFisicaResponse> response =
                pessoaFisicaService.buscarPorId(id);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // GET BY CPF
    // =============================================================

    @Operation(
            summary = "Buscar pessoa física por CPF",
            description = "Retorna os dados de uma pessoa física pelo CPF.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = PessoaFisicaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Pessoa física não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PESSOA_FISICA_VISUALIZAR')")
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<PessoaFisicaResponse> buscarPorCpf(@PathVariable String cpf) {
        Optional<PessoaFisicaResponse> response =
                pessoaFisicaService.buscarPorCpf(cpf);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar todas as pessoas físicas",
            description = "Retorna todas as pessoas físicas cadastradas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaFisicaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PESSOA_FISICA_LISTAR')")
    @GetMapping
    public ResponseEntity<List<PessoaFisicaListDTO>> listarTodas() {
        return ResponseEntity.ok(pessoaFisicaService.listarTodas());
    }

    @Operation(
            summary = "Listar pessoas físicas por nome social",
            description = "Retorna pessoas físicas filtrando pelo nome social.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaFisicaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PESSOA_FISICA_LISTAR')")
    @GetMapping("/nome-social/{nomeSocial}")
    public ResponseEntity<List<PessoaFisicaListDTO>> listarPorNomeSocial(
            @PathVariable String nomeSocial
    ) {
        return ResponseEntity.ok(pessoaFisicaService.listarPorNomeSocial(nomeSocial));
    }

    @Operation(
            summary = "Listar pessoas físicas por nome",
            description = "Retorna pessoas físicas cujo nome contenha o valor informado (ignore case).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaFisicaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PESSOA_FISICA_LISTAR')")
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<PessoaFisicaListDTO>> listarPorNome(
            @PathVariable String nome
    ) {
        return ResponseEntity.ok(pessoaFisicaService.listarPorNome(nome));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover pessoa física",
            description = "Remove uma pessoa física pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pessoa física removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pessoa física não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'PESSOA_FISICA_REMOVER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pessoaFisicaService.excluir(id);
        return ResponseEntity.ok().build();
    }
}
