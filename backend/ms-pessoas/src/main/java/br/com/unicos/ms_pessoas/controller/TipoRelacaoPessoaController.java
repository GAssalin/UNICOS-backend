package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaResponse;
import br.com.unicos.ms_pessoas.service.TipoRelacaoPessoaService;
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
import java.util.Optional;

@RestController
@RequestMapping("/v1/tipos-relacao-pessoa")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Tipos de Relação entre Pessoas",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção dos tipos de relação entre pessoas."
)
public class TipoRelacaoPessoaController {

    private final UtilsService utilsService;
    private final TipoRelacaoPessoaService tipoRelacaoPessoaService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar tipo de relação",
            description = "Cria um novo tipo de relação entre pessoas.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Tipo de relação criado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = TipoRelacaoPessoaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<TipoRelacaoPessoaResponse> criar(@Valid @RequestBody TipoRelacaoPessoaRequest request) {
        if (utilsService.verificarPermissao("PESSOA_TIPO_RELACAO_PESSOA_CRIAR")) {
            TipoRelacaoPessoaResponse response =
                    tipoRelacaoPessoaService.criar(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar tipo de relação",
            description = "Atualiza os dados de um tipo de relação existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tipo de relação atualizado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = TipoRelacaoPessoaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Tipo de relação não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<TipoRelacaoPessoaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TipoRelacaoPessoaRequest request
    ) {
        if (utilsService.verificarPermissao("PESSOA_TIPO_RELACAO_PESSOA_EDITAR"))
            return ResponseEntity.ok(tipoRelacaoPessoaService.atualizar(id, request));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar tipo de relação por ID",
            description = "Retorna os dados de um tipo de relação pelo identificador.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = TipoRelacaoPessoaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Tipo de relação não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TipoRelacaoPessoaResponse> buscarPorId(@PathVariable Long id) {
        if (utilsService.verificarPermissao("PESSOA_TIPO_RELACAO_PESSOA_LISTAR")) {
            Optional<TipoRelacaoPessoaResponse> response =
                    tipoRelacaoPessoaService.buscarPorId(id);
            return response
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar todos os tipos de relação",
            description = "Retorna todos os tipos de relação cadastrados.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = TipoRelacaoPessoaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<List<TipoRelacaoPessoaListDTO>> listarTodos() {
        if (utilsService.verificarPermissao("PESSOA_TIPO_RELACAO_PESSOA_LISTAR"))
            return ResponseEntity.ok(tipoRelacaoPessoaService.listarTodos());
        else
            return ResponseEntity.status(403).build();
    }

    @Operation(
            summary = "Listar tipos de relação por nome",
            description = "Retorna tipos de relação cujo nome contenha o valor informado (ignore case).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = TipoRelacaoPessoaListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<TipoRelacaoPessoaListDTO>> listarPorNome(@PathVariable String nome) {
        if (utilsService.verificarPermissao("PESSOA_TIPO_RELACAO_PESSOA_LISTAR"))
            return ResponseEntity.ok(tipoRelacaoPessoaService.listarPorNome(nome));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // GET BY NOME EXATO
    // =============================================================

    @Operation(
            summary = "Buscar tipo de relação por nome exato",
            description = "Retorna um tipo de relação cujo nome seja exatamente igual ao informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = TipoRelacaoPessoaResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Tipo de relação não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/nome-exato/{nome}")
    public ResponseEntity<TipoRelacaoPessoaResponse> buscarPorNomeExato(@PathVariable String nome) {
        if (utilsService.verificarPermissao("PESSOA_TIPO_RELACAO_PESSOA_LISTAR")) {
            Optional<TipoRelacaoPessoaResponse> response =
                    tipoRelacaoPessoaService.buscarPorNomeExato(nome);
            return response
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover tipo de relação",
            description = "Remove um tipo de relação pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tipo de relação removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Tipo de relação não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (utilsService.verificarPermissao("PESSOA_TIPO_RELACAO_PESSOA_EXCLUIR")) {
            tipoRelacaoPessoaService.excluir(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
