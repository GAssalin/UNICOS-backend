package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.model.HistoricoPreco;

import java.util.List;
import java.util.Optional;

public interface HistoricoPrecoService {
    HistoricoPreco salvar(HistoricoPreco historico);

    HistoricoPreco atualizar(Long id, HistoricoPreco historico);

    Optional<HistoricoPreco> buscarPorId(Long id);

    List<HistoricoPreco> listarPorProduto(Long produtoId);

    void deletar(Long id);
}