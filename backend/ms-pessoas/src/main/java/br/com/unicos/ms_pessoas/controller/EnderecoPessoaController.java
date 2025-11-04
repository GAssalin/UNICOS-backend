package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.EnderecoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.EnderecoPessoaResponse;
import br.com.unicos.ms_pessoas.service.EnderecoPessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos endereços vinculados às pessoas.
 *
 * <p>Fornece endpoints para criação, atualização, listagem e exclusão de endereços,
 * com suporte à definição de endereço principal.</p>
 */
@RestController
@RequestMapping("/v1/enderecos")
@RequiredArgsConstructor
public class EnderecoPessoaController {

    private final EnderecoPessoaService enderecoPessoaService;

    /**
     * Cria um novo endereço vinculado a uma pessoa.
     *
     * @param request DTO com os dados do endereço
     * @return {@link EnderecoPessoaResponse} com o endereço criado
     */
    @PostMapping
    public ResponseEntity<EnderecoPessoaResponse> salvar(@Valid @RequestBody EnderecoPessoaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoPessoaService.salvar(request));
    }

    /**
     * Atualiza os dados de um endereço existente.
     *
     * @param id      identificador do endereço
     * @param request DTO com os novos dados
     * @return {@link EnderecoPessoaResponse} atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoPessoaResponse> atualizar(@PathVariable Long id,
                                                            @Valid @RequestBody EnderecoPessoaRequest request) {
        return ResponseEntity.ok(enderecoPessoaService.atualizar(id, request));
    }

    /**
     * Lista todos os endereços vinculados a uma pessoa.
     *
     * @param pessoaId identificador da pessoa
     * @return lista de {@link EnderecoPessoaResponse}
     */
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<EnderecoPessoaResponse>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(enderecoPessoaService.listarPorPessoa(pessoaId));
    }

    /**
     * Busca um endereço pelo seu ID.
     *
     * @param id identificador do endereço
     * @return {@link EnderecoPessoaResponse} se encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoPessoaResponse> buscarPorId(@PathVariable Long id) {
        return enderecoPessoaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Exclui um endereço pelo seu ID.
     *
     * @param id identificador do endereço
     * @return status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        enderecoPessoaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
