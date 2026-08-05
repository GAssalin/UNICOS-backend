package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoResponse;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoUpdateRequest;
import br.com.unicos.ms_empresa.mapper.EmpresaConfiguracaoMapper;
import br.com.unicos.ms_empresa.model.EmpresaConfiguracao;
import br.com.unicos.ms_empresa.repository.EmpresaConfiguracaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmpresaConfiguracaoService extends BaseTenantService<EmpresaConfiguracao, Long> {

    private final EmpresaConfiguracaoRepository repository;
    private final EmpresaConfiguracaoMapper mapper;

    public EmpresaConfiguracaoService(EmpresaConfiguracaoRepository repository, EmpresaConfiguracaoMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    public EmpresaConfiguracaoResponse criar(EmpresaConfiguracaoCreateRequest request) {
        validarChaveDuplicada(request.chave(), TenantContext.getEmpresaId());

        EmpresaConfiguracao entity = mapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(entity));
    }

    public EmpresaConfiguracaoResponse atualizar(String chave, EmpresaConfiguracaoUpdateRequest request) {
        EmpresaConfiguracao configuracao = buscarConfiguracaoPorChave(chave, TenantContext.getEmpresaId());

        if (!configuracao.getChave().equals(request.chave()))
            validarChaveDuplicada(request.chave(), TenantContext.getEmpresaId());

        mapper.updateEntity(request, configuracao);

        return mapper.toResponse(repository.save(configuracao));
    }

    @Transactional(readOnly = true)
    public EmpresaConfiguracaoResponse buscarPorChave(String chave) {
        return mapper.toResponse(buscarConfiguracaoPorChave(chave, TenantContext.getEmpresaId()));
    }

    @Transactional(readOnly = true)
    public Page<EmpresaConfiguracaoResumoResponse> listar(Pageable pageable) {
        return repository.findByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    public void remover(String chave) {
        repository.delete(buscarConfiguracaoPorChave(chave, TenantContext.getEmpresaId()));
    }

    // ============================================================
    // AUX
    // ============================================================

    private EmpresaConfiguracao buscarConfiguracaoPorChave(String chave, Long empresaId) {
        return repository.findByChaveAndEmpresaId(chave, empresaId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Configuração não encontrada para a chave: " + chave));
    }

    private void validarChaveDuplicada(String chave, Long empresaId) {
        if (repository.existsByChaveAndEmpresaId(chave, empresaId))
            throw new IllegalArgumentException("Já existe uma configuração cadastrada com a chave informada.");
    }
}