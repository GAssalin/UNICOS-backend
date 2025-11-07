package br.com.unicos.ms_compras.service.requisicao.impl;

import br.com.unicos.ms_compras.dto.requisicao.RequisicaoItemListDTO;
import br.com.unicos.ms_compras.dto.requisicao.RequisicaoItemRequest;
import br.com.unicos.ms_compras.dto.requisicao.RequisicaoItemResponse;
import br.com.unicos.ms_compras.model.requisicao.RequisicaoCompra;
import br.com.unicos.ms_compras.model.requisicao.RequisicaoItem;
import br.com.unicos.ms_compras.repository.requisicao.RequisicaoCompraRepository;
import br.com.unicos.ms_compras.repository.requisicao.RequisicaoItemRepository;
import br.com.unicos.ms_compras.service.requisicao.RequisicaoItemService;
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
 * Implementação da interface {@link br.com.unicos.ms_compras.service.requisicao.RequisicaoItemService}.
 *
 * <p>
 * Gerencia os itens vinculados às requisições de compra,
 * controlando as quantidades solicitadas e atendidas.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class RequisicaoItemServiceImpl implements RequisicaoItemService {

    private final RequisicaoItemRepository requisicaoItemRepository;
    private final RequisicaoCompraRepository requisicaoCompraRepository;
    private final ModelMapper modelMapper;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    @Override
    @Transactional
    public RequisicaoItemResponse criar(RequisicaoItemRequest request) {
        RequisicaoCompra requisicao = requisicaoCompraRepository.findById(request.requisicaoCompraId())
                .orElseThrow(() -> new EntityNotFoundException("Requisição de compra não encontrada para o ID informado."));

        RequisicaoItem item = modelMapper.map(request, RequisicaoItem.class);
        item.setRequisicaoCompra(requisicao);

        if (item.getQuantidadeAtendida() == null) {
            item.setQuantidadeAtendida(BigDecimal.ZERO);
        }

        RequisicaoItem salvo = requisicaoItemRepository.save(item);
        return modelMapper.map(salvo, RequisicaoItemResponse.class);
    }

    @Override
    @Transactional
    public RequisicaoItemResponse atualizar(Long id, RequisicaoItemRequest request) {
        RequisicaoItem existente = requisicaoItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de requisição não encontrado para o ID: " + id));

        modelMapper.map(request, existente);

        if (existente.getQuantidadeAtendida() == null) {
            existente.setQuantidadeAtendida(BigDecimal.ZERO);
        }

        RequisicaoItem atualizado = requisicaoItemRepository.save(existente);
        return modelMapper.map(atualizado, RequisicaoItemResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RequisicaoItemResponse> buscarPorId(Long id) {
        return requisicaoItemRepository.findById(id)
                .map(i -> modelMapper.map(i, RequisicaoItemResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequisicaoItemListDTO> listarPorRequisicao(Long requisicaoCompraId) {
        return requisicaoItemRepository.findByRequisicaoCompraId(requisicaoCompraId).stream()
                .map(i -> modelMapper.map(i, RequisicaoItemListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequisicaoItemListDTO> listarPorProduto(Long produtoId) {
        return requisicaoItemRepository.findByProdutoId(produtoId).stream()
                .map(i -> modelMapper.map(i, RequisicaoItemListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!requisicaoItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Item de requisição não encontrado para exclusão. ID: " + id);
        }
        requisicaoItemRepository.deleteById(id);
    }
}
