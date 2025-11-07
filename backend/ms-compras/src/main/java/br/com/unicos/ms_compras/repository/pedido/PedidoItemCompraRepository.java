package br.com.unicos.ms_compras.repository.pedido;

import br.com.unicos.ms_compras.model.pedido.PedidoItemCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pelas operações de persistência
 * da entidade {@link PedidoItemCompra}.
 *
 * <p>
 * Fornece métodos padrão de CRUD e pode ser estendido
 * com consultas específicas conforme a necessidade do módulo de compras.
 * </p>
 */
@Repository
public interface PedidoItemCompraRepository extends JpaRepository<PedidoItemCompra, Long> {

    /**
     * Busca todos os itens vinculados a um pedido de compra específico.
     *
     * @param pedidoCompraId ID do pedido de compra.
     * @return Lista de itens pertencentes ao pedido informado.
     */
    List<PedidoItemCompra> findByPedidoCompraId(Long pedidoCompraId);
}
