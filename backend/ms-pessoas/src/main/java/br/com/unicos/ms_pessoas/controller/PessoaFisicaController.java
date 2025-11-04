package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.PessoaFisicaRequest;
import br.com.unicos.ms_pessoas.dto.PessoaFisicaResponse;
import br.com.unicos.ms_pessoas.service.PessoaFisicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento de pessoas físicas.
 *
 * <p>Fornece endpoints para operações de CRUD e consultas específicas
 * baseadas no CPF da entidade {@link br.com.unicos.ms_pessoas.model.PessoaFisica}.</p>
 */
@RestController
@RequestMapping("/v1/pessoas/fisicas")
@RequiredArgsConstructor
public class PessoaFisicaController {

    private final PessoaFisicaService pessoaFisicaService;

    /**
     * Cria uma nova pessoa física.
     *
     * @param request DTO com os dados da pessoa física
     * @return {@link PessoaFisicaResponse} com os dados da pessoa criada
     */
    @PostMapping
    public ResponseEntity<PessoaFisicaResponse> salvar(@Valid @RequestBody PessoaFisicaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaFisicaService.salvar(request));
    }

    /**
     * Atualiza os dados de uma pessoa física existente.
     *
     * @param id      identificador da pessoa física
     * @param request DTO com os novos dados
     * @return {@link PessoaFisicaResponse} com os dados atualizados
     */
    @PutMapping("/{id}")
    public ResponseEntity<PessoaFisicaResponse> atualizar(@PathVariable Long id,
                                                          @Valid @RequestBody PessoaFisicaRequest request) {
        return ResponseEntity.ok(pessoaFisicaService.atualizar(id, request));
    }

    /**
     * Lista todas as pessoas físicas cadastradas.
     *
     * @return lista de {@link PessoaFisicaResponse}
     */
    @GetMapping
    public ResponseEntity<List<PessoaFisicaResponse>> listarTodos() {
        return ResponseEntity.ok(pessoaFisicaService.listarTodos());
    }

    /**
     * Busca uma pessoa física pelo ID.
     *
     * @param id identificador da pessoa
     * @return {@link PessoaFisicaResponse} se encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<PessoaFisicaResponse> buscarPorId(@PathVariable Long id) {
        return pessoaFisicaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca uma pessoa física pelo CPF.
     *
     * @param cpf CPF da pessoa (somente números)
     * @return {@link PessoaFisicaResponse} se encontrada
     */
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<PessoaFisicaResponse> buscarPorCpf(@PathVariable String cpf) {
        return pessoaFisicaService.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Exclui uma pessoa física pelo ID.
     *
     * @param id identificador da pessoa
     * @return status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pessoaFisicaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
