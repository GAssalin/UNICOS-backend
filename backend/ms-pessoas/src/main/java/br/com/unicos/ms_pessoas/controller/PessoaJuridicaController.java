package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.PessoaJuridicaRequest;
import br.com.unicos.ms_pessoas.dto.PessoaJuridicaResponse;
import br.com.unicos.ms_pessoas.service.PessoaJuridicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento de pessoas jurídicas.
 *
 * <p>Fornece endpoints para criação, atualização, listagem e consultas
 * baseadas no CNPJ da entidade {@link br.com.unicos.ms_pessoas.model.PessoaJuridica}.</p>
 */
@RestController
@RequestMapping("/v1/pessoas/juridicas")
@RequiredArgsConstructor
public class PessoaJuridicaController {

    private final PessoaJuridicaService pessoaJuridicaService;

    /**
     * Cria uma nova pessoa jurídica.
     *
     * @param request DTO com os dados da pessoa jurídica
     * @return {@link PessoaJuridicaResponse} com os dados criados
     */
    @PostMapping
    public ResponseEntity<PessoaJuridicaResponse> salvar(@Valid @RequestBody PessoaJuridicaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaJuridicaService.salvar(request));
    }

    /**
     * Atualiza os dados de uma pessoa jurídica existente.
     *
     * @param id      identificador da pessoa jurídica
     * @param request DTO com os novos dados
     * @return {@link PessoaJuridicaResponse} com os dados atualizados
     */
    @PutMapping("/{id}")
    public ResponseEntity<PessoaJuridicaResponse> atualizar(@PathVariable Long id,
                                                            @Valid @RequestBody PessoaJuridicaRequest request) {
        return ResponseEntity.ok(pessoaJuridicaService.atualizar(id, request));
    }

    /**
     * Lista todas as pessoas jurídicas cadastradas.
     *
     * @return lista de {@link PessoaJuridicaResponse}
     */
    @GetMapping
    public ResponseEntity<List<PessoaJuridicaResponse>> listarTodos() {
        return ResponseEntity.ok(pessoaJuridicaService.listarTodos());
    }

    /**
     * Busca uma pessoa jurídica pelo ID.
     *
     * @param id identificador da pessoa
     * @return {@link PessoaJuridicaResponse} se encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<PessoaJuridicaResponse> buscarPorId(@PathVariable Long id) {
        return pessoaJuridicaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca uma pessoa jurídica pelo CNPJ.
     *
     * @param cnpj CNPJ da empresa (somente números)
     * @return {@link PessoaJuridicaResponse} se encontrada
     */
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<PessoaJuridicaResponse> buscarPorCnpj(@PathVariable String cnpj) {
        return pessoaJuridicaService.buscarPorCnpj(cnpj)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Exclui uma pessoa jurídica do sistema.
     *
     * @param id identificador da pessoa jurídica
     * @return status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pessoaJuridicaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
