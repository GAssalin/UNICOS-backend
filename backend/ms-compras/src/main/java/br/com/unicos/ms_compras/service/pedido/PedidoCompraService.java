package br.com.unicos.ms_compras.service.pedido;

import br.com.unicos.ms_compras.dto.pedido.PedidoCompraListDTO;
import br.com.unicos.ms_compras.dto.pedido.PedidoCompraRequest;
import br.com.unicos.ms_compras.dto.pedido.PedidoCompraResponse;
import br.com.unicos.ms_compras.enums.StatusPedidoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio da entidade
 * {@link br.com.unicos.ms_compras.model.pedido.PedidoCompra}.
 *
 * <p>
 * Controla o ciclo de vida dos pedidos de compra, desde a geração a partir
 * de uma cotação aprovada até o recebimento e faturamento.
 * </p>
 */
public interface PedidoCompraService {

    /**
     * Cria um novo pedido de compra.
     *
     * @param request dados do pedido
     * @return pedido criado
     */
    PedidoCompraResponse criar(PedidoCompraRequest request);

    /**
     * Atualiza um pedido de compra existente.
     *
     * @param id      identificador do pedido
     * @param request dados atualizados
     * @return pedido atualizado
     */
    PedidoCompraResponse atualizar(Long id, PedidoCompraRequest request);

    /**
     * Busca um pedido de compra pelo seu identificador.
     *
     * @param id identificador do pedido
     * @return pedido correspondente, se existir
     */
    Optional<PedidoCompraResponse> buscarPorId(Long id);

    /**
     * Lista todos os pedidos de compra, com suporte a paginação.
     *
     * @param pageable parâmetros de paginação
     * @return página de pedidos
     */
    Page<PedidoCompraListDTO> listar(Pageable pageable);

    /**
     * Lista todos os pedidos de compra de um fornecedor específico.
     *
     * @param fornecedorId identificador do fornecedor
     * @return lista de pedidos do fornecedor
     */
    List<PedidoCompraListDTO> listarPorFornecedor(Long fornecedorId);

    /**
     * Lista pedidos dentro de um intervalo de datas de criação.
     *
     * @param inicio data inicial
     * @param fim    data final
     * @return lista de pedidos dentro do período informado
     */
    List<PedidoCompraListDTO> listarPorPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Atualiza o status de um pedido de compra.
     *
     * @param id     identificador do pedido
     * @param status novo status
     */
    void atualizarStatus(Long id, StatusPedidoCompra status);

    /**
     * Exclui um pedido de compra.
     *
     * @param id identificador do pedido
     */
    void deletar(Long id);
}
