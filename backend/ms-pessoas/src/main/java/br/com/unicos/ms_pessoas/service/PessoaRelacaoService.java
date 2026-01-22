package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoRequest;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoResponse;
import br.com.unicos.ms_pessoas.mapper.PessoaRelacaoMapper;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.model.PessoaRelacao;
import br.com.unicos.ms_pessoas.model.TipoRelacaoPessoa;
import br.com.unicos.ms_pessoas.repository.PessoaRelacaoRepository;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import br.com.unicos.ms_pessoas.repository.TipoRelacaoPessoaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Implementação das regras de negócio aplicadas às relações entre pessoas.
 */
@Service
@RequiredArgsConstructor
public class PessoaRelacaoService {

    private final PessoaRelacaoRepository repository;
    private final PessoaRepository pessoaRepository;
    private final TipoRelacaoPessoaRepository tipoRelacaoPessoaRepository;
    private final PessoaRelacaoMapper mapper;
    private final ModelMapper modelMapper;

    // ============================================================
    // CREATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-relacao-admin", fallbackMethod = "fallbackAdmin")
    public PessoaRelacaoResponse criar(PessoaRelacaoRequest request) {
        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa principal não encontrada."));

        Pessoa relacionado = pessoaRepository.findById(request.relacionadoId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa relacionada não encontrada."));

        if (pessoa.getId().equals(relacionado.getId()))
            throw new IllegalArgumentException("A pessoa não pode se relacionar consigo mesma.");

        TipoRelacaoPessoa tipoRelacao = tipoRelacaoPessoaRepository.findById(request.tipoRelacaoPessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de relação não encontrado."));

        repository.findByPessoaAndRelacionado(pessoa, relacionado).stream()
                .filter(rel -> rel.getTipoRelacao().getId().equals(tipoRelacao.getId()))
                .findAny()
                .ifPresent(rel -> {
                    throw new IllegalArgumentException("A relação entre essas pessoas já está cadastrada.");
                });

        PessoaRelacao relacao = mapper.toEntity(request);
        relacao.setPessoa(pessoa);
        relacao.setRelacionado(relacionado);
        relacao.setTipoRelacao(tipoRelacao);

        repository.save(relacao);

        return mapper.toResponse(relacao);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-relacao-admin", fallbackMethod = "fallbackAdmin")
    public PessoaRelacaoResponse atualizar(Long id, PessoaRelacaoRequest request) {
        PessoaRelacao relacao = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Relação não encontrada."));

        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa principal não encontrada."));

        Pessoa relacionado = pessoaRepository.findById(request.relacionadoId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa relacionada não encontrada."));

        if (pessoa.getId().equals(relacionado.getId()))
            throw new IllegalArgumentException("A pessoa não pode se relacionar consigo mesma.");

        TipoRelacaoPessoa tipoRelacao = tipoRelacaoPessoaRepository.findById(request.tipoRelacaoPessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de relação não encontrado."));

        repository.findByPessoaAndRelacionado(pessoa, relacionado).stream()
                .filter(existing -> !existing.getId().equals(id))
                .filter(existing -> existing.getTipoRelacao().getId().equals(tipoRelacao.getId()))
                .findAny()
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Já existe outra relação igual cadastrada.");
                });

        modelMapper.map(request, relacao);
        relacao.setPessoa(pessoa);
        relacao.setRelacionado(relacionado);
        relacao.setTipoRelacao(tipoRelacao);

        repository.save(relacao);

        return mapper.toResponse(relacao);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-relacao-admin", fallbackMethod = "fallbackAdminVoid")
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Relação não encontrada.");

        repository.deleteById(id);
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-relacao-admin", fallbackMethod = "fallbackAdminOptional")
    public Optional<PessoaRelacaoResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-relacao-admin", fallbackMethod = "fallbackAdminList")
    public List<PessoaRelacaoListDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-relacao-admin", fallbackMethod = "fallbackAdminListPessoa")
    public List<PessoaRelacaoListDTO> listarPorPessoa(Long pessoaId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));

        return repository.findByPessoa(pessoa)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-relacao-admin", fallbackMethod = "fallbackAdminListRelacionado")
    public List<PessoaRelacaoListDTO> listarPorRelacionado(Long relacionadoId) {
        Pessoa relacionado = pessoaRepository.findById(relacionadoId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa relacionada não encontrada."));

        return repository.findByRelacionado(relacionado)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-relacao-admin", fallbackMethod = "fallbackAdminListTipo")
    public List<PessoaRelacaoListDTO> listarPorTipo(Long tipoRelacaoPessoaId) {
        TipoRelacaoPessoa tipoRelacao = tipoRelacaoPessoaRepository.findById(tipoRelacaoPessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de relação não encontrado."));

        return repository.findByTipoRelacao(tipoRelacao)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-relacao-admin", fallbackMethod = "fallbackAdminListPessoaNome")
    public List<PessoaRelacaoListDTO> listarPorPessoaENome(String nome) {
        return repository.findByPessoa_NomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-relacao-admin", fallbackMethod = "fallbackAdminListRelacionadoNome")
    public List<PessoaRelacaoListDTO> listarPorRelacionadoENome(String nome) {
        return repository.findByRelacionado_NomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-relacao-admin", fallbackMethod = "fallbackAdminListPessoaRelacionado")
    public List<PessoaRelacaoListDTO> listarPorPessoaERelacionado(Long pessoaId, Long relacionadoId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa principal não encontrada."));

        Pessoa relacionado = pessoaRepository.findById(relacionadoId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa relacionada não encontrada."));

        return repository.findByPessoaAndRelacionado(pessoa, relacionado)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private PessoaRelacaoResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de relações entre pessoas temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de relações entre pessoas temporariamente indisponível");
    }

    private Optional<PessoaRelacaoResponse> fallbackAdminOptional(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de relações entre pessoas temporariamente indisponível");
    }

    private List<PessoaRelacaoListDTO> fallbackAdminList(Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de relações entre pessoas temporariamente indisponível");
    }

    private List<PessoaRelacaoListDTO> fallbackAdminListPessoa(Long pessoaId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de relações entre pessoas temporariamente indisponível");
    }

    private List<PessoaRelacaoListDTO> fallbackAdminListRelacionado(Long relacionadoId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de relações entre pessoas temporariamente indisponível");
    }

    private List<PessoaRelacaoListDTO> fallbackAdminListTipo(Long tipoRelacaoPessoaId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de relações entre pessoas temporariamente indisponível");
    }

    private List<PessoaRelacaoListDTO> fallbackAdminListPessoaNome(String nome, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de relações entre pessoas temporariamente indisponível");
    }

    private List<PessoaRelacaoListDTO> fallbackAdminListRelacionadoNome(String nome, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de relações entre pessoas temporariamente indisponível");
    }

    private List<PessoaRelacaoListDTO> fallbackAdminListPessoaRelacionado(
            Long pessoaId,
            Long relacionadoId,
            Throwable ex
    ) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de relações entre pessoas temporariamente indisponível");
    }
}
