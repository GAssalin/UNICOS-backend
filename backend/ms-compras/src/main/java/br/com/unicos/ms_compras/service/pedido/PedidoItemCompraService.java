package br.com.unicos.ms_compras.service.pedido;

import br.com.unicos.ms_compras.dto.pedido.PedidoItemCompraListDTO;
import br.com.unicos.ms_compras.dto.pedido.PedidoItemCompraRequest;
import br.com.unicos.ms_compras.dto.pedido.PedidoItemCompraResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio da entidade
 * {@link br.com.unicos.ms_compras.model.pedido.PedidoItemCompra}.
 *
 * <p>
 * Define as operações de gerenciamento dos itens vinculados aos pedidos de compra,
 * incluindo criação, atualização, exclusão e listagem por pedido.
 * </p>
 */
public interface PedidoItemCompraService {

    // -----------------------------------------------------------------------
    // 🔹 CRUD
    // -----------------------------------------------------------------------

    /**
     * Cria um novo item de pedido de compra.
     *
     * @param request DTO contendo as informações do item.
     * @param pedidoCompraId identificador do pedido de compra ao qual o item pertence.
     * @return DTO representando o item criado.
     */
    PedidoItemCompraResponse criar(PedidoItemCompraRequest request, Long pedidoCompraId);

    /**
     * Atualiza um item de pedido de compra existente.
     *
     * @param id identificador do item.
     * @param request DTO contendo os novos dados do item.
     * @return DTO representando o item atualizado.
     */
    PedidoItemCompraResponse atualizar(Long id, PedidoItemCompraRequest request);

    /**
     * Exclui um item de pedido de compra pelo seu identificador.
     *
     * @param id identificador do item a ser removido.
     */
    void deletar(Long id);

    // -----------------------------------------------------------------------
    // 🔹 Consultas
    // -----------------------------------------------------------------------

    /**
     * Busca um item de pedido de compra pelo seu identificador.
     *
     * @param id identificador do item.
     * @return DTO do item encontrado, se existir.
     */
    Optional<PedidoItemCompraResponse> buscarPorId(Long id);

    /**
     * Lista todos os itens vinculados a um pedido de compra específico.
     *
     * @param pedidoCompraId identificador do pedido de compra.
     * @return lista de itens pertencentes ao pedido informado.
     */
    List<PedidoItemCompraListDTO> listarPorPedido(Long pedidoCompraId);
}
