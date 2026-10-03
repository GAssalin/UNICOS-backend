package br.com.unicos.ms_pessoas.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaResponse;
import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import br.com.unicos.ms_pessoas.mapper.PessoaFisicaMapper;
import br.com.unicos.ms_pessoas.model.PessoaFisica;
import br.com.unicos.ms_pessoas.repository.PessoaFisicaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Regras de negócio aplicadas à entidade {@link PessoaFisica}.
 */
@Service
public class PessoaFisicaService extends BaseTenantService<PessoaFisica, Long> {

    private final PessoaFisicaRepository repository;
    private final PessoaFisicaMapper mapper;

    public PessoaFisicaService(PessoaFisicaRepository repository, PessoaFisicaMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public PessoaFisicaResponse criar(PessoaFisicaRequest request) {
        validarCpfDisponivel(request.cpf(), null);

        PessoaFisica pessoa = mapper.toEntity(request);
        pessoa.setTipoPessoa(TipoPessoa.FISICA);
        pessoa.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(pessoa));
    }

    @Transactional
    public PessoaFisicaResponse atualizar(Long id, PessoaFisicaRequest request) {
        PessoaFisica pessoa = buscarEntidade(id);

        validarCpfDisponivel(request.cpf(), id);
        mapper.updateEntity(pessoa, request);

        return mapper.toResponse(repository.save(pessoa));
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public Optional<PessoaFisicaResponse> buscarPorId(Long id) {
        return findById(id)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<PessoaFisicaResponse> buscarPorCpf(String cpf) {
        return repository.findByCpfAndEmpresaId(cpf, TenantContext.getEmpresaId())
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<PessoaFisicaListDTO> listarTodas() {
        return repository.findByEmpresaId(TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PessoaFisicaListDTO> listarPorNomeSocial(String nomeSocial) {
        return repository.findByNomeSocialAndEmpresaId(nomeSocial, TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PessoaFisicaListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCaseAndEmpresaId(nome, TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    private PessoaFisica buscarEntidade(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa Física não encontrada."));
    }

    /**
     * O CPF é único em toda a base (restrição do banco), independentemente da empresa.
     */
    private void validarCpfDisponivel(String cpf, Long idAtual) {
        repository.findByCpf(cpf).ifPresent(existing -> {
            if (!existing.getId().equals(idAtual))
                throw new IllegalArgumentException("Já existe uma pessoa física cadastrada com este CPF.");
        });
    }
}
