package br.com.erp.ms_produtos.service.impl;

import br.com.erp.ms_produtos.dto.MarcaListDTO;
import br.com.erp.ms_produtos.dto.MarcaRequest;
import br.com.erp.ms_produtos.dto.MarcaResponse;
import br.com.erp.ms_produtos.model.Marca;
import br.com.erp.ms_produtos.repository.MarcaRepository;
import br.com.erp.ms_produtos.service.MarcaService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface MarcaService.
 * Responsável pela lógica de negócio e orquestração das operações
 * de criação, atualização, exclusão e consulta de marcas.
 */
@Service
@Transactional
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository repository;
    private final ModelMapper mapper;

    public MarcaServiceImpl(MarcaRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public MarcaResponse salvar(MarcaRequest request) {
        if (repository.existsByNomeIgnoreCase(request.getNome())) {
            throw new IllegalArgumentException("Já existe uma marca com o nome informado.");
        }

        Marca marca = mapper.map(request, Marca.class);
        Marca salva = repository.save(marca);
        return toResponse(salva);
    }

    @Override
    public MarcaResponse atualizar(Long id, MarcaRequest request) {
        Marca existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Marca não encontrada com ID: " + id));

        Optional<Marca> duplicada = repository.findByNomeIgnoreCase(request.getNome());
        if (duplicada.isPresent() && !duplicada.get().getId().equals(id)) {
            throw new IllegalArgumentException("Já existe uma marca com esse nome.");
        }

        existente.setNome(request.getNome());
        Marca atualizada = repository.save(existente);
        return toResponse(atualizada);
    }

    @Override
    public Optional<MarcaResponse> buscarPorId(Long id) {
        return repository.findById(id).map(this::toResponse);
    }

    @Override
    public List<MarcaResponse> listarTodas() {
        return repository.findAllByOrderByNomeAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<MarcaListDTO> listarSimples() {
        return repository.findAllByOrderByNomeAsc()
                .stream()
                .map(m -> MarcaListDTO.builder()
                        .id(m.getId())
                        .nome(m.getNome())
                        .build())
                .toList();
    }

    @Override
    public List<MarcaResponse> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        Marca marca = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Marca não encontrada com ID: " + id));

        if (marca.getProdutos() != null && !marca.getProdutos().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir uma marca com produtos associados.");
        }

        repository.deleteById(id);
    }

    @Override
    public boolean existePorNome(String nome) {
        return repository.existsByNomeIgnoreCase(nome);
    }

    // ==================================
    // 🧭 MÉTODOS AUXILIARES
    // ==================================

    private MarcaResponse toResponse(Marca marca) {
        int qtdProdutos = (marca.getProdutos() != null) ? marca.getProdutos().size() : 0;

        return MarcaResponse.builder()
                .id(marca.getId())
                .nome(marca.getNome())
                .quantidadeProdutos(qtdProdutos)
                .build();
    }
}