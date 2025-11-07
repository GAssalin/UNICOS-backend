package br.com.unicos.ms_compras.controller.pedido;

import br.com.unicos.ms_compras.dto.pedido.PedidoItemCompraListDTO;
import br.com.unicos.ms_compras.dto.pedido.PedidoItemCompraRequest;
import br.com.unicos.ms_compras.dto.pedido.PedidoItemCompraResponse;
import br.com.unicos.ms_compras.service.pedido.PedidoItemCompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos itens dos pedidos de compra.
 *
 * <p>
 * Permite criar, atualizar, buscar, listar e excluir itens vinculados
 * a um pedido de compra específico.
 * </p>
 */
@RestController
@RequestMapping("/v1/pedidos-compras/itens")
@RequiredArgsConstructor
public class PedidoItemCompraController {

    private final PedidoItemCompraService pedidoItemCompraService;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    /**
     * Cria um novo item para um pedido de compra existente.
     *
     * @param pedidoCompraId ID do pedido de compra.
     * @param request        DTO contendo as informações do item.
     * @return Item criado.
     */
    @PostMapping("/pedido/{pedidoCompraId}")
    public ResponseEntity<PedidoItemCompraResponse> criar(
            @PathVariable Long pedidoCompraId,
            @Valid @RequestBody PedidoItemCompraRequest request) {

        PedidoItemCompraResponse response = pedidoItemCompraService.criar(request, pedidoCompraId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um item de pedido de compra existente.
     *
     * @param id      ID do item.
     * @param request DTO contendo os novos dados.
     * @return Item atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PedidoItemCompraResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoItemCompraRequest request) {

        PedidoItemCompraResponse response = pedidoItemCompraService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui um item de pedido de compra pelo seu identificador.
     *
     * @param id ID do item a ser excluído.
     * @return Resposta sem conteúdo.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        pedidoItemCompraService.deletar(id);
    }

    // ==========================================================
    // 🔹 CONSULTAS
    // ==========================================================

    /**
     * Busca um item de pedido de compra pelo seu identificador.
     *
     * @param id ID do item.
     * @return Item encontrado, caso exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoItemCompraResponse> buscarPorId(@PathVariable Long id) {
        return pedidoItemCompraService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os itens vinculados a um pedido de compra específico.
     *
     * @param pedidoCompraId ID do pedido de compra.
     * @return Lista de itens vinculados ao pedido.
     */
    @GetMapping("/pedido/{pedidoCompraId}")
    public ResponseEntity<List<PedidoItemCompraListDTO>> listarPorPedido(@PathVariable Long pedidoCompraId) {
        List<PedidoItemCompraListDTO> lista = pedidoItemCompraService.listarPorPedido(pedidoCompraId);
        return ResponseEntity.ok(lista);
    }
}
