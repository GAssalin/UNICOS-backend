package br.com.unicos.ms_compras.service.recebimento.impl;

import br.com.unicos.ms_compras.dto.recebimento.RecebimentoCompraListDTO;
import br.com.unicos.ms_compras.dto.recebimento.RecebimentoCompraRequest;
import br.com.unicos.ms_compras.dto.recebimento.RecebimentoCompraResponse;
import br.com.unicos.ms_compras.enums.StatusRecebimentoCompra;
import br.com.unicos.ms_compras.enums.TipoRecebimento;
import br.com.unicos.ms_compras.model.fiscal.NotaFiscalCompra;
import br.com.unicos.ms_compras.model.pedido.PedidoCompra;
import br.com.unicos.ms_compras.model.recebimento.RecebimentoCompra;
import br.com.unicos.ms_compras.repository.fiscal.NotaFiscalCompraRepository;
import br.com.unicos.ms_compras.repository.pedido.PedidoCompraRepository;
import br.com.unicos.ms_compras.repository.recebimento.RecebimentoCompraRepository;
import br.com.unicos.ms_compras.service.recebimento.RecebimentoCompraService;
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
 * Implementação da interface {@link br.com.unicos.ms_compras.service.recebimento.RecebimentoCompraService}.
 *
 * <p>
 * Responsável pelo gerenciamento do processo de recebimento de mercadorias,
 * controlando vínculos com pedidos e notas fiscais, bem como status e datas.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class RecebimentoCompraServiceImpl implements RecebimentoCompraService {

    private final RecebimentoCompraRepository recebimentoCompraRepository;
    private final PedidoCompraRepository pedidoCompraRepository;
    private final NotaFiscalCompraRepository notaFiscalCompraRepository;
    private final ModelMapper modelMapper;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    @Override
    @Transactional
    public RecebimentoCompraResponse criar(RecebimentoCompraRequest request) {
        PedidoCompra pedido = pedidoCompraRepository.findById(request.pedidoCompraId())
                .orElseThrow(() -> new EntityNotFoundException("Pedido de compra não encontrado para o ID informado."));

        NotaFiscalCompra notaFiscal = null;
        if (request.notaFiscalCompraId() != null) {
            notaFiscal = notaFiscalCompraRepository.findById(request.notaFiscalCompraId())
                    .orElseThrow(() -> new EntityNotFoundException("Nota fiscal não encontrada para o ID informado."));
        }

        RecebimentoCompra recebimento = modelMapper.map(request, RecebimentoCompra.class);
        recebimento.setPedidoCompra(pedido);
        recebimento.setNotaFiscalCompra(notaFiscal);
        recebimento.setStatus(StatusRecebimentoCompra.PENDENTE_CONFERENCIA);
        recebimento.setDataRecebimento(LocalDate.now());

        RecebimentoCompra salvo = recebimentoCompraRepository.save(recebimento);
        return modelMapper.map(salvo, RecebimentoCompraResponse.class);
    }

    @Override
    @Transactional
    public RecebimentoCompraResponse atualizar(Long id, RecebimentoCompraRequest request) {
        RecebimentoCompra existente = recebimentoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recebimento não encontrado para o ID: " + id));

        modelMapper.map(request, existente);
        RecebimentoCompra atualizado = recebimentoCompraRepository.save(existente);
        return modelMapper.map(atualizado, RecebimentoCompraResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecebimentoCompraResponse> buscarPorId(Long id) {
        return recebimentoCompraRepository.findById(id)
                .map(r -> modelMapper.map(r, RecebimentoCompraResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecebimentoCompraListDTO> listar(Pageable pageable) {
        return recebimentoCompraRepository.findAll(pageable)
                .map(r -> modelMapper.map(r, RecebimentoCompraListDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecebimentoCompraListDTO> listarPorTipo(TipoRecebimento tipo) {
        return recebimentoCompraRepository.findByTipoRecebimento(tipo).stream()
                .map(r -> modelMapper.map(r, RecebimentoCompraListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecebimentoCompraListDTO> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return recebimentoCompraRepository.findByDataRecebimentoBetween(inicio, fim).stream()
                .map(r -> modelMapper.map(r, RecebimentoCompraListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void atualizarStatus(Long id, StatusRecebimentoCompra status) {
        RecebimentoCompra recebimento = recebimentoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recebimento não encontrado para o ID: " + id));

        recebimento.setStatus(status);

        // caso finalizado, preenche a data de conclusão
        if (status == StatusRecebimentoCompra.FINALIZADO) {
            recebimento.setDataConclusao(LocalDate.now());
        }

        recebimentoCompraRepository.save(recebimento);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!recebimentoCompraRepository.existsById(id)) {
            throw new EntityNotFoundException("Recebimento não encontrado para exclusão. ID: " + id);
        }
        recebimentoCompraRepository.deleteById(id);
    }
}
