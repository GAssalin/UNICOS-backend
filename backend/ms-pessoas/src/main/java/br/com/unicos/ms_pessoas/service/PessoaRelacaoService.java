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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    // Criar
    // ============================================================
    @Transactional
    public PessoaRelacaoResponse criar(PessoaRelacaoRequest request) {
        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa principal não encontrada."));

        Pessoa relacionado = pessoaRepository.findById(request.relacionadoId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa relacionada não encontrada."));

        if (pessoa.getId().equals(relacionado.getId()))
            throw new IllegalArgumentException("A pessoa não pode se relacionar consigo mesma.");

        TipoRelacaoPessoa tipoRelacao = tipoRelacaoPessoaRepository.findById(request.tipoRelacaoPessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de relação não encontrado."));

        // Verifica duplicidade: já existe vínculo entre estas duas pessoas?
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
    // Atualizar
    // ============================================================
    @Transactional
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

        // Verifica duplicidade para atualização
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
    // Excluir
    // ============================================================
    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Relação não encontrada.");
        repository.deleteById(id);
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @Transactional(readOnly = true)
    public Optional<PessoaRelacaoResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Listar Todas
    // ============================================================

    @Transactional(readOnly = true)
    public List<PessoaRelacaoListDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por Pessoa
    // ============================================================

    @Transactional(readOnly = true)
    public List<PessoaRelacaoListDTO> listarPorPessoa(Long pessoaId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));

        return repository.findByPessoa(pessoa)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por Relacionado
    // ============================================================

    @Transactional(readOnly = true)
    public List<PessoaRelacaoListDTO> listarPorRelacionado(Long relacionadoId) {
        Pessoa relacionado = pessoaRepository.findById(relacionadoId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa relacionada não encontrada."));

        return repository.findByRelacionado(relacionado)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por Tipo de Relação
    // ============================================================

    @Transactional(readOnly = true)
    public List<PessoaRelacaoListDTO> listarPorTipo(Long tipoRelacaoPessoaId) {
        TipoRelacaoPessoa tipoRelacao = tipoRelacaoPessoaRepository.findById(tipoRelacaoPessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de relação não encontrado."));

        return repository.findByTipoRelacao(tipoRelacao)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Busca por Nome da Pessoa Principal
    // ============================================================

    @Transactional(readOnly = true)
    public List<PessoaRelacaoListDTO> listarPorPessoaENome(String nome) {
        return repository.findByPessoa_NomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Busca por Nome da Pessoa Relacionada
    // ============================================================

    @Transactional(readOnly = true)
    public List<PessoaRelacaoListDTO> listarPorRelacionadoENome(String nome) {
        return repository.findByRelacionado_NomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Filtrar por Pessoa e Relacionado ao mesmo tempo
    // ============================================================

    @Transactional(readOnly = true)
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
}
