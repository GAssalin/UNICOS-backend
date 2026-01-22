package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaResponse;
import br.com.unicos.ms_pessoas.service.PessoaJuridicaService;
import br.com.unicos.ms_pessoas.service.UtilsService;
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

@RestController
@RequestMapping("/v1/pessoas-juridicas")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Pessoas Jurídicas",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de pessoas jurídicas."
)
public class PessoaJuridicaController {

    private final UtilsService utilsService;
    private final PessoaJuridicaService pessoaJuridicaService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar pessoa jurídica",
            description = "Cria uma nova pessoa jurídica no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Pessoa jurídica criada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = PessoaJuridicaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<PessoaJuridicaResponse> criar(@Valid @RequestBody PessoaJuridicaRequest request) {
        if (utilsService.verificarPermissao("PESSOA_JURIDICA_CRIAR"))
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(pessoaJuridicaService.criar(request));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar pessoa jurídica",
            description = "Atualiza os dados de uma pessoa jurídica existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pessoa jurídica atualizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = PessoaJuridicaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Pessoa jurídica não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PessoaJuridicaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PessoaJuridicaRequest request
    ) {
        if (utilsService.verificarPermissao("PESSOA_JURIDICA_EDITAR"))
            return ResponseEntity.ok(pessoaJuridicaService.atualizar(id, request));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar pessoa jurídica por ID",
            description = "Retorna os dados de uma pessoa jurídica pelo identificador.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = PessoaJuridicaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Pessoa jurídica não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PessoaJuridicaResponse> buscarPorId(@PathVariable Long id) {
        if (utilsService.verificarPermissao("PESSOA_JURIDICA_LISTAR"))
            return pessoaJuridicaService.buscarPorId(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // GET BY CNPJ
    // =============================================================

    @Operation(
            summary = "Buscar pessoa jurídica por CNPJ",
            description = "Retorna os dados de uma pessoa jurídica pelo CNPJ.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = PessoaJuridicaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Pessoa jurídica não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<PessoaJuridicaResponse> buscarPorCnpj(@PathVariable String cnpj) {
        if (utilsService.verificarPermissao("PESSOA_JURIDICA_LISTAR"))
            return pessoaJuridicaService.buscarPorCnpj(cnpj)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar todas as pessoas jurídicas",
            description = "Retorna todas as pessoas jurídicas cadastradas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaJuridicaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<List<PessoaJuridicaListDTO>> listarTodas() {
        if (utilsService.verificarPermissao("PESSOA_JURIDICA_LISTAR"))
            return ResponseEntity.ok(pessoaJuridicaService.listarTodas());
        else
            return ResponseEntity.status(403).build();
    }

    @Operation(
            summary = "Listar pessoas jurídicas por nome fantasia",
            description = "Retorna pessoas jurídicas filtrando pelo nome fantasia.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaJuridicaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/nome-fantasia/{nomeFantasia}")
    public ResponseEntity<List<PessoaJuridicaListDTO>> listarPorNomeFantasia(@PathVariable String nomeFantasia) {
        if (utilsService.verificarPermissao("PESSOA_JURIDICA_LISTAR"))
            return ResponseEntity.ok(pessoaJuridicaService.listarPorNomeFantasia(nomeFantasia));
        else
            return ResponseEntity.status(403).build();
    }

    @Operation(
            summary = "Listar pessoas jurídicas por nome",
            description = "Retorna pessoas jurídicas cujo nome contenha o valor informado (ignore case).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PessoaJuridicaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<PessoaJuridicaListDTO>> listarPorNome(@PathVariable String nome) {
        if (utilsService.verificarPermissao("PESSOA_JURIDICA_LISTAR"))
            return ResponseEntity.ok(pessoaJuridicaService.listarPorNome(nome));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover pessoa jurídica",
            description = "Remove uma pessoa jurídica pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pessoa jurídica removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pessoa jurídica não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (utilsService.verificarPermissao("PESSOA_JURIDICA_EXCLUIR")) {
            pessoaJuridicaService.excluir(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
