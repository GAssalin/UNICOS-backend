package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaResponse;
import br.com.unicos.ms_pessoas.mapper.PessoaJuridicaMapper;
import br.com.unicos.ms_pessoas.model.PessoaJuridica;
import br.com.unicos.ms_pessoas.repository.PessoaJuridicaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação das regras de negócio para cadastro e consulta
 * de Pessoas Jurídicas no UniCoS.
 */
@Service
@RequiredArgsConstructor
public class PessoaJuridicaService {

    private final PessoaJuridicaRepository repository;
    private final PessoaJuridicaMapper mapper;
    private final ModelMapper modelMapper;

    // ============================================================
    // Criar
    // ============================================================
    @Transactional
    public PessoaJuridicaResponse criar(PessoaJuridicaRequest request) {
        // Validação: CNPJ deve ser único
        repository.findByCnpj(request.cnpj()).ifPresent(existing -> {
            throw new IllegalArgumentException("Já existe uma pessoa jurídica cadastrada com este CNPJ.");
        });

        PessoaJuridica pessoa = mapper.toEntity(request);
        repository.save(pessoa);

        return mapper.toResponse(pessoa);
    }

    // ============================================================
    // Atualizar
    // ============================================================
    @Transactional
    public PessoaJuridicaResponse atualizar(Long id, PessoaJuridicaRequest request) {
        PessoaJuridica pessoa = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa Jurídica não encontrada."));

        // Validação: CNPJ único para outra pessoa jurídica
        repository.findByCnpj(request.cnpj()).ifPresent(existing -> {
            if (!existing.getId().equals(id))
                throw new IllegalArgumentException("Já existe outra pessoa jurídica com este CNPJ.");
        });

        modelMapper.map(request, pessoa);
        repository.save(pessoa);

        return mapper.toResponse(pessoa);
    }

    // ============================================================
    // Excluir
    // ============================================================
    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Pessoa Jurídica não encontrada.");
        repository.deleteById(id);
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @Transactional(readOnly = true)
    public Optional<PessoaJuridicaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Buscar por CNPJ
    // ============================================================

    @Transactional(readOnly = true)
    public Optional<PessoaJuridicaResponse> buscarPorCnpj(String cnpj) {
        return repository.findByCnpj(cnpj)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Listar todas
    // ============================================================

    @Transactional(readOnly = true)
    public List<PessoaJuridicaListDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por Nome Fantasia
    // ============================================================

    @Transactional(readOnly = true)
    public List<PessoaJuridicaListDTO> listarPorNomeFantasia(String nomeFantasia) {
        return repository.findByNomeFantasia(nomeFantasia)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por Nome (contains)
    // ============================================================

    @Transactional(readOnly = true)
    public List<PessoaJuridicaListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }
}
