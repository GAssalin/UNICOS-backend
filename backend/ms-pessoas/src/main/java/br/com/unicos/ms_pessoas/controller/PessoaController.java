package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaResponse;
import br.com.unicos.ms_pessoas.service.PessoaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelas operações genéricas
 * de consulta aplicadas à entidade Pessoa.
 *
 * <p>
 * Esta entidade serve como base para Pessoa Física e Pessoa Jurídica,
 * permitindo consultas unificadas.
 * </p>
 */
@RestController
@RequestMapping("/v1/pessoas")
@RequiredArgsConstructor
@Tag(
        name = "Pessoas",
        description = "Endpoints de consulta genérica para pessoas físicas e jurídicas."
)
public class PessoaController {

    private final PessoaService pessoaService;

    // =============================================================
    // GET BY ID
    // =============================================================

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponse> buscarPorId(@PathVariable Long id) {
        Optional<PessoaResponse> response =
                pessoaService.buscarPorId(id);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    /**
     * Lista todas as pessoas cadastradas (PF e PJ).
     */
    @GetMapping
    public ResponseEntity<List<PessoaListDTO>> listarTodas() {
        return ResponseEntity.ok(pessoaService.listarTodas());
    }

    /**
     * Lista pessoas filtrando por nome parcial (contains ignore case).
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<PessoaListDTO>> listarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(pessoaService.listarPorNome(nome));
    }

    /**
     * Lista pessoas filtrando por nome exato.
     */
    @GetMapping("/nome-exato/{nome}")
    public ResponseEntity<List<PessoaListDTO>> listarPorNomeExato(@PathVariable String nome) {
        return ResponseEntity.ok(pessoaService.listarPorNomeExato(nome));
    }

    /**
     * Lista pessoas filtrando por tipo (FISICA / JURIDICA).
     */
    @GetMapping("/tipo/{tipoPessoa}")
    public ResponseEntity<List<PessoaListDTO>> listarPorTipo(@PathVariable String tipoPessoa) {
        return ResponseEntity.ok(pessoaService.listarPorTipo(tipoPessoa));
    }
}
