package br.com.unicos.ms_pessoas.service.impl;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaResponse;
import br.com.unicos.ms_pessoas.mapper.PessoaFisicaMapper;
import br.com.unicos.ms_pessoas.model.PessoaFisica;
import br.com.unicos.ms_pessoas.repository.PessoaFisicaRepository;
import br.com.unicos.ms_pessoas.service.interfaces.PessoaFisicaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação das regras de negócio para cadastro e consulta
 * de Pessoas Físicas no UniCoS.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PessoaFisicaServiceImpl implements PessoaFisicaService {

    private final PessoaFisicaRepository repository;
    private final PessoaFisicaMapper mapper;
    private final ModelMapper modelMapper;

    // ============================================================
    // Criar
    // ============================================================

    @Override
    public PessoaFisicaResponse criar(PessoaFisicaRequest request) {

        // Validação: CPF deve ser único
        repository.findByCpf(request.cpf()).ifPresent(existing -> {
            throw new IllegalArgumentException("Já existe uma pessoa física cadastrada com este CPF.");
        });

        PessoaFisica pessoa = mapper.toEntity(request);
        repository.save(pessoa);

        return mapper.toResponse(pessoa);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @Override
    public PessoaFisicaResponse atualizar(Long id, PessoaFisicaRequest request) {

        PessoaFisica pessoa = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa Física não encontrada."));

        // Validação: CPF único para outra pessoa
        repository.findByCpf(request.cpf()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalArgumentException("Já existe outra pessoa física com este CPF.");
            }
        });

        modelMapper.map(request, pessoa);
        repository.save(pessoa);

        return mapper.toResponse(pessoa);
    }

    // ============================================================
    // Excluir
    // ============================================================

    @Override
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Pessoa Física não encontrada.");
        }
        repository.deleteById(id);
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<PessoaFisicaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Buscar por CPF
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<PessoaFisicaResponse> buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Listar todas
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<PessoaFisicaListDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por Nome Social
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<PessoaFisicaListDTO> listarPorNomeSocial(String nomeSocial) {
        return repository.findByNomeSocial(nomeSocial)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por Nome (contains)
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<PessoaFisicaListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }
}
