package br.com.unicos.ms_compras.service.pedido.impl;

import br.com.unicos.ms_compras.dto.pedido.PedidoCompraListDTO;
import br.com.unicos.ms_compras.dto.pedido.PedidoCompraRequest;
import br.com.unicos.ms_compras.dto.pedido.PedidoCompraResponse;
import br.com.unicos.ms_compras.enums.StatusPedidoCompra;
import br.com.unicos.ms_compras.model.pedido.PedidoCompra;
import br.com.unicos.ms_compras.model.pedido.PedidoItemCompra;
import br.com.unicos.ms_compras.repository.pedido.PedidoCompraRepository;
import br.com.unicos.ms_compras.service.pedido.PedidoCompraService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link PedidoCompraService}.
 *
 * <p>
 * Gerencia os pedidos de compra do sistema, controlando fornecedores,
 * status, períodos e integrações com notas fiscais e recebimentos.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class PedidoCompraServiceImpl implements PedidoCompraService {

    private final PedidoCompraRepository pedidoCompraRepository;
    private final ModelMapper modelMapper;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    @Override
    @Transactional
    public PedidoCompraResponse criar(PedidoCompraRequest request) {
        PedidoCompra pedido = modelMapper.map(request, PedidoCompra.class);

        // Define status inicial, se não informado
        if (pedido.getStatus() == null) {
            pedido.setStatus(StatusPedidoCompra.AGUARDANDO_COTACAO);
        }

        // Data de criação conforme modelo base
        pedido.setDataCriacao(LocalDateTime.now());

        PedidoCompra salvo = pedidoCompraRepository.save(pedido);
        return modelMapper.map(salvo, PedidoCompraResponse.class);
    }

    @Override
    @Transactional
    public PedidoCompraResponse atualizar(Long id, PedidoCompraRequest request) {
        PedidoCompra existente = pedidoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de compra não encontrado para o ID: " + id));

        // Atualiza apenas campos editáveis
        existente.setFornecedorId(request.fornecedorId());
        existente.setStatus(request.status());
        existente.setObservacao(request.observacao());
        existente.setDataAtualizacao(LocalDateTime.now());

        PedidoCompra atualizado = pedidoCompraRepository.save(existente);
        return modelMapper.map(atualizado, PedidoCompraResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PedidoCompraResponse> buscarPorId(Long id) {
        return pedidoCompraRepository.findById(id)
                .map(p -> modelMapper.map(p, PedidoCompraResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PedidoCompraListDTO> listar(Pageable pageable) {
        return pedidoCompraRepository.findAll(pageable)
                .map(p -> modelMapper.map(p, PedidoCompraListDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoCompraListDTO> listarPorFornecedor(Long fornecedorId) {
        return pedidoCompraRepository.findByFornecedorId(fornecedorId).stream()
                .map(p -> modelMapper.map(p, PedidoCompraListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoCompraListDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoCompraRepository.findByDataCriacaoBetween(inicio, fim).stream()
                .map(p -> modelMapper.map(p, PedidoCompraListDTO.class))
                .collect(Collectors.toList());
    }

    // ==========================================================
    // 🔹 REGRAS DE DOMÍNIO
    // ==========================================================

    @Override
    @Transactional
    public void atualizarStatus(Long id, StatusPedidoCompra status) {
        PedidoCompra pedido = pedidoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de compra não encontrado para o ID: " + id));

        pedido.setStatus(status);
        pedido.setDataAtualizacao(LocalDateTime.now());
        pedidoCompraRepository.save(pedido);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!pedidoCompraRepository.existsById(id)) {
            throw new EntityNotFoundException("Pedido de compra não encontrado para exclusão. ID: " + id);
        }
        pedidoCompraRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void recalcularValorTotal(Long id) {
        PedidoCompra pedido = pedidoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de compra não encontrado para recalcular valor. ID: " + id));

        BigDecimal total = pedido.getItens().stream()
                .map(this::calcularValorItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setValorTotal(total);
        pedido.setDataAtualizacao(LocalDateTime.now());
        pedidoCompraRepository.save(pedido);
    }

    // ==========================================================
    // 🔹 Métodos auxiliares
    // ==========================================================

    /**
     * Calcula o valor total de um item (quantidade × preço - desconto).
     *
     * @param item item de compra
     * @return valor total calculado
     */
    private BigDecimal calcularValorItem(PedidoItemCompra item) {
        BigDecimal subtotal = item.getPrecoUnitario().multiply(item.getQuantidade());
        BigDecimal desconto = item.getDesconto() != null ? item.getDesconto() : BigDecimal.ZERO;
        return subtotal.subtract(desconto);
    }
}
