package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.documento.DocumentoListDTO;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoRequest;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoResponse;
import br.com.unicos.ms_pessoas.service.DocumentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento de documentos
 * vinculados a pessoas.
 *
 * <p>
 * Disponibiliza endpoints para criação, atualização, consulta,
 * listagem e exclusão de documentos.
 * </p>
 */
@RestController
@RequestMapping("/v1/documentos")
@RequiredArgsConstructor
@Tag(
        name = "Documentos",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de documentos de pessoas."
)
public class DocumentoController {

    private final DocumentoService documentoService;

    // =============================================================
    // CREATE
    // =============================================================

    @PostMapping
    public ResponseEntity<DocumentoResponse> criar(@Valid @RequestBody DocumentoRequest request) {
        DocumentoResponse response = documentoService.criar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @PutMapping("/{id}")
    public ResponseEntity<DocumentoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody DocumentoRequest request) {
        return ResponseEntity.ok(documentoService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoResponse> buscarPorId(@PathVariable Long id) {
        Optional<DocumentoResponse> response = documentoService.buscarPorId(id);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    /**
     * Lista todos os documentos cadastrados.
     */
    @GetMapping
    public ResponseEntity<List<DocumentoListDTO>> listarTodos() {
        return ResponseEntity.ok(documentoService.listarTodos());
    }

    /**
     * Lista documentos vinculados a uma pessoa.
     */
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<DocumentoListDTO>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(documentoService.listarPorPessoa(pessoaId));
    }

    /**
     * Lista documentos filtrando por tipo.
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<DocumentoListDTO>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(documentoService.listarPorTipo(tipo));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        documentoService.excluir(id);
        return ResponseEntity.ok().build();
    }
}
