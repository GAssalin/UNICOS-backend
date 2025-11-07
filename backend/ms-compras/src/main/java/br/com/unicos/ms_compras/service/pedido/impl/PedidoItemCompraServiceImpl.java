package br.com.unicos.ms_compras.service.pedido.impl;

import br.com.unicos.ms_compras.dto.pedido.PedidoItemCompraListDTO;
import br.com.unicos.ms_compras.dto.pedido.PedidoItemCompraRequest;
import br.com.unicos.ms_compras.dto.pedido.PedidoItemCompraResponse;
import br.com.unicos.ms_compras.model.pedido.PedidoCompra;
import br.com.unicos.ms_compras.model.pedido.PedidoItemCompra;
import br.com.unicos.ms_compras.repository.pedido.PedidoCompraRepository;
import br.com.unicos.ms_compras.repository.pedido.PedidoItemCompraRepository;
import br.com.unicos.ms_compras.service.pedido.PedidoItemCompraService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link PedidoItemCompraService}.
 *
 * <p>
 * Gerencia os itens vinculados aos pedidos de compra, controlando
 * criação, atualização, exclusão e listagem. Também calcula o valor
 * total de cada item e garante a consistência com o pedido associado.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class PedidoItemCompraServiceImpl implements PedidoItemCompraService {

    private final PedidoItemCompraRepository pedidoItemCompraRepository;
    private final PedidoCompraRepository pedidoCompraRepository;
    private final ModelMapper modelMapper;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    @Override
    @Transactional
    public PedidoItemCompraResponse criar(PedidoItemCompraRequest request, Long pedidoCompraId) {
        PedidoCompra pedido = pedidoCompraRepository.findById(pedidoCompraId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de compra não encontrado para o ID: " + pedidoCompraId));

        PedidoItemCompra item = modelMapper.map(request, PedidoItemCompra.class);
        item.setPedidoCompra(pedido);

        // Calcula valor total do item
        item.setValorTotal(calcularValorItem(item));

        PedidoItemCompra salvo = pedidoItemCompraRepository.save(item);
        return modelMapper.map(salvo, PedidoItemCompraResponse.class);
    }

    @Override
    @Transactional
    public PedidoItemCompraResponse atualizar(Long id, PedidoItemCompraRequest request) {
        PedidoItemCompra existente = pedidoItemCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de pedido de compra não encontrado para o ID: " + id));

        existente.setProdutoId(request.produtoId());
        existente.setQuantidade(request.quantidade());
        existente.setPrecoUnitario(request.precoUnitario());
        existente.setDesconto(request.desconto());
        existente.setValorTotal(calcularValorItem(existente));

        PedidoItemCompra atualizado = pedidoItemCompraRepository.save(existente);
        return modelMapper.map(atualizado, PedidoItemCompraResponse.class);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!pedidoItemCompraRepository.existsById(id)) {
            throw new EntityNotFoundException("Item de pedido de compra não encontrado para exclusão. ID: " + id);
        }
        pedidoItemCompraRepository.deleteById(id);
    }

    // ==========================================================
    // 🔹 CONSULTAS
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<PedidoItemCompraResponse> buscarPorId(Long id) {
        return pedidoItemCompraRepository.findById(id)
                .map(p -> modelMapper.map(p, PedidoItemCompraResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoItemCompraListDTO> listarPorPedido(Long pedidoCompraId) {
        return pedidoItemCompraRepository.findByPedidoCompraId(pedidoCompraId).stream()
                .map(p -> modelMapper.map(p, PedidoItemCompraListDTO.class))
                .collect(Collectors.toList());
    }

    // ==========================================================
    // 🔹 MÉTODOS AUXILIARES
    // ==========================================================

    /**
     * Calcula o valor total de um item com base na quantidade,
     * preço unitário e desconto aplicado.
     *
     * @param item item de pedido
     * @return valor total calculado
     */
    private BigDecimal calcularValorItem(PedidoItemCompra item) {
        BigDecimal subtotal = item.getPrecoUnitario().multiply(item.getQuantidade());
        BigDecimal desconto = item.getDesconto() != null ? item.getDesconto() : BigDecimal.ZERO;
        return subtotal.subtract(desconto);
    }
}
