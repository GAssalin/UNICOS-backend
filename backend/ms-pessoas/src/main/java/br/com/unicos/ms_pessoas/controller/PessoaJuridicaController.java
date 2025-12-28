package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaResponse;
import br.com.unicos.ms_pessoas.service.PessoaJuridicaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento de Pessoas Jurídicas.
 *
 * <p>
 * Disponibiliza endpoints para criação, atualização, consulta,
 * listagem e exclusão de pessoas jurídicas no sistema.
 * </p>
 */
@RestController
@RequestMapping("/v1/pessoas-juridicas")
@RequiredArgsConstructor
@Tag(
        name = "Pessoas Jurídicas",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de pessoas jurídicas."
)
public class PessoaJuridicaController {

    private final PessoaJuridicaService pessoaJuridicaService;

    // =============================================================
    // CREATE
    // =============================================================

    @PostMapping
    public ResponseEntity<PessoaJuridicaResponse> criar(@Valid @RequestBody PessoaJuridicaRequest request) {
        PessoaJuridicaResponse response = pessoaJuridicaService.criar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @PutMapping("/{id}")
    public ResponseEntity<PessoaJuridicaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody PessoaJuridicaRequest request) {
        return ResponseEntity.ok(pessoaJuridicaService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @GetMapping("/{id}")
    public ResponseEntity<PessoaJuridicaResponse> buscarPorId(@PathVariable Long id) {
        Optional<PessoaJuridicaResponse> response =
                pessoaJuridicaService.buscarPorId(id);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // GET BY CNPJ
    // =============================================================

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<PessoaJuridicaResponse> buscarPorCnpj(@PathVariable String cnpj) {
        Optional<PessoaJuridicaResponse> response =
                pessoaJuridicaService.buscarPorCnpj(cnpj);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    /**
     * Lista todas as pessoas jurídicas cadastradas.
     */
    @GetMapping
    public ResponseEntity<List<PessoaJuridicaListDTO>> listarTodas() {
        return ResponseEntity.ok(pessoaJuridicaService.listarTodas());
    }

    /**
     * Lista pessoas jurídicas pelo nome fantasia.
     */
    @GetMapping("/nome-fantasia/{nomeFantasia}")
    public ResponseEntity<List<PessoaJuridicaListDTO>> listarPorNomeFantasia(@PathVariable String nomeFantasia) {
        return ResponseEntity.ok(pessoaJuridicaService.listarPorNomeFantasia(nomeFantasia));
    }

    /**
     * Lista pessoas jurídicas filtrando por nome (contains ignore case).
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<PessoaJuridicaListDTO>> listarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(pessoaJuridicaService.listarPorNome(nome));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pessoaJuridicaService.excluir(id);
        return ResponseEntity.ok().build();
    }
}
