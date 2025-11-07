package br.com.unicos.ms_compras.controller.fiscal;

import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalItemListDTO;
import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalItemRequest;
import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalItemResponse;
import br.com.unicos.ms_compras.service.fiscal.NotaFiscalItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos itens
 * vinculados às notas fiscais de compra.
 *
 * <p>Permite criar, atualizar, buscar e excluir itens de notas fiscais,
 * além de listar itens por nota fiscal ou por produto.</p>
 */
@RestController
@RequestMapping("/v1/notas-fiscais-itens")
@RequiredArgsConstructor
public class NotaFiscalItemController {

    private final NotaFiscalItemService notaFiscalItemService;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    /**
     * Cria um novo item vinculado a uma nota fiscal de compra.
     *
     * @param request DTO contendo as informações do item.
     * @return O item criado.
     */
    @PostMapping
    public ResponseEntity<NotaFiscalItemResponse> criar(@Valid @RequestBody NotaFiscalItemRequest request) {
        NotaFiscalItemResponse response = notaFiscalItemService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um item existente de uma nota fiscal.
     *
     * @param id      ID do item.
     * @param request DTO com os novos dados.
     * @return O item atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotaFiscalItemResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody NotaFiscalItemRequest request) {
        NotaFiscalItemResponse response = notaFiscalItemService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um item de nota fiscal pelo seu ID.
     *
     * @param id ID do item.
     * @return O item correspondente, caso exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotaFiscalItemResponse> buscarPorId(@PathVariable Long id) {
        return notaFiscalItemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os itens pertencentes a uma nota fiscal específica.
     *
     * @param notaFiscalCompraId ID da nota fiscal de compra.
     * @return Lista de itens vinculados à nota.
     */
    @GetMapping("/nota-fiscal/{notaFiscalCompraId}")
    public ResponseEntity<List<NotaFiscalItemListDTO>> listarPorNotaFiscal(@PathVariable Long notaFiscalCompraId) {
        List<NotaFiscalItemListDTO> lista = notaFiscalItemService.listarPorNotaFiscal(notaFiscalCompraId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista todos os itens relacionados a um produto específico.
     *
     * @param produtoId ID do produto.
     * @return Lista de itens vinculados ao produto.
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<NotaFiscalItemListDTO>> listarPorProduto(@PathVariable Long produtoId) {
        List<NotaFiscalItemListDTO> lista = notaFiscalItemService.listarPorProduto(produtoId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Exclui um item de nota fiscal.
     *
     * @param id ID do item a ser removido.
     * @return Resposta sem conteúdo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        notaFiscalItemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
