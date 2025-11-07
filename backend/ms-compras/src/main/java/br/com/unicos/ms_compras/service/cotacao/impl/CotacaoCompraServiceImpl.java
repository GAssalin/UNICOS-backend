package br.com.unicos.ms_compras.service.cotacao.impl;

import br.com.unicos.ms_compras.dto.cotacao.CotacaoCompraListDTO;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoCompraRequest;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoCompraResponse;
import br.com.unicos.ms_compras.enums.StatusCotacao;
import br.com.unicos.ms_compras.model.cotacao.CotacaoCompra;
import br.com.unicos.ms_compras.repository.cotacao.CotacaoCompraRepository;
import br.com.unicos.ms_compras.service.cotacao.CotacaoCompraService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link CotacaoCompraService}.
 *
 * <p>Gerencia o ciclo de vida das cotações de compra, permitindo
 * criação, atualização, listagem e alteração de status.</p>
 */
@Service
@RequiredArgsConstructor
public class CotacaoCompraServiceImpl implements CotacaoCompraService {

    private final CotacaoCompraRepository cotacaoCompraRepository;
    private final ModelMapper modelMapper;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    @Override
    @Transactional
    public CotacaoCompraResponse criar(CotacaoCompraRequest request) {
        CotacaoCompra cotacao = modelMapper.map(request, CotacaoCompra.class);
        cotacao.setStatus(StatusCotacao.AGUARDANDO_RESPOSTAS);
        CotacaoCompra salva = cotacaoCompraRepository.save(cotacao);
        return modelMapper.map(salva, CotacaoCompraResponse.class);
    }

    @Override
    @Transactional
    public CotacaoCompraResponse atualizar(Long id, CotacaoCompraRequest request) {
        CotacaoCompra existente = cotacaoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cotação não encontrada para o ID: " + id));

        modelMapper.map(request, existente);
        CotacaoCompra atualizada = cotacaoCompraRepository.save(existente);
        return modelMapper.map(atualizada, CotacaoCompraResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CotacaoCompraResponse> buscarPorId(Long id) {
        return cotacaoCompraRepository.findById(id)
                .map(c -> modelMapper.map(c, CotacaoCompraResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CotacaoCompraResponse> buscarPorCodigo(String codigo) {
        return cotacaoCompraRepository.findByCodigo(codigo)
                .map(c -> modelMapper.map(c, CotacaoCompraResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CotacaoCompraListDTO> listar(Pageable pageable) {
        return cotacaoCompraRepository.findAll(pageable)
                .map(c -> modelMapper.map(c, CotacaoCompraListDTO.class));
    }

    @Override
    @Transactional
    public void atualizarStatus(Long id, StatusCotacao status) {
        CotacaoCompra cotacao = cotacaoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cotação não encontrada para o ID: " + id));
        cotacao.setStatus(status);
        cotacaoCompraRepository.save(cotacao);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!cotacaoCompraRepository.existsById(id)) {
            throw new EntityNotFoundException("Cotação não encontrada para exclusão. ID: " + id);
        }
        cotacaoCompraRepository.deleteById(id);
    }

    // ==========================================================
    // 🔹 Métodos auxiliares
    // ==========================================================

    /**
     * Converte uma lista de entidades em uma lista de DTOs.
     *
     * @param cotacoes lista de entidades
     * @return lista de DTOs
     */
    private List<CotacaoCompraListDTO> toListDTO(List<CotacaoCompra> cotacoes) {
        return cotacoes.stream()
                .map(c -> modelMapper.map(c, CotacaoCompraListDTO.class))
                .collect(Collectors.toList());
    }
}
