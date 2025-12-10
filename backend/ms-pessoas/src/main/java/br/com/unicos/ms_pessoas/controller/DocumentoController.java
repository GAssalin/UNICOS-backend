package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.documento.DocumentoListDTO;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoRequest;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoResponse;
import br.com.unicos.ms_pessoas.service.interfaces.DocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento dos documentos associados
 * a pessoas dentro do UniCoS.
 *
 * <p>Permite operações de criação, atualização, exclusão e consultas de
 * documentos como CPF, RG, CNPJ e demais identificadores formais.</p>
 */
@RestController
@RequestMapping("/v1/documentos")
@RequiredArgsConstructor
public class DocumentoController {

    private final DocumentoService service;

    // ============================================================
    // Criar
    // ============================================================

    /**
     * Cria um novo documento vinculado a uma pessoa.
     *
     * @param request dados do documento a ser criado.
     * @return ResponseEntity com o documento criado e header Location.
     */
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

    /**
     * Atualiza os dados de um documento existente.
     *
     * @param id      identificador do documento.
     * @param request dados atualizados do documento.
     * @return ResponseEntity contendo o documento atualizado.
     */
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

    /**
     * Exclui um documento pelo seu identificador.
     *
     * @param id identificador do documento.
     * @return ResponseEntity sem conteúdo (204 No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    /**
     * Busca um documento pelo seu ID.
     *
     * @param id identificador do documento.
     * @return ResponseEntity contendo o documento ou 404 se não for encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    /**
     * Lista todos os documentos cadastrados.
     *
     * @return lista simplificada de documentos.
     */
    @GetMapping
    public ResponseEntity<List<DocumentoListDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // ============================================================
    // Listar por Pessoa
    // ============================================================

    /**
     * Lista todos os documentos pertencentes a uma pessoa específica.
     *
     * @param pessoaId identificador da pessoa.
     * @return lista de documentos da pessoa informada.
     */
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<DocumentoListDTO>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(service.listarPorPessoa(pessoaId));
    }

    // ============================================================
    // Listar por Tipo
    // ============================================================

    /**
     * Lista documentos filtrados por tipo (CPF, RG, CNPJ etc.).
     *
     * @param tipo nome do tipo de documento.
     * @return lista de documentos do tipo especificado.
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<DocumentoListDTO>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.listarPorTipo(tipo));
    }
}
