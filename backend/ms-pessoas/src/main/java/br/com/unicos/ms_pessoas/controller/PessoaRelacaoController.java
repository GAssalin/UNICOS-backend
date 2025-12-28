package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoRequest;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoResponse;
import br.com.unicos.ms_pessoas.service.PessoaRelacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento das relações entre pessoas.
 *
 * <p>
 * Disponibiliza endpoints para criação, atualização, consulta,
 * listagem e exclusão de vínculos entre pessoas, com diversos
 * filtros de consulta.
 * </p>
 */
@RestController
@RequestMapping("/v1/pessoas-relacoes")
@RequiredArgsConstructor
@Tag(
        name = "Relações entre Pessoas",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de relações entre pessoas."
)
public class PessoaRelacaoController {

    private final PessoaRelacaoService pessoaRelacaoService;

    // =============================================================
    // CREATE
    // =============================================================

    @PostMapping
    public ResponseEntity<PessoaRelacaoResponse> criar(
            @Valid @RequestBody PessoaRelacaoRequest request
    ) {
        PessoaRelacaoResponse response = pessoaRelacaoService.criar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @PutMapping("/{id}")
    public ResponseEntity<PessoaRelacaoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PessoaRelacaoRequest request
    ) {
        return ResponseEntity.ok(
                pessoaRelacaoService.atualizar(id, request)
        );
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @GetMapping("/{id}")
    public ResponseEntity<PessoaRelacaoResponse> buscarPorId(
            @PathVariable Long id
    ) {
        Optional<PessoaRelacaoResponse> response =
                pessoaRelacaoService.buscarPorId(id);

        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    /**
     * Lista todas as relações cadastradas.
     */
    @GetMapping
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarTodas() {
        return ResponseEntity.ok(
                pessoaRelacaoService.listarTodas()
        );
    }

    /**
     * Lista relações por pessoa principal.
     */
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorPessoa(
            @PathVariable Long pessoaId
    ) {
        return ResponseEntity.ok(
                pessoaRelacaoService.listarPorPessoa(pessoaId)
        );
    }

    /**
     * Lista relações por pessoa relacionada.
     */
    @GetMapping("/relacionado/{relacionadoId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorRelacionado(
            @PathVariable Long relacionadoId
    ) {
        return ResponseEntity.ok(
                pessoaRelacaoService.listarPorRelacionado(relacionadoId)
        );
    }

    /**
     * Lista relações por tipo de relação.
     */
    @GetMapping("/tipo/{tipoRelacaoPessoaId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorTipo(
            @PathVariable Long tipoRelacaoPessoaId
    ) {
        return ResponseEntity.ok(
                pessoaRelacaoService.listarPorTipo(tipoRelacaoPessoaId)
        );
    }

    /**
     * Lista relações filtrando pelo nome da pessoa principal.
     */
    @GetMapping("/pessoa/nome/{nome}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorPessoaENome(@PathVariable String nome) {
        return ResponseEntity.ok(pessoaRelacaoService.listarPorPessoaENome(nome));
    }

    /**
     * Lista relações filtrando pelo nome da pessoa relacionada.
     */
    @GetMapping("/relacionado/nome/{nome}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorRelacionadoENome(@PathVariable String nome) {
        return ResponseEntity.ok(pessoaRelacaoService.listarPorRelacionadoENome(nome));
    }

    /**
     * Lista relações filtrando por pessoa e relacionado simultaneamente.
     */
    @GetMapping("/pessoa/{pessoaId}/relacionado/{relacionadoId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorPessoaERelacionado(@PathVariable Long pessoaId, @PathVariable Long relacionadoId) {
        return ResponseEntity.ok(pessoaRelacaoService.listarPorPessoaERelacionado(pessoaId, relacionadoId));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pessoaRelacaoService.excluir(id);
        return ResponseEntity.ok().build();
    }
}
