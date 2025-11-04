package br.com.unicos.ms_pessoas.service.impl;

import br.com.unicos.ms_pessoas.dto.TipoRelacaoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.TipoRelacaoPessoaResponse;
import br.com.unicos.ms_pessoas.model.TipoRelacaoPessoa;
import br.com.unicos.ms_pessoas.repository.TipoRelacaoPessoaRepository;
import br.com.unicos.ms_pessoas.service.TipoRelacaoPessoaService;
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
 * Implementação da interface {@link TipoRelacaoPessoaService}.
 *
 * <p>Gerencia os tipos de relacionamento entre pessoas, como Cliente, Fornecedor, Colaborador, etc.
 * Essa entidade é de apoio e normalmente usada para preencher seletores e vinculações no sistema.</p>
 */
@Service
@RequiredArgsConstructor
public class TipoRelacaoPessoaServiceImpl implements TipoRelacaoPessoaService {

    private final TipoRelacaoPessoaRepository tipoRelacaoPessoaRepository;
    private final ModelMapper modelMapper;

    /**
     * Cria e salva um novo tipo de relação de pessoa.
     *
     * @param request DTO com os dados do tipo de relação
     * @return tipo criado
     * @throws DataIntegrityViolationException se já existir um tipo com o mesmo código
     */
    @Override
    @Transactional
    public TipoRelacaoPessoaResponse salvar(TipoRelacaoPessoaRequest request) {
        if (tipoRelacaoPessoaRepository.existsByCodigoIgnoreCase(request.codigo())) {
            throw new DataIntegrityViolationException("Já existe um tipo de relação com o código informado.");
        }

        TipoRelacaoPessoa tipo = modelMapper.map(request, TipoRelacaoPessoa.class);
        tipo = tipoRelacaoPessoaRepository.save(tipo);

        return modelMapper.map(tipo, TipoRelacaoPessoaResponse.class);
    }

    /**
     * Atualiza os dados de um tipo de relação existente.
     *
     * @param id      ID do tipo de relação
     * @param request novos dados
     * @return tipo atualizado
     * @throws EntityNotFoundException         se o tipo de relação não for encontrado
     * @throws DataIntegrityViolationException se o novo código já estiver em uso
     */
    @Override
    @Transactional
    public TipoRelacaoPessoaResponse atualizar(Long id, TipoRelacaoPessoaRequest request) {
        TipoRelacaoPessoa tipo = tipoRelacaoPessoaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de relação não encontrado."));

        if (!tipo.getCodigo().equalsIgnoreCase(request.codigo())
                && tipoRelacaoPessoaRepository.existsByCodigoIgnoreCase(request.codigo())) {
            throw new DataIntegrityViolationException("Já existe outro tipo de relação com o mesmo código.");
        }

        modelMapper.map(request, tipo);
        tipo = tipoRelacaoPessoaRepository.save(tipo);

        return modelMapper.map(tipo, TipoRelacaoPessoaResponse.class);
    }

    /**
     * Lista todos os tipos de relação cadastrados.
     *
     * @return lista de tipos de relação
     */
    @Override
    @Transactional(readOnly = true)
    public List<TipoRelacaoPessoaResponse> listarTodos() {
        return tipoRelacaoPessoaRepository.findAll()
                .stream()
                .map(t -> modelMapper.map(t, TipoRelacaoPessoaResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca um tipo de relação pelo ID.
     *
     * @param id identificador do tipo
     * @return tipo (se encontrado)
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TipoRelacaoPessoaResponse> buscarPorId(Long id) {
        return tipoRelacaoPessoaRepository.findById(id)
                .map(t -> modelMapper.map(t, TipoRelacaoPessoaResponse.class));
    }

    /**
     * Exclui um tipo de relação pelo ID.
     *
     * @param id identificador do tipo
     * @throws EntityNotFoundException se o tipo não for encontrado
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        TipoRelacaoPessoa tipo = tipoRelacaoPessoaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de relação não encontrado."));
        tipoRelacaoPessoaRepository.delete(tipo);
    }
}
