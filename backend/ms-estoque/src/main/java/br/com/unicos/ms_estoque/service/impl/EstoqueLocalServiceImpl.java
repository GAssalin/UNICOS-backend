package br.com.unicos.ms_estoque.service.impl;

import br.com.unicos.ms_estoque.dto.EstoqueLocalListDTO;
import br.com.unicos.ms_estoque.dto.EstoqueLocalRequest;
import br.com.unicos.ms_estoque.dto.EstoqueLocalResponse;
import br.com.unicos.ms_estoque.enums.TipoLocalEstoque;
import br.com.unicos.ms_estoque.model.EstoqueLocal;
import br.com.unicos.ms_estoque.repository.EstoqueLocalRepository;
import br.com.unicos.ms_estoque.service.EstoqueLocalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementação da interface {@link EstoqueLocalService}.
 * <p>
 * Responsável pelas regras de negócio relacionadas aos locais de armazenamento de produtos.
 * Pode representar estoques de depósitos, lojas, produção ou terceiros.
 */
@Service
@RequiredArgsConstructor
public class EstoqueLocalServiceImpl implements EstoqueLocalService {

    private final EstoqueLocalRepository estoqueLocalRepository;
    private final ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public EstoqueLocalResponse salvar(EstoqueLocalRequest request) {
        if (estoqueLocalRepository.existsByNomeIgnoreCase(request.nome())) {
            throw new DataIntegrityViolationException("Já existe um local de estoque com este nome.");
        }

        EstoqueLocal entity = EstoqueLocal.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .tipo(request.tipo())
                .empresaId(request.empresaId())
                .build();

        estoqueLocalRepository.save(entity);
        return modelMapper.map(entity, EstoqueLocalResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public EstoqueLocalResponse atualizar(Long id, EstoqueLocalRequest request) {
        EstoqueLocal entity = estoqueLocalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Local de estoque não encontrado."));

        if (request.nome() != null && !request.nome().equalsIgnoreCase(entity.getNome())) {
            if (estoqueLocalRepository.existsByNomeIgnoreCase(request.nome())) {
                throw new DataIntegrityViolationException("Já existe outro local de estoque com este nome.");
            }
            entity.setNome(request.nome());
        }

        if (request.descricao() != null) {
            entity.setDescricao(request.descricao());
        }

        if (request.tipo() != null) {
            entity.setTipo(request.tipo());
        }

        estoqueLocalRepository.save(entity);
        return modelMapper.map(entity, EstoqueLocalResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        if (!estoqueLocalRepository.existsById(id)) {
            throw new EntityNotFoundException("Local de estoque não encontrado.");
        }

        try {
            estoqueLocalRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Não é possível excluir: o local está vinculado a produtos ou inventários.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<EstoqueLocalListDTO> listarTodos() {
        return estoqueLocalRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, EstoqueLocalListDTO.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public EstoqueLocalResponse buscarPorId(Long id) {
        EstoqueLocal entity = estoqueLocalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Local de estoque não encontrado."));
        return modelMapper.map(entity, EstoqueLocalResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<EstoqueLocalListDTO> buscarPorTipo(TipoLocalEstoque tipo) {
        return estoqueLocalRepository.findByTipo(tipo).stream()
                .map(entity -> modelMapper.map(entity, EstoqueLocalListDTO.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<EstoqueLocalListDTO> buscarPorEmpresa(Long empresaId) {
        return estoqueLocalRepository.findByEmpresaId(empresaId).stream()
                .map(entity -> modelMapper.map(entity, EstoqueLocalListDTO.class))
                .toList();
    }
}
