package br.com.unicos.ms_empresa.service.impl;

import br.com.unicos.ms_empresa.dto.DepartamentoEmpresaRequest;
import br.com.unicos.ms_empresa.dto.DepartamentoEmpresaResponse;
import br.com.unicos.ms_empresa.model.DepartamentoEmpresa;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.repository.DepartamentoEmpresaRepository;
import br.com.unicos.ms_empresa.repository.EmpresaRepository;
import br.com.unicos.ms_empresa.service.DepartamentoEmpresaService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link DepartamentoEmpresaService}.
 *
 * Contém as regras de negócio e interações com o repositório de DepartamentoEmpresa.
 */
@Service
@Transactional
public class DepartamentoEmpresaServiceImpl implements DepartamentoEmpresaService {

    private final DepartamentoEmpresaRepository departamentoEmpresaRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    public DepartamentoEmpresaServiceImpl(DepartamentoEmpresaRepository departamentoEmpresaRepository,
                                          EmpresaRepository empresaRepository,
                                          ModelMapper modelMapper) {
        this.departamentoEmpresaRepository = departamentoEmpresaRepository;
        this.empresaRepository = empresaRepository;
        this.modelMapper = modelMapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public DepartamentoEmpresaResponse salvar(DepartamentoEmpresaRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));

        // Evita duplicidade de nome por empresa
        if (departamentoEmpresaRepository.existsByNomeAndEmpresaId(request.nome(), request.empresaId())) {
            throw new IllegalStateException("Já existe um departamento com este nome para esta empresa.");
        }

        DepartamentoEmpresa entity = modelMapper.map(request, DepartamentoEmpresa.class);
        entity.setEmpresa(empresa);

        DepartamentoEmpresa salvo = departamentoEmpresaRepository.save(entity);
        return toResponse(salvo);
    }

    @Override
    public DepartamentoEmpresaResponse atualizar(Long id, DepartamentoEmpresaRequest request) {
        DepartamentoEmpresa existente = departamentoEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado."));

        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));

        // Atualiza os campos do departamento
        existente.setNome(request.nome());
        existente.setDescricao(request.descricao());
        existente.setAtivo(request.ativo());
        existente.setEmpresa(empresa);

        DepartamentoEmpresa atualizado = departamentoEmpresaRepository.save(existente);
        return toResponse(atualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartamentoEmpresaResponse> buscarPorId(Long id) {
        return departamentoEmpresaRepository.findById(id)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoEmpresaResponse> listarTodos() {
        return departamentoEmpresaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoEmpresaResponse> listarPorEmpresa(Long empresaId) {
        return departamentoEmpresaRepository.findByEmpresaId(empresaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoEmpresaResponse> buscarPorNome(String nome) {
        return departamentoEmpresaRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoEmpresaResponse> listarAtivos() {
        return departamentoEmpresaRepository.findByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoEmpresaResponse> listarInativos() {
        return departamentoEmpresaRepository.findByAtivoFalse()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        if (!departamentoEmpresaRepository.existsById(id)) {
            throw new EntityNotFoundException("Departamento não encontrado para exclusão.");
        }
        departamentoEmpresaRepository.deleteById(id);
    }

    // ==================================
    // 🧭 MÉTODO AUXILIAR
    // ==================================

    private DepartamentoEmpresaResponse toResponse(DepartamentoEmpresa entity) {
        Empresa empresa = entity.getEmpresa();
        return new DepartamentoEmpresaResponse(
                entity.getId(),
                empresa != null ? empresa.getId() : null,
                empresa != null ? empresa.getRazaoSocial() : null,
                entity.getNome(),
                entity.getDescricao(),
                entity.getAtivo()
        );
    }
}