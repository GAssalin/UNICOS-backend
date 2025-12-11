package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoRequest;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoResponse;
import br.com.unicos.ms_pessoas.service.interfaces.PessoaRelacaoService;
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
 * Controller responsável pelo gerenciamento das relações entre pessoas no UniCoS.
 * <p>
 * Permite criar, atualizar, remover e consultar vínculos como responsável,
 * dependente, sócio, representante legal e outros tipos definidos no domínio.
 */
@RestController
@RequestMapping("/v1/pessoas/relacoes")
@RequiredArgsConstructor
@Tag(
        name = "Relações entre Pessoas",
        description = "Operações de criação, atualização, exclusão e consultas de vínculos entre pessoas."
)
public class PessoaRelacaoController {

    private final PessoaRelacaoService service;

    // ============================================================
    // Criar
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_RELACAO_CRIAR')")
    @Operation(
            summary = "Criar relação entre pessoas",
            description = "Registra um novo vínculo entre duas pessoas, como dependente, responsável, sócio etc.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Relação criada com sucesso",
                            content = @Content(schema = @Schema(implementation = PessoaRelacaoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<PessoaRelacaoResponse> criar(@RequestBody PessoaRelacaoRequest request) {
        PessoaRelacaoResponse response = service.criar(request);
        return ResponseEntity
                .created(URI.create("/v1/pessoas/relacoes/" + response.id()))
                .body(response);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_RELACAO_EDITAR')")
    @Operation(
            summary = "Atualizar relação entre pessoas",
            description = "Atualiza os dados de um vínculo previamente cadastrado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Relação atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = PessoaRelacaoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Relação não encontrada"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PessoaRelacaoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody PessoaRelacaoRequest request) {

        PessoaRelacaoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Excluir
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_RELACAO_EXCLUIR')")
    @Operation(
            summary = "Excluir relação",
            description = "Remove permanentemente uma relação entre pessoas.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Relação excluída com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Relação não encontrada"
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

    @PreAuthorize("hasAuthority('PESSOA_RELACAO_LISTAR')")
    @Operation(
            summary = "Buscar relação por ID",
            description = "Retorna os dados completos de uma relação a partir de seu identificador.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Relação encontrada",
                            content = @Content(schema = @Schema(implementation = PessoaRelacaoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Relação não encontrada"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PessoaRelacaoResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar todas
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_RELACAO_LISTAR')")
    @Operation(
            summary = "Listar todas as relações",
            description = "Retorna todas as relações entre pessoas cadastradas no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaRelacaoListDTO.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // ============================================================
    // Listar por Pessoa Principal
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_RELACAO_LISTAR')")
    @Operation(
            summary = "Listar relações por pessoa principal",
            description = "Retorna todas as relações onde a pessoa informada é o ator principal.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaRelacaoListDTO.class)))
                    )
            }
    )
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(service.listarPorPessoa(pessoaId));
    }

    // ============================================================
    // Listar por Pessoa Relacionada
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_RELACAO_LISTAR')")
    @Operation(
            summary = "Listar relações por pessoa relacionada",
            description = "Retorna todas as relações onde a pessoa informada é o indivíduo relacionado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaRelacaoListDTO.class)))
                    )
            }
    )
    @GetMapping("/relacionado/{relacionadoId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorRelacionado(@PathVariable Long relacionadoId) {
        return ResponseEntity.ok(service.listarPorRelacionado(relacionadoId));
    }

    // ============================================================
    // Listar por Tipo
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_RELACAO_LISTAR')")
    @Operation(
            summary = "Listar relações por tipo",
            description = "Retorna todas as relações pertencentes ao tipo de vínculo informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista filtrada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaRelacaoListDTO.class)))
                    )
            }
    )
    @GetMapping("/tipo/{tipoRelacaoPessoaId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorTipo(@PathVariable Long tipoRelacaoPessoaId) {
        return ResponseEntity.ok(service.listarPorTipo(tipoRelacaoPessoaId));
    }

    // ============================================================
    // Buscar por Nome da Pessoa Principal
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_RELACAO_LISTAR')")
    @Operation(
            summary = "Listar relações filtrando pelo nome da pessoa principal",
            description = "Busca relações onde o nome da pessoa principal contém o termo informado (ignore case).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaRelacaoListDTO.class)))
                    )
            }
    )
    @GetMapping("/pessoa/nome/{nome}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorPessoaENome(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorPessoaENome(nome));
    }

    // ============================================================
    // Buscar por Nome da Pessoa Relacionada
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_RELACAO_LISTAR')")
    @Operation(
            summary = "Listar relações filtrando pelo nome da pessoa relacionada",
            description = "Busca relações onde o nome da pessoa relacionada contém o termo informado (ignore case).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaRelacaoListDTO.class)))
                    )
            }
    )
    @GetMapping("/relacionado/nome/{nome}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorRelacionadoENome(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorRelacionadoENome(nome));
    }

    // ============================================================
    // Filtrar por Pessoa Principal e Relacionada
    // ============================================================

    @PreAuthorize("hasAuthority('PESSOA_RELACAO_LISTAR')")
    @Operation(
            summary = "Listar relações entre duas pessoas",
            description = "Retorna vínculos existentes onde uma pessoa é a principal e a outra é a relacionada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaRelacaoListDTO.class)))
                    )
            }
    )
    @GetMapping("/pessoa/{pessoaId}/relacionado/{relacionadoId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorPessoaERelacionado(
            @PathVariable Long pessoaId,
            @PathVariable Long relacionadoId) {

        return ResponseEntity.ok(service.listarPorPessoaERelacionado(pessoaId, relacionadoId));
    }
}
