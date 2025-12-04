package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.categoria.CategoriaListDTO;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaRequest;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaResponse;
import br.com.unicos.ms_produtos.mapper.CategoriaMapper;
import br.com.unicos.ms_produtos.model.Categoria;
import br.com.unicos.ms_produtos.repository.CategoriaRepository;
import br.com.unicos.ms_produtos.service.CategoriaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela gestão de categorias,
 * com suporte a hierarquia e integridade estrutural.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository repository;
    private final CategoriaMapper mapper;
    private final ModelMapper modelMapper;

    // ============================================================
    // SALVAR
    // ============================================================

    @Override
    public CategoriaResponse salvar(CategoriaRequest request) {

        Categoria categoriaPai = carregarCategoriaPai(request.categoriaPaiId());

        Categoria categoria = new Categoria();
        categoria.setNome(request.nome());
        categoria.setDescricao(request.descricao());
        categoria.setCategoriaPai(categoriaPai);
        categoria.setAtivo(true);

        repository.save(categoria);

        return mapper.toResponse(categoria);
    }

    // ============================================================
    // ATUALIZAR
    // ============================================================

    @Override
    public CategoriaResponse atualizar(Long id, CategoriaRequest request) {

        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        validarHierarquia(id, request.categoriaPaiId());
        Categoria novaCategoriaPai = carregarCategoriaPai(request.categoriaPaiId());

        // Atualiza campos simples via ModelMapper
        modelMapper.map(request, categoria);

        categoria.setCategoriaPai(novaCategoriaPai);

        repository.save(categoria);

        return mapper.toResponse(categoria);
    }

    // ============================================================
    // BUSCAR POR ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // LISTAR TODAS (DETALHADO)
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarTodas() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ============================================================
    // LISTAR SIMPLES
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaListDTO> listarSimples() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // BUSCAR POR NOME
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ============================================================
    // DELETAR
    // ============================================================

    @Override
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Categoria não encontrada");
        }
        repository.deleteById(id);
    }

    // ============================================================
    // EXISTE POR NOME
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNome(String nome) {
        return repository.existsByNomeIgnoreCase(nome);
    }

    // ============================================================
    // MÉTODOS DE APOIO
    // ============================================================

    private Categoria carregarCategoriaPai(Long categoriaPaiId) {
        if (categoriaPaiId == null) {
            return null;
        }
        return repository.findById(categoriaPaiId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria pai não encontrada"));
    }

    private void validarHierarquia(Long categoriaId, Long categoriaPaiId) {

        if (categoriaPaiId == null) return;

        if (categoriaId.equals(categoriaPaiId)) {
            throw new IllegalArgumentException("Uma categoria não pode ser pai dela mesma.");
        }

        Categoria pai = repository.findById(categoriaPaiId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria pai não encontrada"));

        while (pai.getCategoriaPai() != null) {
            if (pai.getCategoriaPai().getId().equals(categoriaId)) {
                throw new IllegalArgumentException(
                        "Hierarquia inválida: o pai está dentro da árvore da própria categoria."
                );
            }
            pai = pai.getCategoriaPai();
        }
    }
}
