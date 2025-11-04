package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.PessoaRelacaoRequest;
import br.com.unicos.ms_pessoas.dto.PessoaRelacaoResponse;
import br.com.unicos.ms_pessoas.service.PessoaRelacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelos vínculos entre pessoas e seus tipos de relação.
 *
 * <p>Permite relacionar uma pessoa com múltiplos papéis (Cliente, Fornecedor, Colaborador, etc.).</p>
 */
@RestController
@RequestMapping("/v1/pessoas/relacoes")
@RequiredArgsConstructor
public class PessoaRelacaoController {

    private final PessoaRelacaoService pessoaRelacaoService;

    /**
     * Cria uma nova relação entre pessoa e tipo.
     *
     * @param request DTO com os dados da relação
     * @return {@link PessoaRelacaoResponse} criada
     */
    @PostMapping
    public ResponseEntity<PessoaRelacaoResponse> salvar(@Valid @RequestBody PessoaRelacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaRelacaoService.salvar(request));
    }

    /**
     * Atualiza uma relação existente.
     *
     * @param id      identificador da relação
     * @param request DTO com os novos dados
     * @return {@link PessoaRelacaoResponse} atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<PessoaRelacaoResponse> atualizar(@PathVariable Long id,
                                                           @Valid @RequestBody PessoaRelacaoRequest request) {
        return ResponseEntity.ok(pessoaRelacaoService.atualizar(id, request));
    }

    /**
     * Lista todas as relações de uma pessoa.
     *
     * @param pessoaId identificador da pessoa
     * @return lista de {@link PessoaRelacaoResponse}
     */
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<PessoaRelacaoResponse>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(pessoaRelacaoService.listarPorPessoa(pessoaId));
    }

    /**
     * Busca uma relação específica pelo ID.
     *
     * @param id identificador da relação
     * @return {@link PessoaRelacaoResponse} se encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<PessoaRelacaoResponse> buscarPorId(@PathVariable Long id) {
        return pessoaRelacaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Exclui uma relação.
     *
     * @param id identificador da relação
     * @return status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pessoaRelacaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
