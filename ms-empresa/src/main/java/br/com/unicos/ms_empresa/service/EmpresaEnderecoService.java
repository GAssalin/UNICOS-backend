package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoResponse;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoUpdateRequest;
import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;
import br.com.unicos.ms_empresa.mapper.EmpresaEnderecoMapper;
import br.com.unicos.ms_empresa.model.EmpresaEndereco;
import br.com.unicos.ms_empresa.repository.EmpresaEnderecoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmpresaEnderecoService extends BaseTenantService<EmpresaEndereco, Long> {

    private final EmpresaEnderecoRepository repository;
    private final EmpresaEnderecoMapper mapper;

    public EmpresaEnderecoService(EmpresaEnderecoRepository repository, EmpresaEnderecoMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    public EmpresaEnderecoResponse criar(EmpresaEnderecoCreateRequest request) {
        validarEnderecoDuplicado(
                request.logradouro(),
                request.numero(),
                request.cep(),
                TenantContext.getEmpresaId()
        );

        if (request.principal())
            removerEnderecoPrincipalAtual(TenantContext.getEmpresaId());

        EmpresaEndereco endereco = mapper.toEntity(request);
        endereco.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponseDTO(repository.save(endereco));
    }

    public EmpresaEnderecoResponse atualizar(Long id, EmpresaEnderecoUpdateRequest request) {
        EmpresaEndereco endereco = buscarEndereco(id, TenantContext.getEmpresaId());

        boolean alterouEndereco =
                !endereco.getLogradouro().equalsIgnoreCase(request.logradouro())
                        || !endereco.getNumero().equalsIgnoreCase(request.numero())
                        || !endereco.getCep().equalsIgnoreCase(request.cep());

        if (alterouEndereco)
            validarEnderecoDuplicado(
                    request.logradouro(),
                    request.numero(),
                    request.cep(),
                    TenantContext.getEmpresaId()
            );

        if (request.principal())
            removerEnderecoPrincipalAtual(TenantContext.getEmpresaId(), id);

        mapper.updateEntityFromDTO(request, endereco);

        return mapper.toResponseDTO(repository.save(endereco));
    }

    @Transactional(readOnly = true)
    public EmpresaEnderecoResponse buscarPorId(Long id) {
        return mapper.toResponseDTO(buscarEndereco(id, TenantContext.getEmpresaId()));
    }

    @Transactional(readOnly = true)
    public Page<EmpresaEnderecoResumoResponse> listar(Pageable pageable) {
        return repository.findByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoDTO);
    }

    @Transactional(readOnly = true)
    public Page<EmpresaEnderecoResumoResponse> listarPorTipo(TipoEnderecoEmpresa tipo, Pageable pageable) {
        return repository.findByTipoEnderecoAndEmpresaId(tipo, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoDTO);
    }

    public void remover(Long id) {
        repository.delete(buscarEndereco(id, TenantContext.getEmpresaId()));
    }

    // ============================================================
    // AUX
    // ============================================================

    private EmpresaEndereco buscarEndereco(Long id, Long empresaId) {
        return repository.findById(id)
                .filter(endereco -> empresaId.equals(endereco.getEmpresaId()))
                .orElseThrow(() ->
                        new EntityNotFoundException("Endereço institucional não encontrado: " + id));
    }

    private void validarEnderecoDuplicado(String logradouro, String numero, String cep, Long empresaId) {
        if (repository.existsByLogradouroAndNumeroAndCepAndEmpresaId(logradouro, numero, cep, empresaId))
            throw new IllegalArgumentException("Já existe um endereço cadastrado com os dados informados para esta empresa.");
    }

    private void removerEnderecoPrincipalAtual(Long empresaId) {
        repository.findByPrincipalTrueAndEmpresaId(empresaId)
                .ifPresent(endereco -> {
                    endereco.setPrincipal(false);
                    repository.save(endereco);
                });
    }

    private void removerEnderecoPrincipalAtual(Long empresaId, Long enderecoAtualId) {
        repository.findByPrincipalTrueAndEmpresaId(empresaId)
                .filter(endereco -> !endereco.getId().equals(enderecoAtualId))
                .ifPresent(endereco -> {
                    endereco.setPrincipal(false);
                    repository.save(endereco);
                });
    }
}