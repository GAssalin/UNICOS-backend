package br.com.unicos.ms_empresa.service.impl;

import br.com.unicos.ms_empresa.dto.FilialListDTO;
import br.com.unicos.ms_empresa.dto.FilialRequest;
import br.com.unicos.ms_empresa.dto.FilialResponse;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.Filial;
import br.com.unicos.ms_empresa.repository.EmpresaRepository;
import br.com.unicos.ms_empresa.repository.FilialRepository;
import br.com.unicos.ms_empresa.service.FilialService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link FilialService}.
 *
 * Contém as regras de negócio e interações com o repositório de Filial.
 */
@Service
@Transactional
public class FilialServiceImpl implements FilialService {

    private final FilialRepository filialRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    public FilialServiceImpl(FilialRepository filialRepository,
                             EmpresaRepository empresaRepository,
                             ModelMapper modelMapper) {
        this.filialRepository = filialRepository;
        this.empresaRepository = empresaRepository;
        this.modelMapper = modelMapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public FilialResponse create(FilialRequest request) {
        // Verifica duplicidade de CNPJ
        if (filialRepository.existsByCnpj(request.cnpj())) {
            throw new DataIntegrityViolationException(
                    "Já existe uma filial cadastrada com o CNPJ informado: " + request.cnpj());
        }

        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Empresa não encontrada com ID: " + request.empresaId()));

        Filial filial = modelMapper.map(request, Filial.class);
        filial.setEmpresa(empresa);
        filial.setEmpresaMatrizNome(empresa.getNomeFantasia());

        Filial saved = filialRepository.save(filial);
        return toResponse(saved);
    }

    @Override
    public FilialResponse update(Long id, FilialRequest request) {
        Filial filial = filialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Filial não encontrada com ID: " + id));

        // Verifica se o CNPJ já está em uso por outra filial
        filialRepository.findByCnpj(request.cnpj()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DataIntegrityViolationException(
                        "Já existe outra filial com o mesmo CNPJ: " + request.cnpj());
            }
        });

        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Empresa não encontrada com ID: " + request.empresaId()));

        filial.setEmpresa(empresa);
        filial.setEmpresaMatrizNome(empresa.getNomeFantasia());
        filial.setMatriz(Boolean.TRUE.equals(request.matriz()));
        filial.setRazaoSocial(request.razaoSocial());
        filial.setNomeFantasia(request.nomeFantasia());
        filial.setCnpj(request.cnpj());
        filial.setInscricaoEstadual(request.inscricaoEstadual());
        filial.setInscricaoMunicipal(request.inscricaoMunicipal());
        filial.setTelefone(request.telefone());
        filial.setEmail(request.email());
        filial.setEndereco(request.endereco());
        filial.setCidade(request.cidade());
        filial.setUf(request.uf());
        filial.setCep(request.cep());

        Filial updated = filialRepository.save(filial);
        return toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public FilialResponse findById(Long id) {
        Filial filial = filialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filial não encontrada com ID: " + id));
        return toResponse(filial);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> findByEmpresa(Long empresaId) {
        return filialRepository.findByEmpresaId(empresaId)
                .stream()
                .map(f -> new FilialListDTO(
                        f.getId(),
                        f.getRazaoSocial(),
                        f.getNomeFantasia(),
                        f.getCnpj(),
                        f.getCidade(),
                        f.getUf()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FilialResponse> findByCnpj(String cnpj) {
        return filialRepository.findByCnpj(cnpj)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> findByCidade(String cidade) {
        return filialRepository.findByCidadeIgnoreCase(cidade)
                .stream()
                .map(f -> new FilialListDTO(
                        f.getId(),
                        f.getRazaoSocial(),
                        f.getNomeFantasia(),
                        f.getCnpj(),
                        f.getCidade(),
                        f.getUf()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> findByUf(String uf) {
        return filialRepository.findByUfIgnoreCase(uf)
                .stream()
                .map(f -> new FilialListDTO(
                        f.getId(),
                        f.getRazaoSocial(),
                        f.getNomeFantasia(),
                        f.getCnpj(),
                        f.getCidade(),
                        f.getUf()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialListDTO> findAllOrderedByRazaoSocial() {
        return filialRepository.findAllByOrderByRazaoSocialAsc()
                .stream()
                .map(f -> new FilialListDTO(
                        f.getId(),
                        f.getRazaoSocial(),
                        f.getNomeFantasia(),
                        f.getCnpj(),
                        f.getCidade(),
                        f.getUf()
                ))
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!filialRepository.existsById(id)) {
            throw new EntityNotFoundException("Filial não encontrada com ID: " + id);
        }
        filialRepository.deleteById(id);
    }

    // ==================================
    // 🧭 MÉTODO AUXILIAR
    // ==================================

    private FilialResponse toResponse(Filial entity) {
        Empresa empresa = entity.getEmpresa();
        return new FilialResponse(
                entity.getId(),
                empresa != null ? empresa.getId() : null,
                empresa != null ? empresa.getNomeFantasia() : null,
                entity.getRazaoSocial(),
                entity.getNomeFantasia(),
                entity.getCnpj(),
                entity.getInscricaoEstadual(),
                entity.getInscricaoMunicipal(),
                entity.getMatriz(),
                entity.getTelefone(),
                entity.getEmail(),
                entity.getEndereco(),
                entity.getCidade(),
                entity.getUf(),
                entity.getCep()
        );
    }
}