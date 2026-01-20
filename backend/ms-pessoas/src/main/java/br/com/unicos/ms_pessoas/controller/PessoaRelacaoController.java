package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoRequest;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoResponse;
import br.com.unicos.ms_pessoas.service.PessoaRelacaoService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/pessoas-relacoes")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Relações entre Pessoas",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de relações entre pessoas."
)
public class PessoaRelacaoController {

    private final PessoaRelacaoService pessoaRelacaoService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Criar relação entre pessoas",
            description = "Cria um novo vínculo entre duas pessoas.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Relação criada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = PessoaRelacaoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<PessoaRelacaoResponse> criar(@Valid @RequestBody PessoaRelacaoRequest request) {
        PessoaRelacaoResponse response = pessoaRelacaoService.criar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar relação entre pessoas",
            description = "Atualiza os dados de uma relação existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Relação atualizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = PessoaRelacaoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Relação não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PessoaRelacaoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PessoaRelacaoRequest request
    ) {
        return ResponseEntity.ok(pessoaRelacaoService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar relação por ID",
            description = "Retorna os dados de uma relação específica entre pessoas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = PessoaRelacaoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Relação não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PessoaRelacaoResponse> buscarPorId(@PathVariable Long id) {
        Optional<PessoaRelacaoResponse> response =
                pessoaRelacaoService.buscarPorId(id);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar todas as relações",
            description = "Retorna todas as relações cadastradas entre pessoas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaRelacaoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarTodas() {
        return ResponseEntity.ok(pessoaRelacaoService.listarTodas());
    }

    @Operation(
            summary = "Listar relações por pessoa principal",
            description = "Retorna relações vinculadas à pessoa principal.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaRelacaoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(pessoaRelacaoService.listarPorPessoa(pessoaId));
    }

    @Operation(
            summary = "Listar relações por pessoa relacionada",
            description = "Retorna relações vinculadas à pessoa relacionada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaRelacaoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/relacionado/{relacionadoId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorRelacionado(@PathVariable Long relacionadoId) {
        return ResponseEntity.ok(pessoaRelacaoService.listarPorRelacionado(relacionadoId));
    }

    @Operation(
            summary = "Listar relações por tipo",
            description = "Retorna relações filtrando pelo tipo de relação.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaRelacaoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/tipo/{tipoRelacaoPessoaId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorTipo(@PathVariable Long tipoRelacaoPessoaId) {
        return ResponseEntity.ok(pessoaRelacaoService.listarPorTipo(tipoRelacaoPessoaId));
    }

    @Operation(
            summary = "Listar relações por nome da pessoa principal",
            description = "Retorna relações filtrando pelo nome da pessoa principal.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaRelacaoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pessoa/nome/{nome}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorPessoaENome(@PathVariable String nome) {
        return ResponseEntity.ok(pessoaRelacaoService.listarPorPessoaENome(nome));
    }

    @Operation(
            summary = "Listar relações por nome da pessoa relacionada",
            description = "Retorna relações filtrando pelo nome da pessoa relacionada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaRelacaoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/relacionado/nome/{nome}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorRelacionadoENome(@PathVariable String nome) {
        return ResponseEntity.ok(pessoaRelacaoService.listarPorRelacionadoENome(nome));
    }

    @Operation(
            summary = "Listar relações por pessoa e relacionado",
            description = "Retorna relações filtrando simultaneamente por pessoa e relacionado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaRelacaoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pessoa/{pessoaId}/relacionado/{relacionadoId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorPessoaERelacionado(
            @PathVariable Long pessoaId,
            @PathVariable Long relacionadoId
    ) {
        return ResponseEntity.ok(pessoaRelacaoService.listarPorPessoaERelacionado(pessoaId, relacionadoId));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover relação entre pessoas",
            description = "Remove uma relação entre pessoas pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relação removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Relação não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pessoaRelacaoService.excluir(id);
        return ResponseEntity.ok().build();
    }
}
