package br.com.erp.ms_empresa.service.impl;

import br.com.erp.ms_empresa.dto.ContatoEmpresaRequest;
import br.com.erp.ms_empresa.dto.ContatoEmpresaResponse;
import br.com.erp.ms_empresa.model.ContatoEmpresa;
import br.com.erp.ms_empresa.model.Empresa;
import br.com.erp.ms_empresa.repository.ContatoEmpresaRepository;
import br.com.erp.ms_empresa.repository.EmpresaRepository;
import br.com.erp.ms_empresa.service.ContatoEmpresaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link ContatoEmpresaService}.
 *
 * Contém as regras de negócio e interações com o repositório de ContatoEmpresa.
 */
@Service
@RequiredArgsConstructor
public class ContatoEmpresaServiceImpl implements ContatoEmpresaService {

    private final ContatoEmpresaRepository contatoEmpresaRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ContatoEmpresaResponse salvar(ContatoEmpresaRequest request) {
        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));

        if (request.getEmail() != null && contatoEmpresaRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Já existe um contato cadastrado com este e-mail.");
        }

        ContatoEmpresa entity = modelMapper.map(request, ContatoEmpresa.class);
        entity.setEmpresa(empresa);

        ContatoEmpresa salvo = contatoEmpresaRepository.save(entity);
        return modelMapper.map(salvo, ContatoEmpresaResponse.class);
    }

    @Override
    @Transactional
    public ContatoEmpresaResponse atualizar(Long id, ContatoEmpresaRequest request) {
        ContatoEmpresa existente = contatoEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado."));

        modelMapper.map(request, existente);

        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));
        existente.setEmpresa(empresa);

        ContatoEmpresa atualizado = contatoEmpresaRepository.save(existente);
        return modelMapper.map(atualizado, ContatoEmpresaResponse.class);
    }

    @Override
    public Optional<ContatoEmpresaResponse> buscarPorId(Long id) {
        return contatoEmpresaRepository.findById(id)
                .map(entity -> modelMapper.map(entity, ContatoEmpresaResponse.class));
    }

    @Override
    public List<ContatoEmpresaResponse> listarTodos() {
        return contatoEmpresaRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, ContatoEmpresaResponse.class))
                .toList();
    }

    @Override
    public List<ContatoEmpresaResponse> listarPorEmpresa(Long empresaId) {
        return contatoEmpresaRepository.findByEmpresaId(empresaId)
                .stream()
                .map(entity -> modelMapper.map(entity, ContatoEmpresaResponse.class))
                .toList();
    }

    @Override
    public List<ContatoEmpresaResponse> buscarPorNome(String nome) {
        return contatoEmpresaRepository.findByNomeContatoContainingIgnoreCase(nome)
                .stream()
                .map(entity -> modelMapper.map(entity, ContatoEmpresaResponse.class))
                .toList();
    }

    @Override
    public Optional<ContatoEmpresaResponse> buscarPorEmail(String email) {
        return contatoEmpresaRepository.findByEmail(email)
                .map(entity -> modelMapper.map(entity, ContatoEmpresaResponse.class));
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!contatoEmpresaRepository.existsById(id)) {
            throw new EntityNotFoundException("Contato não encontrado para exclusão.");
        }
        contatoEmpresaRepository.deleteById(id);
    }
}