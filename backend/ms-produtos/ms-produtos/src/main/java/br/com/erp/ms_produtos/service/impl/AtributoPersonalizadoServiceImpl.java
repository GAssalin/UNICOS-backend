package br.com.erp.ms_produtos.service.impl;

import br.com.erp.ms_produtos.dto.AtributoPersonalizadoListDTO;
import br.com.erp.ms_produtos.dto.AtributoPersonalizadoRequest;
import br.com.erp.ms_produtos.dto.AtributoPersonalizadoResponse;
import br.com.erp.ms_produtos.model.AtributoPersonalizado;
import br.com.erp.ms_produtos.model.Produto;
import br.com.erp.ms_produtos.repository.AtributoPersonalizadoRepository;
import br.com.erp.ms_produtos.repository.ProdutoRepository;
import br.com.erp.ms_produtos.service.AtributoPersonalizadoService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface AtributoPersonalizadoService.
 *
 * Responsável pela lógica de negócio e persistência dos atributos personalizados dos produtos.
 */
@Service
@Transactional
public class AtributoPersonalizadoServiceImpl implements AtributoPersonalizadoService {

    private final AtributoPersonalizadoRepository repository;
    private final ProdutoRepository produtoRepository;
    private final ModelMapper mapper;

    public AtributoPersonalizadoServiceImpl(AtributoPersonalizadoRepository repository,
                                            ProdutoRepository produtoRepository,
                                            ModelMapper mapper) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.mapper = mapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public AtributoPersonalizadoResponse salvar(AtributoPersonalizadoRequest request) {
        Produto produto = produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com ID: " + request.getProdutoId()));

        if (repository.existsByProdutoIdAndNomeIgnoreCase(produto.getId(), request.getNome())) {
            throw new IllegalArgumentException("Já existe um atributo com este nome para o produto informado.");
        }

        AtributoPersonalizado entity = mapper.map(request, AtributoPersonalizado.class);
        entity.setProduto(produto);

        AtributoPersonalizado salvo = repository.save(entity);
        return mapper.map(salvo, AtributoPersonalizadoResponse.class);
    }

    @Override
    public AtributoPersonalizadoResponse atualizar(Long id, AtributoPersonalizadoRequest request) {
        AtributoPersonalizado existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Atributo personalizado não encontrado com ID: " + id));

        if (!existente.getNome().equalsIgnoreCase(request.getNome()) &&
                repository.existsByProdutoIdAndNomeIgnoreCase(existente.getProduto().getId(), request.getNome())) {
            throw new IllegalArgumentException("Já existe outro atributo com este nome para o mesmo produto.");
        }

        existente.setNome(request.getNome());
        existente.setValor(request.getValor());

        AtributoPersonalizado atualizado = repository.save(existente);
        return mapper.map(atualizado, AtributoPersonalizadoResponse.class);
    }

    @Override
    public Optional<AtributoPersonalizadoResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entity -> mapper.map(entity, AtributoPersonalizadoResponse.class));
    }

    @Override
    public List<AtributoPersonalizadoResponse> listarTodos() {
        return repository.findAll()
                .stream()
                .map(entity -> mapper.map(entity, AtributoPersonalizadoResponse.class))
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
    public List<AtributoPersonalizadoResponse> listarPorProduto(Long produtoId) {
        return repository.findByProdutoId(produtoId)
                .stream()
                .map(entity -> mapper.map(entity, AtributoPersonalizadoResponse.class))
                .toList();
    }

    @Override
    public List<AtributoPersonalizadoListDTO> buscarPorNomeContendo(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(entity -> mapper.map(entity, AtributoPersonalizadoListDTO.class))
                .toList();
    }

    @Override
    public boolean verificarDuplicidade(Long produtoId, String nome) {
        return repository.existsByProdutoIdAndNomeIgnoreCase(produtoId, nome);
    }
}