package br.com.erp.ms_empresa.service.impl;

import br.com.erp.ms_empresa.dto.DepartamentoEmpresaRequest;
import br.com.erp.ms_empresa.dto.DepartamentoEmpresaResponse;
import br.com.erp.ms_empresa.model.DepartamentoEmpresa;
import br.com.erp.ms_empresa.model.Empresa;
import br.com.erp.ms_empresa.repository.DepartamentoEmpresaRepository;
import br.com.erp.ms_empresa.repository.EmpresaRepository;
import br.com.erp.ms_empresa.service.DepartamentoEmpresaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class DepartamentoEmpresaServiceImpl implements DepartamentoEmpresaService {

    private final DepartamentoEmpresaRepository departamentoEmpresaRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public DepartamentoEmpresaResponse salvar(DepartamentoEmpresaRequest request) {
        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));

        // Evita duplicidade de nome por empresa
        if (departamentoEmpresaRepository.existsByNomeAndEmpresaId(request.getNome(), request.getEmpresaId())) {
            throw new IllegalStateException("Já existe um departamento com este nome para esta empresa.");
        }

        DepartamentoEmpresa entity = modelMapper.map(request, DepartamentoEmpresa.class);
        entity.setEmpresa(empresa);

        DepartamentoEmpresa salvo = departamentoEmpresaRepository.save(entity);
        return modelMapper.map(salvo, DepartamentoEmpresaResponse.class);
    }

    @Override
    @Transactional
    public DepartamentoEmpresaResponse atualizar(Long id, DepartamentoEmpresaRequest request) {
        DepartamentoEmpresa existente = departamentoEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado."));

        // Atualiza dados
        modelMapper.map(request, existente);

        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));
        existente.setEmpresa(empresa);

        DepartamentoEmpresa atualizado = departamentoEmpresaRepository.save(existente);
        return modelMapper.map(atualizado, DepartamentoEmpresaResponse.class);
    }

    @Override
    public Optional<DepartamentoEmpresaResponse> buscarPorId(Long id) {
        return departamentoEmpresaRepository.findById(id)
                .map(entity -> modelMapper.map(entity, DepartamentoEmpresaResponse.class));
    }

    @Override
    public List<DepartamentoEmpresaResponse> listarTodos() {
        return departamentoEmpresaRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, DepartamentoEmpresaResponse.class))
                .toList();
    }

    @Override
    public List<DepartamentoEmpresaResponse> listarPorEmpresa(Long empresaId) {
        return departamentoEmpresaRepository.findByEmpresaId(empresaId)
                .stream()
                .map(entity -> modelMapper.map(entity, DepartamentoEmpresaResponse.class))
                .toList();
    }

    @Override
    public List<DepartamentoEmpresaResponse> buscarPorNome(String nome) {
        return departamentoEmpresaRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(entity -> modelMapper.map(entity, DepartamentoEmpresaResponse.class))
                .toList();
    }

    @Override
    public List<DepartamentoEmpresaResponse> listarAtivos() {
        return departamentoEmpresaRepository.findByAtivoTrue()
                .stream()
                .map(entity -> modelMapper.map(entity, DepartamentoEmpresaResponse.class))
                .toList();
    }

    @Override
    public List<DepartamentoEmpresaResponse> listarInativos() {
        return departamentoEmpresaRepository.findByAtivoFalse()
                .stream()
                .map(entity -> modelMapper.map(entity, DepartamentoEmpresaResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!departamentoEmpresaRepository.existsById(id)) {
            throw new EntityNotFoundException("Departamento não encontrado para exclusão.");
        }
        departamentoEmpresaRepository.deleteById(id);
    }
}