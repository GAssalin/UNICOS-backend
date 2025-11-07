package br.com.unicos.ms_compras.service.cotacao.impl;

import br.com.unicos.ms_compras.dto.cotacao.CotacaoItemListDTO;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoItemRequest;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoItemResponse;
import br.com.unicos.ms_compras.model.cotacao.CotacaoFornecedor;
import br.com.unicos.ms_compras.model.cotacao.CotacaoItem;
import br.com.unicos.ms_compras.repository.cotacao.CotacaoFornecedorRepository;
import br.com.unicos.ms_compras.repository.cotacao.CotacaoItemRepository;
import br.com.unicos.ms_compras.service.cotacao.CotacaoItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link CotacaoItemService}.
 *
 * <p>
 * Responsável pelo gerenciamento dos itens cotados dentro das propostas
 * enviadas por fornecedores, incluindo cálculos de valores totais e
 * integração com o fornecedor vinculado.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CotacaoItemServiceImpl implements CotacaoItemService {

    private final CotacaoItemRepository cotacaoItemRepository;
    private final CotacaoFornecedorRepository cotacaoFornecedorRepository;
    private final ModelMapper modelMapper;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    @Override
    @Transactional
    public CotacaoItemResponse criar(CotacaoItemRequest request) {
        CotacaoFornecedor fornecedor = cotacaoFornecedorRepository.findById(request.cotacaoFornecedorId())
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado para o ID informado."));

        CotacaoItem item = modelMapper.map(request, CotacaoItem.class);
        item.setCotacaoFornecedor(fornecedor);

        // cálculo de valor total = quantidade × valor unitário
        if (item.getQuantidade() != null && item.getValorUnitario() != null) {
            item.setValorTotal(item.getQuantidade().multiply(item.getValorUnitario()));
        }

        CotacaoItem salvo = cotacaoItemRepository.save(item);
        return modelMapper.map(salvo, CotacaoItemResponse.class);
    }

    @Override
    @Transactional
    public CotacaoItemResponse atualizar(Long id, CotacaoItemRequest request) {
        CotacaoItem existente = cotacaoItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de cotação não encontrado para o ID: " + id));

        modelMapper.map(request, existente);

        if (existente.getQuantidade() != null && existente.getValorUnitario() != null) {
            existente.setValorTotal(existente.getQuantidade().multiply(existente.getValorUnitario()));
        }

        CotacaoItem atualizado = cotacaoItemRepository.save(existente);
        return modelMapper.map(atualizado, CotacaoItemResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CotacaoItemResponse> buscarPorId(Long id) {
        return cotacaoItemRepository.findById(id)
                .map(i -> modelMapper.map(i, CotacaoItemResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CotacaoItemListDTO> listarPorFornecedor(Long cotacaoFornecedorId) {
        List<CotacaoItem> itens = cotacaoItemRepository.findByCotacaoFornecedorId(cotacaoFornecedorId);
        return itens.stream()
                .map(i -> modelMapper.map(i, CotacaoItemListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!cotacaoItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Item de cotação não encontrado para exclusão. ID: " + id);
        }
        cotacaoItemRepository.deleteById(id);
    }
}
