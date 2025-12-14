package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoListDTO;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoRequest;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoResponse;
import br.com.unicos.ms_produtos.mapper.AtributoPersonalizadoMapper;
import br.com.unicos.ms_produtos.model.AtributoPersonalizado;
import br.com.unicos.ms_produtos.model.Categoria;
import br.com.unicos.ms_produtos.repository.AtributoPersonalizadoRepository;
import br.com.unicos.ms_produtos.repository.CategoriaRepository;
import br.com.unicos.ms_produtos.service.interfaces.AtributoPersonalizadoService;
import br.com.unicos.ms_produtos.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação do serviço responsável pelo gerenciamento
 * de atributos personalizados associados às categorias de produtos.
 *
 * <p>
 * Todas as operações são executadas respeitando o contexto
 * da empresa (tenant).
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AtributoPersonalizadoServiceImpl implements AtributoPersonalizadoService {

    private final AtributoPersonalizadoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final AtributoPersonalizadoMapper mapper;

    // ============================================================
    // 🔹 Criar
    // ============================================================

    @Override
    public AtributoPersonalizadoResponse criar(AtributoPersonalizadoRequest request) {

        Long empresaId = TenantContext.getEmpresaId();

        Categoria categoria = categoriaRepository
                .findById(request.categoriaId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Categoria não encontrada.")
                );

        boolean existe = repository.existsByEmpresaIdAndCategoriaIdAndNomeIgnoreCase(
                empresaId,
                categoria.getId(),
                request.nome()
        );

        if (existe) {
            throw new IllegalArgumentException(
                    "Já existe um atributo com este nome para esta categoria."
            );
        }

        AtributoPersonalizado atributo = AtributoPersonalizado.builder()
                .empresaId(empresaId)
                .nome(request.nome())
                .categoria(categoria)
                .build();

        repository.save(atributo);

        return mapper.toResponse(atributo);
    }

    // ============================================================
    // 🔹 Atualizar
    // ============================================================

    @Override
    public AtributoPersonalizadoResponse atualizar(Long id, AtributoPersonalizadoRequest request) {

        Long empresaId = TenantContext.getEmpresaId();

        AtributoPersonalizado atributo = repository.findById(id)
                .filter(a -> a.getEmpresaId().equals(empresaId))
                .orElseThrow(() ->
                        new EntityNotFoundException("Atributo personalizado não encontrado.")
                );

        Categoria categoria = categoriaRepository
                .findById(request.categoriaId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Categoria não encontrada.")
                );

        boolean existe = repository.existsByEmpresaIdAndCategoriaIdAndNomeIgnoreCase(
                empresaId,
                categoria.getId(),
                request.nome()
        );

        if (existe && !atributo.getNome().equalsIgnoreCase(request.nome())) {
            throw new IllegalArgumentException(
                    "Já existe outro atributo com este nome para esta categoria."
            );
        }

        atributo.setNome(request.nome());
        atributo.setCategoria(categoria);

        repository.save(atributo);

        return mapper.toResponse(atributo);
    }

    // ============================================================
    // 🔹 Excluir
    // ============================================================

    @Override
    public void excluir(Long id) {

        Long empresaId = TenantContext.getEmpresaId();

        AtributoPersonalizado atributo = repository.findById(id)
                .filter(a -> a.getEmpresaId().equals(empresaId))
                .orElseThrow(() ->
                        new EntityNotFoundException("Atributo personalizado não encontrado.")
                );

        repository.delete(atributo);
    }

    // ============================================================
    // 🔹 Buscar por ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<AtributoPersonalizadoResponse> buscarPorId(Long id) {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findById(id)
                .filter(a -> a.getEmpresaId().equals(empresaId))
                .map(mapper::toResponse);
    }

    // ============================================================
    // 🔹 Listar Todos (por empresa)
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<AtributoPersonalizadoListDTO> listarTodos() {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaIdOrderByNomeAsc(empresaId)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // 🔹 Listar por Categoria
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<AtributoPersonalizadoListDTO> listarPorCategoria(Long categoriaId) {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaIdAndCategoriaId(empresaId, categoriaId)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }
}
