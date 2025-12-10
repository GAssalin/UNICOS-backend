package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaResponse;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.service.interfaces.PessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller responsável pelas operações genéricas relacionadas à entidade {@link Pessoa},
 * contemplando consultas independentes do tipo (Física ou Jurídica).
 *
 * <p>Este controller atende buscas por ID, nome, nome exato e tipo de pessoa,
 * servindo como ponto central de consulta para diversas integrações do UniCoS.</p>
 */
@RestController
@RequestMapping("/v1/pessoas")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService service;

    // ============================================================
    // Buscar por ID
    // ============================================================

    /**
     * Busca uma pessoa pelo seu identificador único.
     *
     * @param id identificador da pessoa.
     * @return ResponseEntity contendo os dados ou 404 caso não encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar todas
    // ============================================================

    /**
     * Lista todas as pessoas cadastradas (Físicas e Jurídicas).
     *
     * @return lista simplificada contendo ID, Nome e TipoPessoa.
     */
    @GetMapping
    public ResponseEntity<List<PessoaListDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // ============================================================
    // Listar por nome (contains)
    // ============================================================

    /**
     * Lista todas as pessoas cujo nome contenha o termo informado,
     * ignorando diferenciação entre maiúsculas e minúsculas.
     *
     * @param nome termo parcial para busca.
     * @return lista de pessoas correspondentes.
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<PessoaListDTO>> listarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorNome(nome));
    }

    // ============================================================
    // Listar por nome exato
    // ============================================================

    /**
     * Lista pessoas cujo nome seja exatamente igual ao informado.
     *
     * @param nome nome completo e exato.
     * @return lista de pessoas encontradas.
     */
    @GetMapping("/nome-exato/{nome}")
    public ResponseEntity<List<PessoaListDTO>> listarPorNomeExato(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorNomeExato(nome));
    }

    // ============================================================
    // Listar por tipo (FISICA / JURIDICA)
    // ============================================================

    /**
     * Lista todas as pessoas do tipo informado (FISICA ou JURIDICA).
     *
     * @param tipoPessoa texto representando o tipo da pessoa.
     * @return lista de pessoas desse tipo.
     */
    @GetMapping("/tipo/{tipoPessoa}")
    public ResponseEntity<List<PessoaListDTO>> listarPorTipo(@PathVariable String tipoPessoa) {
        return ResponseEntity.ok(service.listarPorTipo(tipoPessoa));
    }
}
