package br.com.erp.ms_empresa.service.impl;

import br.com.erp.ms_empresa.dto.ConfiguracaoEmpresaRequest;
import br.com.erp.ms_empresa.dto.ConfiguracaoEmpresaResponse;
import br.com.erp.ms_empresa.model.ConfiguracaoFiscal;
import br.com.erp.ms_empresa.model.Empresa;
import br.com.erp.ms_empresa.repository.ConfiguracaoFiscalRepository;
import br.com.erp.ms_empresa.repository.EmpresaRepository;
import br.com.erp.ms_empresa.service.ConfiguracaoFiscalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ConfiguracaoFiscalServiceImpl implements ConfiguracaoFiscalService {

    private final ConfiguracaoFiscalRepository configuracaoFiscalRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ConfiguracaoEmpresaResponse salvar(ConfiguracaoEmpresaRequest request) {
        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));

        // Verifica se já existe configuração para a empresa
        Optional<ConfiguracaoFiscal> existente = configuracaoFiscalRepository.findByEmpresaId(empresa.getId());
        if (existente.isPresent()) {
            throw new IllegalStateException("Já existe uma configuração fiscal cadastrada para esta empresa.");
        }

        ConfiguracaoFiscal entity = modelMapper.map(request, ConfiguracaoFiscal.class);
        entity.setEmpresa(empresa);

        ConfiguracaoFiscal salva = configuracaoFiscalRepository.save(entity);
        return modelMapper.map(salva, ConfiguracaoEmpresaResponse.class);
    }

    @Override
    @Transactional
    public ConfiguracaoEmpresaResponse atualizar(Long id, ConfiguracaoEmpresaRequest request) {
        ConfiguracaoFiscal existente = configuracaoFiscalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Configuração fiscal não encontrada."));

        modelMapper.map(request, existente);
        existente.setEmpresa(empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado.")));

        ConfiguracaoFiscal atualizada = configuracaoFiscalRepository.save(existente);
        return modelMapper.map(atualizada, ConfiguracaoEmpresaResponse.class);
    }

    @Override
    public Optional<ConfiguracaoEmpresaResponse> buscarPorId(Long id) {
        return configuracaoFiscalRepository.findById(id)
                .map(entity -> modelMapper.map(entity, ConfiguracaoEmpresaResponse.class));
    }

    @Override
    public List<ConfiguracaoEmpresaResponse> listarTodas() {
        return configuracaoFiscalRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, ConfiguracaoEmpresaResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!configuracaoFiscalRepository.existsById(id)) {
            throw new EntityNotFoundException("Configuração fiscal não encontrada para exclusão.");
        }
        configuracaoFiscalRepository.deleteById(id);
    }

    @Override
    public Optional<ConfiguracaoEmpresaResponse> buscarPorEmpresa(Long empresaId) {
        return configuracaoFiscalRepository.findByEmpresaId(empresaId)
                .map(entity -> modelMapper.map(entity, ConfiguracaoEmpresaResponse.class));
    }
}