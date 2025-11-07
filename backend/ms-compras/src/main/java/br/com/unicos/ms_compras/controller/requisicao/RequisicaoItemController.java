package br.com.unicos.ms_compras.controller.requisicao;

import br.com.unicos.ms_compras.dto.requisicao.RequisicaoItemListDTO;
import br.com.unicos.ms_compras.dto.requisicao.RequisicaoItemRequest;
import br.com.unicos.ms_compras.dto.requisicao.RequisicaoItemResponse;
import br.com.unicos.ms_compras.service.requisicao.RequisicaoItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos itens de requisição de compra.
 *
 * <p>Permite criar, atualizar, buscar e excluir itens vinculados a uma requisição,
 * além de listar por produto ou por requisição associada.</p>
 */
@RestController
@RequestMapping("/v1/requisicoes-itens")
@RequiredArgsConstructor
public class RequisicaoItemController {

    private final RequisicaoItemService requisicaoItemService;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    /**
     * Cria um novo item vinculado a uma requisição de compra.
     *
     * @param request DTO contendo as informações do item.
     * @return O item criado.
     */
    @PostMapping
    public ResponseEntity<RequisicaoItemResponse> criar(@Valid @RequestBody RequisicaoItemRequest request) {
        RequisicaoItemResponse response = requisicaoItemService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um item existente vinculado a uma requisição.
     *
     * @param id      ID do item.
     * @param request DTO contendo os novos dados.
     * @return O item atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RequisicaoItemResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RequisicaoItemRequest request) {
        RequisicaoItemResponse response = requisicaoItemService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um item de requisição pelo seu ID.
     *
     * @param id ID do item.
     * @return O item encontrado, caso exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RequisicaoItemResponse> buscarPorId(@PathVariable Long id) {
        return requisicaoItemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os itens vinculados a uma requisição de compra específica.
     *
     * @param requisicaoCompraId ID da requisição de compra.
     * @return Lista de itens associados à requisição.
     */
    @GetMapping("/requisicao/{requisicaoCompraId}")
    public ResponseEntity<List<RequisicaoItemListDTO>> listarPorRequisicao(@PathVariable Long requisicaoCompraId) {
        List<RequisicaoItemListDTO> lista = requisicaoItemService.listarPorRequisicao(requisicaoCompraId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista todos os itens vinculados a um produto específico.
     *
     * @param produtoId ID do produto.
     * @return Lista de itens que fazem referência ao produto.
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<RequisicaoItemListDTO>> listarPorProduto(@PathVariable Long produtoId) {
        List<RequisicaoItemListDTO> lista = requisicaoItemService.listarPorProduto(produtoId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Remove um item de requisição de compra.
     *
     * @param id ID do item a ser removido.
     * @return Resposta sem conteúdo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        requisicaoItemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
