package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.contato.ContatoListDTO;
import br.com.unicos.ms_pessoas.dto.contato.ContatoRequest;
import br.com.unicos.ms_pessoas.dto.contato.ContatoResponse;
import br.com.unicos.ms_pessoas.service.interfaces.ContatoService;
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
 * Controller responsável pela gestão dos contatos associados a pessoas
 * dentro do UniCoS.
 * <p>
 * Permite operações de criação, atualização, exclusão e consultas
 * relacionadas a telefones, celulares, e-mails e demais meios de contato.
 */
@RestController
@RequestMapping("/v1/contatos")
@RequiredArgsConstructor
@Tag(
        name = "Contatos",
        description = "Operações relacionadas aos meios de contato associados às pessoas no UniCoS."
)
public class ContatoController {

    private final ContatoService service;

    // ============================================================
    // Criar
    // ============================================================

    @Operation(
            summary = "Criar um novo contato",
            description = "Registra um novo contato (telefone, e-mail, etc.) vinculado a uma pessoa.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Contato criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ContatoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados na requisição",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ContatoResponse> criar(@RequestBody ContatoRequest request) {
        ContatoResponse response = service.criar(request);
        return ResponseEntity
                .created(URI.create("/v1/contatos/" + response.id()))
                .body(response);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @Operation(
            summary = "Atualizar contato existente",
            description = "Atualiza os dados de um contato previamente cadastrado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contato atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ContatoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Contato não encontrado",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ContatoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody ContatoRequest request) {

        ContatoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Excluir
    // ============================================================

    @Operation(
            summary = "Excluir contato",
            description = "Remove permanentemente um contato vinculado a uma pessoa.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Contato excluído com sucesso",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Contato não encontrado",
                            content = @Content
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
            summary = "Buscar contato por ID",
            description = "Retorna os dados de um contato específico com base no ID informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contato encontrado",
                            content = @Content(schema = @Schema(implementation = ContatoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Contato não encontrado",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ContatoResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    @Operation(
            summary = "Listar todos os contatos",
            description = "Retorna todos os contatos cadastrados no módulo de pessoas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContatoListDTO.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<ContatoListDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // ============================================================
    // Listar Por Pessoa
    // ============================================================

    @Operation(
            summary = "Listar contatos por pessoa",
            description = "Retorna todos os contatos associados a uma pessoa específica.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContatoListDTO.class)))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Pessoa não encontrada ou sem contatos cadastrados",
                            content = @Content
                    )
            }
    )
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<ContatoListDTO>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(service.listarPorPessoa(pessoaId));
    }

    // ============================================================
    // Buscar Contato Principal
    // ============================================================

    @Operation(
            summary = "Buscar contato principal da pessoa",
            description = "Retorna o contato principal definido para a pessoa informada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contato principal encontrado",
                            content = @Content(schema = @Schema(implementation = ContatoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Nenhum contato principal encontrado para esta pessoa",
                            content = @Content
                    )
            }
    )
    @GetMapping("/pessoa/{pessoaId}/principal")
    public ResponseEntity<ContatoResponse> buscarPrincipal(@PathVariable Long pessoaId) {
        return service.buscarPrincipal(pessoaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
