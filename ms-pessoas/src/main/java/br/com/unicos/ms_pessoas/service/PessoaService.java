package br.com.unicos.ms_pessoas.service;

import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaResponse;
import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import br.com.unicos.ms_pessoas.mapper.PessoaMapper;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação das operações genéricas de consulta aplicadas à entidade {@link br.com.unicos.ms_pessoas.model.Pessoa},
 * que serve como base para Pessoa Física e Pessoa Jurídica.
 */
@Service
public class PessoaService extends BaseTenantService<Pessoa, Long> {

    private final PessoaRepository repository;
    private final PessoaMapper mapper;

    public PessoaService(PessoaRepository repository, PessoaMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Optional<PessoaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<PessoaListDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PessoaListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PessoaListDTO> listarPorNomeExato(String nome) {
        return repository.findByNome(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PessoaListDTO> listarPorTipo(String tipoPessoa) {
        return repository.findByTipoPessoa(TipoPessoa.valueOf(tipoPessoa.toUpperCase()))
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

}
