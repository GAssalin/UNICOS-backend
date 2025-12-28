package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.contato.ContatoListDTO;
import br.com.unicos.ms_pessoas.dto.contato.ContatoRequest;
import br.com.unicos.ms_pessoas.dto.contato.ContatoResponse;
import br.com.unicos.ms_pessoas.enums.TipoContato;
import br.com.unicos.ms_pessoas.service.ContatoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsável pelo gerenciamento de contatos de pessoas.
 *
 * <p>
 * Disponibiliza endpoints para criação, atualização, consulta,
 * listagem paginada e exclusão de contatos.
 * </p>
 */
@RestController
@RequestMapping("/v1/contatos")
@RequiredArgsConstructor
@Tag(
        name = "Contatos",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de contatos de pessoas."
)
public class ContatoController {

    private final ContatoService contatoService;

    // =============================================================
    // CREATE
    // =============================================================

    @PostMapping
    public ResponseEntity<ContatoResponse> salvar(@Valid @RequestBody ContatoRequest request) {
        ContatoResponse response = contatoService.salvar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @PutMapping("/{id}")
    public ResponseEntity<ContatoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ContatoRequest request) {
        return ResponseEntity.ok(contatoService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @GetMapping("/{id}")
    public ResponseEntity<ContatoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(contatoService.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    /**
     * Lista contatos de uma pessoa de forma paginada.
     */
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<Page<ContatoListDTO>> listarPorPessoa(@PathVariable Long pessoaId, Pageable pageable) {
        return ResponseEntity.ok(contatoService.listarPorPessoa(pessoaId, pageable));
    }

    /**
     * Lista contatos de uma pessoa filtrando por tipo.
     */
    @GetMapping("/pessoa/{pessoaId}/tipo/{tipo}")
    public ResponseEntity<Page<ContatoListDTO>> listarPorPessoaETipo(@PathVariable Long pessoaId, @PathVariable TipoContato tipo, Pageable pageable) {
        return ResponseEntity.ok(contatoService.listarPorPessoaETipo(pessoaId, tipo, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        contatoService.deletar(id);
        return ResponseEntity.ok().build();
    }
}
