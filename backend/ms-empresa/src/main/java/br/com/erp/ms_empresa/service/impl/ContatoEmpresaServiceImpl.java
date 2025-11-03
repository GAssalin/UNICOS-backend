package br.com.erp.ms_empresa.service.impl;

import br.com.erp.ms_empresa.dto.ContatoEmpresaRequest;
import br.com.erp.ms_empresa.dto.ContatoEmpresaResponse;
import br.com.erp.ms_empresa.model.ContatoEmpresa;
import br.com.erp.ms_empresa.model.Empresa;
import br.com.erp.ms_empresa.repository.ContatoEmpresaRepository;
import br.com.erp.ms_empresa.repository.EmpresaRepository;
import br.com.erp.ms_empresa.service.ContatoEmpresaService;
import jakarta.persistence.EntityNotFoundException;
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
@Transactional
public class ContatoEmpresaServiceImpl implements ContatoEmpresaService {

    private final ContatoEmpresaRepository contatoEmpresaRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    public ContatoEmpresaServiceImpl(ContatoEmpresaRepository contatoEmpresaRepository,
                                     EmpresaRepository empresaRepository,
                                     ModelMapper modelMapper) {
        this.contatoEmpresaRepository = contatoEmpresaRepository;
        this.empresaRepository = empresaRepository;
        this.modelMapper = modelMapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public ContatoEmpresaResponse salvar(ContatoEmpresaRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));

        if (request.email() != null && contatoEmpresaRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Já existe um contato cadastrado com este e-mail.");
        }

        ContatoEmpresa entity = modelMapper.map(request, ContatoEmpresa.class);
        entity.setEmpresa(empresa);

        ContatoEmpresa salvo = contatoEmpresaRepository.save(entity);
        return toResponse(salvo);
    }

    @Override
    public ContatoEmpresaResponse atualizar(Long id, ContatoEmpresaRequest request) {
        ContatoEmpresa existente = contatoEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado."));

        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));

        modelMapper.map(request, existente);
        existente.setEmpresa(empresa);

        ContatoEmpresa atualizado = contatoEmpresaRepository.save(existente);
        return toResponse(atualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContatoEmpresaResponse> buscarPorId(Long id) {
        return contatoEmpresaRepository.findById(id)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContatoEmpresaResponse> listarTodos() {
        return contatoEmpresaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContatoEmpresaResponse> listarPorEmpresa(Long empresaId) {
        return contatoEmpresaRepository.findByEmpresaId(empresaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContatoEmpresaResponse> buscarPorNome(String nome) {
        return contatoEmpresaRepository.findByNomeContatoContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContatoEmpresaResponse> buscarPorEmail(String email) {
        return contatoEmpresaRepository.findByEmail(email)
                .map(this::toResponse);
    }

    @Override
    public void deletar(Long id) {
        if (!contatoEmpresaRepository.existsById(id)) {
            throw new EntityNotFoundException("Contato não encontrado para exclusão.");
        }
        contatoEmpresaRepository.deleteById(id);
    }

    // ==================================
    // 🧭 MÉTODO AUXILIAR
    // ==================================

    private ContatoEmpresaResponse toResponse(ContatoEmpresa entity) {
        Empresa empresa = entity.getEmpresa();
        return new ContatoEmpresaResponse(
                entity.getId(),
                empresa != null ? empresa.getId() : null,
                empresa != null ? empresa.getRazaoSocial() : null,
                entity.getNomeContato(),
                entity.getCargo(),
                entity.getTelefone(),
                entity.getCelular(),
                entity.getEmail()
        );
    }
}