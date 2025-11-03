package br.com.erp.ms_empresa.service.impl;

import br.com.erp.ms_empresa.dto.*;
import br.com.erp.ms_empresa.enums.TipoEnderecoEmpresa;
import br.com.erp.ms_empresa.model.Empresa;
import br.com.erp.ms_empresa.model.EnderecoEmpresa;
import br.com.erp.ms_empresa.model.Filial;
import br.com.erp.ms_empresa.repository.EmpresaRepository;
import br.com.erp.ms_empresa.repository.EnderecoEmpresaRepository;
import br.com.erp.ms_empresa.repository.FilialRepository;
import br.com.erp.ms_empresa.service.EmpresaService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link EmpresaService}.
 * <p>
 * Contém as regras de negócio e interações com o repositório de Empresa.
 */
@Service
@Transactional
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final EnderecoEmpresaRepository enderecoEmpresaRepository;
    private final FilialRepository filialRepository;
    private final ModelMapper modelMapper;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository,
                              EnderecoEmpresaRepository enderecoEmpresaRepository,
                              FilialRepository filialRepository,
                              ModelMapper modelMapper) {
        this.empresaRepository = empresaRepository;
        this.enderecoEmpresaRepository = enderecoEmpresaRepository;
        this.filialRepository = filialRepository;
        this.modelMapper = modelMapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public EmpresaResponse salvar(EmpresaRequest request,
                                  EnderecoEmpresaRequest enderecoRequest,
                                  FilialRequest filialRequest) {

        // Verifica duplicidade de CNPJ
        if (empresaRepository.existsByCnpj(request.cnpj())) {
            throw new IllegalStateException("Já existe uma empresa cadastrada com este CNPJ.");
        }

        // Cria e salva a empresa
        Empresa empresa = modelMapper.map(request, Empresa.class);
        Empresa salva = empresaRepository.save(empresa);

        // Cria e associa a filial
        Filial filial = modelMapper.map(filialRequest, Filial.class);
        filial.setEmpresa(salva);
        filial.setMatriz(true); // marca como matriz
        filialRepository.save(filial);

        // Cria e associa o endereço da matriz
        EnderecoEmpresa endereco = modelMapper.map(enderecoRequest, EnderecoEmpresa.class);
        endereco.setEmpresa(salva);
        endereco.setTipo(TipoEnderecoEmpresa.MATRIZ);
        enderecoEmpresaRepository.save(endereco);

        return toResponse(salva);
    }

    @Override
    public EmpresaResponse atualizar(Long id, EmpresaRequest request) {
        Empresa existente = empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada."));

        // Verifica duplicidade de CNPJ
        if (!existente.getCnpj().equals(request.cnpj())
                && empresaRepository.existsByCnpj(request.cnpj())) {
            throw new IllegalStateException("Já existe uma empresa cadastrada com este CNPJ.");
        }

        modelMapper.map(request, existente);
        Empresa atualizada = empresaRepository.save(existente);
        return toResponse(atualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmpresaResponse> buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmpresaResponse> buscarPorCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpresaResponse> listarTodas() {
        return empresaRepository.findAllByOrderByRazaoSocialAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpresaResponse> buscarPorRazaoSocial(String razaoSocial) {
        return empresaRepository.findByRazaoSocialContainingIgnoreCase(razaoSocial)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpresaResponse> buscarPorNomeFantasia(String nomeFantasia) {
        return empresaRepository.findByNomeFantasiaContainingIgnoreCase(nomeFantasia)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new EntityNotFoundException("Empresa não encontrada para exclusão.");
        }
        empresaRepository.deleteById(id);
    }

    // ==================================
    // 🧭 MÉTODO AUXILIAR
    // ==================================

    private EmpresaResponse toResponse(Empresa entity) {
        return new EmpresaResponse(
                entity.getId(),
                entity.getRazaoSocial(),
                entity.getNomeFantasia(),
                entity.getCnpj(),
                entity.getInscricaoEstadual(),
                entity.getInscricaoMunicipal(),
                entity.getFiliais() != null ? entity.getFiliais()
                        .stream()
                        .map(f -> new FilialListDTO(
                                f.getId(),
                                f.getRazaoSocial(),
                                f.getNomeFantasia(),
                                f.getCnpj(),
                                f.getCidade(),
                                f.getUf()
                        )).toList() : List.of(),
                entity.getEnderecos() != null ? entity.getEnderecos()
                        .stream()
                        .map(e -> new EnderecoEmpresaListDTO(
                                e.getId(),
                                e.getLogradouro(),
                                e.getNumero(),
                                e.getCidade(),
                                e.getUf(),
                                e.getTipo()
                        )).toList() : List.of(),
                entity.getContatos() != null ? entity.getContatos()
                        .stream()
                        .map(c -> new ContatoEmpresaListDTO(
                                c.getId(),
                                c.getNomeContato(),
                                c.getCargo(),
                                c.getTelefone(),
                                c.getCelular(),
                                c.getEmail()
                        )).toList() : List.of(),
                entity.getDepartamentos() != null ? entity.getDepartamentos()
                        .stream()
                        .map(d -> new DepartamentoEmpresaListDTO(
                                d.getId(),
                                d.getNome(),
                                d.getDescricao(),
                                d.getAtivo()
                        )).toList() : List.of()
        );
    }
}