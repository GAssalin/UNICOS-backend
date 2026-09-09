package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroResponse;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroListDTO;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroUpdateRequest;
import br.com.unicos.ms_empresa.mapper.EmpresaParametroMapper;
import br.com.unicos.ms_empresa.model.EmpresaParametro;
import br.com.unicos.ms_empresa.repository.EmpresaParametroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmpresaParametroService extends BaseTenantService<EmpresaParametro, Long> {


    private final EmpresaParametroRepository repository;
    private final EmpresaParametroMapper mapper;

    public EmpresaParametroService(
            EmpresaParametroRepository repository,
            EmpresaParametroMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    public EmpresaParametroResponse criar(EmpresaParametroCreateRequest request) {
        validarChaveDuplicada(request.chave(), TenantContext.getEmpresaId());

        EmpresaParametro entity = mapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(entity));
    }

    public EmpresaParametroResponse atualizar(String chave, EmpresaParametroUpdateRequest request) {
        EmpresaParametro entity = buscarPorChaveInterno(chave, TenantContext.getEmpresaId());

        mapper.updateEntity(entity, request);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public EmpresaParametroResponse buscarPorChave(String chave) {
        return mapper.toResponse(buscarPorChaveInterno(chave, TenantContext.getEmpresaId()));
    }

    @Transactional(readOnly = true)
    public Page<EmpresaParametroListDTO> listar(Pageable pageable) {
        return repository.findByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(mapper::toListDTO);
    }

    public void remover(String chave) {
        repository.deleteByChaveAndEmpresaId(chave, TenantContext.getEmpresaId());
    }

    // ============================================================
    // AUX
    // ============================================================

    private EmpresaParametro buscarPorChaveInterno(String chave, Long empresaId) {
        return repository.findByChaveAndEmpresaId(chave, empresaId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Parâmetro não encontrado para a chave: " + chave)
                );
    }

    private void validarChaveDuplicada(String chave, Long empresaId) {
        if (repository.existsByChaveAndEmpresaId(chave, empresaId))
            throw new IllegalArgumentException("Já existe um parâmetro com essa chave.");
    }
}