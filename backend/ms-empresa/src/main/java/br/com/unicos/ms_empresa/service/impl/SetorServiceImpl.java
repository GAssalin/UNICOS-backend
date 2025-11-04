package br.com.unicos.ms_empresa.service.impl;

import br.com.unicos.ms_empresa.dto.*;
import br.com.unicos.ms_empresa.model.Departamento;
import br.com.unicos.ms_empresa.model.Setor;
import br.com.unicos.ms_empresa.repository.DepartamentoRepository;
import br.com.unicos.ms_empresa.repository.SetorRepository;
import br.com.unicos.ms_empresa.service.SetorService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link SetorService}.
 * <p>
 * Contém as regras de negócio e interações com o repositório de {@link Setor}.
 */
@Service
@Transactional
public class SetorServiceImpl implements SetorService {

    private final SetorRepository setorRepository;
    private final DepartamentoRepository departamentoRepository;
    private final ModelMapper modelMapper;

    public SetorServiceImpl(SetorRepository setorRepository,
                            DepartamentoRepository departamentoRepository,
                            ModelMapper modelMapper) {
        this.setorRepository = setorRepository;
        this.departamentoRepository = departamentoRepository;
        this.modelMapper = modelMapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public SetorResponse salvar(SetorRequest request) {
        Departamento departamento = departamentoRepository.findById(request.departamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado para o ID informado."));

        // Evita duplicidade de nome por departamento
        if (setorRepository.existsByNomeAndDepartamentoId(request.nome(), request.departamentoId())) {
            throw new DataIntegrityViolationException(
                    "Já existe um setor com este nome vinculado ao departamento informado.");
        }

        Setor setor = modelMapper.map(request, Setor.class);
        setor.setDepartamento(departamento);

        Setor salvo = setorRepository.save(setor);
        return toResponse(salvo);
    }

    @Override
    public SetorResponse atualizar(Long id, SetorRequest request) {
        Setor existente = setorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado."));

        Departamento departamento = departamentoRepository.findById(request.departamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado para o ID informado."));

        // Verifica duplicidade de nome dentro do mesmo departamento
        if (setorRepository.existsByNomeAndDepartamentoId(request.nome(), request.departamentoId())
                && !existente.getNome().equalsIgnoreCase(request.nome())) {
            throw new DataIntegrityViolationException(
                    "Já existe outro setor com este nome neste departamento.");
        }

        modelMapper.map(request, existente);
        existente.setDepartamento(departamento);

        Setor atualizado = setorRepository.save(existente);
        return toResponse(atualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SetorResponse> buscarPorId(Long id) {
        return setorRepository.findById(id)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SetorListDTO> listarTodos() {
        return setorRepository.findAll()
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SetorListDTO> listarAtivos() {
        return setorRepository.findByAtivoTrue()
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SetorListDTO> listarInativos() {
        return setorRepository.findByAtivoFalse()
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SetorListDTO> listarPorDepartamento(Long departamentoId) {
        return setorRepository.findByDepartamentoId(departamentoId)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SetorListDTO> listarPorDepartamentoAtivos(Long departamentoId) {
        return setorRepository.findByDepartamentoIdAndAtivoTrue(departamentoId)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SetorListDTO> buscarPorNome(String nome) {
        return setorRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SetorListDTO> buscarPorNomeEAtivo(String nome) {
        return setorRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SetorListDTO> listarOrdenadosPorNome() {
        return setorRepository.findAll().stream()
                .sorted(Comparator.comparing(Setor::getNome, String.CASE_INSENSITIVE_ORDER))
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SetorListDTO> listarPorDepartamentoOrdenados(Long departamentoId) {
        return setorRepository.findByDepartamentoId(departamentoId).stream()
                .sorted(Comparator.comparing(Setor::getNome, String.CASE_INSENSITIVE_ORDER))
                .map(this::toListDTO)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        if (!setorRepository.existsById(id)) {
            throw new EntityNotFoundException("Setor não encontrado para exclusão.");
        }
        setorRepository.deleteById(id);
    }

    // ==================================
    // MÉTODOS AUXILIARES
    // ==================================

    private SetorResponse toResponse(Setor entity) {
        Departamento departamento = entity.getDepartamento();
        return new SetorResponse(
                entity.getId(),
                entity.getNome(),
                entity.getAtivo()
        );
    }

    private SetorListDTO toListDTO(Setor entity) {
        return new SetorListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getAtivo()
        );
    }
}
