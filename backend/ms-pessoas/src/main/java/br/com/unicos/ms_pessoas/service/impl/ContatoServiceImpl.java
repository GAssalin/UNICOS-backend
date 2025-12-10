package br.com.unicos.ms_pessoas.service.impl;

import br.com.unicos.ms_pessoas.dto.contato.ContatoListDTO;
import br.com.unicos.ms_pessoas.dto.contato.ContatoRequest;
import br.com.unicos.ms_pessoas.dto.contato.ContatoResponse;
import br.com.unicos.ms_pessoas.mapper.ContatoMapper;
import br.com.unicos.ms_pessoas.model.Contato;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.repository.ContatoRepository;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import br.com.unicos.ms_pessoas.service.interfaces.ContatoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação das regras de negócio relacionadas aos contatos de pessoas.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ContatoServiceImpl implements ContatoService {

    private final ContatoRepository repository;
    private final PessoaRepository pessoaRepository;
    private final ContatoMapper mapper;
    private final ModelMapper modelMapper;

    // ============================================================
    // Criar
    // ============================================================

    @Override
    public ContatoResponse criar(ContatoRequest request) {

        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        // Se marcou como principal, remover flag dos demais contatos
        if (request.principal())
            removerPrincipalExistente(pessoa);

        Contato contato = mapper.toEntity(request);
        contato.setPessoa(pessoa);

        repository.save(contato);

        return mapper.toResponse(contato);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @Override
    public ContatoResponse atualizar(Long id, ContatoRequest request) {

        Contato contato = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado"));

        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        if (request.principal())
            removerPrincipalExistente(pessoa);

        modelMapper.map(request, contato);
        contato.setPessoa(pessoa);

        repository.save(contato);

        return mapper.toResponse(contato);
    }

    private void removerPrincipalExistente(Pessoa pessoa) {
        repository.findByPessoaAndPrincipalTrue(pessoa)
                .ifPresent(c -> {
                    c.setPrincipal(false);
                    repository.save(c);
                });
    }

    // ============================================================
    // Excluir
    // ============================================================

    @Override
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Contato não encontrado");

        repository.deleteById(id);
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<ContatoResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<ContatoListDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar Por Pessoa
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<ContatoListDTO> listarPorPessoa(Long pessoaId) {

        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        return repository.findByPessoa(pessoa)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Buscar Principal
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<ContatoResponse> buscarPrincipal(Long pessoaId) {

        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        return repository.findByPessoaAndPrincipalTrue(pessoa)
                .map(mapper::toResponse);
    }
}
