package br.com.unicos.ms_pessoas.service.impl;

import br.com.unicos.ms_pessoas.dto.ContatoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.ContatoPessoaResponse;
import br.com.unicos.ms_pessoas.model.ContatoPessoa;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.repository.ContatoPessoaRepository;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import br.com.unicos.ms_pessoas.service.ContatoPessoaService;
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
 * Implementação da interface {@link ContatoPessoaService}.
 *
 * <p>Responsável pelas regras de negócio e persistência da entidade {@link ContatoPessoa},
 * incluindo validações de contato principal e vínculo com {@link Pessoa}.</p>
 */
@Service
@RequiredArgsConstructor
public class ContatoPessoaServiceImpl implements ContatoPessoaService {

    private final ContatoPessoaRepository contatoPessoaRepository;
    private final PessoaRepository pessoaRepository;
    private final ModelMapper modelMapper;

    /**
     * Cria e salva um novo contato para a pessoa informada.
     *
     * @param request DTO com os dados do contato
     * @return contato criado
     * @throws EntityNotFoundException         se a pessoa informada não existir
     * @throws DataIntegrityViolationException se já existir outro contato principal do mesmo tipo
     */
    @Override
    @Transactional
    public ContatoPessoaResponse salvar(ContatoPessoaRequest request) {
        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada para o contato informado."));

        ContatoPessoa contato = modelMapper.map(request, ContatoPessoa.class);
        contato.setPessoa(pessoa);

        // Se o contato for principal, desativa os outros contatos principais da mesma pessoa
        if (request.principal()) {
            contatoPessoaRepository.findByPessoaId(pessoa.getId())
                    .forEach(c -> {
                        c.setPrincipal(false);
                        contatoPessoaRepository.save(c);
                    });
        }

        contato = contatoPessoaRepository.save(contato);
        return modelMapper.map(contato, ContatoPessoaResponse.class);
    }

    /**
     * Atualiza um contato existente.
     *
     * @param id      ID do contato
     * @param request DTO com os novos dados
     * @return contato atualizado
     * @throws EntityNotFoundException se o contato ou a pessoa não forem encontrados
     */
    @Override
    @Transactional
    public ContatoPessoaResponse atualizar(Long id, ContatoPessoaRequest request) {
        ContatoPessoa contato = contatoPessoaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado."));

        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada para o contato informado."));

        // Atualiza dados
        modelMapper.map(request, contato);
        contato.setPessoa(pessoa);

        // Se for marcado como principal, desativa os outros principais
        if (request.principal()) {
            contatoPessoaRepository.findByPessoaId(pessoa.getId())
                    .forEach(c -> {
                        if (!c.getId().equals(id)) {
                            c.setPrincipal(false);
                            contatoPessoaRepository.save(c);
                        }
                    });
        }

        contato = contatoPessoaRepository.save(contato);
        return modelMapper.map(contato, ContatoPessoaResponse.class);
    }

    /**
     * Lista todos os contatos de uma pessoa.
     *
     * @param pessoaId ID da pessoa
     * @return lista de contatos associados
     * @throws EntityNotFoundException se a pessoa não existir
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContatoPessoaResponse> listarPorPessoa(Long pessoaId) {
        if (!pessoaRepository.existsById(pessoaId)) {
            throw new EntityNotFoundException("Pessoa não encontrada.");
        }

        return contatoPessoaRepository.findByPessoaId(pessoaId)
                .stream()
                .map(c -> modelMapper.map(c, ContatoPessoaResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca um contato pelo ID.
     *
     * @param id identificador do contato
     * @return contato (se encontrado)
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ContatoPessoaResponse> buscarPorId(Long id) {
        return contatoPessoaRepository.findById(id)
                .map(c -> modelMapper.map(c, ContatoPessoaResponse.class));
    }

    /**
     * Exclui um contato pelo ID.
     *
     * <p>Se o contato excluído for o principal, o sistema
     * não define automaticamente outro como principal.</p>
     *
     * @param id identificador do contato
     * @throws EntityNotFoundException se o contato não for encontrado
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        ContatoPessoa contato = contatoPessoaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado."));
        contatoPessoaRepository.delete(contato);
    }
}
