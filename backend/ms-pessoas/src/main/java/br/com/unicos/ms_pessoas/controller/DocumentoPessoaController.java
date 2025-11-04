package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.DocumentoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.DocumentoPessoaResponse;
import br.com.unicos.ms_pessoas.service.DocumentoPessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelos documentos adicionais de uma pessoa.
 *
 * <p>Gerencia documentos como RG, CNH, passaporte, certificados e afins.</p>
 */
@RestController
@RequestMapping("/v1/documentos")
@RequiredArgsConstructor
public class DocumentoPessoaController {

    private final DocumentoPessoaService documentoPessoaService;

    /**
     * Cria um novo documento vinculado a uma pessoa.
     *
     * @param request DTO com os dados do documento
     * @return {@link DocumentoPessoaResponse} criado
     */
    @PostMapping
    public ResponseEntity<DocumentoPessoaResponse> salvar(@Valid @RequestBody DocumentoPessoaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentoPessoaService.salvar(request));
    }

    /**
     * Atualiza um documento existente.
     *
     * @param id      identificador do documento
     * @param request DTO com os novos dados
     * @return {@link DocumentoPessoaResponse} atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentoPessoaResponse> atualizar(@PathVariable Long id,
                                                             @Valid @RequestBody DocumentoPessoaRequest request) {
        return ResponseEntity.ok(documentoPessoaService.atualizar(id, request));
    }

    /**
     * Lista todos os documentos de uma pessoa.
     *
     * @param pessoaId identificador da pessoa
     * @return lista de {@link DocumentoPessoaResponse}
     */
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<DocumentoPessoaResponse>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(documentoPessoaService.listarPorPessoa(pessoaId));
    }

    /**
     * Busca um documento pelo ID.
     *
     * @param id identificador do documento
     * @return {@link DocumentoPessoaResponse} se encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoPessoaResponse> buscarPorId(@PathVariable Long id) {
        return documentoPessoaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Exclui um documento.
     *
     * @param id identificador do documento
     * @return status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        documentoPessoaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
