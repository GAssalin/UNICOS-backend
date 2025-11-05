package br.com.unicos.ms_ativos.service.impl;

import br.com.unicos.ms_ativos.dto.FornecedorManutencaoListDTO;
import br.com.unicos.ms_ativos.dto.FornecedorManutencaoRequest;
import br.com.unicos.ms_ativos.dto.FornecedorManutencaoResponse;
import br.com.unicos.ms_ativos.model.FornecedorManutencao;
import br.com.unicos.ms_ativos.repository.FornecedorManutencaoRepository;
import br.com.unicos.ms_ativos.service.FornecedorManutencaoService;
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
 * Implementação da interface {@link FornecedorManutencaoService}.
 * <p>
 * Contém as regras de negócio relacionadas aos fornecedores e prestadores de serviço
 * responsáveis por manutenções de ativos patrimoniais.
 */
@Service
@RequiredArgsConstructor
public class FornecedorManutencaoServiceImpl implements FornecedorManutencaoService {

    private final FornecedorManutencaoRepository fornecedorRepository;
    private final ModelMapper modelMapper;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public FornecedorManutencaoResponse salvar(FornecedorManutencaoRequest request) {
        // Impede duplicidade de CNPJ
        if (fornecedorRepository.existsByCnpj(request.cnpj())) {
            throw new DataIntegrityViolationException(
                    "Já existe um fornecedor cadastrado com o CNPJ informado: " + request.cnpj()
            );
        }

        FornecedorManutencao fornecedor = modelMapper.map(request, FornecedorManutencao.class);
        FornecedorManutencao salvo = fornecedorRepository.save(fornecedor);
        return modelMapper.map(salvo, FornecedorManutencaoResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public FornecedorManutencaoResponse atualizar(Long id, FornecedorManutencaoRequest request) {
        FornecedorManutencao existente = fornecedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado com ID: " + id));

        existente.setNome(request.nome());
        existente.setCnpj(request.cnpj());
        existente.setTelefone(request.telefone());
        existente.setEmail(request.email());
        existente.setResponsavel(request.responsavel());

        FornecedorManutencao atualizado = fornecedorRepository.save(existente);
        return modelMapper.map(atualizado, FornecedorManutencaoResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        FornecedorManutencao fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado com ID: " + id));
        fornecedorRepository.delete(fornecedor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<FornecedorManutencaoResponse> buscarPorId(Long id) {
        return fornecedorRepository.findById(id)
                .map(entity -> modelMapper.map(entity, FornecedorManutencaoResponse.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FornecedorManutencaoListDTO> listarTodos() {
        return fornecedorRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, FornecedorManutencaoListDTO.class))
                .collect(Collectors.toList());
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FornecedorManutencaoListDTO> buscarPorNome(String nome) {
        return fornecedorRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(entity -> modelMapper.map(entity, FornecedorManutencaoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<FornecedorManutencaoResponse> buscarPorCnpj(String cnpj) {
        return fornecedorRepository.findByCnpj(cnpj)
                .map(entity -> modelMapper.map(entity, FornecedorManutencaoResponse.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existePorCnpj(String cnpj) {
        return fornecedorRepository.existsByCnpj(cnpj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FornecedorManutencaoListDTO> buscarPorEmail(String email) {
        return fornecedorRepository.findByEmailContainingIgnoreCase(email)
                .stream()
                .map(entity -> modelMapper.map(entity, FornecedorManutencaoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FornecedorManutencaoListDTO> buscarPorTelefone(String telefone) {
        return fornecedorRepository.findByTelefoneContainingIgnoreCase(telefone)
                .stream()
                .map(entity -> modelMapper.map(entity, FornecedorManutencaoListDTO.class))
                .collect(Collectors.toList());
    }

    // ===========================================================
    // 📊 RELATÓRIOS E INDICADORES
    // ===========================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FornecedorManutencaoListDTO> buscarFornecedoresComManutencoes() {
        return fornecedorRepository.findComManutencoes()
                .stream()
                .map(entity -> modelMapper.map(entity, FornecedorManutencaoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FornecedorManutencaoListDTO> buscarFornecedoresSemManutencoes() {
        return fornecedorRepository.findSemManutencoes()
                .stream()
                .map(entity -> modelMapper.map(entity, FornecedorManutencaoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FornecedorManutencaoListDTO> buscarFornecedoresMaisAtivos() {
        return fornecedorRepository.findMaisAtivos()
                .stream()
                .map(entity -> modelMapper.map(entity, FornecedorManutencaoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FornecedorManutencaoListDTO> buscarComMaisDe(int quantidade) {
        return fornecedorRepository.findComMaisDe(quantidade)
                .stream()
                .map(entity -> modelMapper.map(entity, FornecedorManutencaoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FornecedorManutencaoListDTO> buscarComEmailInvalido() {
        return fornecedorRepository.findComEmailInvalido()
                .stream()
                .map(entity -> modelMapper.map(entity, FornecedorManutencaoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FornecedorManutencaoListDTO> buscarSemTelefone() {
        return fornecedorRepository.findSemTelefone()
                .stream()
                .map(entity -> modelMapper.map(entity, FornecedorManutencaoListDTO.class))
                .collect(Collectors.toList());
    }
}
