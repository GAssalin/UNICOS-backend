package br.com.unicos.ms_empresa.service.impl;

import br.com.unicos.ms_empresa.dto.ConfiguracaoEmpresaRequest;
import br.com.unicos.ms_empresa.dto.ConfiguracaoEmpresaResponse;
import br.com.unicos.ms_empresa.model.ConfiguracaoFiscal;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.repository.ConfiguracaoFiscalRepository;
import br.com.unicos.ms_empresa.repository.EmpresaRepository;
import br.com.unicos.ms_empresa.service.ConfiguracaoFiscalService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link ConfiguracaoFiscalService}.
 *
 * Contém as regras de negócio e interações com o repositório de ConfiguracaoFiscal.
 */
@Service
@Transactional
public class ConfiguracaoFiscalServiceImpl implements ConfiguracaoFiscalService {

    private final ConfiguracaoFiscalRepository configuracaoFiscalRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    public ConfiguracaoFiscalServiceImpl(ConfiguracaoFiscalRepository configuracaoFiscalRepository,
                                         EmpresaRepository empresaRepository,
                                         ModelMapper modelMapper) {
        this.configuracaoFiscalRepository = configuracaoFiscalRepository;
        this.empresaRepository = empresaRepository;
        this.modelMapper = modelMapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public ConfiguracaoEmpresaResponse salvar(ConfiguracaoEmpresaRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));

        // Verifica se já existe configuração para a empresa
        if (configuracaoFiscalRepository.findByEmpresaId(empresa.getId()).isPresent()) {
            throw new IllegalStateException("Já existe uma configuração fiscal cadastrada para esta empresa.");
        }

        ConfiguracaoFiscal entity = modelMapper.map(request, ConfiguracaoFiscal.class);
        entity.setEmpresa(empresa);

        ConfiguracaoFiscal salva = configuracaoFiscalRepository.save(entity);
        return toResponse(salva);
    }

    @Override
    public ConfiguracaoEmpresaResponse atualizar(Long id, ConfiguracaoEmpresaRequest request) {
        ConfiguracaoFiscal existente = configuracaoFiscalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Configuração fiscal não encontrada."));

        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));

        modelMapper.map(request, existente);
        existente.setEmpresa(empresa);

        ConfiguracaoFiscal atualizada = configuracaoFiscalRepository.save(existente);
        return toResponse(atualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConfiguracaoEmpresaResponse> buscarPorId(Long id) {
        return configuracaoFiscalRepository.findById(id)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfiguracaoEmpresaResponse> listarTodas() {
        return configuracaoFiscalRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        if (!configuracaoFiscalRepository.existsById(id)) {
            throw new EntityNotFoundException("Configuração fiscal não encontrada para exclusão.");
        }
        configuracaoFiscalRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConfiguracaoEmpresaResponse> buscarPorEmpresa(Long empresaId) {
        return configuracaoFiscalRepository.findByEmpresaId(empresaId)
                .map(this::toResponse);
    }

    // ==================================
    // 🧭 MÉTODO AUXILIAR
    // ==================================

    private ConfiguracaoEmpresaResponse toResponse(ConfiguracaoFiscal entity) {
        Empresa empresa = entity.getEmpresa();
        return new ConfiguracaoEmpresaResponse(
                entity.getId(),
                empresa != null ? empresa.getId() : null,
                empresa != null ? empresa.getRazaoSocial() : null,
                entity.getRegimeTributario(),
                entity.getCertificadoDigital(),
                entity.getTipoAmbiente(),
                entity.getTipoAmbiente() != null ? entity.getTipoAmbiente().getDescricao() : null
        );
    }
}