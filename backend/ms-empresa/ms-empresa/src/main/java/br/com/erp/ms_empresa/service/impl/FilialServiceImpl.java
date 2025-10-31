package br.com.erp.ms_empresa.service.impl;

import br.com.erp.ms_empresa.dto.FilialListDTO;
import br.com.erp.ms_empresa.dto.FilialRequest;
import br.com.erp.ms_empresa.dto.FilialResponse;
import br.com.erp.ms_empresa.model.Empresa;
import br.com.erp.ms_empresa.model.Filial;
import br.com.erp.ms_empresa.repository.EmpresaRepository;
import br.com.erp.ms_empresa.repository.FilialRepository;
import br.com.erp.ms_empresa.service.FilialService;
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
 * Implementação da interface FilialService.
 */
@Service
@RequiredArgsConstructor
public class FilialServiceImpl implements FilialService {

    private final FilialRepository filialRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public FilialResponse create(FilialRequest request) {
        if (filialRepository.existsByCnpj(request.getCnpj())) {
            throw new DataIntegrityViolationException("Já existe uma filial cadastrada com o CNPJ informado: " + request.getCnpj());
        }

        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.getEmpresaId()));

        Filial filial = modelMapper.map(request, Filial.class);
        filial.setEmpresa(empresa);
        filial.setEmpresaMatrizNome(empresa.getNomeFantasia());

        Filial saved = filialRepository.save(filial);
        return modelMapper.map(saved, FilialResponse.class);
    }

    @Override
    @Transactional
    public FilialResponse update(Long id, FilialRequest request) {
        Filial filial = filialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filial não encontrada com ID: " + id));

        // Verifica se o CNPJ já está em uso por outra filial
        filialRepository.findByCnpj(request.getCnpj()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DataIntegrityViolationException("Já existe outra filial com o mesmo CNPJ: " + request.getCnpj());
            }
        });

        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.getEmpresaId()));

        modelMapper.map(request, filial);
        filial.setEmpresa(empresa);
        filial.setEmpresaMatrizNome(empresa.getNomeFantasia());

        Filial updated = filialRepository.save(filial);
        return modelMapper.map(updated, FilialResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public FilialResponse findById(Long id) {
        Filial filial = filialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filial não encontrada com ID: " + id));
        return modelMapper.map(filial, FilialResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> findByEmpresa(Long empresaId) {
        return filialRepository.findByEmpresaId(empresaId)
                .stream()
                .map(f -> modelMapper.map(f, FilialListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FilialResponse> findByCnpj(String cnpj) {
        return filialRepository.findByCnpj(cnpj)
                .map(f -> modelMapper.map(f, FilialResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> findByCidade(String cidade) {
        return filialRepository.findByCidadeIgnoreCase(cidade)
                .stream()
                .map(f -> modelMapper.map(f, FilialListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> findByUf(String uf) {
        return filialRepository.findByUfIgnoreCase(uf)
                .stream()
                .map(f -> modelMapper.map(f, FilialListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> findAllOrderedByRazaoSocial() {
        return filialRepository.findAllByOrderByRazaoSocialAsc()
                .stream()
                .map(f -> modelMapper.map(f, FilialListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!filialRepository.existsById(id)) {
            throw new EntityNotFoundException("Filial não encontrada com ID: " + id);
        }
        filialRepository.deleteById(id);
    }
}