package br.com.unicos.ms_ativos.service.impl;

import br.com.unicos.ms_ativos.dto.DocumentoAtivoListDTO;
import br.com.unicos.ms_ativos.dto.DocumentoAtivoRequest;
import br.com.unicos.ms_ativos.dto.DocumentoAtivoResponse;
import br.com.unicos.ms_ativos.enums.StatusDocumento;
import br.com.unicos.ms_ativos.enums.TipoDocumentoAtivo;
import br.com.unicos.ms_ativos.model.DocumentoAtivo;
import br.com.unicos.ms_ativos.repository.DocumentoAtivoRepository;
import br.com.unicos.ms_ativos.service.DocumentoAtivoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link DocumentoAtivoService}.
 * <p>
 * Responsável pelas regras de negócio e persistência dos documentos vinculados aos ativos,
 * como notas fiscais, garantias, laudos técnicos e certificados.
 */
@Service
@RequiredArgsConstructor
public class DocumentoAtivoServiceImpl implements DocumentoAtivoService {

    private final DocumentoAtivoRepository documentoAtivoRepository;
    private final ModelMapper modelMapper;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public DocumentoAtivoResponse salvar(DocumentoAtivoRequest request) {
        DocumentoAtivo documento = modelMapper.map(request, DocumentoAtivo.class);

        // Evita duplicação por número de documento e ativo
        boolean existe = documentoAtivoRepository
                .existsByNumeroAndAtivoId(request.numero(), request.ativoId());

        if (existe) {
            throw new DataIntegrityViolationException(
                    "Já existe um documento com o número informado vinculado a este ativo."
            );
        }

        DocumentoAtivo salvo = documentoAtivoRepository.save(documento);
        return modelMapper.map(salvo, DocumentoAtivoResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public DocumentoAtivoResponse atualizar(Long id, DocumentoAtivoRequest request) {
        DocumentoAtivo existente = documentoAtivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado com ID: " + id));

        existente.setTipo(request.tipo());
        existente.setNumero(request.numero());
        existente.setDataEmissao(request.dataEmissao());
        existente.setArquivoUrl(request.arquivoUrl());
        existente.setStatus(request.status());
        existente.setObservacao(request.observacao());

        DocumentoAtivo atualizado = documentoAtivoRepository.save(existente);
        return modelMapper.map(atualizado, DocumentoAtivoResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        DocumentoAtivo documento = documentoAtivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado com ID: " + id));
        documentoAtivoRepository.delete(documento);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DocumentoAtivoResponse> buscarPorId(Long id) {
        return documentoAtivoRepository.findById(id)
                .map(entity -> modelMapper.map(entity, DocumentoAtivoResponse.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentoAtivoListDTO> listarTodos() {
        return documentoAtivoRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, DocumentoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentoAtivoListDTO> buscarPorAtivo(Long ativoId) {
        return documentoAtivoRepository.findByAtivoId(ativoId)
                .stream()
                .map(entity -> modelMapper.map(entity, DocumentoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentoAtivoListDTO> buscarPorTipo(TipoDocumentoAtivo tipo) {
        return documentoAtivoRepository.findByTipo(tipo)
                .stream()
                .map(entity -> modelMapper.map(entity, DocumentoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentoAtivoListDTO> buscarPorStatus(StatusDocumento status) {
        return documentoAtivoRepository.findByStatus(status)
                .stream()
                .map(entity -> modelMapper.map(entity, DocumentoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentoAtivoListDTO> buscarPorPeriodoEmissao(LocalDate inicio, LocalDate fim) {
        return documentoAtivoRepository.findByDataEmissaoBetween(inicio, fim)
                .stream()
                .map(entity -> modelMapper.map(entity, DocumentoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DocumentoAtivoResponse> buscarUltimaNotaFiscal(Long ativoId) {
        return documentoAtivoRepository.findFirstByAtivoIdAndTipoOrderByDataEmissaoDesc(ativoId, TipoDocumentoAtivo.NOTA_FISCAL)
                .map(entity -> modelMapper.map(entity, DocumentoAtivoResponse.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentoAtivoListDTO> buscarGarantiasValidas() {
        return documentoAtivoRepository.findByTipoAndStatus(TipoDocumentoAtivo.GARANTIA, StatusDocumento.VALIDO)
                .stream()
                .map(entity -> modelMapper.map(entity, DocumentoAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentoAtivoListDTO> buscarDocumentosVencidos() {
        return documentoAtivoRepository.findByStatusIn(List.of(StatusDocumento.VENCIDO, StatusDocumento.EXPIRADO))
                .stream()
                .map(entity -> modelMapper.map(entity, DocumentoAtivoListDTO.class))
                .collect(Collectors.toList());
    }
}
