package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.TipoRelacaoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.TipoRelacaoPessoaResponse;
import br.com.unicos.ms_pessoas.service.TipoRelacaoPessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelos tipos de relação de pessoa.
 *
 * <p>Gerencia tipos de vínculo como Cliente, Fornecedor, Colaborador, entre outros.</p>
 */
@RestController
@RequestMapping("/v1/tipos-relacao")
@RequiredArgsConstructor
public class TipoRelacaoPessoaController {

    private final TipoRelacaoPessoaService tipoRelacaoPessoaService;

    /**
     * Cria um novo tipo de relação de pessoa.
     *
     * @param request DTO com os dados do tipo de relação
     * @return {@link TipoRelacaoPessoaResponse} criado
     */
    @PostMapping
    public ResponseEntity<TipoRelacaoPessoaResponse> salvar(@Valid @RequestBody TipoRelacaoPessoaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoRelacaoPessoaService.salvar(request));
    }

    /**
     * Atualiza um tipo de relação existente.
     *
     * @param id      identificador do tipo
     * @param request DTO com os novos dados
     * @return {@link TipoRelacaoPessoaResponse} atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoRelacaoPessoaResponse> atualizar(@PathVariable Long id,
                                                               @Valid @RequestBody TipoRelacaoPessoaRequest request) {
        return ResponseEntity.ok(tipoRelacaoPessoaService.atualizar(id, request));
    }

    /**
     * Lista todos os tipos de relação de pessoa.
     *
     * @return lista de {@link TipoRelacaoPessoaResponse}
     */
    @GetMapping
    public ResponseEntity<List<TipoRelacaoPessoaResponse>> listarTodos() {
        return ResponseEntity.ok(tipoRelacaoPessoaService.listarTodos());
    }

    /**
     * Busca um tipo de relação pelo ID.
     *
     * @param id identificador do tipo
     * @return {@link TipoRelacaoPessoaResponse} se encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoRelacaoPessoaResponse> buscarPorId(@PathVariable Long id) {
        return tipoRelacaoPessoaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Exclui um tipo de relação.
     *
     * @param id identificador do tipo
     * @return status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        tipoRelacaoPessoaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
