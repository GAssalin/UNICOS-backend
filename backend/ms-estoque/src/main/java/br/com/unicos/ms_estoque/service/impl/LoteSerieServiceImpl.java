package br.com.unicos.ms_estoque.service.impl;

import br.com.unicos.ms_estoque.dto.LoteSerieRequest;
import br.com.unicos.ms_estoque.dto.LoteSerieResponse;
import br.com.unicos.ms_estoque.model.LoteSerie;
import br.com.unicos.ms_estoque.repository.LoteSerieRepository;
import br.com.unicos.ms_estoque.service.LoteSerieService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementação da interface {@link LoteSerieService}.
 * <p>
 * Responsável pelas regras de negócio relacionadas aos lotes e séries de produtos,
 * incluindo controle de validade e rastreabilidade.
 */
@Service
@RequiredArgsConstructor
public class LoteSerieServiceImpl implements LoteSerieService {

    private final LoteSerieRepository loteSerieRepository;
    private final ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public LoteSerieResponse salvar(LoteSerieRequest request) {
        boolean existe = loteSerieRepository.findByCodigo(request.codigo()).isPresent();
        if (existe) {
            throw new DataIntegrityViolationException("Já existe um lote com o código informado.");
        }

        LoteSerie lote = LoteSerie.builder()
                .codigo(request.codigo())
                .dataValidade(request.dataValidade())
                .observacao(request.observacao())
                .build();

        loteSerieRepository.save(lote);
        return modelMapper.map(lote, LoteSerieResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public LoteSerieResponse atualizar(Long id, LoteSerieRequest request) {
        LoteSerie lote = loteSerieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lote não encontrado."));

        if (request.codigo() != null && !request.codigo().equalsIgnoreCase(lote.getCodigo())) {
            boolean duplicado = loteSerieRepository.findByCodigo(request.codigo()).isPresent();
            if (duplicado) {
                throw new DataIntegrityViolationException("Já existe outro lote com este código.");
            }
            lote.setCodigo(request.codigo());
        }

        if (request.dataValidade() != null) {
            lote.setDataValidade(request.dataValidade());
        }

        if (request.observacao() != null) {
            lote.setObservacao(request.observacao());
        }

        loteSerieRepository.save(lote);
        return modelMapper.map(lote, LoteSerieResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        if (!loteSerieRepository.existsById(id)) {
            throw new EntityNotFoundException("Lote não encontrado.");
        }

        try {
            loteSerieRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Não é possível excluir: o lote está vinculado a movimentações.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoteSerieResponse> listarTodos() {
        return loteSerieRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, LoteSerieResponse.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public LoteSerieResponse buscarPorId(Long id) {
        LoteSerie lote = loteSerieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lote não encontrado."));
        return modelMapper.map(lote, LoteSerieResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public LoteSerieResponse buscarPorCodigo(String codigo) {
        LoteSerie lote = loteSerieRepository.findByCodigo(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Lote não encontrado com o código informado."));
        return modelMapper.map(lote, LoteSerieResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoteSerieResponse> listarLotesProximosDoVencimento(int dias) {
        return loteSerieRepository.findLotesProximosDoVencimento(dias).stream()
                .filter(l -> l.getDataValidade() != null && !l.getDataValidade().isBefore(LocalDate.now()))
                .map(entity -> modelMapper.map(entity, LoteSerieResponse.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoteSerieResponse> listarLotesVencidos() {
        return loteSerieRepository.findLotesVencidos().stream()
                .map(entity -> modelMapper.map(entity, LoteSerieResponse.class))
                .toList();
    }
}
