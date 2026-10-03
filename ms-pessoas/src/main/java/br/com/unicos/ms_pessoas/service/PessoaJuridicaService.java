package br.com.unicos.ms_pessoas.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaResponse;
import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import br.com.unicos.ms_pessoas.mapper.PessoaJuridicaMapper;
import br.com.unicos.ms_pessoas.model.PessoaJuridica;
import br.com.unicos.ms_pessoas.repository.PessoaJuridicaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Regras de negócio aplicadas à entidade {@link PessoaJuridica}.
 */
@Service
public class PessoaJuridicaService extends BaseTenantService<PessoaJuridica, Long> {

    private final PessoaJuridicaRepository repository;
    private final PessoaJuridicaMapper mapper;

    public PessoaJuridicaService(PessoaJuridicaRepository repository, PessoaJuridicaMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public PessoaJuridicaResponse criar(PessoaJuridicaRequest request) {
        validarCnpjDisponivel(request.cnpj(), null);

        PessoaJuridica pessoa = mapper.toEntity(request);
        pessoa.setTipoPessoa(TipoPessoa.JURIDICA);
        pessoa.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(pessoa));
    }

    @Transactional
    public PessoaJuridicaResponse atualizar(Long id, PessoaJuridicaRequest request) {
        PessoaJuridica pessoa = buscarEntidade(id);

        validarCnpjDisponivel(request.cnpj(), id);
        mapper.updateEntity(pessoa, request);

        return mapper.toResponse(repository.save(pessoa));
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public Optional<PessoaJuridicaResponse> buscarPorId(Long id) {
        return findById(id)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<PessoaJuridicaResponse> buscarPorCnpj(String cnpj) {
        return repository.findByCnpjAndEmpresaId(cnpj, TenantContext.getEmpresaId())
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<PessoaJuridicaListDTO> listarTodas() {
        return repository.findByEmpresaId(TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PessoaJuridicaListDTO> listarPorNomeFantasia(String nomeFantasia) {
        return repository.findByNomeFantasiaAndEmpresaId(nomeFantasia, TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PessoaJuridicaListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCaseAndEmpresaId(nome, TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    private PessoaJuridica buscarEntidade(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa Jurídica não encontrada."));
    }

    /**
     * O CNPJ é único em toda a base (restrição do banco), independentemente da empresa.
     */
    private void validarCnpjDisponivel(String cnpj, Long idAtual) {
        repository.findByCnpj(cnpj).ifPresent(existing -> {
            if (!existing.getId().equals(idAtual))
                throw new IllegalArgumentException("Já existe uma pessoa jurídica cadastrada com este CNPJ.");
        });
    }
}
