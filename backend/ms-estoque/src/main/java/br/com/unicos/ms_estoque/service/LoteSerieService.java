package br.com.unicos.ms_estoque.service;

import br.com.unicos.ms_estoque.dto.LoteSerieRequest;
import br.com.unicos.ms_estoque.dto.LoteSerieResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas aos lotes e séries de produtos.
 */
public interface LoteSerieService {

    @Transactional
    LoteSerieResponse salvar(LoteSerieRequest request);

    @Transactional
    LoteSerieResponse atualizar(Long id, LoteSerieRequest request);

    @Transactional
    void excluir(Long id);

    List<LoteSerieResponse> listarTodos();

    LoteSerieResponse buscarPorId(Long id);

    LoteSerieResponse buscarPorCodigo(String codigo);

    /**
     * Lista lotes com vencimento próximo (em até N dias).
     */
    List<LoteSerieResponse> listarLotesProximosDoVencimento(int dias);

    /**
     * Lista lotes vencidos.
     */
    List<LoteSerieResponse> listarLotesVencidos();
}
