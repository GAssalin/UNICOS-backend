package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.ContatoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.ContatoPessoaResponse;
import br.com.unicos.ms_pessoas.service.ContatoPessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelos contatos de uma pessoa (telefone, celular, e-mail).
 *
 * <p>Permite definir contatos principais e manter múltiplos contatos por pessoa.</p>
 */
@RestController
@RequestMapping("/v1/contatos")
@RequiredArgsConstructor
public class ContatoPessoaController {

    private final ContatoPessoaService contatoPessoaService;

    /**
     * Cria um novo contato vinculado a uma pessoa.
     *
     * @param request DTO com os dados do contato
     * @return {@link ContatoPessoaResponse} criado
     */
    @PostMapping
    public ResponseEntity<ContatoPessoaResponse> salvar(@Valid @RequestBody ContatoPessoaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contatoPessoaService.salvar(request));
    }

    /**
     * Atualiza um contato existente.
     *
     * @param id      identificador do contato
     * @param request DTO com os novos dados
     * @return {@link ContatoPessoaResponse} atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContatoPessoaResponse> atualizar(@PathVariable Long id,
                                                           @Valid @RequestBody ContatoPessoaRequest request) {
        return ResponseEntity.ok(contatoPessoaService.atualizar(id, request));
    }

    /**
     * Lista todos os contatos de uma pessoa.
     *
     * @param pessoaId identificador da pessoa
     * @return lista de {@link ContatoPessoaResponse}
     */
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<ContatoPessoaResponse>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(contatoPessoaService.listarPorPessoa(pessoaId));
    }

    /**
     * Busca um contato pelo seu ID.
     *
     * @param id identificador do contato
     * @return {@link ContatoPessoaResponse} se encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContatoPessoaResponse> buscarPorId(@PathVariable Long id) {
        return contatoPessoaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Exclui um contato.
     *
     * @param id identificador do contato
     * @return status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        contatoPessoaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
