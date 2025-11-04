package br.com.unicos.ms_empresa.service.impl;

import br.com.unicos.ms_empresa.dto.*;
import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EnderecoEmpresa;
import br.com.unicos.ms_empresa.model.Filial;
import br.com.unicos.ms_empresa.repository.EmpresaRepository;
import br.com.unicos.ms_empresa.repository.EnderecoEmpresaRepository;
import br.com.unicos.ms_empresa.repository.FilialRepository;
import br.com.unicos.ms_empresa.service.EmpresaService;
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

        if (empresaRepository.existsByCnpj(request.cnpj())) {
            throw new IllegalStateException("Já existe uma empresa cadastrada com este CNPJ.");
        }

        // Salva empresa principal
        Empresa empresa = modelMapper.map(request, Empresa.class);
        Empresa salva = empresaRepository.save(empresa);

        // Cria e associa a filial matriz
        Filial filial = modelMapper.map(filialRequest, Filial.class);
        filial.setEmpresa(salva);
        Filial filialSalva = filialRepository.save(filial);

        // Cria e associa o endereço principal
        EnderecoEmpresa endereco = modelMapper.map(enderecoRequest, EnderecoEmpresa.class);
        endereco.setEmpresa(salva);
        endereco.setFilial(filialSalva);
        endereco.setTipoEndereco(TipoEnderecoEmpresa.MATRIZ);
        enderecoEmpresaRepository.save(endereco);

        return toResponse(salva);
    }

    @Override
    public EmpresaResponse atualizar(Long id, EmpresaRequest request) {
        Empresa existente = empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada."));

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
        return empresaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpresaResponse> listarOrdenadasPorRazaoSocial() {
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
    @Transactional(readOnly = true)
    public List<EmpresaResponse> listarComInscricaoEstadual() {
        return empresaRepository.findAll().stream()
                .filter(e -> e.getInscricaoEstadual() != null && !e.getInscricaoEstadual().isBlank())
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpresaResponse> listarComInscricaoMunicipal() {
        return empresaRepository.findAll().stream()
                .filter(e -> e.getInscricaoMunicipal() != null && !e.getInscricaoMunicipal().isBlank())
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
    // MÉTODO AUXILIAR
    // ==================================

    private EmpresaResponse toResponse(Empresa entity) {

        // 🔹 Endereços
        List<EnderecoEmpresaResponse> enderecoEmpresaResponses = entity.getEnderecos() != null
                ? entity.getEnderecos().stream()
                .map(e -> new EnderecoEmpresaResponse(
                        e.getId(),
                        e.getLogradouro(),
                        e.getNumero(),
                        e.getComplemento(),
                        e.getBairro(),
                        e.getCidade(),
                        e.getEstado(),
                        e.getCep(),
                        e.getTipoEndereco()
                ))
                .toList()
                : List.of();

        // 🔹 Departamentos
        List<DepartamentoResponse> departamentoResponses = entity.getDepartamentos() != null
                ? entity.getDepartamentos().stream()
                .map(d -> new DepartamentoResponse(
                        d.getId(),
                        d.getNome(),
                        d.getAtivo(),
                        new EmpresaListDTO(
                                entity.getId(),
                                entity.getNomeFantasia(),
                                entity.getCnpj()
                        ),
                        d.getSetores() != null
                                ? d.getSetores().stream()
                                .map(s -> new SetorResponse(
                                        s.getId(),
                                        s.getNome(),
                                        s.getAtivo()
                                ))
                                .toList()
                                : List.of()
                ))
                .toList()
                : List.of();

        // 🔹 Contatos
        List<ContatoEmpresaResponse> contatoEmpresaResponses = entity.getContatos() != null
                ? entity.getContatos().stream()
                .map(c -> new ContatoEmpresaResponse(
                        c.getId(),
                        c.getTelefone(),
                        c.getEmail(),
                        c.getAtivo()
                ))
                .toList()
                : List.of();

        // 🔹 Configuração Fiscal
        ConfiguracaoFiscalResponse configuracaoFiscalResponse = entity.getConfiguracaoFiscal() != null
                ? new ConfiguracaoFiscalResponse(
                entity.getConfiguracaoFiscal().getId(),
                entity.getConfiguracaoFiscal().getRegimeTributario(),
                entity.getConfiguracaoFiscal().getCertificadoDigital(),
                entity.getConfiguracaoFiscal().getTipoAmbiente(),
                entity.getConfiguracaoFiscal().getAtivo()
        )
                : null;

        // 🔹 Filiais
        List<FilialResponse> filialResponses = entity.getFiliais() != null
                ? entity.getFiliais().stream()
                .map(f -> new FilialResponse(
                        f.getId(),
                        f.getNome(),
                        f.getCnpj(),
                        f.getAtivo(),
                        new EmpresaListDTO(
                                entity.getId(),
                                entity.getNomeFantasia(),
                                entity.getCnpj()
                        ),
                        f.getEndereco() != null
                                ? new EnderecoEmpresaResponse(
                                f.getEndereco().getId(),
                                f.getEndereco().getLogradouro(),
                                f.getEndereco().getNumero(),
                                f.getEndereco().getComplemento(),
                                f.getEndereco().getBairro(),
                                f.getEndereco().getCidade(),
                                f.getEndereco().getEstado(),
                                f.getEndereco().getCep(),
                                f.getEndereco().getTipoEndereco()
                        )
                                : null
                ))
                .toList()
                : List.of();

        // 🔹 Retorno final
        return new EmpresaResponse(
                entity.getId(),
                entity.getRazaoSocial(),
                entity.getNomeFantasia(),
                entity.getCnpj(),
                entity.getInscricaoEstadual(),
                entity.getInscricaoMunicipal(),
                filialResponses,
                enderecoEmpresaResponses,
                departamentoResponses,
                contatoEmpresaResponses,
                configuracaoFiscalResponse
        );
    }
}
