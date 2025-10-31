package br.com.erp.ms_empresa.service.impl;

import br.com.erp.ms_empresa.dto.EmpresaRequest;
import br.com.erp.ms_empresa.dto.EmpresaResponse;
import br.com.erp.ms_empresa.dto.EnderecoEmpresaRequest;
import br.com.erp.ms_empresa.dto.FilialRequest;
import br.com.erp.ms_empresa.model.Empresa;
import br.com.erp.ms_empresa.model.EnderecoEmpresa;
import br.com.erp.ms_empresa.model.Filial;
import br.com.erp.ms_empresa.model.TipoEnderecoEmpresa;
import br.com.erp.ms_empresa.repository.EmpresaRepository;
import br.com.erp.ms_empresa.repository.EnderecoEmpresaRepository;
import br.com.erp.ms_empresa.repository.FilialRepository;
import br.com.erp.ms_empresa.service.EmpresaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link EmpresaService}.
 *
 * Contém as regras de negócio e interações com o repositório de Empresa.
 */
@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final EnderecoEmpresaRepository enderecoEmpresaRepository;
    private final FilialRepository filialRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public EmpresaResponse salvar(EmpresaRequest request, EnderecoEmpresaRequest enderecoRequest, FilialRequest filialRequest) {
        // Verifica duplicidade de CNPJ
        if (empresaRepository.existsByCnpj(request.getCnpj())) {
            throw new IllegalStateException("Já existe uma empresa cadastrada com este CNPJ.");
        }

        Empresa entity = modelMapper.map(request, Empresa.class);
        Empresa salva = empresaRepository.save(entity);

        Filial filial = modelMapper.map(filialRequest, Filial.class);
        filial.setEmpresa(salva);

        // marca a filial como matriz
        filial.setMatriz(true);
        filialRepository.save(filial);

        EnderecoEmpresa endereco = modelMapper.map(enderecoRequest, EnderecoEmpresa.class);
        endereco.setEmpresa(salva);
        endereco.setTipo(TipoEnderecoEmpresa.MATRIZ); // Define tipo fixo MATRIZ

        enderecoEmpresaRepository.save(endereco);

        return modelMapper.map(salva, EmpresaResponse.class);
    }

    @Override
    @Transactional
    public EmpresaResponse atualizar(Long id, EmpresaRequest request) {
        Empresa existente = empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada."));

        // Se o CNPJ foi alterado, valida duplicidade
        if (!existente.getCnpj().equals(request.getCnpj())
                && empresaRepository.existsByCnpj(request.getCnpj())) {
            throw new IllegalStateException("Já existe uma empresa cadastrada com este CNPJ.");
        }

        modelMapper.map(request, existente);
        Empresa atualizada = empresaRepository.save(existente);
        return modelMapper.map(atualizada, EmpresaResponse.class);
    }

    @Override
    public Optional<EmpresaResponse> buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .map(entity -> modelMapper.map(entity, EmpresaResponse.class));
    }

    @Override
    public Optional<EmpresaResponse> buscarPorCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj)
                .map(entity -> modelMapper.map(entity, EmpresaResponse.class));
    }

    @Override
    public List<EmpresaResponse> listarTodas() {
        return empresaRepository.findAllByOrderByRazaoSocialAsc()
                .stream()
                .map(entity -> modelMapper.map(entity, EmpresaResponse.class))
                .toList();
    }

    @Override
    public List<EmpresaResponse> buscarPorRazaoSocial(String razaoSocial) {
        return empresaRepository.findByRazaoSocialContainingIgnoreCase(razaoSocial)
                .stream()
                .map(entity -> modelMapper.map(entity, EmpresaResponse.class))
                .toList();
    }

    @Override
    public List<EmpresaResponse> buscarPorNomeFantasia(String nomeFantasia) {
        return empresaRepository.findByNomeFantasiaContainingIgnoreCase(nomeFantasia)
                .stream()
                .map(entity -> modelMapper.map(entity, EmpresaResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new EntityNotFoundException("Empresa não encontrada para exclusão.");
        }
        empresaRepository.deleteById(id);
    }
}