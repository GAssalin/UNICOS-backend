package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.dto.ProdutoRequest;
import br.com.erp.ms_produtos.dto.ProdutoResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProdutoService {

    ProdutoResponse salvar(ProdutoRequest request);

    ProdutoResponse atualizar(Long id, ProdutoRequest request);

    Optional<ProdutoResponse> buscarPorId(Long id);

    List<ProdutoResponse> listarTodos();

    void deletar(Long id);

    // Métodos específicos
    Optional<ProdutoResponse> buscarPorSku(String sku);
    List<ProdutoResponse> buscarPorNome(String nome);
    List<ProdutoResponse> listarPorCategoria(Long categoriaId);
    List<ProdutoResponse> listarPorMarca(Long marcaId);
    List<ProdutoResponse> listarAtivos();
    List<ProdutoResponse> listarInativos();
    List<ProdutoResponse> listarPorFaixaDePreco(BigDecimal precoMin, BigDecimal precoMax);

    ProdutoResponse ativarProduto(Long id);
    ProdutoResponse inativarProduto(Long id);
    ProdutoResponse atualizarPreco(Long id, BigDecimal novoPreco);
    boolean verificarDisponibilidadeSku(String sku);
}