package br.com.erp.ms_produtos.controller;

import br.com.erp.ms_produtos.dto.AtributoPersonalizadoListDTO;
import br.com.erp.ms_produtos.dto.AtributoPersonalizadoRequest;
import br.com.erp.ms_produtos.dto.AtributoPersonalizadoResponse;
import br.com.erp.ms_produtos.service.AtributoPersonalizadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos atributos personalizados de produtos.
 *
 * Permite a criação, atualização, listagem, exclusão e busca de atributos
 * vinculados aos produtos cadastrados no sistema.
 */
@RestController
@RequestMapping("/v1/atributos-personalizados")
public class AtributoPersonalizadoController {

    private final AtributoPersonalizadoService atributoPersonalizadoService;

    public AtributoPersonalizadoController(AtributoPersonalizadoService atributoPersonalizadoService) {
        this.atributoPersonalizadoService = atributoPersonalizadoService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria um novo atributo personalizado para um produto.
     *
     * @param request Dados do atributo personalizado.
     * @return AtributoPersonalizadoResponse criado.
     */
    @PostMapping
    public ResponseEntity<AtributoPersonalizadoResponse> criar(
            @Valid @RequestBody AtributoPersonalizadoRequest request) {
        AtributoPersonalizadoResponse response = atributoPersonalizadoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um atributo personalizado existente.
     *
     * @param id      Identificador do atributo.
     * @param request Dados atualizados do atributo.
     * @return AtributoPersonalizadoResponse atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AtributoPersonalizadoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtributoPersonalizadoRequest request) {
        AtributoPersonalizadoResponse response = atributoPersonalizadoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um atributo personalizado pelo ID.
     *
     * @param id Identificador do atributo.
     * @return AtributoPersonalizadoResponse, se encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AtributoPersonalizadoResponse> buscarPorId(@PathVariable Long id) {
        return atributoPersonalizadoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os atributos personalizados cadastrados.
     *
     * @return Lista de AtributoPersonalizadoResponse.
     */
    @GetMapping
    public ResponseEntity<List<AtributoPersonalizadoResponse>> listarTodos() {
        List<AtributoPersonalizadoResponse> lista = atributoPersonalizadoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    /**
     * Exclui um atributo personalizado pelo ID.
     *
     * @param id Identificador do atributo.
     * @return Resposta 204 (sem conteúdo) em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        atributoPersonalizadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Lista todos os atributos personalizados de um produto.
     *
     * @param produtoId ID do produto.
     * @return Lista de AtributoPersonalizadoResponse.
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<AtributoPersonalizadoResponse>> listarPorProduto(@PathVariable Long produtoId) {
        List<AtributoPersonalizadoResponse> lista = atributoPersonalizadoService.listarPorProduto(produtoId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Busca todos os atributos cujo nome contenha o termo informado.
     *
     * @param nome Termo parcial de busca.
     * @return Lista de AtributoPersonalizadoListDTO.
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<AtributoPersonalizadoListDTO>> buscarPorNomeContendo(@RequestParam String nome) {
        List<AtributoPersonalizadoListDTO> lista = atributoPersonalizadoService.buscarPorNomeContendo(nome);
        return ResponseEntity.ok(lista);
    }

    /**
     * Verifica se já existe um atributo com o mesmo nome para o produto informado.
     *
     * @param produtoId ID do produto.
     * @param nome      Nome do atributo.
     * @return true se já existir, false caso contrário.
     */
    @GetMapping("/verificar")
    public ResponseEntity<Boolean> verificarDuplicidade(@RequestParam Long produtoId,
                                                        @RequestParam String nome) {
        boolean duplicado = atributoPersonalizadoService.verificarDuplicidade(produtoId, nome);
        return ResponseEntity.ok(duplicado);
    }
}