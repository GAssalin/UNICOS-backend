package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaResponse;
import br.com.unicos.ms_pessoas.service.interfaces.PessoaFisicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento de Pessoas Físicas dentro do UniCoS.
 * <p>
 * Inclui operações de criação, atualização, exclusão e consultas
 * específicas, como busca por CPF, nome e nome social.
 */
@RestController
@RequestMapping("/v1/pessoas/fisicas")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Pessoas Físicas",
        description = "Operações de criação, atualização, exclusão e consultas específicas de Pessoa Física."
)
public class PessoaFisicaController {

    private final PessoaFisicaService service;

    // ============================================================
    // Criar
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_FISICA_CRIAR')")
    @Operation(
            summary = "Criar Pessoa Física",
            description = "Registra uma nova Pessoa Física no UniCoS.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Pessoa Física criada com sucesso",
                            content = @Content(schema = @Schema(implementation = PessoaFisicaResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<PessoaFisicaResponse> criar(@RequestBody PessoaFisicaRequest request) {
        PessoaFisicaResponse response = service.criar(request);
        return ResponseEntity
                .created(URI.create("/v1/pessoas/fisicas/" + response.id()))
                .body(response);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_FISICA_EDITAR')")
    @Operation(
            summary = "Atualizar Pessoa Física",
            description = "Atualiza os dados de uma Pessoa Física previamente cadastrada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Dados atualizados com sucesso",
                            content = @Content(schema = @Schema(implementation = PessoaFisicaResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados enviados inválidos"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Pessoa Física não encontrada"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PessoaFisicaResponse> atualizar(
            @PathVariable Long id,
            @RequestBody PessoaFisicaRequest request) {

        PessoaFisicaResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Excluir
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_FISICA_EXCLUIR')")
    @Operation(
            summary = "Excluir Pessoa Física",
            description = "Remove definitivamente uma Pessoa Física identificada pelo ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Pessoa Física excluída com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Pessoa Física não encontrada"
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

    @PreAuthorize("hasAuthority('PESSOA_FISICA_LISTAR')")
    @Operation(
            summary = "Buscar Pessoa Física por ID",
            description = "Retorna os dados completos de uma Pessoa Física pelo seu identificador.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pessoa encontrada",
                            content = @Content(schema = @Schema(implementation = PessoaFisicaResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Pessoa Física não encontrada"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PessoaFisicaResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Buscar por CPF
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_FISICA_LISTAR')")
    @Operation(
            summary = "Buscar Pessoa Física por CPF",
            description = "Consulta uma Pessoa Física pelo CPF informado (somente números).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pessoa encontrada",
                            content = @Content(schema = @Schema(implementation = PessoaFisicaResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "CPF não encontrado"
                    )
            }
    )
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<PessoaFisicaResponse> buscarPorCpf(@PathVariable String cpf) {
        return service.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar todas
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_FISICA_LISTAR')")
    @Operation(
            summary = "Listar todas as Pessoas Físicas",
            description = "Retorna uma lista simplificada contendo ID, Nome e CPF.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaFisicaListDTO.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<PessoaFisicaListDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // ============================================================
    // Listar por Nome Social
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_FISICA_LISTAR')")
    @Operation(
            summary = "Listar Pessoas Físicas por Nome Social",
            description = "Retorna todas as pessoas cujo nome social é exatamente igual ao informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaFisicaListDTO.class)))
                    )
            }
    )
    @GetMapping("/nome-social/{nomeSocial}")
    public ResponseEntity<List<PessoaFisicaListDTO>> listarPorNomeSocial(@PathVariable String nomeSocial) {
        return ResponseEntity.ok(service.listarPorNomeSocial(nomeSocial));
    }

    // ============================================================
    // Listar por Nome
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_FISICA_LISTAR')")
    @Operation(
            summary = "Listar Pessoas Físicas por Nome",
            description = "Retorna pessoas cujo nome contenha o termo informado (busca parcial).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaFisicaListDTO.class)))
                    )
            }
    )
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<PessoaFisicaListDTO>> listarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorNome(nome));
    }
}
