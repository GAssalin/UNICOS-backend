package br.com.unicos.ms_compras.service.fiscal.impl;

import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalCompraListDTO;
import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalCompraRequest;
import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalCompraResponse;
import br.com.unicos.ms_compras.enums.StatusNotaFiscalCompra;
import br.com.unicos.ms_compras.enums.TipoNotaFiscal;
import br.com.unicos.ms_compras.model.fiscal.NotaFiscalCompra;
import br.com.unicos.ms_compras.repository.fiscal.NotaFiscalCompraRepository;
import br.com.unicos.ms_compras.service.fiscal.NotaFiscalCompraService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link br.com.unicos.ms_compras.service.fiscal.NotaFiscalCompraService}.
 *
 * <p>
 * Responsável pelo gerenciamento das notas fiscais de compra, integrando informações
 * de fornecedores, pedidos e recebimentos.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class NotaFiscalCompraServiceImpl implements NotaFiscalCompraService {

    private final NotaFiscalCompraRepository notaFiscalCompraRepository;
    private final ModelMapper modelMapper;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    @Override
    @Transactional
    public NotaFiscalCompraResponse criar(NotaFiscalCompraRequest request) {
        NotaFiscalCompra notaFiscal = modelMapper.map(request, NotaFiscalCompra.class);
        notaFiscal.setStatus(StatusNotaFiscalCompra.EMITIDA);
        NotaFiscalCompra salva = notaFiscalCompraRepository.save(notaFiscal);
        return modelMapper.map(salva, NotaFiscalCompraResponse.class);
    }

    @Override
    @Transactional
    public NotaFiscalCompraResponse atualizar(Long id, NotaFiscalCompraRequest request) {
        NotaFiscalCompra existente = notaFiscalCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota fiscal não encontrada para o ID: " + id));

        modelMapper.map(request, existente);
        NotaFiscalCompra atualizada = notaFiscalCompraRepository.save(existente);
        return modelMapper.map(atualizada, NotaFiscalCompraResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotaFiscalCompraResponse> buscarPorId(Long id) {
        return notaFiscalCompraRepository.findById(id)
                .map(n -> modelMapper.map(n, NotaFiscalCompraResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotaFiscalCompraResponse> buscarPorChaveAcesso(String chaveAcesso) {
        return notaFiscalCompraRepository.findByChaveAcesso(chaveAcesso)
                .map(n -> modelMapper.map(n, NotaFiscalCompraResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotaFiscalCompraListDTO> listar(Pageable pageable) {
        return notaFiscalCompraRepository.findAll(pageable)
                .map(n -> modelMapper.map(n, NotaFiscalCompraListDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotaFiscalCompraListDTO> listarPorFornecedor(Long fornecedorId) {
        return notaFiscalCompraRepository.findByFornecedorId(fornecedorId).stream()
                .map(n -> modelMapper.map(n, NotaFiscalCompraListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void atualizarStatus(Long id, StatusNotaFiscalCompra status) {
        NotaFiscalCompra nota = notaFiscalCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota fiscal não encontrada para o ID: " + id));

        nota.setStatus(status);
        notaFiscalCompraRepository.save(nota);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotaFiscalCompraListDTO> listarPorTipoEPeriodo(TipoNotaFiscal tipo, LocalDate inicio, LocalDate fim) {
        return notaFiscalCompraRepository.findByTipoNotaFiscalAndDataEmissaoBetween(tipo, inicio, fim)
                .stream()
                .map(n -> modelMapper.map(n, NotaFiscalCompraListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!notaFiscalCompraRepository.existsById(id)) {
            throw new EntityNotFoundException("Nota fiscal não encontrada para exclusão. ID: " + id);
        }
        notaFiscalCompraRepository.deleteById(id);
    }
}
