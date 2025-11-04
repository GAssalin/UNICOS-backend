package br.com.unicos.ms_pessoas.service.impl;

import br.com.unicos.ms_pessoas.dto.DocumentoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.DocumentoPessoaResponse;
import br.com.unicos.ms_pessoas.model.DocumentoPessoa;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.repository.DocumentoPessoaRepository;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import br.com.unicos.ms_pessoas.service.DocumentoPessoaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link DocumentoPessoaService}.
 *
 * <p>Gerencia as regras de negócio e persistência da entidade {@link DocumentoPessoa},
 * garantindo consistência dos tipos de documento por pessoa e integridade das referências.</p>
 */
@Service
@RequiredArgsConstructor
public class DocumentoPessoaServiceImpl implements DocumentoPessoaService {

    private final DocumentoPessoaRepository documentoPessoaRepository;
    private final PessoaRepository pessoaRepository;
    private final ModelMapper modelMapper;

    /**
     * Cria e salva um novo documento para uma pessoa.
     *
     * <p>Valida a existência da pessoa e garante que não haja outro documento
     * do mesmo tipo vinculado à mesma pessoa.</p>
     *
     * @param request DTO com os dados do documento
     * @return documento criado
     * @throws EntityNotFoundException         se a pessoa não existir
     * @throws DataIntegrityViolationException se já existir documento do mesmo tipo
     */
    @Override
    @Transactional
    public DocumentoPessoaResponse salvar(DocumentoPessoaRequest request) {
        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada para o documento informado."));

        // Impede duplicidade de tipo de documento para a mesma pessoa
        DocumentoPessoa existente = documentoPessoaRepository
                .findFirstByPessoaIdAndTipoDocumento(request.pessoaId(), request.tipoDocumento());
        if (existente != null) {
            throw new DataIntegrityViolationException("Já existe um documento desse tipo vinculado à pessoa informada.");
        }

        DocumentoPessoa documento = modelMapper.map(request, DocumentoPessoa.class);
        documento.setPessoa(pessoa);

        documento = documentoPessoaRepository.save(documento);
        return modelMapper.map(documento, DocumentoPessoaResponse.class);
    }

    /**
     * Atualiza um documento existente.
     *
     * @param id      ID do documento
     * @param request novos dados
     * @return documento atualizado
     * @throws EntityNotFoundException         se o documento ou a pessoa não existirem
     * @throws DataIntegrityViolationException se houver conflito de tipo de documento duplicado
     */
    @Override
    @Transactional
    public DocumentoPessoaResponse atualizar(Long id, DocumentoPessoaRequest request) {
        DocumentoPessoa documento = documentoPessoaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado."));

        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada para o documento informado."));

        // Verifica se há outro documento do mesmo tipo
        DocumentoPessoa duplicado = documentoPessoaRepository
                .findFirstByPessoaIdAndTipoDocumento(pessoa.getId(), request.tipoDocumento());

        if (duplicado != null && !duplicado.getId().equals(id)) {
            throw new DataIntegrityViolationException("Já existe outro documento desse tipo vinculado à pessoa.");
        }

        modelMapper.map(request, documento);
        documento.setPessoa(pessoa);

        documento = documentoPessoaRepository.save(documento);
        return modelMapper.map(documento, DocumentoPessoaResponse.class);
    }

    /**
     * Lista todos os documentos de uma pessoa.
     *
     * @param pessoaId ID da pessoa
     * @return lista de documentos
     * @throws EntityNotFoundException se a pessoa não existir
     */
    @Override
    @Transactional(readOnly = true)
    public List<DocumentoPessoaResponse> listarPorPessoa(Long pessoaId) {
        if (!pessoaRepository.existsById(pessoaId)) {
            throw new EntityNotFoundException("Pessoa não encontrada.");
        }

        return documentoPessoaRepository.findByPessoaId(pessoaId)
                .stream()
                .map(doc -> modelMapper.map(doc, DocumentoPessoaResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca um documento pelo ID.
     *
     * @param id identificador do documento
     * @return documento (se encontrado)
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentoPessoaResponse> buscarPorId(Long id) {
        return documentoPessoaRepository.findById(id)
                .map(doc -> modelMapper.map(doc, DocumentoPessoaResponse.class));
    }

    /**
     * Exclui um documento pelo ID.
     *
     * @param id identificador do documento
     * @throws EntityNotFoundException se o documento não for encontrado
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        DocumentoPessoa documento = documentoPessoaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado."));
        documentoPessoaRepository.delete(documento);
    }
}
