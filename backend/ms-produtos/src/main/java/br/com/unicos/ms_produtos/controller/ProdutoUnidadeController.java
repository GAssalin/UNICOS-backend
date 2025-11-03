package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.ProdutoUnidadeRequest;
import br.com.unicos.ms_produtos.dto.ProdutoUnidadeResponse;
import br.com.unicos.ms_produtos.service.ProdutoUnidadeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos vínculos
 * entre produtos e suas respectivas unidades de medida.
 *
 * Fornece endpoints para criação, atualização, listagem e exclusão de vínculos.
 */
@RestController
@RequestMapping("/v1/produtos-unidades")
public class ProdutoUnidadeController {

    private final ProdutoUnidadeService produtoUnidadeService;

    public ProdutoUnidadeController(ProdutoUnidadeService produtoUnidadeService) {
        this.produtoUnidadeService = produtoUnidadeService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria um novo vínculo entre produto e unidade de medida.
     *
     * @param request Dados do vínculo a ser criado.
     * @return ProdutoUnidadeResponse criado.
     */
    @PostMapping
    public ResponseEntity<ProdutoUnidadeResponse> criarVinculo(@Valid @RequestBody ProdutoUnidadeRequest request) {
        ProdutoUnidadeResponse response = produtoUnidadeService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um vínculo existente entre produto e unidade de medida.
     *
     * @param id      Identificador do vínculo.
     * @param request Dados atualizados do vínculo.
     * @return ProdutoUnidadeResponse atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoUnidadeResponse> atualizarVinculo(@PathVariable Long id,
                                                                   @Valid @RequestBody ProdutoUnidadeRequest request) {
        ProdutoUnidadeResponse response = produtoUnidadeService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um vínculo pelo ID.
     *
     * @param id Identificador do vínculo.
     * @return ProdutoUnidadeResponse, se encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoUnidadeResponse> buscarPorId(@PathVariable Long id) {
        return produtoUnidadeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os vínculos produto–unidade cadastrados.
     *
     * @return Lista de ProdutoUnidadeResponse.
     */
    @GetMapping
    public ResponseEntity<List<ProdutoUnidadeResponse>> listarTodos() {
        List<ProdutoUnidadeResponse> lista = produtoUnidadeService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista todos os vínculos associados a um produto específico.
     *
     * @param produtoId ID do produto.
     * @return Lista de ProdutoUnidadeResponse.
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<ProdutoUnidadeResponse>> listarPorProduto(@PathVariable Long produtoId) {
        List<ProdutoUnidadeResponse> lista = produtoUnidadeService.listarPorProduto(produtoId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista todos os vínculos associados a uma unidade de medida específica.
     *
     * @param unidadeMedidaId ID da unidade de medida.
     * @return Lista de ProdutoUnidadeResponse.
     */
    @GetMapping("/unidade/{unidadeMedidaId}")
    public ResponseEntity<List<ProdutoUnidadeResponse>> listarPorUnidade(@PathVariable Long unidadeMedidaId) {
        List<ProdutoUnidadeResponse> lista = produtoUnidadeService.listarPorUnidadeMedida(unidadeMedidaId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Verifica se existe vínculo entre um produto e uma unidade de medida.
     *
     * @param produtoId       ID do produto.
     * @param unidadeMedidaId ID da unidade de medida.
     * @return true se o vínculo existir, false caso contrário.
     */
    @GetMapping("/verificar")
    public ResponseEntity<Boolean> verificarVinculo(@RequestParam Long produtoId,
                                                    @RequestParam Long unidadeMedidaId) {
        boolean existe = produtoUnidadeService.verificarVinculo(produtoId, unidadeMedidaId);
        return ResponseEntity.ok(existe);
    }

    /**
     * Exclui um vínculo produto–unidade de medida pelo ID.
     *
     * @param id Identificador do vínculo.
     * @return Resposta 204 (sem conteúdo) em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVinculo(@PathVariable Long id) {
        produtoUnidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}