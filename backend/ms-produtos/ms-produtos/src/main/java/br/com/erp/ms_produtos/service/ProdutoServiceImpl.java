package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.model.Produto;
import br.com.erp.ms_produtos.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl extends AbstractCrudService<Produto, Long> implements ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Override
    @Transactional
    public Produto atualizar(Long id, Produto produto) {
        Produto existente = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        produto.setId(existente.getId());
        return produtoRepository.save(produto);
    }

    @Override
    public Optional<Produto> buscarPorSku(String sku) {
        return produtoRepository.findBySku(sku);
    }

    @Override
    public List<Produto> listarAtivos() {
        return produtoRepository.findByAtivoTrue();
    }

    @Override
    public List<Produto> listarPorCategoria(Long categoriaId) {
        return produtoRepository.findByCategoriaId(categoriaId);
    }

    @Override
    public List<Produto> listarPorMarca(Long marcaId) {
        return produtoRepository.findByMarcaId(marcaId);
    }
}