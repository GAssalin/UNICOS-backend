package br.com.unicos.ms_pessoas.service.impl;

import br.com.unicos.ms_pessoas.dto.endereco.EnderecoListDTO;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoRequest;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoResponse;
import br.com.unicos.ms_pessoas.enums.TipoEndereco;
import br.com.unicos.ms_pessoas.mapper.EnderecoMapper;
import br.com.unicos.ms_pessoas.model.Endereco;
import br.com.unicos.ms_pessoas.model.Municipio;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.repository.EnderecoRepository;
import br.com.unicos.ms_pessoas.repository.MunicipioRepository;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import br.com.unicos.ms_pessoas.service.interfaces.EnderecoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação responsável pelas regras de negócio de endereços,
 * incluindo definição de endereço principal, validações e filtros
 * por município, tipo, CEP e pessoa.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EnderecoServiceImpl implements EnderecoService {

    private final EnderecoRepository repository;
    private final PessoaRepository pessoaRepository;
    private final MunicipioRepository municipioRepository;
    private final EnderecoMapper mapper;
    private final ModelMapper modelMapper;

    // ============================================================
    // Criar
    // ============================================================

    @Override
    public EnderecoResponse criar(EnderecoRequest request) {

        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        Municipio municipio = municipioRepository.findById(request.municipioId())
                .orElseThrow(() -> new EntityNotFoundException("Município não encontrado"));

        Endereco endereco = mapper.toEntity(request);
        endereco.setPessoa(pessoa);
        endereco.setMunicipio(municipio);

        // Se este novo endereço for o principal, remove o existente
        if (Boolean.TRUE.equals(request.principal())) {
            removerPrincipalExistente(pessoa);
        }

        repository.save(endereco);

        return mapper.toResponse(endereco);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @Override
    public EnderecoResponse atualizar(Long id, EnderecoRequest request) {

        Endereco endereco = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        Municipio municipio = municipioRepository.findById(request.municipioId())
                .orElseThrow(() -> new EntityNotFoundException("Município não encontrado"));

        // Se este for marcado como principal, remove o anterior
        if (Boolean.TRUE.equals(request.principal())) {
            removerPrincipalExistente(pessoa);
        }

        modelMapper.map(request, endereco);
        endereco.setPessoa(pessoa);
        endereco.setMunicipio(municipio);

        repository.save(endereco);

        return mapper.toResponse(endereco);
    }

    private void removerPrincipalExistente(Pessoa pessoa) {
        repository.findByPessoaAndPrincipalTrue(pessoa)
                .ifPresent(existing -> {
                    existing.setPrincipal(false);
                    repository.save(existing);
                });
    }

    // ============================================================
    // Excluir
    // ============================================================

    @Override
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Endereço não encontrado.");
        }
        repository.deleteById(id);
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<EnderecoResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoListDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por Pessoa
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoListDTO> listarPorPessoa(Long pessoaId) {

        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        return repository.findByPessoa(pessoa)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por Pessoa e Tipo
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoListDTO> listarPorPessoaETipo(Long pessoaId, String tipo) {

        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        TipoEndereco tipoEnum;
        try {
            tipoEnum = TipoEndereco.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de endereço inválido: " + tipo);
        }

        return repository.findByPessoaAndTipo(pessoa, tipoEnum)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por Município
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoListDTO> listarPorMunicipio(Long municipioId) {

        Municipio municipio = municipioRepository.findById(municipioId)
                .orElseThrow(() -> new EntityNotFoundException("Município não encontrado"));

        return repository.findByMunicipio(municipio)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por CEP
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoListDTO> listarPorCep(String cep) {
        return repository.findByCep(cep)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Buscar Endereço Principal
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<EnderecoResponse> buscarPrincipal(Long pessoaId) {

        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        return repository.findByPessoaAndPrincipalTrue(pessoa)
                .map(mapper::toResponse);
    }
}
