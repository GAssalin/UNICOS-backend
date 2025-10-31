package br.com.erp.ms_empresa.service.impl;

import br.com.erp.ms_empresa.dto.EnderecoEmpresaListDTO;
import br.com.erp.ms_empresa.dto.EnderecoEmpresaRequest;
import br.com.erp.ms_empresa.dto.EnderecoEmpresaResponse;
import br.com.erp.ms_empresa.model.Empresa;
import br.com.erp.ms_empresa.model.EnderecoEmpresa;
import br.com.erp.ms_empresa.model.TipoEnderecoEmpresa;
import br.com.erp.ms_empresa.repository.EmpresaRepository;
import br.com.erp.ms_empresa.repository.EnderecoEmpresaRepository;
import br.com.erp.ms_empresa.service.EnderecoEmpresaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface EnderecoEmpresaService.
 */
@Service
@RequiredArgsConstructor
public class EnderecoEmpresaServiceImpl implements EnderecoEmpresaService {

    private final EnderecoEmpresaRepository enderecoEmpresaRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public EnderecoEmpresaResponse create(EnderecoEmpresaRequest request) {
        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.getEmpresaId()));

        // Validação: não permitir duplicidade de tipo para mesma empresa
        if (enderecoEmpresaRepository.existsByEmpresaIdAndTipo(request.getEmpresaId(), request.getTipo())) {
            throw new DataIntegrityViolationException(
                    "A empresa já possui um endereço cadastrado do tipo: " + request.getTipo());
        }

        EnderecoEmpresa endereco = modelMapper.map(request, EnderecoEmpresa.class);
        endereco.setEmpresa(empresa);

        EnderecoEmpresa saved = enderecoEmpresaRepository.save(endereco);
        EnderecoEmpresaResponse response = modelMapper.map(saved, EnderecoEmpresaResponse.class);
        response.setEmpresaId(empresa.getId());
        response.setEmpresaRazaoSocial(empresa.getRazaoSocial());
        return response;
    }

    @Override
    @Transactional
    public EnderecoEmpresaResponse update(Long id, EnderecoEmpresaRequest request) {
        EnderecoEmpresa endereco = enderecoEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com ID: " + id));

        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.getEmpresaId()));

        // Verifica duplicidade de tipo (exceto se for o mesmo registro)
        if (enderecoEmpresaRepository.existsByEmpresaIdAndTipo(request.getEmpresaId(), request.getTipo())
                && !endereco.getTipo().equals(request.getTipo())) {
            throw new DataIntegrityViolationException(
                    "A empresa já possui outro endereço cadastrado do tipo: " + request.getTipo());
        }

        modelMapper.map(request, endereco);
        endereco.setEmpresa(empresa);

        EnderecoEmpresa updated = enderecoEmpresaRepository.save(endereco);
        EnderecoEmpresaResponse response = modelMapper.map(updated, EnderecoEmpresaResponse.class);
        response.setEmpresaId(empresa.getId());
        response.setEmpresaRazaoSocial(empresa.getRazaoSocial());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public EnderecoEmpresaResponse findById(Long id) {
        EnderecoEmpresa endereco = enderecoEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com ID: " + id));
        EnderecoEmpresaResponse response = modelMapper.map(endereco, EnderecoEmpresaResponse.class);
        response.setEmpresaId(endereco.getEmpresa().getId());
        response.setEmpresaRazaoSocial(endereco.getEmpresa().getRazaoSocial());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> findByEmpresa(Long empresaId) {
        return enderecoEmpresaRepository.findByEmpresaId(empresaId)
                .stream()
                .map(e -> modelMapper.map(e, EnderecoEmpresaListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnderecoEmpresaResponse> findByCep(String cep) {
        return enderecoEmpresaRepository.findByCep(cep)
                .map(e -> {
                    EnderecoEmpresaResponse resp = modelMapper.map(e, EnderecoEmpresaResponse.class);
                    resp.setEmpresaId(e.getEmpresa().getId());
                    resp.setEmpresaRazaoSocial(e.getEmpresa().getRazaoSocial());
                    return resp;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> findByCidade(String cidade) {
        return enderecoEmpresaRepository.findByCidadeIgnoreCase(cidade)
                .stream()
                .map(e -> modelMapper.map(e, EnderecoEmpresaListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> findByUf(String uf) {
        return enderecoEmpresaRepository.findByUfIgnoreCase(uf)
                .stream()
                .map(e -> modelMapper.map(e, EnderecoEmpresaListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoEmpresaListDTO> findByTipo(TipoEnderecoEmpresa tipo) {
        return enderecoEmpresaRepository.findByTipo(tipo)
                .stream()
                .map(e -> modelMapper.map(e, EnderecoEmpresaListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!enderecoEmpresaRepository.existsById(id)) {
            throw new EntityNotFoundException("Endereço não encontrado com ID: " + id);
        }
        enderecoEmpresaRepository.deleteById(id);
    }
}