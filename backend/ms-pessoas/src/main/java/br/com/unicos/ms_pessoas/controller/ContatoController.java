package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.contato.ContatoListDTO;
import br.com.unicos.ms_pessoas.dto.contato.ContatoRequest;
import br.com.unicos.ms_pessoas.dto.contato.ContatoResponse;
import br.com.unicos.ms_pessoas.service.interfaces.ContatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller responsável pela gestão dos contatos associados a pessoas
 * dentro do UniCoS.
 *
 * <p>Permite operações de criação, atualização, exclusão e consultas
 * relacionadas a telefones, celulares, e-mails e demais meios de contato.</p>
 */
@RestController
@RequestMapping("/v1/contatos")
@RequiredArgsConstructor
public class ContatoController {

    private final ContatoService service;

    // ============================================================
    // Criar
    // ============================================================

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<ContatoResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    @GetMapping
    public ResponseEntity<List<ContatoListDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // ============================================================
    // Listar Por Pessoa
    // ============================================================

    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<ContatoListDTO>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(service.listarPorPessoa(pessoaId));
    }

    // ============================================================
    // Buscar Contato Principal de uma Pessoa
    // ============================================================

    @GetMapping("/pessoa/{pessoaId}/principal")
    public ResponseEntity<ContatoResponse> buscarPrincipal(@PathVariable Long pessoaId) {
        return service.buscarPrincipal(pessoaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
