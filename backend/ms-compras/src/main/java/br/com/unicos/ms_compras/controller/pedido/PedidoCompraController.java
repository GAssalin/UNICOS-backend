package br.com.unicos.ms_compras.controller.pedido;

import br.com.unicos.ms_compras.dto.pedido.PedidoCompraListDTO;
import br.com.unicos.ms_compras.dto.pedido.PedidoCompraRequest;
import br.com.unicos.ms_compras.dto.pedido.PedidoCompraResponse;
import br.com.unicos.ms_compras.enums.StatusPedidoCompra;
import br.com.unicos.ms_compras.service.pedido.PedidoCompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos pedidos de compra.
 *
 * <p>Permite criar, atualizar, buscar, listar e excluir pedidos,
 * além de filtrar por fornecedor, período e atualizar o status.</p>
 */
@RestController
@RequestMapping("/v1/pedidos-compras")
@RequiredArgsConstructor
public class PedidoCompraController {

    private final PedidoCompraService pedidoCompraService;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    /**
     * Cria um novo pedido de compra.
     *
     * @param request DTO contendo as informações do pedido.
     * @return O pedido criado.
     */
    @PostMapping
    public ResponseEntity<PedidoCompraResponse> criar(@Valid @RequestBody PedidoCompraRequest request) {
        PedidoCompraResponse response = pedidoCompraService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um pedido de compra existente.
     *
     * @param id      ID do pedido.
     * @param request DTO contendo os novos dados.
     * @return O pedido atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PedidoCompraResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoCompraRequest request) {
        PedidoCompraResponse response = pedidoCompraService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um pedido de compra pelo seu ID.
     *
     * @param id ID do pedido.
     * @return O pedido encontrado, caso exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoCompraResponse> buscarPorId(@PathVariable Long id) {
        return pedidoCompraService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os pedidos de compra com paginação.
     *
     * @param pageable informações de paginação.
     * @return Página de pedidos de compra.
     */
    @GetMapping
    public ResponseEntity<Page<PedidoCompraListDTO>> listar(Pageable pageable) {
        Page<PedidoCompraListDTO> page = pedidoCompraService.listar(pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * Lista pedidos de compra realizados por um fornecedor específico.
     *
     * @param fornecedorId ID do fornecedor.
     * @return Lista de pedidos do fornecedor.
     */
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<List<PedidoCompraListDTO>> listarPorFornecedor(@PathVariable Long fornecedorId) {
        List<PedidoCompraListDTO> lista = pedidoCompraService.listarPorFornecedor(fornecedorId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista pedidos de compra realizados dentro de um período.
     *
     * @param inicio Data inicial (formato ISO: yyyy-MM-dd).
     * @param fim    Data final (formato ISO: yyyy-MM-dd).
     * @return Lista de pedidos dentro do período especificado.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<PedidoCompraListDTO>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<PedidoCompraListDTO> lista = pedidoCompraService.listarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(lista);
    }

    /**
     * Atualiza o status de um pedido de compra.
     *
     * @param id     ID do pedido.
     * @param status Novo status do pedido.
     * @return Resposta sem conteúdo.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusPedidoCompra status) {
        pedidoCompraService.atualizarStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    /**
     * Remove um pedido de compra existente.
     *
     * @param id ID do pedido a ser removido.
     * @return Resposta sem conteúdo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoCompraService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
