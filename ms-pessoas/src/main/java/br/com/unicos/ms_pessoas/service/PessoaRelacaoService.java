package br.com.unicos.ms_pessoas.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação das regras de negócio aplicadas às relações entre pessoas.
 */
@Service
public class PessoaRelacaoService extends BaseTenantService<PessoaRelacao, Long> {

    private final PessoaRelacaoRepository repository;
    private final PessoaRepository pessoaRepository;
    private final TipoRelacaoPessoaRepository tipoRelacaoPessoaRepository;
    private final PessoaRelacaoMapper mapper;

    public PessoaRelacaoService(PessoaRelacaoRepository repository, PessoaRepository pessoaRepository, TipoRelacaoPessoaRepository tipoRelacaoPessoaRepository, PessoaRelacaoMapper mapper) {
        super(repository);
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
        this.tipoRelacaoPessoaRepository = tipoRelacaoPessoaRepository;
        this.mapper = mapper;
    }

    @Transactional
    public PessoaRelacaoResponse criar(PessoaRelacaoRequest request) {
        Pessoa pessoa = buscarPessoa(request.pessoaId(), "Pessoa principal não encontrada.");
        Pessoa relacionado = buscarPessoa(request.relacionadoId(), "Pessoa relacionada não encontrada.");
        TipoRelacaoPessoa tipoRelacao = buscarTipoRelacao(request.tipoRelacaoPessoaId());

        validarRelacao(pessoa, relacionado, tipoRelacao, null);

        PessoaRelacao relacao = mapper.toEntity(request, pessoa, relacionado, tipoRelacao);
        relacao.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(relacao));
    }

    @Transactional
    public PessoaRelacaoResponse atualizar(Long id, PessoaRelacaoRequest request) {
        PessoaRelacao relacao = findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Relação não encontrada."));

        Pessoa pessoa = buscarPessoa(request.pessoaId(), "Pessoa principal não encontrada.");
        Pessoa relacionado = buscarPessoa(request.relacionadoId(), "Pessoa relacionada não encontrada.");
        TipoRelacaoPessoa tipoRelacao = buscarTipoRelacao(request.tipoRelacaoPessoaId());

        validarRelacao(pessoa, relacionado, tipoRelacao, id);
        mapper.updateEntity(relacao, request, pessoa, relacionado, tipoRelacao);

        return mapper.toResponse(repository.save(relacao));
    }

    @Transactional
    public void excluir(Long id) {
        PessoaRelacao relacao = findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Relação não encontrada."));

        repository.delete(relacao);
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    public Optional<PessoaRelacaoResponse> buscarPorId(Long id) {
        return findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    public List<PessoaRelacaoListDTO> listarTodas() {
        return toListDTO(repository.findByEmpresaId(TenantContext.getEmpresaId()));
    }

    @Transactional(readOnly = true)
    public List<PessoaRelacaoListDTO> listarPorPessoa(Long pessoaId) {
        Pessoa pessoa = buscarPessoa(pessoaId, "Pessoa não encontrada.");
        return toListDTO(repository.findByPessoaAndEmpresaId(pessoa, TenantContext.getEmpresaId()));
    }

    @Transactional(readOnly = true)
    public List<PessoaRelacaoListDTO> listarPorRelacionado(Long relacionadoId) {
        Pessoa relacionado = buscarPessoa(relacionadoId, "Pessoa relacionada não encontrada.");
        return toListDTO(repository.findByRelacionadoAndEmpresaId(relacionado, TenantContext.getEmpresaId()));
    }

    @Transactional(readOnly = true)
    public List<PessoaRelacaoListDTO> listarPorTipo(Long tipoRelacaoPessoaId) {
        TipoRelacaoPessoa tipoRelacao = buscarTipoRelacao(tipoRelacaoPessoaId);
        return toListDTO(repository.findByTipoRelacaoAndEmpresaId(tipoRelacao, TenantContext.getEmpresaId()));
    }

    @Transactional(readOnly = true)
    public List<PessoaRelacaoListDTO> listarPorPessoaENome(String nome) {
        return toListDTO(repository.findByPessoa_NomeContainingIgnoreCaseAndEmpresaId(nome, TenantContext.getEmpresaId()));
    }

    @Transactional(readOnly = true)
    public List<PessoaRelacaoListDTO> listarPorRelacionadoENome(String nome) {
        return toListDTO(repository.findByRelacionado_NomeContainingIgnoreCaseAndEmpresaId(nome, TenantContext.getEmpresaId()));
    }

    @Transactional(readOnly = true)
    public List<PessoaRelacaoListDTO> listarPorPessoaERelacionado(Long pessoaId, Long relacionadoId) {
        Pessoa pessoa = buscarPessoa(pessoaId, "Pessoa principal não encontrada.");
        Pessoa relacionado = buscarPessoa(relacionadoId, "Pessoa relacionada não encontrada.");

        return toListDTO(repository.findByPessoaAndRelacionadoAndEmpresaId(pessoa, relacionado, TenantContext.getEmpresaId()));
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Pessoa buscarPessoa(Long id, String mensagemNaoEncontrada) {
        return pessoaRepository.findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException(mensagemNaoEncontrada));
    }

    /**
     * Tipos de relação são dados de referência compartilhados entre empresas.
     */
    private TipoRelacaoPessoa buscarTipoRelacao(Long id) {
        return tipoRelacaoPessoaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de relação não encontrado."));
    }

    private void validarRelacao(Pessoa pessoa, Pessoa relacionado, TipoRelacaoPessoa tipoRelacao, Long idAtual) {
        if (pessoa.getId().equals(relacionado.getId()))
            throw new IllegalArgumentException("A pessoa não pode se relacionar consigo mesma.");

        boolean duplicada = repository
                .findByPessoaAndRelacionadoAndEmpresaId(pessoa, relacionado, TenantContext.getEmpresaId())
                .stream()
                .filter(existing -> !existing.getId().equals(idAtual))
                .anyMatch(existing -> existing.getTipoRelacao().getId().equals(tipoRelacao.getId()));

        if (duplicada)
            throw new IllegalArgumentException("A relação entre essas pessoas já está cadastrada.");
    }

    private List<PessoaRelacaoListDTO> toListDTO(List<PessoaRelacao> relacoes) {
        return relacoes.stream()
                .map(mapper::toListDTO)
                .toList();
    }
}
