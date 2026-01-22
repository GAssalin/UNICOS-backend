package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.documento.DocumentoListDTO;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoRequest;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoResponse;
import br.com.unicos.ms_pessoas.service.DocumentoService;
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
@RequestMapping("/v1/documentos")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Documentos",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de documentos de pessoas."
)
public class DocumentoController {

    private final UtilsService utilsService;
    private final DocumentoService documentoService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar documento",
            description = "Cria um novo documento vinculado a uma pessoa.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Documento criado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = DocumentoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<DocumentoResponse> criar(@Valid @RequestBody DocumentoRequest request) {
        if (utilsService.verificarPermissao("PESSOA_DOCUMENTO_CRIAR"))
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(documentoService.criar(request));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar documento",
            description = "Atualiza os dados de um documento existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Documento atualizado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = DocumentoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Documento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<DocumentoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DocumentoRequest request
    ) {
        if (utilsService.verificarPermissao("PESSOA_DOCUMENTO_EDITAR"))
            return ResponseEntity.ok(documentoService.atualizar(id, request));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar documento por ID",
            description = "Retorna os dados de um documento específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = DocumentoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Documento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoResponse> buscarPorId(@PathVariable Long id) {
        if (utilsService.verificarPermissao("PESSOA_DOCUMENTO_LISTAR"))
            return documentoService.buscarPorId(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar todos os documentos",
            description = "Retorna todos os documentos cadastrados.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = DocumentoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<List<DocumentoListDTO>> listarTodos() {
        if (utilsService.verificarPermissao("PESSOA_DOCUMENTO_LISTAR"))
            return ResponseEntity.ok(documentoService.listarTodos());
        else
            return ResponseEntity.status(403).build();
    }

    @Operation(
            summary = "Listar documentos por pessoa",
            description = "Retorna os documentos vinculados a uma pessoa.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = DocumentoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<DocumentoListDTO>> listarPorPessoa(@PathVariable Long pessoaId) {
        if (utilsService.verificarPermissao("PESSOA_DOCUMENTO_LISTAR"))
            return ResponseEntity.ok(documentoService.listarPorPessoa(pessoaId));
        else
            return ResponseEntity.status(403).build();

    }

    @Operation(
            summary = "Listar documentos por tipo",
            description = "Retorna os documentos filtrando pelo tipo informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = DocumentoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<DocumentoListDTO>> listarPorTipo(@PathVariable String tipo) {
        if (utilsService.verificarPermissao("PESSOA_DOCUMENTO_LISTAR"))
            return ResponseEntity.ok(documentoService.listarPorTipo(tipo));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover documento",
            description = "Remove um documento pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Documento removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Documento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (utilsService.verificarPermissao("PESSOA_DOCUMENTO_EXCLUIR")) {
            documentoService.excluir(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
