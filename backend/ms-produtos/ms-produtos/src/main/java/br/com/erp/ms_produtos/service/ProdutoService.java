package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.model.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoService extends CrudService<Produto, Long> {
    Optional<Produto> buscarPorSku(String sku);

    List<Produto> listarAtivos();

    List<Produto> listarPorCategoria(Long categoriaId);

    List<Produto> listarPorMarca(Long marcaId);
}