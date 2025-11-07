package br.com.unicos.ms_compras.controller.recebimento;

import br.com.unicos.ms_compras.dto.recebimento.RecebimentoItemListDTO;
import br.com.unicos.ms_compras.dto.recebimento.RecebimentoItemRequest;
import br.com.unicos.ms_compras.dto.recebimento.RecebimentoItemResponse;
import br.com.unicos.ms_compras.service.recebimento.RecebimentoItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos itens de recebimento de compra.
 *
 * <p>Permite criar, atualizar, buscar e excluir itens conferidos,
 * além de listar por produto ou por recebimento associado.</p>
 */
@RestController
@RequestMapping("/v1/recebimentos-itens")
@RequiredArgsConstructor
public class RecebimentoItemController {

    private final RecebimentoItemService recebimentoItemService;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    /**
     * Cria um novo item vinculado a um recebimento de compra.
     *
     * @param request DTO contendo as informações do item.
     * @return O item criado.
     */
    @PostMapping
    public ResponseEntity<RecebimentoItemResponse> criar(@Valid @RequestBody RecebimentoItemRequest request) {
        RecebimentoItemResponse response = recebimentoItemService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um item existente de um recebimento.
     *
     * @param id      ID do item.
     * @param request DTO com os novos dados.
     * @return O item atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecebimentoItemResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RecebimentoItemRequest request) {
        RecebimentoItemResponse response = recebimentoItemService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um item de recebimento pelo seu ID.
     *
     * @param id ID do item.
     * @return O item correspondente, caso exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecebimentoItemResponse> buscarPorId(@PathVariable Long id) {
        return recebimentoItemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os itens vinculados a um recebimento específico.
     *
     * @param recebimentoCompraId ID do recebimento de compra.
     * @return Lista de itens associados ao recebimento.
     */
    @GetMapping("/recebimento/{recebimentoCompraId}")
    public ResponseEntity<List<RecebimentoItemListDTO>> listarPorRecebimento(@PathVariable Long recebimentoCompraId) {
        List<RecebimentoItemListDTO> lista = recebimentoItemService.listarPorRecebimento(recebimentoCompraId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista todos os itens relacionados a um produto específico.
     *
     * @param produtoId ID do produto.
     * @return Lista de itens que envolvem o produto informado.
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<RecebimentoItemListDTO>> listarPorProduto(@PathVariable Long produtoId) {
        List<RecebimentoItemListDTO> lista = recebimentoItemService.listarPorProduto(produtoId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Remove um item de recebimento.
     *
     * @param id ID do item a ser removido.
     * @return Resposta sem conteúdo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        recebimentoItemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
