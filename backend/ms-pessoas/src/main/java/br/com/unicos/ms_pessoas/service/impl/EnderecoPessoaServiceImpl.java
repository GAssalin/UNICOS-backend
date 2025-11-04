package br.com.unicos.ms_pessoas.service.impl;

import br.com.unicos.ms_pessoas.dto.EnderecoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.EnderecoPessoaResponse;
import br.com.unicos.ms_pessoas.model.EnderecoPessoa;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.repository.EnderecoPessoaRepository;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import br.com.unicos.ms_pessoas.service.EnderecoPessoaService;
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
 * Implementação da interface {@link EnderecoPessoaService}.
 *
 * <p>Responsável pelas regras de negócio relacionadas à entidade {@link EnderecoPessoa},
 * incluindo validações de endereço principal e vínculo com {@link Pessoa}.</p>
 */
@Service
@RequiredArgsConstructor
public class EnderecoPessoaServiceImpl implements EnderecoPessoaService {

    private final EnderecoPessoaRepository enderecoPessoaRepository;
    private final PessoaRepository pessoaRepository;
    private final ModelMapper modelMapper;

    /**
     * Cria e salva um novo endereço de pessoa.
     *
     * <p>Garante que, se o endereço for marcado como principal,
     * os demais endereços da pessoa sejam desmarcados automaticamente.</p>
     *
     * @param request DTO com os dados do endereço
     * @return DTO do endereço criado
     * @throws EntityNotFoundException         se a pessoa informada não existir
     * @throws DataIntegrityViolationException se o CEP estiver em formato incorreto
     */
    @Override
    @Transactional
    public EnderecoPessoaResponse salvar(EnderecoPessoaRequest request) {
        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada para o endereço informado."));

        EnderecoPessoa endereco = modelMapper.map(request, EnderecoPessoa.class);
        endereco.setPessoa(pessoa);

        // Se o endereço for principal, desativa os outros principais
        if (request.principal()) {
            enderecoPessoaRepository.findByPessoaId(pessoa.getId())
                    .forEach(e -> {
                        e.setPrincipal(false);
                        enderecoPessoaRepository.save(e);
                    });
        }

        endereco = enderecoPessoaRepository.save(endereco);
        return modelMapper.map(endereco, EnderecoPessoaResponse.class);
    }

    /**
     * Atualiza um endereço existente.
     *
     * <p>Se o endereço for marcado como principal, os demais endereços da mesma pessoa
     * terão o campo principal desativado automaticamente.</p>
     *
     * @param id      ID do endereço
     * @param request dados atualizados
     * @return endereço atualizado
     * @throws EntityNotFoundException se o endereço ou a pessoa não forem encontrados
     */
    @Override
    @Transactional
    public EnderecoPessoaResponse atualizar(Long id, EnderecoPessoaRequest request) {
        EnderecoPessoa endereco = enderecoPessoaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado."));

        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada para o endereço informado."));

        // Atualiza dados
        modelMapper.map(request, endereco);
        endereco.setPessoa(pessoa);

        // Caso o endereço atualizado seja principal, desativa os outros
        if (request.principal()) {
            enderecoPessoaRepository.findByPessoaId(pessoa.getId())
                    .forEach(e -> {
                        if (!e.getId().equals(id)) {
                            e.setPrincipal(false);
                            enderecoPessoaRepository.save(e);
                        }
                    });
        }

        endereco = enderecoPessoaRepository.save(endereco);
        return modelMapper.map(endereco, EnderecoPessoaResponse.class);
    }

    /**
     * Lista todos os endereços de uma pessoa.
     *
     * @param pessoaId ID da pessoa
     * @return lista de endereços associados
     * @throws EntityNotFoundException se a pessoa não existir
     */
    @Override
    @Transactional(readOnly = true)
    public List<EnderecoPessoaResponse> listarPorPessoa(Long pessoaId) {
        if (!pessoaRepository.existsById(pessoaId)) {
            throw new EntityNotFoundException("Pessoa não encontrada.");
        }

        return enderecoPessoaRepository.findByPessoaId(pessoaId)
                .stream()
                .map(e -> modelMapper.map(e, EnderecoPessoaResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca um endereço específico pelo ID.
     *
     * @param id identificador do endereço
     * @return endereço (se encontrado)
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EnderecoPessoaResponse> buscarPorId(Long id) {
        return enderecoPessoaRepository.findById(id)
                .map(e -> modelMapper.map(e, EnderecoPessoaResponse.class));
    }

    /**
     * Exclui um endereço pelo ID.
     *
     * <p>Se o endereço excluído for o principal, o sistema
     * não define automaticamente outro como principal — isso deve ser feito manualmente.</p>
     *
     * @param id identificador do endereço
     * @throws EntityNotFoundException se o endereço não for encontrado
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        EnderecoPessoa endereco = enderecoPessoaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado."));
        enderecoPessoaRepository.delete(endereco);
    }
}
