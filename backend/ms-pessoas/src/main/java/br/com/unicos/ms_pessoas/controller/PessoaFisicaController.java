package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaResponse;
import br.com.unicos.ms_pessoas.service.interfaces.PessoaFisicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento de Pessoas Físicas dentro do UniCoS.
 *
 * <p>Inclui operações de criação, atualização, exclusão e consultas
 * específicas, como busca por CPF, nome e nome social.</p>
 */
@RestController
@RequestMapping("/v1/pessoas/fisicas")
@RequiredArgsConstructor
public class PessoaFisicaController {

    private final PessoaFisicaService service;

    // ============================================================
    // Criar
    // ============================================================

    /**
     * Cria uma nova Pessoa Física.
     *
     * @param request dados da pessoa física a ser cadastrada.
     * @return ResponseEntity contendo os dados criados.
     */
    @PostMapping
    public ResponseEntity<PessoaFisicaResponse> criar(@RequestBody PessoaFisicaRequest request) {
        PessoaFisicaResponse response = service.criar(request);
        return ResponseEntity
                .created(URI.create("/v1/pessoas/fisicas/" + response.id()))
                .body(response);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    /**
     * Atualiza os dados de uma Pessoa Física existente.
     *
     * @param id      identificador da pessoa.
     * @param request dados atualizados.
     * @return PessoaFisicaResponse atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PessoaFisicaResponse> atualizar(
            @PathVariable Long id,
            @RequestBody PessoaFisicaRequest request) {

        PessoaFisicaResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Excluir
    // ============================================================

    /**
     * Remove uma Pessoa Física pelo ID.
     *
     * @param id identificador da pessoa.
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
     * Retorna os dados completos de uma Pessoa Física pelo ID.
     *
     * @param id identificador da pessoa.
     * @return ResponseEntity com os dados ou 404 caso não encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PessoaFisicaResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Buscar por CPF
    // ============================================================

    /**
     * Busca uma Pessoa Física pelo CPF.
     *
     * @param cpf CPF sem formatação.
     * @return ResponseEntity contendo a pessoa encontrada ou 404.
     */
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<PessoaFisicaResponse> buscarPorCpf(@PathVariable String cpf) {
        return service.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar todas
    // ============================================================

    /**
     * Lista todas as Pessoas Físicas cadastradas.
     *
     * @return lista simplificada contendo ID, Nome e CPF.
     */
    @GetMapping
    public ResponseEntity<List<PessoaFisicaListDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // ============================================================
    // Listar por Nome Social
    // ============================================================

    /**
     * Lista pessoas cujo nome social seja exatamente igual ao informado.
     *
     * @param nomeSocial nome social completo.
     * @return lista de pessoas encontradas.
     */
    @GetMapping("/nome-social/{nomeSocial}")
    public ResponseEntity<List<PessoaFisicaListDTO>> listarPorNomeSocial(@PathVariable String nomeSocial) {
        return ResponseEntity.ok(service.listarPorNomeSocial(nomeSocial));
    }

    // ============================================================
    // Listar por Nome
    // ============================================================

    /**
     * Lista Pessoas Físicas cujo nome contenha o termo informado.
     *
     * @param nome parte do nome da pessoa.
     * @return lista correspondente.
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<PessoaFisicaListDTO>> listarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorNome(nome));
    }
}
