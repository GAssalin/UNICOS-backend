package br.com.unicos.ms_compras.service.fiscal.impl;

import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalItemListDTO;
import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalItemRequest;
import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalItemResponse;
import br.com.unicos.ms_compras.model.fiscal.NotaFiscalCompra;
import br.com.unicos.ms_compras.model.fiscal.NotaFiscalItem;
import br.com.unicos.ms_compras.repository.fiscal.NotaFiscalCompraRepository;
import br.com.unicos.ms_compras.repository.fiscal.NotaFiscalItemRepository;
import br.com.unicos.ms_compras.service.fiscal.NotaFiscalItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link br.com.unicos.ms_compras.service.fiscal.NotaFiscalItemService}.
 *
 * <p>
 * Gerencia os itens que compõem uma nota fiscal de compra,
 * controlando valores unitários, impostos e vínculo com a nota principal.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class NotaFiscalItemServiceImpl implements NotaFiscalItemService {

    private final NotaFiscalItemRepository notaFiscalItemRepository;
    private final NotaFiscalCompraRepository notaFiscalCompraRepository;
    private final ModelMapper modelMapper;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    @Override
    @Transactional
    public NotaFiscalItemResponse criar(NotaFiscalItemRequest request) {
        NotaFiscalCompra notaFiscal = notaFiscalCompraRepository.findById(request.notaFiscalCompraId())
                .orElseThrow(() -> new EntityNotFoundException("Nota fiscal não encontrada para o ID informado."));

        NotaFiscalItem item = modelMapper.map(request, NotaFiscalItem.class);
        item.setNotaFiscalCompra(notaFiscal);

        // cálculo simples: valor total = quantidade × valor unitário - desconto
        if (item.getQuantidade() != null && item.getValorUnitario() != null) {
            item.setValorTotal(item.getQuantidade().multiply(item.getValorUnitario()));
            if (item.getValorDesconto() != null)
                item.setValorTotal(item.getValorTotal().subtract(item.getValorDesconto()));
        }

        NotaFiscalItem salvo = notaFiscalItemRepository.save(item);
        return modelMapper.map(salvo, NotaFiscalItemResponse.class);
    }

    @Override
    @Transactional
    public NotaFiscalItemResponse atualizar(Long id, NotaFiscalItemRequest request) {
        NotaFiscalItem existente = notaFiscalItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item da nota fiscal não encontrado para o ID: " + id));

        modelMapper.map(request, existente);

        // recalcular total
        if (existente.getQuantidade() != null && existente.getValorUnitario() != null) {
            existente.setValorTotal(existente.getQuantidade().multiply(existente.getValorUnitario()));
            if (existente.getValorDesconto() != null)
                existente.setValorTotal(existente.getValorTotal().subtract(existente.getValorDesconto()));
        }

        NotaFiscalItem atualizado = notaFiscalItemRepository.save(existente);
        return modelMapper.map(atualizado, NotaFiscalItemResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotaFiscalItemResponse> buscarPorId(Long id) {
        return notaFiscalItemRepository.findById(id)
                .map(i -> modelMapper.map(i, NotaFiscalItemResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotaFiscalItemListDTO> listarPorNotaFiscal(Long notaFiscalCompraId) {
        return notaFiscalItemRepository.findByNotaFiscalCompraId(notaFiscalCompraId).stream()
                .map(i -> modelMapper.map(i, NotaFiscalItemListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotaFiscalItemListDTO> listarPorProduto(Long produtoId) {
        return notaFiscalItemRepository.findByProdutoId(produtoId).stream()
                .map(i -> modelMapper.map(i, NotaFiscalItemListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!notaFiscalItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Item da nota fiscal não encontrado para exclusão. ID: " + id);
        }
        notaFiscalItemRepository.deleteById(id);
    }
}
