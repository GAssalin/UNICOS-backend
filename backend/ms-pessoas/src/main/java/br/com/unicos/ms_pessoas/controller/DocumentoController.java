package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.documento.DocumentoListDTO;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoRequest;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoResponse;
import br.com.unicos.ms_pessoas.service.interfaces.DocumentoService;
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
 * Controller responsável pelo gerenciamento dos documentos associados
 * a pessoas dentro do UniCoS.
 * <p>
 * Permite operações de criação, atualização, exclusão e consultas de
 * documentos como CPF, RG, CNPJ e demais identificadores formais.
 */
@RestController
@RequestMapping("/v1/documentos")
@RequiredArgsConstructor
@Tag(
        name = "Documentos",
        description = "Operações relativas a documentos formais (CPF, RG, CNPJ etc.) associados a pessoas."
)
public class DocumentoController {

    private final DocumentoService service;

    // ============================================================
    // Criar
    // ============================================================

    @PreAuthorize("hasAuthority('DOCUMENTO_CRIAR')")
    @Operation(
            summary = "Criar um novo documento",
            description = "Registra um novo documento vinculado a uma pessoa.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Documento criado com sucesso",
                            content = @Content(schema = @Schema(implementation = DocumentoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados para criação",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<DocumentoResponse> criar(@RequestBody DocumentoRequest request) {
        DocumentoResponse response = service.criar(request);
        return ResponseEntity
                .created(URI.create("/v1/documentos/" + response.id()))
                .body(response);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @PreAuthorize("hasAuthority('DOCUMENTO_EDITAR')")
    @Operation(
            summary = "Atualizar documento existente",
            description = "Altera os dados de um documento previamente cadastrado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Documento atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = DocumentoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Documento não encontrado",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<DocumentoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody DocumentoRequest request) {

        DocumentoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Excluir
    // ============================================================

    @PreAuthorize("hasAuthority('DOCUMENTO_EXCLUIR')")
    @Operation(
            summary = "Excluir documento",
            description = "Remove definitivamente um documento do sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Documento excluído com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Documento não encontrado"
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

    @PreAuthorize("hasAuthority('DOCUMENTO_LISTAR')")
    @Operation(
            summary = "Buscar documento por ID",
            description = "Retorna os dados de um documento específico pelo seu identificador.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Documento encontrado",
                            content = @Content(schema = @Schema(implementation = DocumentoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Documento não encontrado",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    @PreAuthorize("hasAuthority('DOCUMENTO_LISTAR')")
    @Operation(
            summary = "Listar todos os documentos",
            description = "Retorna todos os documentos cadastrados no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentoListDTO.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<DocumentoListDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // ============================================================
    // Listar por Pessoa
    // ============================================================

    @PreAuthorize("hasAuthority('DOCUMENTO_LISTAR')")
    @Operation(
            summary = "Listar documentos por pessoa",
            description = "Retorna todos os documentos pertencentes a uma pessoa específica.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentoListDTO.class)))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Pessoa não encontrada ou sem documentos",
                            content = @Content
                    )
            }
    )
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<DocumentoListDTO>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(service.listarPorPessoa(pessoaId));
    }

    // ============================================================
    // Listar por Tipo
    // ============================================================

    @PreAuthorize("hasAuthority('DOCUMENTO_LISTAR')")
    @Operation(
            summary = "Listar documentos por tipo",
            description = "Retorna todos os documentos filtrados por tipo (CPF, RG, CNPJ, etc.).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentoListDTO.class)))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Tipo informado inválido",
                            content = @Content
                    )
            }
    )
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<DocumentoListDTO>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.listarPorTipo(tipo));
    }
}
