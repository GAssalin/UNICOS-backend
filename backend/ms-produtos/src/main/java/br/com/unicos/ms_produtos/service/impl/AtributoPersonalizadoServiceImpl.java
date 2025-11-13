package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoListDTO;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoRequest;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoResponse;
import br.com.unicos.ms_produtos.model.AtributoPersonalizado;
import br.com.unicos.ms_produtos.model.Categoria;
import br.com.unicos.ms_produtos.repository.AtributoPersonalizadoRepository;
import br.com.unicos.ms_produtos.repository.CategoriaRepository;
import br.com.unicos.ms_produtos.service.AtributoPersonalizadoService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link AtributoPersonalizadoService}.
 * <p>
 * Responsável pela lógica de negócio e persistência dos atributos
 * configuráveis de categorias de produtos (ex: "Cor", "Tamanho").
 */
@Service
@Transactional
public class AtributoPersonalizadoServiceImpl implements AtributoPersonalizadoService {

    private final AtributoPersonalizadoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final ModelMapper mapper;

    public AtributoPersonalizadoServiceImpl(AtributoPersonalizadoRepository repository,
                                            CategoriaRepository categoriaRepository,
                                            ModelMapper mapper) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public AtributoPersonalizadoResponse salvar(AtributoPersonalizadoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com ID: " + request.categoriaId()));

        boolean existeDuplicado = repository.findByCategoriaId(categoria.getId())
                .stream()
                .anyMatch(attr -> attr.getNome().equalsIgnoreCase(request.nome()));

        if (existeDuplicado) {
            throw new IllegalArgumentException("Já existe um atributo com este nome para a categoria informada.");
        }

        AtributoPersonalizado entity = mapper.map(request, AtributoPersonalizado.class);
        entity.setCategoria(categoria);

        AtributoPersonalizado salvo = repository.save(entity);
        return toResponse(salvo);
    }

    @Override
    public AtributoPersonalizadoResponse atualizar(Long id, AtributoPersonalizadoRequest request) {
        AtributoPersonalizado existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Atributo personalizado não encontrado com ID: " + id));

        boolean nomeDuplicado = repository.findByCategoriaId(existente.getCategoria().getId())
                .stream()
                .anyMatch(attr -> !attr.getId().equals(id) &&
                        attr.getNome().equalsIgnoreCase(request.nome()));

        if (nomeDuplicado) {
            throw new IllegalArgumentException("Já existe outro atributo com este nome para a mesma categoria.");
        }

        existente.setNome(request.nome());
        AtributoPersonalizado atualizado = repository.save(existente);
        return toResponse(atualizado);
    }

    @Override
    public Optional<AtributoPersonalizadoResponse> buscarPorId(Long id) {
        return repository.findById(id).map(this::toResponse);
    }

    @Override
    public List<AtributoPersonalizadoResponse> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Atributo personalizado não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    // ==================================
    // 🔹 MÉTODOS ESPECÍFICOS
    // ==================================

    @Override
    public List<AtributoPersonalizadoResponse> listarPorCategoria(Long categoriaId) {
        return repository.findByCategoriaId(categoriaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<AtributoPersonalizadoListDTO> buscarPorNomeContendo(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(entity -> new AtributoPersonalizadoListDTO(
                        entity.getId(),
                        entity.getNome(),
                        entity.getCategoria() != null ? entity.getCategoria().getNome() : null
                ))
                .toList();
    }

    // ==================================
    // MÉTODO AUXILIAR
    // ==================================

    private AtributoPersonalizadoResponse toResponse(AtributoPersonalizado entity) {
        return new AtributoPersonalizadoResponse(
                entity.getId(),
                entity.getCategoria() != null ? entity.getCategoria().getId() : null,
                entity.getCategoria() != null ? entity.getCategoria().getNome() : null,
                entity.getNome()
        );
    }
}
