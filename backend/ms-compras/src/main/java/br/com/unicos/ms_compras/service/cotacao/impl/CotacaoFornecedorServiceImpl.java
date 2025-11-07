package br.com.unicos.ms_compras.service.cotacao.impl;

import br.com.unicos.ms_compras.dto.cotacao.CotacaoFornecedorListDTO;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoFornecedorRequest;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoFornecedorResponse;
import br.com.unicos.ms_compras.enums.StatusFornecedorCotacao;
import br.com.unicos.ms_compras.model.cotacao.CotacaoCompra;
import br.com.unicos.ms_compras.model.cotacao.CotacaoFornecedor;
import br.com.unicos.ms_compras.repository.cotacao.CotacaoCompraRepository;
import br.com.unicos.ms_compras.repository.cotacao.CotacaoFornecedorRepository;
import br.com.unicos.ms_compras.service.cotacao.CotacaoFornecedorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link CotacaoFornecedorService}.
 *
 * <p>
 * Gerencia as propostas enviadas por fornecedores dentro de uma cotação de compra,
 * controlando valores, prazos, status e vínculo com os itens cotados.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CotacaoFornecedorServiceImpl implements CotacaoFornecedorService {

    private final CotacaoFornecedorRepository cotacaoFornecedorRepository;
    private final CotacaoCompraRepository cotacaoCompraRepository;
    private final ModelMapper modelMapper;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    @Override
    @Transactional
    public CotacaoFornecedorResponse criar(CotacaoFornecedorRequest request) {
        CotacaoCompra cotacaoCompra = cotacaoCompraRepository.findById(request.cotacaoCompraId())
                .orElseThrow(() -> new EntityNotFoundException("Cotação não encontrada para o ID informado."));

        CotacaoFornecedor fornecedor = modelMapper.map(request, CotacaoFornecedor.class);
        fornecedor.setCotacaoCompra(cotacaoCompra);
        fornecedor.setStatus(StatusFornecedorCotacao.AGUARDANDO_AVALIACAO);

        CotacaoFornecedor salvo = cotacaoFornecedorRepository.save(fornecedor);
        return modelMapper.map(salvo, CotacaoFornecedorResponse.class);
    }

    @Override
    @Transactional
    public CotacaoFornecedorResponse atualizar(Long id, CotacaoFornecedorRequest request) {
        CotacaoFornecedor existente = cotacaoFornecedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proposta de fornecedor não encontrada para o ID: " + id));

        modelMapper.map(request, existente);
        CotacaoFornecedor atualizado = cotacaoFornecedorRepository.save(existente);
        return modelMapper.map(atualizado, CotacaoFornecedorResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CotacaoFornecedorResponse> buscarPorId(Long id) {
        return cotacaoFornecedorRepository.findById(id)
                .map(f -> modelMapper.map(f, CotacaoFornecedorResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CotacaoFornecedorListDTO> listarPorCotacao(Long cotacaoCompraId) {
        List<CotacaoFornecedor> fornecedores = cotacaoFornecedorRepository.findByCotacaoCompraId(cotacaoCompraId);
        return fornecedores.stream()
                .map(f -> modelMapper.map(f, CotacaoFornecedorListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void atualizarStatus(Long id, StatusFornecedorCotacao status) {
        CotacaoFornecedor fornecedor = cotacaoFornecedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado para o ID: " + id));

        fornecedor.setStatus(status);
        cotacaoFornecedorRepository.save(fornecedor);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!cotacaoFornecedorRepository.existsById(id)) {
            throw new EntityNotFoundException("Proposta de fornecedor não encontrada para exclusão. ID: " + id);
        }
        cotacaoFornecedorRepository.deleteById(id);
    }

    // ==========================================================
    // 🔹 Métodos auxiliares
    // ==========================================================

    /**
     * Converte uma lista de entidades {@link CotacaoFornecedor} para uma lista de DTOs {@link CotacaoFornecedorListDTO}.
     *
     * @param fornecedores lista de entidades
     * @return lista de DTOs correspondentes
     */
    private List<CotacaoFornecedorListDTO> toListDTO(List<CotacaoFornecedor> fornecedores) {
        return fornecedores.stream()
                .map(f -> modelMapper.map(f, CotacaoFornecedorListDTO.class))
                .collect(Collectors.toList());
    }
}
