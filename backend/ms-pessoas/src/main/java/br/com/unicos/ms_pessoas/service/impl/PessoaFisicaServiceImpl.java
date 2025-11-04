package br.com.unicos.ms_pessoas.service.impl;

import br.com.unicos.ms_pessoas.dto.PessoaFisicaRequest;
import br.com.unicos.ms_pessoas.dto.PessoaFisicaResponse;
import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import br.com.unicos.ms_pessoas.model.PessoaFisica;
import br.com.unicos.ms_pessoas.repository.PessoaFisicaRepository;
import br.com.unicos.ms_pessoas.service.PessoaFisicaService;
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
 * Implementação da interface {@link PessoaFisicaService}.
 *
 * <p>Gerencia as regras de negócio da entidade {@link PessoaFisica},
 * incluindo validações de CPF e persistência no banco de dados.</p>
 */
@Service
@RequiredArgsConstructor
public class PessoaFisicaServiceImpl implements PessoaFisicaService {

    private final PessoaFisicaRepository pessoaFisicaRepository;
    private final ModelMapper modelMapper;

    /**
     * Cria uma nova pessoa física.
     *
     * @param request DTO com os dados da pessoa física
     * @return pessoa física criada
     * @throws DataIntegrityViolationException se já existir uma pessoa com o mesmo CPF
     */
    @Override
    @Transactional
    public PessoaFisicaResponse salvar(PessoaFisicaRequest request) {
        if (pessoaFisicaRepository.existsByCpf(request.cpf())) {
            throw new DataIntegrityViolationException("Já existe uma pessoa física com o CPF informado.");
        }

        PessoaFisica pessoa = modelMapper.map(request, PessoaFisica.class);
        pessoa.setTipoPessoa(TipoPessoa.FISICA);

        pessoa = pessoaFisicaRepository.save(pessoa);
        return modelMapper.map(pessoa, PessoaFisicaResponse.class);
    }

    /**
     * Atualiza os dados de uma pessoa física existente.
     *
     * @param id      ID da pessoa física
     * @param request DTO com os novos dados
     * @return pessoa física atualizada
     * @throws EntityNotFoundException se a pessoa não for encontrada
     */
    @Override
    @Transactional
    public PessoaFisicaResponse atualizar(Long id, PessoaFisicaRequest request) {
        PessoaFisica pessoa = pessoaFisicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa física não encontrada."));

        // Evita duplicidade de CPF durante atualização
        if (!pessoa.getCpf().equals(request.cpf())
                && pessoaFisicaRepository.existsByCpf(request.cpf())) {
            throw new DataIntegrityViolationException("Já existe outra pessoa física com o CPF informado.");
        }

        modelMapper.map(request, pessoa);
        pessoa.setTipoPessoa(TipoPessoa.FISICA);

        pessoa = pessoaFisicaRepository.save(pessoa);
        return modelMapper.map(pessoa, PessoaFisicaResponse.class);
    }

    /**
     * Retorna a lista de todas as pessoas físicas cadastradas.
     *
     * @return lista de pessoas físicas
     */
    @Override
    @Transactional(readOnly = true)
    public List<PessoaFisicaResponse> listarTodos() {
        return pessoaFisicaRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, PessoaFisicaResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca uma pessoa física pelo ID.
     *
     * @param id identificador da pessoa física
     * @return pessoa física (se encontrada)
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PessoaFisicaResponse> buscarPorId(Long id) {
        return pessoaFisicaRepository.findById(id)
                .map(p -> modelMapper.map(p, PessoaFisicaResponse.class));
    }

    /**
     * Busca uma pessoa física pelo CPF.
     *
     * @param cpf CPF da pessoa
     * @return pessoa física (se encontrada)
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PessoaFisicaResponse> buscarPorCpf(String cpf) {
        return pessoaFisicaRepository.findByCpf(cpf)
                .map(p -> modelMapper.map(p, PessoaFisicaResponse.class));
    }

    /**
     * Exclui uma pessoa física pelo ID.
     *
     * @param id identificador da pessoa física
     * @throws EntityNotFoundException se a pessoa não for encontrada
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        PessoaFisica pessoa = pessoaFisicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa física não encontrada."));
        pessoaFisicaRepository.delete(pessoa);
    }
}
