package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoListDTO;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoRequest;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoResponse;
import br.com.unicos.ms_produtos.mapper.AtributoPersonalizadoMapper;
import br.com.unicos.ms_produtos.model.AtributoPersonalizado;
import br.com.unicos.ms_produtos.model.Categoria;
import br.com.unicos.ms_produtos.repository.AtributoPersonalizadoRepository;
import br.com.unicos.ms_produtos.repository.CategoriaRepository;
import br.com.unicos.ms_produtos.service.AtributoPersonalizadoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelo gerenciamento de atributos personalizados
 * associados às categorias de produtos.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AtributoPersonalizadoServiceImpl implements AtributoPersonalizadoService {

    private final AtributoPersonalizadoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final AtributoPersonalizadoMapper mapper;
    private final ModelMapper modelMapper;

    // ============================================================
    // Criar
    // ============================================================

    @Override
    public AtributoPersonalizadoResponse criar(AtributoPersonalizadoRequest request) {

        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        AtributoPersonalizado atributo = new AtributoPersonalizado();
        atributo.setNome(request.nome());
        atributo.setCategoria(categoria);

        repository.save(atributo);

        return mapper.toResponse(atributo);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @Override
    public AtributoPersonalizadoResponse atualizar(Long id, AtributoPersonalizadoRequest request) {

        AtributoPersonalizado atributo = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atributo não encontrado"));

        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        modelMapper.map(request, atributo); // atualiza campos simples
        atributo.setCategoria(categoria);   // mapeamento manual necessário

        repository.save(atributo);

        return mapper.toResponse(atributo);
    }

    // ============================================================
    // Excluir
    // ============================================================

    @Override
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Atributo não encontrado");
        }
        repository.deleteById(id);
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<AtributoPersonalizadoResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<AtributoPersonalizadoListDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar Por Categoria
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<AtributoPersonalizadoListDTO> listarPorCategoria(Long categoriaId) {
        return repository.findByCategoriaId(categoriaId)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }
}
