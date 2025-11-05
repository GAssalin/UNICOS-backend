package br.com.unicos.ms_estoque.service;

import br.com.unicos.ms_estoque.dto.ProdutoEstoqueListDTO;
import br.com.unicos.ms_estoque.dto.ProdutoEstoqueRequest;
import br.com.unicos.ms_estoque.dto.ProdutoEstoqueResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas aos produtos armazenados nos estoques.
 */
public interface ProdutoEstoqueService {

    @Transactional
    ProdutoEstoqueResponse salvar(ProdutoEstoqueRequest request);

    @Transactional
    ProdutoEstoqueResponse atualizar(Long id, ProdutoEstoqueRequest request);

    @Transactional
    void excluir(Long id);

    List<ProdutoEstoqueListDTO> listarTodos();

    ProdutoEstoqueResponse buscarPorId(Long id);

    /**
     * Lista produtos de um local de estoque específico.
     */
    List<ProdutoEstoqueListDTO> listarPorEstoque(Long estoqueLocalId);

    /**
     * Retorna produtos com saldo abaixo do mínimo.
     */
    List<ProdutoEstoqueListDTO> listarEstoqueBaixo();

    /**
     * Retorna produtos com saldo acima do máximo.
     */
    List<ProdutoEstoqueListDTO> listarEstoqueExcedente();
}
