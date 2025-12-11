package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaResponse;
import br.com.unicos.ms_pessoas.service.interfaces.PessoaJuridicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento de Pessoas Jurídicas dentro do UniCoS.
 * <p>
 * Oferece operações de criação, atualização, exclusão e consultas especializadas,
 * como busca por CNPJ, razão social e nome fantasia.
 */
@RestController
@RequestMapping("/v1/pessoas/juridicas")
@RequiredArgsConstructor
@Tag(
        name = "Pessoas Jurídicas",
        description = "Endpoints para criação, atualização, exclusão e consulta de empresas (Pessoa Jurídica)."
)
public class PessoaJuridicaController {

    private final PessoaJuridicaService service;

    // ============================================================
    // Criar
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_JURIDICA_CRIAR')")
    @Operation(
            summary = "Criar Pessoa Jurídica",
            description = "Registra uma nova Pessoa Jurídica no sistema UniCoS.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Pessoa Jurídica criada com sucesso",
                            content = @Content(schema = @Schema(implementation = PessoaJuridicaResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<PessoaJuridicaResponse> criar(@RequestBody PessoaJuridicaRequest request) {
        PessoaJuridicaResponse response = service.criar(request);
        return ResponseEntity
                .created(URI.create("/v1/pessoas/juridicas/" + response.id()))
                .body(response);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_JURIDICA_EDITAR')")
    @Operation(
            summary = "Atualizar Pessoa Jurídica",
            description = "Atualiza os dados de uma empresa previamente cadastrada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Empresa atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = PessoaJuridicaResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Pessoa Jurídica não encontrada"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PessoaJuridicaResponse> atualizar(
            @PathVariable Long id,
            @RequestBody PessoaJuridicaRequest request) {

        PessoaJuridicaResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Excluir
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_JURIDICA_EXCLUIR')")
    @Operation(
            summary = "Excluir Pessoa Jurídica",
            description = "Remove uma Pessoa Jurídica com base no seu ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Empresa excluída com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Pessoa Jurídica não encontrada"
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

    @PreAuthorize("hasAuthority('PESSOA_JURIDICA_LISTAR')")
    @Operation(
            summary = "Buscar Pessoa Jurídica por ID",
            description = "Retorna os dados completos de uma Pessoa Jurídica pelo identificador informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Empresa encontrada",
                            content = @Content(schema = @Schema(implementation = PessoaJuridicaResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Pessoa Jurídica não encontrada"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PessoaJuridicaResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Buscar por CNPJ
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_JURIDICA_LISTAR')")
    @Operation(
            summary = "Buscar Pessoa Jurídica por CNPJ",
            description = "Consulta uma empresa pelo CNPJ informado (somente dígitos).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Empresa encontrada",
                            content = @Content(schema = @Schema(implementation = PessoaJuridicaResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "CNPJ não encontrado"
                    )
            }
    )
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<PessoaJuridicaResponse> buscarPorCnpj(@PathVariable String cnpj) {
        return service.buscarPorCnpj(cnpj)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar todas
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_JURIDICA_LISTAR')")
    @Operation(
            summary = "Listar todas as Pessoas Jurídicas",
            description = "Retorna uma lista simplificada contendo ID, Razão Social e CNPJ.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista obtida com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaJuridicaListDTO.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<PessoaJuridicaListDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // ============================================================
    // Listar por Nome Fantasia
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_JURIDICA_LISTAR')")
    @Operation(
            summary = "Listar por Nome Fantasia (exato)",
            description = "Retorna empresas cujo nome fantasia corresponda exatamente ao valor informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaJuridicaListDTO.class)))
                    )
            }
    )
    @GetMapping("/nome-fantasia/{nomeFantasia}")
    public ResponseEntity<List<PessoaJuridicaListDTO>> listarPorNomeFantasia(@PathVariable String nomeFantasia) {
        return ResponseEntity.ok(service.listarPorNomeFantasia(nomeFantasia));
    }

    // ============================================================
    // Listar por Nome (contains)
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_JURIDICA_LISTAR')")
    @Operation(
            summary = "Listar empresas por nome contendo termo",
            description = "Busca empresas cuja razão social ou nome contenha o termo informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaJuridicaListDTO.class)))
                    )
            }
    )
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<PessoaJuridicaListDTO>> listarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorNome(nome));
    }
}
