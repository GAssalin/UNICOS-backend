package br.com.unicos.ms_compras.service.pedido;

import br.com.unicos.ms_compras.dto.pedido.PedidoCompraListDTO;
import br.com.unicos.ms_compras.dto.pedido.PedidoCompraRequest;
import br.com.unicos.ms_compras.dto.pedido.PedidoCompraResponse;
import br.com.unicos.ms_compras.enums.StatusPedidoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio da entidade
 * {@link br.com.unicos.ms_compras.model.pedido.PedidoCompra}.
 *
 * <p>
 * Controla o ciclo de vida dos pedidos de compra, desde sua criação,
 * atualização e cancelamento até o recebimento e faturamento.
 * </p>
 *
 * <p>
 * Este serviço atua como intermediário entre os controladores REST e
 * a camada de persistência, aplicando as regras específicas do domínio
 * de Compras.
 * </p>
 */
public interface PedidoCompraService {

    // -----------------------------------------------------------------------
    // CRUD principal
    // -----------------------------------------------------------------------

    /**
     * Cria um novo pedido de compra, incluindo seus itens associados.
     *
     * @param request DTO contendo os dados do pedido e seus itens.
     * @return DTO representando o pedido criado.
     */
    PedidoCompraResponse criar(PedidoCompraRequest request);

    /**
     * Atualiza um pedido de compra existente.
     *
     * @param id      identificador do pedido a ser atualizado.
     * @param request DTO contendo os novos dados do pedido.
     * @return DTO representando o pedido atualizado.
     */
    PedidoCompraResponse atualizar(Long id, PedidoCompraRequest request);

    /**
     * Remove permanentemente um pedido de compra.
     *
     * @param id identificador do pedido.
     */
    void deletar(Long id);

    // -----------------------------------------------------------------------
    // Consultas
    // -----------------------------------------------------------------------

    /**
     * Busca um pedido de compra pelo seu identificador.
     *
     * @param id identificador do pedido.
     * @return DTO do pedido correspondente, se encontrado.
     */
    Optional<PedidoCompraResponse> buscarPorId(Long id);

    /**
     * Lista todos os pedidos de compra com suporte a paginação.
     *
     * @param pageable parâmetros de paginação e ordenação.
     * @return página contendo pedidos resumidos.
     */
    Page<PedidoCompraListDTO> listar(Pageable pageable);

    /**
     * Lista todos os pedidos de compra de um fornecedor específico.
     *
     * @param fornecedorId identificador do fornecedor.
     * @return lista de pedidos associados ao fornecedor informado.
     */
    List<PedidoCompraListDTO> listarPorFornecedor(Long fornecedorId);

    /**
     * Lista pedidos criados dentro de um intervalo de tempo específico.
     *
     * @param inicio data/hora inicial do período.
     * @param fim    data/hora final do período.
     * @return lista de pedidos dentro do período informado.
     */
    List<PedidoCompraListDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);

    // -----------------------------------------------------------------------
    // Regras de domínio
    // -----------------------------------------------------------------------

    /**
     * Atualiza o status de um pedido de compra.
     *
     * @param id     identificador do pedido.
     * @param status novo status a ser aplicado.
     */
    void atualizarStatus(Long id, StatusPedidoCompra status);

    /**
     * Calcula e atualiza o valor total do pedido com base em seus itens.
     *
     * <p>
     * Este método pode ser utilizado internamente após operações de adição
     * ou remoção de itens do pedido.
     * </p>
     *
     * @param id identificador do pedido.
     * @return valor total recalculado.
     */
    void recalcularValorTotal(Long id);
}
