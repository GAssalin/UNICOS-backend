package br.com.unicos.ms_compras.service.recebimento.impl;

import br.com.unicos.ms_compras.dto.recebimento.RecebimentoItemListDTO;
import br.com.unicos.ms_compras.dto.recebimento.RecebimentoItemRequest;
import br.com.unicos.ms_compras.dto.recebimento.RecebimentoItemResponse;
import br.com.unicos.ms_compras.model.recebimento.RecebimentoCompra;
import br.com.unicos.ms_compras.model.recebimento.RecebimentoItem;
import br.com.unicos.ms_compras.repository.recebimento.RecebimentoCompraRepository;
import br.com.unicos.ms_compras.repository.recebimento.RecebimentoItemRepository;
import br.com.unicos.ms_compras.service.recebimento.RecebimentoItemService;
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
 * Implementação da interface {@link br.com.unicos.ms_compras.service.recebimento.RecebimentoItemService}.
 *
 * <p>
 * Gerencia os itens conferidos durante o processo de recebimento de compras,
 * controlando as quantidades previstas, recebidas e devolvidas.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class RecebimentoItemServiceImpl implements RecebimentoItemService {

    private final RecebimentoItemRepository recebimentoItemRepository;
    private final RecebimentoCompraRepository recebimentoCompraRepository;
    private final ModelMapper modelMapper;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    @Override
    @Transactional
    public RecebimentoItemResponse criar(RecebimentoItemRequest request) {
        RecebimentoCompra recebimento = recebimentoCompraRepository.findById(request.recebimentoCompraId())
                .orElseThrow(() -> new EntityNotFoundException("Recebimento de compra não encontrado para o ID informado."));

        RecebimentoItem item = modelMapper.map(request, RecebimentoItem.class);
        item.setRecebimentoCompra(recebimento);

        // validações e ajustes de segurança
        if (item.getQuantidadeDevolvida() == null)
            item.setQuantidadeDevolvida(BigDecimal.ZERO);

        RecebimentoItem salvo = recebimentoItemRepository.save(item);
        return modelMapper.map(salvo, RecebimentoItemResponse.class);
    }

    @Override
    @Transactional
    public RecebimentoItemResponse atualizar(Long id, RecebimentoItemRequest request) {
        RecebimentoItem existente = recebimentoItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de recebimento não encontrado para o ID: " + id));

        modelMapper.map(request, existente);

        if (existente.getQuantidadeDevolvida() == null)
            existente.setQuantidadeDevolvida(BigDecimal.ZERO);

        RecebimentoItem atualizado = recebimentoItemRepository.save(existente);
        return modelMapper.map(atualizado, RecebimentoItemResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecebimentoItemResponse> buscarPorId(Long id) {
        return recebimentoItemRepository.findById(id)
                .map(i -> modelMapper.map(i, RecebimentoItemResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecebimentoItemListDTO> listarPorRecebimento(Long recebimentoCompraId) {
        return recebimentoItemRepository.findByRecebimentoCompraId(recebimentoCompraId).stream()
                .map(i -> modelMapper.map(i, RecebimentoItemListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecebimentoItemListDTO> listarPorProduto(Long produtoId) {
        return recebimentoItemRepository.findByProdutoId(produtoId).stream()
                .map(i -> modelMapper.map(i, RecebimentoItemListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!recebimentoItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Item de recebimento não encontrado para exclusão. ID: " + id);
        }
        recebimentoItemRepository.deleteById(id);
    }
}
