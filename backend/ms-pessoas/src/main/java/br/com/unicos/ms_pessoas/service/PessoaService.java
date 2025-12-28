package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaResponse;
import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import br.com.unicos.ms_pessoas.mapper.PessoaMapper;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação das operações genéricas de consulta aplicadas à entidade {@link Pessoa},
 * que serve como base para Pessoa Física e Pessoa Jurídica.
 */
@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository repository;
    private final PessoaMapper mapper;

    // ============================================================
    // Buscar por ID
    // ============================================================

    @Transactional(readOnly = true)
    public Optional<PessoaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Listar todas
    // ============================================================

    @Transactional(readOnly = true)
    public List<PessoaListDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por nome parcial
    // ============================================================

    @Transactional(readOnly = true)
    public List<PessoaListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por nome exato
    // ============================================================

    @Transactional(readOnly = true)
    public List<PessoaListDTO> listarPorNomeExato(String nome) {
        return repository.findByNome(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por tipo (FÍSICA/JURÍDICA)
    // ============================================================

    @Transactional(readOnly = true)
    public List<PessoaListDTO> listarPorTipo(String tipoPessoa) {
        TipoPessoa tipoEnum;
        try {
            tipoEnum = TipoPessoa.valueOf(tipoPessoa.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de pessoa inválido: " + tipoPessoa);
        }

        return repository.findByTipoPessoa(tipoEnum)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }
}
