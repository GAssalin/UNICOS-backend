package br.com.unicos.ms_empresa.service.impl;

import br.com.unicos.ms_empresa.dto.EnderecoEmpresaListDTO;
import br.com.unicos.ms_empresa.dto.EnderecoEmpresaRequest;
import br.com.unicos.ms_empresa.dto.EnderecoEmpresaResponse;
import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EnderecoEmpresa;
import br.com.unicos.ms_empresa.repository.EmpresaRepository;
import br.com.unicos.ms_empresa.repository.EnderecoEmpresaRepository;
import br.com.unicos.ms_empresa.service.EnderecoEmpresaService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link EnderecoEmpresaService}.
 * <p>
 * Contém as regras de negócio e interações com o repositório de EnderecoEmpresa.
 */
@Service
@Transactional
public class EnderecoEmpresaServiceImpl implements EnderecoEmpresaService {

    private final EnderecoEmpresaRepository enderecoEmpresaRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    public EnderecoEmpresaServiceImpl(EnderecoEmpresaRepository enderecoEmpresaRepository,
                                      EmpresaRepository empresaRepository,
                                      ModelMapper modelMapper) {
        this.enderecoEmpresaRepository = enderecoEmpresaRepository;
        this.empresaRepository = empresaRepository;
        this.modelMapper = modelMapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public EnderecoEmpresaResponse salvar(EnderecoEmpresaRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.empresaId()));

        // Evita duplicidade de tipo de endereço para a mesma empresa
        if (enderecoEmpresaRepository.existsByEmpresaIdAndTipoEndereco(request.empresaId(), request.tipoEndereco())) {
            throw new DataIntegrityViolationException(
                    "A empresa já possui um endereço cadastrado do tipo: " + request.tipoEndereco());
        }

        EnderecoEmpresa endereco = modelMapper.map(request, EnderecoEmpresa.class);
        endereco.setEmpresa(empresa);

        EnderecoEmpresa salvo = enderecoEmpresaRepository.save(endereco);
        return toResponse(salvo);
    }

    @Override
    public EnderecoEmpresaResponse atualizar(Long id, EnderecoEmpresaRequest request) {
        EnderecoEmpresa existente = enderecoEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com ID: " + id));

        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.empresaId()));

        // Verifica duplicidade de tipo (exceto se for o mesmo registro)
        if (enderecoEmpresaRepository.existsByEmpresaIdAndTipoEndereco(request.empresaId(), request.tipoEndereco())
                && !existente.getTipoEndereco().equals(request.tipoEndereco())) {
            throw new DataIntegrityViolationException(
                    "A empresa já possui outro endereço cadastrado do tipo: " + request.tipoEndereco());
        }

        modelMapper.map(request, existente);
        existente.setEmpresa(empresa);

        EnderecoEmpresa atualizado = enderecoEmpresaRepository.save(existente);
        return toResponse(atualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnderecoEmpresaResponse> buscarPorId(Long id) {
        return enderecoEmpresaRepository.findById(id)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> listarPorEmpresa(Long empresaId) {
        return enderecoEmpresaRepository.findByEmpresaId(empresaId)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> listarPorFilial(Long filialId) {
        return enderecoEmpresaRepository.findAll().stream()
                .filter(e -> e.getFilial() != null && filialId.equals(e.getFilial().getId()))
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnderecoEmpresaResponse> buscarPorCep(String cep) {
        return enderecoEmpresaRepository.findByCep(cep)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> listarPorCidade(String cidade) {
        return enderecoEmpresaRepository.findByCidadeIgnoreCase(cidade)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> listarPorEstado(String estado) {
        return enderecoEmpresaRepository.findByEstadoIgnoreCase(estado)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> listarPorTipo(TipoEnderecoEmpresa tipo) {
        return enderecoEmpresaRepository.findByTipoEndereco(tipo)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> listarPorTipoECidade(TipoEnderecoEmpresa tipo, String cidade) {
        return enderecoEmpresaRepository.findByTipoEndereco(tipo).stream()
                .filter(e -> e.getCidade().equalsIgnoreCase(cidade))
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> listarPorTipoEEstado(TipoEnderecoEmpresa tipo, String estado) {
        return enderecoEmpresaRepository.findByTipoEndereco(tipo).stream()
                .filter(e -> e.getEstado().equalsIgnoreCase(estado))
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> listarPorEmpresaOrdenados(Long empresaId) {
        return enderecoEmpresaRepository.findByEmpresaId(empresaId).stream()
                .sorted(Comparator.comparing(EnderecoEmpresa::getCidade, String.CASE_INSENSITIVE_ORDER))
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> listarOrdenadosPorEstadoECidade() {
        return enderecoEmpresaRepository.findAll().stream()
                .sorted(Comparator
                        .comparing(EnderecoEmpresa::getEstado, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(EnderecoEmpresa::getCidade, String.CASE_INSENSITIVE_ORDER))
                .map(this::toListDTO)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        if (!enderecoEmpresaRepository.existsById(id)) {
            throw new EntityNotFoundException("Endereço não encontrado para exclusão.");
        }
        enderecoEmpresaRepository.deleteById(id);
    }

    // ==================================
    // MÉTODOS AUXILIARES
    // ==================================

    private EnderecoEmpresaResponse toResponse(EnderecoEmpresa entity) {
        return new EnderecoEmpresaResponse(
                entity.getId(),
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getCidade(),
                entity.getEstado(),
                entity.getCep(),
                entity.getTipoEndereco()
        );
    }

    private EnderecoEmpresaListDTO toListDTO(EnderecoEmpresa entity) {
        return new EnderecoEmpresaListDTO(
                entity.getId(),
                entity.getCidade(),
                entity.getEstado(),
                entity.getTipoEndereco()
        );
    }
}
