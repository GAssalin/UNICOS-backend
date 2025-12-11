package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaResponse;
import br.com.unicos.ms_pessoas.service.interfaces.TipoRelacaoPessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento dos tipos de relação entre pessoas no UniCoS.
 * <p>
 * Permite criar, atualizar, excluir e consultar os tipos de vínculo utilizados
 * em relacionamentos como dependência, sociedade, representação legal, entre outros.
 */
@RestController
@RequestMapping("/v1/pessoas/tipos-relacao")
@RequiredArgsConstructor
@Tag(
        name = "Tipos de Relação entre Pessoas",
        description = "Gerenciamento de tipos de vínculos utilizados em relações entre pessoas no UniCoS."
)
public class TipoRelacaoPessoaController {

    private final TipoRelacaoPessoaService service;

    // ============================================================
    // Criar
    // ============================================================

    @Operation(
            summary = "Criar tipo de relação",
            description = "Registra um novo tipo de vínculo entre pessoas.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Tipo de relação criado com sucesso",
                            content = @Content(schema = @Schema(implementation = TipoRelacaoPessoaResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<TipoRelacaoPessoaResponse> criar(
            @RequestBody TipoRelacaoPessoaRequest request) {

        TipoRelacaoPessoaResponse response = service.criar(request);
        return ResponseEntity
                .created(URI.create("/v1/pessoas/tipos-relacao/" + response.id()))
                .body(response);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @Operation(
            summary = "Atualizar tipo de relação",
            description = "Atualiza os dados de um tipo de vínculo previamente cadastrado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tipo atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = TipoRelacaoPessoaResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Tipo de relação não encontrado"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<TipoRelacaoPessoaResponse> atualizar(
            @PathVariable Long id,
            @RequestBody TipoRelacaoPessoaRequest request) {

        TipoRelacaoPessoaResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Excluir
    // ============================================================

    @Operation(
            summary = "Excluir tipo de relação",
            description = "Remove um tipo de relação entre pessoas com base no ID informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Tipo excluído com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Tipo de relação não encontrado"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @Operation(
            summary = "Buscar tipo de relação por ID",
            description = "Retorna os dados completos de um tipo de relação pelo identificador.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tipo encontrado",
                            content = @Content(schema = @Schema(implementation = TipoRelacaoPessoaResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Tipo não encontrado"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TipoRelacaoPessoaResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    @Operation(
            summary = "Listar todos os tipos de relação",
            description = "Retorna todos os tipos de relação cadastrados no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TipoRelacaoPessoaListDTO.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<TipoRelacaoPessoaListDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // ============================================================
    // Listar por Nome (contains)
    // ============================================================

    @Operation(
            summary = "Listar tipos de relação por nome",
            description = "Retorna tipos de vínculo cujo nome contenha o termo informado (contains, ignore case).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista filtrada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TipoRelacaoPessoaListDTO.class)))
                    )
            }
    )
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<TipoRelacaoPessoaListDTO>> listarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorNome(nome));
    }

    // ============================================================
    // Buscar por Nome Exato
    // ============================================================

    @Operation(
            summary = "Buscar tipo de relação por nome exato",
            description = "Retorna o tipo de relação cujo nome corresponde exatamente ao informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tipo encontrado",
                            content = @Content(schema = @Schema(implementation = TipoRelacaoPessoaResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Nenhum tipo encontrado com este nome"
                    )
            }
    )
    @GetMapping("/nome-exato/{nome}")
    public ResponseEntity<TipoRelacaoPessoaResponse> buscarPorNomeExato(@PathVariable String nome) {
        return service.buscarPorNomeExato(nome)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
