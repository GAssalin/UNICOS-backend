package br.com.unicos.ms_pessoas.service.impl;

import br.com.unicos.ms_pessoas.dto.PessoaJuridicaRequest;
import br.com.unicos.ms_pessoas.dto.PessoaJuridicaResponse;
import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import br.com.unicos.ms_pessoas.model.PessoaJuridica;
import br.com.unicos.ms_pessoas.repository.PessoaJuridicaRepository;
import br.com.unicos.ms_pessoas.service.PessoaJuridicaService;
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
 * Implementação da interface {@link PessoaJuridicaService}.
 *
 * <p>Responsável pelas regras de negócio e persistência da entidade {@link PessoaJuridica},
 * incluindo validações de CNPJ e atualização segura.</p>
 */
@Service
@RequiredArgsConstructor
public class PessoaJuridicaServiceImpl implements PessoaJuridicaService {

    private final PessoaJuridicaRepository pessoaJuridicaRepository;
    private final ModelMapper modelMapper;

    /**
     * Cria e salva uma nova pessoa jurídica.
     *
     * @param request DTO com os dados da pessoa jurídica
     * @return DTO da pessoa jurídica criada
     * @throws DataIntegrityViolationException se já existir uma empresa com o mesmo CNPJ
     */
    @Override
    @Transactional
    public PessoaJuridicaResponse salvar(PessoaJuridicaRequest request) {
        if (pessoaJuridicaRepository.existsByCnpj(request.cnpj())) {
            throw new DataIntegrityViolationException("Já existe uma pessoa jurídica com o CNPJ informado.");
        }

        PessoaJuridica pessoa = modelMapper.map(request, PessoaJuridica.class);
        pessoa.setTipoPessoa(TipoPessoa.JURIDICA);

        pessoa = pessoaJuridicaRepository.save(pessoa);
        return modelMapper.map(pessoa, PessoaJuridicaResponse.class);
    }

    /**
     * Atualiza os dados de uma pessoa jurídica existente.
     *
     * @param id      ID da pessoa jurídica
     * @param request DTO com os novos dados
     * @return DTO da pessoa jurídica atualizada
     * @throws EntityNotFoundException         se a pessoa jurídica não for encontrada
     * @throws DataIntegrityViolationException se o novo CNPJ já estiver cadastrado em outra empresa
     */
    @Override
    @Transactional
    public PessoaJuridicaResponse atualizar(Long id, PessoaJuridicaRequest request) {
        PessoaJuridica pessoa = pessoaJuridicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa jurídica não encontrada."));

        // Evita duplicidade de CNPJ em atualização
        if (!pessoa.getCnpj().equals(request.cnpj())
                && pessoaJuridicaRepository.existsByCnpj(request.cnpj())) {
            throw new DataIntegrityViolationException("Já existe outra pessoa jurídica com o CNPJ informado.");
        }

        modelMapper.map(request, pessoa);
        pessoa.setTipoPessoa(TipoPessoa.JURIDICA);

        pessoa = pessoaJuridicaRepository.save(pessoa);
        return modelMapper.map(pessoa, PessoaJuridicaResponse.class);
    }

    /**
     * Lista todas as pessoas jurídicas cadastradas.
     *
     * @return lista de DTOs de pessoas jurídicas
     */
    @Override
    @Transactional(readOnly = true)
    public List<PessoaJuridicaResponse> listarTodos() {
        return pessoaJuridicaRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, PessoaJuridicaResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca uma pessoa jurídica pelo ID.
     *
     * @param id identificador da pessoa jurídica
     * @return pessoa jurídica (se encontrada)
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PessoaJuridicaResponse> buscarPorId(Long id) {
        return pessoaJuridicaRepository.findById(id)
                .map(p -> modelMapper.map(p, PessoaJuridicaResponse.class));
    }

    /**
     * Busca uma pessoa jurídica pelo CNPJ.
     *
     * @param cnpj CNPJ formatado (00.000.000/0000-00)
     * @return pessoa jurídica (se encontrada)
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PessoaJuridicaResponse> buscarPorCnpj(String cnpj) {
        return pessoaJuridicaRepository.findByCnpj(cnpj)
                .map(p -> modelMapper.map(p, PessoaJuridicaResponse.class));
    }

    /**
     * Exclui uma pessoa jurídica pelo ID.
     *
     * @param id identificador da pessoa jurídica
     * @throws EntityNotFoundException se a pessoa jurídica não for encontrada
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        PessoaJuridica pessoa = pessoaJuridicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa jurídica não encontrada."));
        pessoaJuridicaRepository.delete(pessoa);
    }
}
