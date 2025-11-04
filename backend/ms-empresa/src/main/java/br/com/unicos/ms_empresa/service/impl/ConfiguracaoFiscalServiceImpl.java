package br.com.unicos.ms_empresa.service.impl;

import br.com.unicos.ms_empresa.dto.ConfiguracaoFiscalRequest;
import br.com.unicos.ms_empresa.dto.ConfiguracaoFiscalResponse;
import br.com.unicos.ms_empresa.model.ConfiguracaoFiscal;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.repository.ConfiguracaoFiscalRepository;
import br.com.unicos.ms_empresa.repository.EmpresaRepository;
import br.com.unicos.ms_empresa.service.ConfiguracaoFiscalService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link ConfiguracaoFiscalService}.
 * <p>
 * Contém as regras de negócio e interações com o repositório de {@link ConfiguracaoFiscal}.
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

    // =====================================================
    // 🔹 CRUD
    // =====================================================

    @Override
    public ConfiguracaoFiscalResponse salvar(ConfiguracaoFiscalRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));

        if (configuracaoFiscalRepository.existsByEmpresaId(empresa.getId())) {
            throw new DataIntegrityViolationException("Já existe uma configuração fiscal cadastrada para esta empresa.");
        }

        ConfiguracaoFiscal entity = modelMapper.map(request, ConfiguracaoFiscal.class);
        entity.setEmpresa(empresa);
        entity.setAtivo(true);

        ConfiguracaoFiscal salva = configuracaoFiscalRepository.save(entity);
        return toResponse(salva);
    }

    @Override
    public ConfiguracaoFiscalResponse atualizar(Long id, ConfiguracaoFiscalRequest request) {
        ConfiguracaoFiscal existente = configuracaoFiscalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Configuração fiscal não encontrada para o ID informado."));

        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));

        modelMapper.map(request, existente);
        existente.setEmpresa(empresa);

        ConfiguracaoFiscal atualizada = configuracaoFiscalRepository.save(existente);
        return toResponse(atualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConfiguracaoFiscalResponse> buscarPorId(Long id) {
        return configuracaoFiscalRepository.findById(id)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfiguracaoFiscalResponse> listarTodas() {
        return configuracaoFiscalRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfiguracaoFiscalResponse> listarAtivas() {
        return configuracaoFiscalRepository.findAll()
                .stream()
                .filter(ConfiguracaoFiscal::getAtivo)
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfiguracaoFiscalResponse> listarInativas() {
        return configuracaoFiscalRepository.findAll()
                .stream()
                .filter(c -> !c.getAtivo())
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
    public Optional<ConfiguracaoFiscalResponse> buscarPorEmpresa(Long empresaId) {
        return configuracaoFiscalRepository.findByEmpresaId(empresaId)
                .map(this::toResponse);
    }

    // =====================================================
    // MÉTODOS AUXILIARES
    // =====================================================

    private ConfiguracaoFiscalResponse toResponse(ConfiguracaoFiscal entity) {
        Empresa empresa = entity.getEmpresa();

        return new ConfiguracaoFiscalResponse(
                entity.getId(),
                entity.getRegimeTributario(),
                entity.getCertificadoDigital(),
                entity.getTipoAmbiente(),
                entity.getAtivo()
        );
    }
}
