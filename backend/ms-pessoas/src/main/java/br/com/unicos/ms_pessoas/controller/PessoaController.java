package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.PessoaRequest;
import br.com.unicos.ms_pessoas.dto.PessoaResponse;
import br.com.unicos.ms_pessoas.service.PessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento de pessoas (físicas e jurídicas).
 *
 * <p>Fornece endpoints genéricos para manipulação e consulta de registros da entidade {@link br.com.unicos.ms_pessoas.model.Pessoa}.</p>
 */
@RestController
@RequestMapping("/v1/pessoas")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService pessoaService;

    /**
     * Cria uma nova pessoa (física ou jurídica).
     *
     * @param request DTO com os dados da nova pessoa
     * @return {@link PessoaResponse} com os dados da pessoa criada
     */
    @PostMapping
    public ResponseEntity<PessoaResponse> salvar(@Valid @RequestBody PessoaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.salvar(request));
    }

    /**
     * Atualiza os dados de uma pessoa existente.
     *
     * @param id      identificador da pessoa
     * @param request DTO com os novos dados
     * @return {@link PessoaResponse} com as informações atualizadas
     */
    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponse> atualizar(@PathVariable Long id,
                                                    @Valid @RequestBody PessoaRequest request) {
        return ResponseEntity.ok(pessoaService.atualizar(id, request));
    }

    /**
     * Lista todas as pessoas cadastradas no sistema.
     *
     * @return lista de {@link PessoaResponse}
     */
    @GetMapping
    public ResponseEntity<List<PessoaResponse>> listarTodos() {
        return ResponseEntity.ok(pessoaService.listarTodos());
    }

    /**
     * Busca uma pessoa pelo ID.
     *
     * @param id identificador da pessoa
     * @return {@link PessoaResponse} se encontrada, ou 404 caso contrário
     */
    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponse> buscarPorId(@PathVariable Long id) {
        return pessoaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Exclui uma pessoa do sistema.
     *
     * @param id identificador da pessoa a ser excluída
     * @return status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pessoaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
