package br.com.unicos.ms_empresa.service.impl;

import br.com.unicos.ms_empresa.dto.DepartamentoRequest;
import br.com.unicos.ms_empresa.dto.DepartamentoResponse;
import br.com.unicos.ms_empresa.dto.EmpresaListDTO;
import br.com.unicos.ms_empresa.dto.SetorResponse;
import br.com.unicos.ms_empresa.model.Departamento;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.repository.DepartamentoRepository;
import br.com.unicos.ms_empresa.repository.EmpresaRepository;
import br.com.unicos.ms_empresa.service.DepartamentoService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link DepartamentoService}.
 * <p>
 * Contém as regras de negócio e interações com o repositório de {@link Departamento}.
 */
@Service
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository,
                                   EmpresaRepository empresaRepository,
                                   ModelMapper modelMapper) {
        this.departamentoRepository = departamentoRepository;
        this.empresaRepository = empresaRepository;
        this.modelMapper = modelMapper;
    }

    // =====================================================
    // 🔹 CRUD
    // =====================================================

    @Override
    public DepartamentoResponse salvar(DepartamentoRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));

        if (departamentoRepository.existsByNomeAndEmpresaId(request.nome(), request.empresaId())) {
            throw new DataIntegrityViolationException("Já existe um departamento com este nome para esta empresa.");
        }

        Departamento entity = modelMapper.map(request, Departamento.class);
        entity.setEmpresa(empresa);
        entity.setAtivo(true);

        Departamento salvo = departamentoRepository.save(entity);
        return toResponse(salvo);
    }

    @Override
    public DepartamentoResponse atualizar(Long id, DepartamentoRequest request) {
        Departamento existente = departamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado."));

        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o ID informado."));

        existente.setNome(request.nome());
        existente.setAtivo(request.ativo());
        existente.setEmpresa(empresa);

        Departamento atualizado = departamentoRepository.save(existente);
        return toResponse(atualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartamentoResponse> buscarPorId(Long id) {
        return departamentoRepository.findById(id)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoResponse> listarTodos() {
        return departamentoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoResponse> listarAtivos() {
        return departamentoRepository.findByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoResponse> listarInativos() {
        return departamentoRepository.findByAtivoFalse()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoResponse> listarPorEmpresa(Long empresaId) {
        return departamentoRepository.findByEmpresaId(empresaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoResponse> listarPorEmpresaAtivos(Long empresaId) {
        return departamentoRepository.findByEmpresaId(empresaId)
                .stream()
                .filter(Departamento::getAtivo)
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoResponse> buscarPorNome(String nome) {
        return departamentoRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoResponse> buscarPorNomeEAtivo(String nome) {
        return departamentoRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .filter(Departamento::getAtivo)
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoResponse> listarOrdenadosPorNome() {
        return departamentoRepository.findAll()
                .stream()
                .sorted((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()))
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoResponse> listarPorEmpresaOrdenados(Long empresaId) {
        return departamentoRepository.findByEmpresaId(empresaId)
                .stream()
                .sorted((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()))
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Departamento não encontrado para exclusão.");
        }
        departamentoRepository.deleteById(id);
    }

    // =====================================================
    // MÉTODO AUXILIAR
    // =====================================================

    private DepartamentoResponse toResponse(Departamento entity) {
        // Monta o DTO da empresa vinculada (resumido)
        EmpresaListDTO empresaDto = null;
        if (entity.getEmpresa() != null) {
            empresaDto = new EmpresaListDTO(
                    entity.getEmpresa().getId(),
                    entity.getEmpresa().getNomeFantasia(),
                    entity.getEmpresa().getCnpj()
            );
        }

        // Mapeia os setores do departamento
        List<SetorResponse> setoresDto = entity.getSetores() != null
                ? entity.getSetores().stream()
                .map(setor -> new SetorResponse(
                        setor.getId(),
                        setor.getNome(),
                        setor.getAtivo()
                ))
                .toList()
                : List.of();

        // Retorna o DTO completo
        return new DepartamentoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getAtivo(),
                empresaDto,
                setoresDto
        );
    }

}
