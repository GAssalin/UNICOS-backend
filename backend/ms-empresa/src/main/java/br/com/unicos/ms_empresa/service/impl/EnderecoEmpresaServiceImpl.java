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

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface EnderecoEmpresaService.
 * Adaptada para uso com records no pacote de DTOs.
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
    public EnderecoEmpresaResponse create(EnderecoEmpresaRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.empresaId()));

        // Evita duplicidade de tipo de endereço para a mesma empresa
        if (enderecoEmpresaRepository.existsByEmpresaIdAndTipo(request.empresaId(), request.tipo())) {
            throw new DataIntegrityViolationException(
                    "A empresa já possui um endereço cadastrado do tipo: " + request.tipo());
        }

        EnderecoEmpresa endereco = modelMapper.map(request, EnderecoEmpresa.class);
        endereco.setEmpresa(empresa);

        EnderecoEmpresa saved = enderecoEmpresaRepository.save(endereco);
        return toResponse(saved);
    }

    @Override
    public EnderecoEmpresaResponse update(Long id, EnderecoEmpresaRequest request) {
        EnderecoEmpresa endereco = enderecoEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com ID: " + id));

        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.empresaId()));

        // Verifica duplicidade de tipo (exceto se for o mesmo registro)
        if (enderecoEmpresaRepository.existsByEmpresaIdAndTipo(request.empresaId(), request.tipo())
                && !endereco.getTipo().equals(request.tipo())) {
            throw new DataIntegrityViolationException(
                    "A empresa já possui outro endereço cadastrado do tipo: " + request.tipo());
        }

        modelMapper.map(request, endereco);
        endereco.setEmpresa(empresa);

        EnderecoEmpresa updated = enderecoEmpresaRepository.save(endereco);
        return toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public EnderecoEmpresaResponse findById(Long id) {
        EnderecoEmpresa endereco = enderecoEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com ID: " + id));
        return toResponse(endereco);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> findByEmpresa(Long empresaId) {
        return enderecoEmpresaRepository.findByEmpresaId(empresaId)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnderecoEmpresaResponse> findByCep(String cep) {
        return enderecoEmpresaRepository.findByCep(cep)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> findByCidade(String cidade) {
        return enderecoEmpresaRepository.findByCidadeIgnoreCase(cidade)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> findByUf(String uf) {
        return enderecoEmpresaRepository.findByUfIgnoreCase(uf)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> findByTipo(TipoEnderecoEmpresa tipo) {
        return enderecoEmpresaRepository.findByTipo(tipo)
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!enderecoEmpresaRepository.existsById(id)) {
            throw new EntityNotFoundException("Endereço não encontrado com ID: " + id);
        }
        enderecoEmpresaRepository.deleteById(id);
    }

    // ==================================
    // 🧭 MÉTODOS AUXILIARES
    // ==================================

    private EnderecoEmpresaResponse toResponse(EnderecoEmpresa entity) {
        Empresa empresa = entity.getEmpresa();
        return new EnderecoEmpresaResponse(
                entity.getId(),
                empresa != null ? empresa.getId() : null,
                empresa != null ? empresa.getRazaoSocial() : null,
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getCidade(),
                entity.getUf(),
                entity.getCep(),
                entity.getTipo()
        );
    }

    private EnderecoEmpresaListDTO toListDTO(EnderecoEmpresa entity) {
        return new EnderecoEmpresaListDTO(
                entity.getId(),
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getCidade(),
                entity.getUf(),
                entity.getTipo()
        );
    }
}