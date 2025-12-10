package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaResponse;
import br.com.unicos.ms_pessoas.service.interfaces.PessoaJuridicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento de Pessoas Jurídicas dentro do UniCoS.
 *
 * <p>Oferece operações de criação, atualização, exclusão e consultas especializadas,
 * como busca por CNPJ, razão social e nome fantasia.</p>
 */
@RestController
@RequestMapping("/v1/pessoas/juridicas")
@RequiredArgsConstructor
public class PessoaJuridicaController {

    private final PessoaJuridicaService service;

    // ============================================================
    // Criar
    // ============================================================

    /**
     * Cadastra uma nova Pessoa Jurídica.
     *
     * @param request dados da pessoa jurídica a ser criada.
     * @return ResponseEntity contendo o objeto criado.
     */
    @PostMapping
    public ResponseEntity<PessoaJuridicaResponse> criar(@RequestBody PessoaJuridicaRequest request) {
        PessoaJuridicaResponse response = service.criar(request);
        return ResponseEntity
                .created(URI.create("/v1/pessoas/juridicas/" + response.id()))
                .body(response);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    /**
     * Atualiza os dados de uma Pessoa Jurídica existente.
     *
     * @param id      identificador da empresa.
     * @param request dados atualizados.
     * @return ResponseEntity com os dados atualizados.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PessoaJuridicaResponse> atualizar(
            @PathVariable Long id,
            @RequestBody PessoaJuridicaRequest request) {

        PessoaJuridicaResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Excluir
    // ============================================================

    /**
     * Remove uma Pessoa Jurídica pelo ID.
     *
     * @param id identificador da empresa.
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
     * Busca os dados completos de uma Pessoa Jurídica pelo seu ID.
     *
     * @param id identificador da empresa.
     * @return ResponseEntity com os dados ou 404 se não encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PessoaJuridicaResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Buscar por CNPJ
    // ============================================================

    /**
     * Busca uma Pessoa Jurídica pelo seu CNPJ.
     *
     * @param cnpj número do CNPJ (somente dígitos).
     * @return ResponseEntity com os dados ou 404 se não encontrada.
     */
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<PessoaJuridicaResponse> buscarPorCnpj(@PathVariable String cnpj) {
        return service.buscarPorCnpj(cnpj)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar todas
    // ============================================================

    /**
     * Lista todas as Pessoas Jurídicas cadastradas.
     *
     * @return lista simplificada contendo ID, Nome e CNPJ.
     */
    @GetMapping
    public ResponseEntity<List<PessoaJuridicaListDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // ============================================================
    // Listar por Nome Fantasia (exato)
    // ============================================================

    /**
     * Lista empresas pelo nome fantasia exato.
     *
     * @param nomeFantasia nome fantasia completo.
     * @return lista de empresas encontradas.
     */
    @GetMapping("/nome-fantasia/{nomeFantasia}")
    public ResponseEntity<List<PessoaJuridicaListDTO>> listarPorNomeFantasia(@PathVariable String nomeFantasia) {
        return ResponseEntity.ok(service.listarPorNomeFantasia(nomeFantasia));
    }

    // ============================================================
    // Listar por Nome (contains)
    // ============================================================

    /**
     * Lista empresas cujo nome contenha o termo informado.
     *
     * @param nome termo parcial.
     * @return lista correspondente.
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<PessoaJuridicaListDTO>> listarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorNome(nome));
    }
}
