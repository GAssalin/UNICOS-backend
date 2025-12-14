package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.categoria.CategoriaListDTO;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaRequest;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaResponse;
import br.com.unicos.ms_produtos.mapper.CategoriaMapper;
import br.com.unicos.ms_produtos.model.Categoria;
import br.com.unicos.ms_produtos.repository.CategoriaRepository;
import br.com.unicos.ms_produtos.service.interfaces.CategoriaService;
import br.com.unicos.ms_produtos.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela gestão de categorias de produtos.
 *
 * <p>
 * Todas as operações são restritas ao contexto da empresa (tenant),
 * obtido automaticamente através do {@link TenantContext}.
 * </p>
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

        Long empresaId = TenantContext.getEmpresaId();

        Categoria categoriaPai = carregarCategoriaPai(empresaId, request.categoriaPaiId());

        Categoria categoria = new Categoria();
        categoria.setEmpresaId(empresaId);
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

        Long empresaId = TenantContext.getEmpresaId();

        Categoria categoria = repository.findByEmpresaIdAndId(empresaId, id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        validarHierarquia(empresaId, id, request.categoriaPaiId());

        Categoria novaCategoriaPai = carregarCategoriaPai(empresaId, request.categoriaPaiId());

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

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaIdAndId(empresaId, id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // LISTAR TODAS (DETALHADO)
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarTodas() {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaId(empresaId)
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

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaId(empresaId)
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

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaIdAndNomeContainingIgnoreCase(empresaId, nome)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ============================================================
    // DELETAR
    // ============================================================

    @Override
    public void deletar(Long id) {

        Long empresaId = TenantContext.getEmpresaId();

        Categoria categoria = repository.findByEmpresaIdAndId(empresaId, id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        repository.delete(categoria);
    }

    // ============================================================
    // EXISTE POR NOME
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNome(String nome) {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.existsByEmpresaIdAndNomeIgnoreCase(empresaId, nome);
    }

    // ============================================================
    // MÉTODOS DE APOIO (TENANT-AWARE)
    // ============================================================

    private Categoria carregarCategoriaPai(Long empresaId, Long categoriaPaiId) {

        if (categoriaPaiId == null) {
            return null;
        }

        return repository.findByEmpresaIdAndId(empresaId, categoriaPaiId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria pai não encontrada"));
    }

    private void validarHierarquia(Long empresaId, Long categoriaId, Long categoriaPaiId) {

        if (categoriaPaiId == null) return;

        if (categoriaId.equals(categoriaPaiId)) {
            throw new IllegalArgumentException("Uma categoria não pode ser pai dela mesma.");
        }

        Categoria pai = repository.findByEmpresaIdAndId(empresaId, categoriaPaiId)
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
