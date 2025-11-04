package br.com.unicos.ms_pessoas.service.impl;

import br.com.unicos.ms_pessoas.dto.PessoaRequest;
import br.com.unicos.ms_pessoas.dto.PessoaResponse;
import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import br.com.unicos.ms_pessoas.service.PessoaService;
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
 * Implementação da interface {@link PessoaService}.
 *
 * <p>Responsável pelas regras de negócio e persistência da entidade {@link Pessoa}.
 * Faz uso do {@link ModelMapper} para conversão entre entidade e DTOs.</p>
 */
@Service
@RequiredArgsConstructor
public class PessoaServiceImpl implements PessoaService {

    private final PessoaRepository pessoaRepository;
    private final ModelMapper modelMapper;

    /**
     * Cria e salva uma nova pessoa.
     *
     * @param request DTO com os dados da pessoa
     * @return DTO representando a pessoa criada
     * @throws DataIntegrityViolationException se já existir uma pessoa com o mesmo nome ativa
     */
    @Override
    @Transactional
    public PessoaResponse salvar(PessoaRequest request) {
        boolean exists = pessoaRepository.existsByNomeIgnoreCaseAndAtivoTrue(request.nome());
        if (exists) {
            throw new DataIntegrityViolationException("Já existe uma pessoa ativa com o nome informado.");
        }

        Pessoa pessoa = modelMapper.map(request, Pessoa.class);
        pessoa = pessoaRepository.save(pessoa);

        return modelMapper.map(pessoa, PessoaResponse.class);
    }

    /**
     * Atualiza os dados de uma pessoa existente.
     *
     * @param id      ID da pessoa a ser atualizada
     * @param request DTO com os novos dados
     * @return DTO da pessoa atualizada
     * @throws EntityNotFoundException se a pessoa não for encontrada
     */
    @Override
    @Transactional
    public PessoaResponse atualizar(Long id, PessoaRequest request) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));

        modelMapper.map(request, pessoa); // Atualiza apenas os campos enviados
        pessoa = pessoaRepository.save(pessoa);

        return modelMapper.map(pessoa, PessoaResponse.class);
    }

    /**
     * Lista todas as pessoas cadastradas.
     *
     * @return lista de DTOs de pessoas
     */
    @Override
    @Transactional(readOnly = true)
    public List<PessoaResponse> listarTodos() {
        return pessoaRepository.findAll()
                .stream()
                .map(pessoa -> modelMapper.map(pessoa, PessoaResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca uma pessoa pelo ID.
     *
     * @param id identificador da pessoa
     * @return DTO da pessoa, se encontrada
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PessoaResponse> buscarPorId(Long id) {
        return pessoaRepository.findById(id)
                .map(pessoa -> modelMapper.map(pessoa, PessoaResponse.class));
    }

    /**
     * Remove uma pessoa pelo ID.
     *
     * @param id identificador da pessoa
     * @throws EntityNotFoundException se a pessoa não existir
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
        pessoaRepository.delete(pessoa);
    }

    /**
     * Lista todas as pessoas de um tipo específico.
     *
     * @param tipo tipo de pessoa (FÍSICA ou JURÍDICA)
     * @return lista de DTOs do tipo informado
     */
    @Override
    @Transactional(readOnly = true)
    public List<PessoaResponse> listarPorTipo(TipoPessoa tipo) {
        return pessoaRepository.findByTipoPessoa(tipo)
                .stream()
                .map(pessoa -> modelMapper.map(pessoa, PessoaResponse.class))
                .collect(Collectors.toList());
    }
}
