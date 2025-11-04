package br.com.unicos.ms_pessoas.service.impl;

import br.com.unicos.ms_pessoas.dto.PessoaRelacaoRequest;
import br.com.unicos.ms_pessoas.dto.PessoaRelacaoResponse;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.model.PessoaRelacao;
import br.com.unicos.ms_pessoas.model.TipoRelacaoPessoa;
import br.com.unicos.ms_pessoas.repository.PessoaRelacaoRepository;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import br.com.unicos.ms_pessoas.repository.TipoRelacaoPessoaRepository;
import br.com.unicos.ms_pessoas.service.PessoaRelacaoService;
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
 * Implementação da interface {@link PessoaRelacaoService}.
 *
 * <p>Gerencia o vínculo entre pessoas e seus tipos de relação (Cliente, Fornecedor, Colaborador, etc.).
 * Responsável por garantir que não existam duplicidades e que as referências sejam válidas.</p>
 */
@Service
@RequiredArgsConstructor
public class PessoaRelacaoServiceImpl implements PessoaRelacaoService {

    private final PessoaRelacaoRepository pessoaRelacaoRepository;
    private final PessoaRepository pessoaRepository;
    private final TipoRelacaoPessoaRepository tipoRelacaoPessoaRepository;
    private final ModelMapper modelMapper;

    /**
     * Cria e salva uma nova relação entre pessoa e tipo de vínculo.
     *
     * @param request DTO com os dados da relação
     * @return relação criada
     * @throws EntityNotFoundException         se a pessoa ou o tipo de relação não existirem
     * @throws DataIntegrityViolationException se a relação já existir
     */
    @Override
    @Transactional
    public PessoaRelacaoResponse salvar(PessoaRelacaoRequest request) {
        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));

        TipoRelacaoPessoa tipoRelacao = tipoRelacaoPessoaRepository.findById(request.tipoRelacaoId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de relação não encontrado."));

        // Impede duplicidade de relação
        boolean jaExiste = pessoaRelacaoRepository.findByPessoaId(pessoa.getId()).stream()
                .anyMatch(r -> r.getTipoRelacao().getId().equals(tipoRelacao.getId()) && r.isAtivo());

        if (jaExiste) {
            throw new DataIntegrityViolationException("Esta pessoa já possui o tipo de relação informado.");
        }

        PessoaRelacao relacao = modelMapper.map(request, PessoaRelacao.class);
        relacao.setPessoa(pessoa);
        relacao.setTipoRelacao(tipoRelacao);

        relacao = pessoaRelacaoRepository.save(relacao);
        return modelMapper.map(relacao, PessoaRelacaoResponse.class);
    }

    /**
     * Atualiza uma relação existente.
     *
     * @param id      ID da relação
     * @param request novos dados da relação
     * @return relação atualizada
     * @throws EntityNotFoundException         se a relação, pessoa ou tipo não existirem
     * @throws DataIntegrityViolationException se houver duplicidade após atualização
     */
    @Override
    @Transactional
    public PessoaRelacaoResponse atualizar(Long id, PessoaRelacaoRequest request) {
        PessoaRelacao relacao = pessoaRelacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Relação não encontrada."));

        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));

        TipoRelacaoPessoa tipoRelacao = tipoRelacaoPessoaRepository.findById(request.tipoRelacaoId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de relação não encontrado."));

        // Valida duplicidade de tipo de relação para a pessoa
        boolean duplicada = pessoaRelacaoRepository.findByPessoaId(pessoa.getId()).stream()
                .anyMatch(r -> !r.getId().equals(id)
                        && r.getTipoRelacao().getId().equals(tipoRelacao.getId())
                        && r.isAtivo());

        if (duplicada) {
            throw new DataIntegrityViolationException("Esta pessoa já possui o tipo de relação informado.");
        }

        modelMapper.map(request, relacao);
        relacao.setPessoa(pessoa);
        relacao.setTipoRelacao(tipoRelacao);

        relacao = pessoaRelacaoRepository.save(relacao);
        return modelMapper.map(relacao, PessoaRelacaoResponse.class);
    }

    /**
     * Lista todas as relações de uma pessoa.
     *
     * @param pessoaId ID da pessoa
     * @return lista de vínculos associados
     * @throws EntityNotFoundException se a pessoa não existir
     */
    @Override
    @Transactional(readOnly = true)
    public List<PessoaRelacaoResponse> listarPorPessoa(Long pessoaId) {
        if (!pessoaRepository.existsById(pessoaId)) {
            throw new EntityNotFoundException("Pessoa não encontrada.");
        }

        return pessoaRelacaoRepository.findByPessoaId(pessoaId)
                .stream()
                .map(r -> modelMapper.map(r, PessoaRelacaoResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca uma relação específica pelo ID.
     *
     * @param id identificador da relação
     * @return relação (se encontrada)
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PessoaRelacaoResponse> buscarPorId(Long id) {
        return pessoaRelacaoRepository.findById(id)
                .map(r -> modelMapper.map(r, PessoaRelacaoResponse.class));
    }

    /**
     * Exclui uma relação pelo ID.
     *
     * @param id identificador da relação
     * @throws EntityNotFoundException se a relação não for encontrada
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        PessoaRelacao relacao = pessoaRelacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Relação não encontrada."));
        pessoaRelacaoRepository.delete(relacao);
    }
}
