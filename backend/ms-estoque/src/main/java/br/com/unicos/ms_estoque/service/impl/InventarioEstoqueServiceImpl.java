package br.com.unicos.ms_estoque.service.impl;

import br.com.unicos.ms_estoque.dto.InventarioEstoqueRequest;
import br.com.unicos.ms_estoque.dto.InventarioEstoqueResponse;
import br.com.unicos.ms_estoque.enums.StatusInventario;
import br.com.unicos.ms_estoque.model.EstoqueLocal;
import br.com.unicos.ms_estoque.model.InventarioEstoque;
import br.com.unicos.ms_estoque.repository.EstoqueLocalRepository;
import br.com.unicos.ms_estoque.repository.InventarioEstoqueRepository;
import br.com.unicos.ms_estoque.service.InventarioEstoqueService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementação da interface {@link InventarioEstoqueService}.
 * <p>
 * Responsável pelas regras de negócio relacionadas aos processos
 * de inventário físico de estoque.
 */
@Service
@RequiredArgsConstructor
public class InventarioEstoqueServiceImpl implements InventarioEstoqueService {

    private final InventarioEstoqueRepository inventarioEstoqueRepository;
    private final EstoqueLocalRepository estoqueLocalRepository;
    private final ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public InventarioEstoqueResponse salvar(InventarioEstoqueRequest request) {
        EstoqueLocal estoqueLocal = estoqueLocalRepository.findById(request.estoqueLocalId())
                .orElseThrow(() -> new EntityNotFoundException("Local de estoque não encontrado."));

        // Verifica se já existe inventário aberto para o mesmo local
        boolean existeAberto = inventarioEstoqueRepository.existsByEstoqueLocalIdAndStatus(
                estoqueLocal.getId(), StatusInventario.ABERTO
        );
        if (existeAberto) {
            throw new DataIntegrityViolationException("Já existe um inventário em aberto para este local de estoque.");
        }

        InventarioEstoque inventario = InventarioEstoque.builder()
                .estoqueLocal(estoqueLocal)
                .dataInicio(request.dataInicio() != null ? request.dataInicio() : LocalDateTime.now())
                .dataFim(request.dataFim())
                .status(request.status() != null ? request.status() : StatusInventario.ABERTO)
                .build();

        inventarioEstoqueRepository.save(inventario);
        return modelMapper.map(inventario, InventarioEstoqueResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public InventarioEstoqueResponse atualizar(Long id, InventarioEstoqueRequest request) {
        InventarioEstoque inventario = inventarioEstoqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventário não encontrado."));

        if (inventario.getStatus() == StatusInventario.FINALIZADO) {
            throw new DataIntegrityViolationException("Inventário finalizado não pode ser alterado.");
        }

        if (request.dataFim() != null) {
            inventario.setDataFim(request.dataFim());
        }

        if (request.status() != null) {
            inventario.setStatus(request.status());
        }

        inventarioEstoqueRepository.save(inventario);
        return modelMapper.map(inventario, InventarioEstoqueResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        InventarioEstoque inventario = inventarioEstoqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventário não encontrado."));

        if (inventario.getStatus() == StatusInventario.FINALIZADO) {
            throw new DataIntegrityViolationException("Inventário finalizado não pode ser excluído.");
        }

        inventarioEstoqueRepository.delete(inventario);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<InventarioEstoqueResponse> listarTodos() {
        return inventarioEstoqueRepository.findAll().stream()
                .map(i -> modelMapper.map(i, InventarioEstoqueResponse.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public InventarioEstoqueResponse buscarPorId(Long id) {
        InventarioEstoque inventario = inventarioEstoqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventário não encontrado."));
        return modelMapper.map(inventario, InventarioEstoqueResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<InventarioEstoqueResponse> listarPorStatus(StatusInventario status) {
        return inventarioEstoqueRepository.findByStatus(status).stream()
                .map(i -> modelMapper.map(i, InventarioEstoqueResponse.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<InventarioEstoqueResponse> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return inventarioEstoqueRepository.findByDataInicioBetween(inicio, fim).stream()
                .map(i -> modelMapper.map(i, InventarioEstoqueResponse.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existeInventarioAberto(Long estoqueLocalId) {
        return inventarioEstoqueRepository.existsByEstoqueLocalIdAndStatus(
                estoqueLocalId, StatusInventario.ABERTO
        );
    }
}
