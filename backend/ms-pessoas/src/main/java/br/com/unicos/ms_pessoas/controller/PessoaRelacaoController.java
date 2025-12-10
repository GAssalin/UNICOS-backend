package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoRequest;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoResponse;
import br.com.unicos.ms_pessoas.service.interfaces.PessoaRelacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento das relações entre pessoas no UniCoS.
 *
 * <p>Permite criar, atualizar, remover e consultar vínculos como responsável,
 * dependente, sócio, representante legal e outros tipos definidos no domínio.</p>
 */
@RestController
@RequestMapping("/v1/pessoas/relacoes")
@RequiredArgsConstructor
public class PessoaRelacaoController {

    private final PessoaRelacaoService service;

    // ============================================================
    // Criar
    // ============================================================

    /**
     * Cria uma nova relação entre duas pessoas.
     *
     * @param request dados da relação a ser criada.
     * @return ResponseEntity contendo a relação criada.
     */
    @PostMapping
    public ResponseEntity<PessoaRelacaoResponse> criar(@RequestBody PessoaRelacaoRequest request) {
        PessoaRelacaoResponse response = service.criar(request);
        return ResponseEntity
                .created(URI.create("/v1/pessoas/relacoes/" + response.id()))
                .body(response);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    /**
     * Atualiza uma relação existente.
     *
     * @param id      identificador da relação.
     * @param request novos dados da relação.
     * @return ResponseEntity contendo a relação atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PessoaRelacaoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody PessoaRelacaoRequest request) {

        PessoaRelacaoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Excluir
    // ============================================================

    /**
     * Remove uma relação pelo identificador.
     *
     * @param id identificador da relação.
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
     * Busca uma relação pelo ID.
     *
     * @param id identificador da relação.
     * @return ResponseEntity com a relação ou 404 se não encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PessoaRelacaoResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar todas
    // ============================================================

    /**
     * Lista todas as relações cadastradas.
     *
     * @return lista simplificada de relações.
     */
    @GetMapping
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // ============================================================
    // Listar por Pessoa Principal
    // ============================================================

    /**
     * Lista todas as relações em que a pessoa informada é o ator principal.
     *
     * @param pessoaId ID da pessoa.
     * @return lista de relações.
     */
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(service.listarPorPessoa(pessoaId));
    }

    // ============================================================
    // Listar por Pessoa Relacionada
    // ============================================================

    /**
     * Lista todas as relações em que a pessoa informada é o indivíduo relacionado.
     *
     * @param relacionadoId ID da pessoa relacionada.
     * @return lista de relações.
     */
    @GetMapping("/relacionado/{relacionadoId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorRelacionado(@PathVariable Long relacionadoId) {
        return ResponseEntity.ok(service.listarPorRelacionado(relacionadoId));
    }

    // ============================================================
    // Listar por Tipo de Relação
    // ============================================================

    /**
     * Lista todas as relações de um tipo específico.
     *
     * @param tipoRelacaoPessoaId identificador do tipo de relação.
     * @return lista de vínculos desse tipo.
     */
    @GetMapping("/tipo/{tipoRelacaoPessoaId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorTipo(@PathVariable Long tipoRelacaoPessoaId) {
        return ResponseEntity.ok(service.listarPorTipo(tipoRelacaoPessoaId));
    }

    // ============================================================
    // Busca por Nome da Pessoa Principal (contains ignore case)
    // ============================================================

    /**
     * Busca relações filtrando pelo nome da pessoa principal.
     *
     * @param nome parte do nome.
     * @return lista correspondente de relações.
     */
    @GetMapping("/pessoa/nome/{nome}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorPessoaENome(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorPessoaENome(nome));
    }

    // ============================================================
    // Busca por Nome da Pessoa Relacionada (contains ignore case)
    // ============================================================

    /**
     * Busca relações filtrando pelo nome da pessoa relacionada.
     *
     * @param nome parte do nome.
     * @return lista de relações.
     */
    @GetMapping("/relacionado/nome/{nome}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorRelacionadoENome(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorRelacionadoENome(nome));
    }

    // ============================================================
    // Filtrar por Pessoa Principal e Relacionado simultaneamente
    // ============================================================

    /**
     * Lista relações específicas entre duas pessoas.
     *
     * @param pessoaId      ID da pessoa principal.
     * @param relacionadoId ID da pessoa relacionada.
     * @return lista de vínculos entre essas duas pessoas.
     */
    @GetMapping("/pessoa/{pessoaId}/relacionado/{relacionadoId}")
    public ResponseEntity<List<PessoaRelacaoListDTO>> listarPorPessoaERelacionado(
            @PathVariable Long pessoaId,
            @PathVariable Long relacionadoId) {

        return ResponseEntity.ok(service.listarPorPessoaERelacionado(pessoaId, relacionadoId));
    }
}
