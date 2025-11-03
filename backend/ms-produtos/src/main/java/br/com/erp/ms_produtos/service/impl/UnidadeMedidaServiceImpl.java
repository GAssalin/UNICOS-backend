package br.com.erp.ms_produtos.service.impl;

import br.com.erp.ms_produtos.dto.UnidadeMedidaListDTO;
import br.com.erp.ms_produtos.dto.UnidadeMedidaRequest;
import br.com.erp.ms_produtos.dto.UnidadeMedidaResponse;
import br.com.erp.ms_produtos.model.UnidadeMedida;
import br.com.erp.ms_produtos.repository.UnidadeMedidaRepository;
import br.com.erp.ms_produtos.service.UnidadeMedidaService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface UnidadeMedidaService.
 *
 * Responsável pela lógica de negócio e operações relacionadas
 * ao gerenciamento das unidades de medida.
 */
@Service
@Transactional
public class UnidadeMedidaServiceImpl implements UnidadeMedidaService {

    private final UnidadeMedidaRepository repository;
    private final ModelMapper mapper;

    public UnidadeMedidaServiceImpl(UnidadeMedidaRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public UnidadeMedidaResponse salvar(UnidadeMedidaRequest request) {
        if (repository.existsBySiglaIgnoreCase(request.getSigla())) {
            throw new IllegalArgumentException("Já existe uma unidade com a sigla informada.");
        }

        UnidadeMedida entity = mapper.map(request, UnidadeMedida.class);
        UnidadeMedida salvo = repository.save(entity);
        return mapper.map(salvo, UnidadeMedidaResponse.class);
    }

    @Override
    public UnidadeMedidaResponse atualizar(Long id, UnidadeMedidaRequest request) {
        UnidadeMedida existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unidade de medida não encontrada com ID: " + id));

        if (!existente.getSigla().equalsIgnoreCase(request.getSigla()) &&
                repository.existsBySiglaIgnoreCase(request.getSigla())) {
            throw new IllegalArgumentException("Já existe uma unidade com a sigla informada.");
        }

        existente.setNome(request.getNome());
        existente.setSigla(request.getSigla());

        UnidadeMedida atualizado = repository.save(existente);
        return mapper.map(atualizado, UnidadeMedidaResponse.class);
    }

    @Override
    public Optional<UnidadeMedidaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entity -> mapper.map(entity, UnidadeMedidaResponse.class));
    }

    @Override
    public List<UnidadeMedidaResponse> listarTodas() {
        return repository.findAllByOrderByNomeAsc()
                .stream()
                .map(entity -> mapper.map(entity, UnidadeMedidaResponse.class))
                .toList();
    }

    @Override
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Unidade de medida não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }

    // ==================================
    // 🔹 MÉTODOS ESPECÍFICOS
    // ==================================

    @Override
    public Optional<UnidadeMedidaResponse> buscarPorNome(String nome) {
        return repository.findByNomeIgnoreCase(nome)
                .map(entity -> mapper.map(entity, UnidadeMedidaResponse.class));
    }

    @Override
    public Optional<UnidadeMedidaResponse> buscarPorSigla(String sigla) {
        return repository.findBySiglaIgnoreCase(sigla)
                .map(entity -> mapper.map(entity, UnidadeMedidaResponse.class));
    }

    @Override
    public List<UnidadeMedidaListDTO> buscarPorNomeContendo(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(entity -> mapper.map(entity, UnidadeMedidaListDTO.class))
                .toList();
    }

    @Override
    public List<UnidadeMedidaListDTO> listarSimples() {
        return repository.findAllByOrderByNomeAsc()
                .stream()
                .map(entity -> mapper.map(entity, UnidadeMedidaListDTO.class))
                .toList();
    }

    @Override
    public boolean verificarSiglaExistente(String sigla) {
        return repository.existsBySiglaIgnoreCase(sigla);
    }
}