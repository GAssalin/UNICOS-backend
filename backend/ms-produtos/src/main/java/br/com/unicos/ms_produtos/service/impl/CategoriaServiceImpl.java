package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.categoria.CategoriaListDTO;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaRequest;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaResponse;
import br.com.unicos.ms_produtos.model.Categoria;
import br.com.unicos.ms_produtos.repository.CategoriaRepository;
import br.com.unicos.ms_produtos.service.CategoriaService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface CategoriaService.
 * Responsável pela lógica de negócio e orquestração das operações
 * de criação, atualização, exclusão e consulta de categorias.
 */
@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository repository;
    private final ModelMapper mapper;

    public CategoriaServiceImpl(CategoriaRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public CategoriaResponse salvar(CategoriaRequest request) {
        // Verifica duplicidade de nome
        if (repository.existsByNomeIgnoreCase(request.nome())) {
            throw new IllegalArgumentException("Já existe uma categoria com o nome informado.");
        }

        Categoria categoria = mapper.map(request, Categoria.class);
        Categoria salva = repository.save(categoria);
        return toResponse(salva);
    }

    @Override
    public CategoriaResponse atualizar(Long id, CategoriaRequest request) {
        Categoria existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com ID: " + id));

        // Evita duplicidade ao atualizar
        Optional<Categoria> categoriaDuplicada = repository.findByNomeIgnoreCase(request.nome());
        if (categoriaDuplicada.isPresent() && !categoriaDuplicada.get().getId().equals(id)) {
            throw new IllegalArgumentException("Já existe uma categoria com esse nome.");
        }

        existente.setNome(request.nome());
        existente.setDescricao(request.descricao());

        Categoria atualizada = repository.save(existente);
        return toResponse(atualizada);
    }

    @Override
    public Optional<CategoriaResponse> buscarPorId(Long id) {
        return repository.findById(id).map(this::toResponse);
    }

    @Override
    public List<CategoriaResponse> listarTodas() {
        return repository.findAllByOrderByNomeAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<CategoriaListDTO> listarSimples() {
        return repository.findAllByOrderByNomeAsc()
                .stream()
                .map(c -> new CategoriaListDTO(c.getId(), c.getNome()))
                .toList();
    }

    @Override
    public List<CategoriaResponse> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com ID: " + id));

        // Se houver produtos associados, poderia lançar uma exceção de regra de negócio
        if (categoria.getProdutos() != null && !categoria.getProdutos().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir uma categoria com produtos associados.");
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

    private CategoriaResponse toResponse(Categoria categoria) {
        return mapper.map(categoria, CategoriaResponse.class);
    }
}