package br.com.unicos.ms_pessoas.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaResponse;
import br.com.unicos.ms_pessoas.mapper.TipoRelacaoPessoaMapper;
import br.com.unicos.ms_pessoas.model.TipoRelacaoPessoa;
import br.com.unicos.ms_pessoas.repository.TipoRelacaoPessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Regras de negócio aplicadas aos tipos de relação entre pessoas.
 *
 * <p>
 * Os tipos de relação são dados de referência compartilhados (nome único em toda a base):
 * qualquer empresa pode consultá-los e utilizá-los, mas apenas a empresa que cadastrou o
 * registro pode alterá-lo ou excluí-lo.
 * </p>
 */
@Service
public class TipoRelacaoPessoaService extends BaseTenantService<TipoRelacaoPessoa, Long> {

    private final TipoRelacaoPessoaRepository repository;
    private final TipoRelacaoPessoaMapper mapper;

    public TipoRelacaoPessoaService(TipoRelacaoPessoaRepository repository, TipoRelacaoPessoaMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public TipoRelacaoPessoaResponse criar(TipoRelacaoPessoaRequest request) {
        validarNomeDisponivel(request.nome(), null);

        TipoRelacaoPessoa entity = mapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public TipoRelacaoPessoaResponse atualizar(Long id, TipoRelacaoPessoaRequest request) {
        TipoRelacaoPessoa entity = buscarDaEmpresa(id);

        validarNomeDisponivel(request.nome(), id);
        mapper.updateEntity(entity, request);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(buscarDaEmpresa(id));
    }

    @Transactional(readOnly = true)
    public Optional<TipoRelacaoPessoaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<TipoRelacaoPessoaListDTO> listarTodos() {
        return repository.findAllByOrderByNomeAsc()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TipoRelacaoPessoaListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<TipoRelacaoPessoaResponse> buscarPorNomeExato(String nome) {
        return repository.findByNomeIgnoreCase(nome)
                .map(mapper::toResponse);
    }

    private TipoRelacaoPessoa buscarDaEmpresa(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de relação não encontrado."));
    }

    private void validarNomeDisponivel(String nome, Long idAtual) {
        repository.findByNomeIgnoreCase(nome).ifPresent(existing -> {
            if (!existing.getId().equals(idAtual))
                throw new IllegalArgumentException("Já existe um tipo de relação com este nome.");
        });
    }
}
