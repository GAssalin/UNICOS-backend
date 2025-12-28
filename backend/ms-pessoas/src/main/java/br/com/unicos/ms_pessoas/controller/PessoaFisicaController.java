package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaResponse;
import br.com.unicos.ms_pessoas.service.PessoaFisicaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento de Pessoas Físicas.
 *
 * <p>
 * Disponibiliza endpoints para criação, atualização, consulta,
 * listagem e exclusão de pessoas físicas no sistema.
 * </p>
 */
@RestController
@RequestMapping("/v1/pessoas-fisicas")
@RequiredArgsConstructor
@Tag(
        name = "Pessoas Físicas",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de pessoas físicas."
)
public class PessoaFisicaController {

    private final PessoaFisicaService pessoaFisicaService;

    // =============================================================
    // CREATE
    // =============================================================

    @PostMapping
    public ResponseEntity<PessoaFisicaResponse> criar(@Valid @RequestBody PessoaFisicaRequest request) {
        PessoaFisicaResponse response = pessoaFisicaService.criar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @PutMapping("/{id}")
    public ResponseEntity<PessoaFisicaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody PessoaFisicaRequest request) {
        return ResponseEntity.ok(pessoaFisicaService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @GetMapping("/{id}")
    public ResponseEntity<PessoaFisicaResponse> buscarPorId(@PathVariable Long id) {
        Optional<PessoaFisicaResponse> response =
                pessoaFisicaService.buscarPorId(id);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // GET BY CPF
    // =============================================================

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<PessoaFisicaResponse> buscarPorCpf(@PathVariable String cpf) {
        Optional<PessoaFisicaResponse> response =
                pessoaFisicaService.buscarPorCpf(cpf);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    /**
     * Lista todas as pessoas físicas cadastradas.
     */
    @GetMapping
    public ResponseEntity<List<PessoaFisicaListDTO>> listarTodas() {
        return ResponseEntity.ok(pessoaFisicaService.listarTodas());
    }

    /**
     * Lista pessoas físicas pelo nome social.
     */
    @GetMapping("/nome-social/{nomeSocial}")
    public ResponseEntity<List<PessoaFisicaListDTO>> listarPorNomeSocial(@PathVariable String nomeSocial) {
        return ResponseEntity.ok(pessoaFisicaService.listarPorNomeSocial(nomeSocial));
    }

    /**
     * Lista pessoas físicas filtrando por nome (contains ignore case).
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<PessoaFisicaListDTO>> listarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(pessoaFisicaService.listarPorNome(nome));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pessoaFisicaService.excluir(id);
        return ResponseEntity.ok().build();
    }
}
