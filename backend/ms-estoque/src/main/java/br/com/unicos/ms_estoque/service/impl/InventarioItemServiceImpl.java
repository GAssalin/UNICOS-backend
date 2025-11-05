package br.com.unicos.ms_estoque.service.impl;

import br.com.unicos.ms_estoque.dto.InventarioItemRequest;
import br.com.unicos.ms_estoque.dto.InventarioItemResponse;
import br.com.unicos.ms_estoque.model.InventarioEstoque;
import br.com.unicos.ms_estoque.model.InventarioItem;
import br.com.unicos.ms_estoque.model.ProdutoEstoque;
import br.com.unicos.ms_estoque.repository.InventarioEstoqueRepository;
import br.com.unicos.ms_estoque.repository.InventarioItemRepository;
import br.com.unicos.ms_estoque.repository.ProdutoEstoqueRepository;
import br.com.unicos.ms_estoque.service.InventarioItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementação da interface {@link InventarioItemService}.
 * <p>
 * Responsável pelas regras de negócio referentes aos itens
 * de um inventário físico de estoque.
 */
@Service
@RequiredArgsConstructor
public class InventarioItemServiceImpl implements InventarioItemService {

    private final InventarioItemRepository inventarioItemRepository;
    private final InventarioEstoqueRepository inventarioEstoqueRepository;
    private final ProdutoEstoqueRepository produtoEstoqueRepository;
    private final ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public InventarioItemResponse salvar(InventarioItemRequest request) {
        InventarioEstoque inventario = inventarioEstoqueRepository.findById(request.inventarioId())
                .orElseThrow(() -> new EntityNotFoundException("Inventário não encontrado."));

        ProdutoEstoque produto = produtoEstoqueRepository.findById(request.produtoEstoqueId())
                .orElseThrow(() -> new EntityNotFoundException("Produto em estoque não encontrado."));

        if (inventario.getDataFim() != null) {
            throw new DataIntegrityViolationException("Não é possível adicionar itens a um inventário finalizado.");
        }

        InventarioItem item = InventarioItem.builder()
                .inventario(inventario)
                .produtoEstoque(produto)
                .quantidadeContada(request.quantidadeContada())
                .quantidadeRegistrada(request.quantidadeRegistrada())
                .observacao(request.observacao())
                .build();

        inventarioItemRepository.save(item);
        return modelMapper.map(item, InventarioItemResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public InventarioItemResponse atualizar(Long id, InventarioItemRequest request) {
        InventarioItem item = inventarioItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de inventário não encontrado."));

        InventarioEstoque inventario = item.getInventario();
        if (inventario.getDataFim() != null) {
            throw new DataIntegrityViolationException("Itens de inventário finalizado não podem ser alterados.");
        }

        item.setQuantidadeContada(request.quantidadeContada());
        item.setQuantidadeRegistrada(request.quantidadeRegistrada());
        item.setObservacao(request.observacao());

        inventarioItemRepository.save(item);
        return modelMapper.map(item, InventarioItemResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        InventarioItem item = inventarioItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de inventário não encontrado."));

        InventarioEstoque inventario = item.getInventario();
        if (inventario.getDataFim() != null) {
            throw new DataIntegrityViolationException("Itens de inventário finalizado não podem ser excluídos.");
        }

        inventarioItemRepository.delete(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<InventarioItemResponse> listarPorInventario(Long inventarioId) {
        return inventarioItemRepository.findByInventarioId(inventarioId).stream()
                .map(entity -> modelMapper.map(entity, InventarioItemResponse.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<InventarioItemResponse> listarItensDivergentes(Long inventarioId) {
        return inventarioItemRepository.findItensDivergentes(inventarioId).stream()
                .map(entity -> modelMapper.map(entity, InventarioItemResponse.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Double calcularDiferencaTotal(Long inventarioId) {
        Double resultado = inventarioItemRepository.calcularDiferencaTotal(inventarioId);
        return resultado != null ? resultado : 0.0;
    }
}
