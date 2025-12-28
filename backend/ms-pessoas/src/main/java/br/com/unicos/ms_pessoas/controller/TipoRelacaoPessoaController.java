package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaResponse;
import br.com.unicos.ms_pessoas.service.TipoRelacaoPessoaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento dos tipos
 * de relação entre pessoas.
 *
 * <p>
 * Disponibiliza endpoints para criação, atualização, consulta,
 * listagem e exclusão de tipos de relação.
 * </p>
 */
@RestController
@RequestMapping("/v1/tipos-relacao-pessoa")
@RequiredArgsConstructor
@Tag(
        name = "Tipos de Relação entre Pessoas",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção dos tipos de relação entre pessoas."
)
public class TipoRelacaoPessoaController {

    private final TipoRelacaoPessoaService tipoRelacaoPessoaService;

    // =============================================================
    // CREATE
    // =============================================================

    @PostMapping
    public ResponseEntity<TipoRelacaoPessoaResponse> criar(@Valid @RequestBody TipoRelacaoPessoaRequest request) {
        TipoRelacaoPessoaResponse response =
                tipoRelacaoPessoaService.criar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @PutMapping("/{id}")
    public ResponseEntity<TipoRelacaoPessoaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody TipoRelacaoPessoaRequest request) {
        return ResponseEntity.ok(tipoRelacaoPessoaService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @GetMapping("/{id}")
    public ResponseEntity<TipoRelacaoPessoaResponse> buscarPorId(@PathVariable Long id) {
        Optional<TipoRelacaoPessoaResponse> response =
                tipoRelacaoPessoaService.buscarPorId(id);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    /**
     * Lista todos os tipos de relação cadastrados.
     */
    @GetMapping
    public ResponseEntity<List<TipoRelacaoPessoaListDTO>> listarTodos() {
        return ResponseEntity.ok(tipoRelacaoPessoaService.listarTodos());
    }

    /**
     * Lista tipos de relação filtrando por nome (contains ignore case).
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<TipoRelacaoPessoaListDTO>> listarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(tipoRelacaoPessoaService.listarPorNome(nome));
    }

    // =============================================================
    // GET BY NOME EXATO
    // =============================================================

    @GetMapping("/nome-exato/{nome}")
    public ResponseEntity<TipoRelacaoPessoaResponse> buscarPorNomeExato(@PathVariable String nome) {
        Optional<TipoRelacaoPessoaResponse> response =
                tipoRelacaoPessoaService.buscarPorNomeExato(nome);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // DELETE
    // =============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        tipoRelacaoPessoaService.excluir(id);
        return ResponseEntity.ok().build();
    }
}
