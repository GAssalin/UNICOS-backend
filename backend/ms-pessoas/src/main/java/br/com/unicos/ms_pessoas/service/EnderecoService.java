package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.client.AuthClient;
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
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Implementação responsável pelas regras de negócio de endereços,
 * incluindo definição de endereço principal, validações e filtros
 * por município, tipo, CEP e pessoa.
 */
@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final AuthClient authClient;
    private final EnderecoRepository repository;
    private final PessoaRepository pessoaRepository;
    private final MunicipioRepository municipioRepository;
    private final EnderecoMapper mapper;
    private final ModelMapper modelMapper;

    // ============================================================
    // CREATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-endereco-admin", fallbackMethod = "fallbackAdmin")
    public EnderecoResponse criar(EnderecoRequest request) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_ENDERECO_CRIAR"))
            throw new AccessDeniedException("Usuário não possui permissão para criar endereços.");

        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        Municipio municipio = municipioRepository.findById(request.municipioId())
                .orElseThrow(() -> new EntityNotFoundException("Município não encontrado"));

        Endereco endereco = mapper.toEntity(request);
        endereco.setPessoa(pessoa);
        endereco.setMunicipio(municipio);

        if (Boolean.TRUE.equals(request.principal()))
            removerPrincipalExistente(pessoa);

        repository.save(endereco);

        return mapper.toResponse(endereco);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-endereco-admin", fallbackMethod = "fallbackAdmin")
    public EnderecoResponse atualizar(Long id, EnderecoRequest request) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_ENDERECO_EDITAR"))
            throw new AccessDeniedException("Usuário não possui permissão para editar endereços.");

        Endereco endereco = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        Municipio municipio = municipioRepository.findById(request.municipioId())
                .orElseThrow(() -> new EntityNotFoundException("Município não encontrado"));

        if (Boolean.TRUE.equals(request.principal()))
            removerPrincipalExistente(pessoa);

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
    // DELETE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-endereco-admin", fallbackMethod = "fallbackAdminVoid")
    public void excluir(Long id) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_ENDERECO_EXCLUIR"))
            throw new AccessDeniedException("Usuário não possui permissão para excluir endereços.");

        if (!repository.existsById(id))
            throw new EntityNotFoundException("Endereço não encontrado.");

        repository.deleteById(id);
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-endereco-admin", fallbackMethod = "fallbackAdminOptional")
    public Optional<EnderecoResponse> buscarPorId(Long id) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_ENDERECO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar endereços.");
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-endereco-admin", fallbackMethod = "fallbackAdminList")
    public List<EnderecoListDTO> listarTodos() {
        if (!authClient.usuarioPossuiPermissao("PESSOA_ENDERECO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar endereços.");
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-endereco-admin", fallbackMethod = "fallbackAdminListPessoa")
    public List<EnderecoListDTO> listarPorPessoa(Long pessoaId) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_ENDERECO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar endereços.");

        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        return repository.findByPessoa(pessoa)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-endereco-admin", fallbackMethod = "fallbackAdminListPessoaTipo")
    public List<EnderecoListDTO> listarPorPessoaETipo(Long pessoaId, String tipo) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_ENDERECO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar endereços.");

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

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-endereco-admin", fallbackMethod = "fallbackAdminListMunicipio")
    public List<EnderecoListDTO> listarPorMunicipio(Long municipioId) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_ENDERECO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar endereços.");

        Municipio municipio = municipioRepository.findById(municipioId)
                .orElseThrow(() -> new EntityNotFoundException("Município não encontrado"));

        return repository.findByMunicipio(municipio)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-endereco-admin", fallbackMethod = "fallbackAdminListCep")
    public List<EnderecoListDTO> listarPorCep(String cep) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_ENDERECO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar endereços.");
        return repository.findByCep(cep)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // PRINCIPAL
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-endereco-admin", fallbackMethod = "fallbackAdminOptionalPessoa")
    public Optional<EnderecoResponse> buscarPrincipal(Long pessoaId) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_ENDERECO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar endereços.");

        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        return repository.findByPessoaAndPrincipalTrue(pessoa)
                .map(mapper::toResponse);
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EnderecoResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços da pessoa temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços da pessoa temporariamente indisponível");
    }

    private Optional<EnderecoResponse> fallbackAdminOptional(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços da pessoa temporariamente indisponível");
    }

    private Optional<EnderecoResponse> fallbackAdminOptionalPessoa(Long pessoaId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços da pessoa temporariamente indisponível");
    }

    private List<EnderecoListDTO> fallbackAdminList(Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços da pessoa temporariamente indisponível");
    }

    private List<EnderecoListDTO> fallbackAdminListPessoa(Long pessoaId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços da pessoa temporariamente indisponível");
    }

    private List<EnderecoListDTO> fallbackAdminListPessoaTipo(Long pessoaId, String tipo, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços da pessoa temporariamente indisponível");
    }

    private List<EnderecoListDTO> fallbackAdminListMunicipio(Long municipioId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços da pessoa temporariamente indisponível");
    }

    private List<EnderecoListDTO> fallbackAdminListCep(String cep, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços da pessoa temporariamente indisponível");
    }
}
