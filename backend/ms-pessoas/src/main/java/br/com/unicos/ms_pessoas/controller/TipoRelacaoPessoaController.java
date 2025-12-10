package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaResponse;
import br.com.unicos.ms_pessoas.service.interfaces.TipoRelacaoPessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento dos tipos de relação entre pessoas no UniCoS.
 *
 * <p>Permite criar, atualizar, excluir e consultar os tipos de vínculo utilizados
 * em relacionamentos como dependência, sociedade, representação legal, entre outros.</p>
 */
@RestController
@RequestMapping("/v1/pessoas/tipos-relacao")
@RequiredArgsConstructor
public class TipoRelacaoPessoaController {

    private final TipoRelacaoPessoaService service;

    // ============================================================
    // Criar
    // ============================================================

    /**
     * Cadastra um novo tipo de relação entre pessoas.
     *
     * @param request dados do tipo de relação.
     * @return ResponseEntity contendo o tipo criado.
     */
    @PostMapping
    public ResponseEntity<TipoRelacaoPessoaResponse> criar(
            @RequestBody TipoRelacaoPessoaRequest request) {

        TipoRelacaoPessoaResponse response = service.criar(request);
        return ResponseEntity
                .created(URI.create("/v1/pessoas/tipos-relacao/" + response.id()))
                .body(response);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    /**
     * Atualiza um tipo de relação existente.
     *
     * @param id      identificador do tipo de relação.
     * @param request dados atualizados.
     * @return ResponseEntity contendo os dados atualizados.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoRelacaoPessoaResponse> atualizar(
            @PathVariable Long id,
            @RequestBody TipoRelacaoPessoaRequest request) {

        TipoRelacaoPessoaResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Excluir
    // ============================================================

    /**
     * Remove um tipo de relação pelo ID.
     *
     * @param id identificador do tipo.
     * @return ResponseEntity vazio (204 No Content).
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
     * Retorna os dados completos de um tipo de relação pelo seu identificador.
     *
     * @param id identificador da relação.
     * @return ResponseEntity com os dados ou 404 se não encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoRelacaoPessoaResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    /**
     * Lista todos os tipos de relação cadastrados.
     *
     * @return lista simplificada contendo ID e Nome.
     */
    @GetMapping
    public ResponseEntity<List<TipoRelacaoPessoaListDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // ============================================================
    // Listar por Nome (contains)
    // ============================================================

    /**
     * Lista tipos de relação cujo nome contenha o termo informado,
     * ignorando maiúsculas e minúsculas.
     *
     * @param nome texto parcial para busca.
     * @return lista correspondente.
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<TipoRelacaoPessoaListDTO>> listarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorNome(nome));
    }

    // ============================================================
    // Buscar por Nome Exato
    // ============================================================

    /**
     * Retorna os dados completos de um tipo de relação pelo nome exato.
     *
     * @param nome nome exato do tipo de relação.
     * @return ResponseEntity com o tipo ou 404.
     */
    @GetMapping("/nome-exato/{nome}")
    public ResponseEntity<TipoRelacaoPessoaResponse> buscarPorNomeExato(@PathVariable String nome) {
        return service.buscarPorNomeExato(nome)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
