package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.model.UnidadeMedida;

import java.util.List;
import java.util.Optional;

public interface UnidadeMedidaService {
    UnidadeMedida salvar(UnidadeMedida unidade);

    UnidadeMedida atualizar(Long id, UnidadeMedida unidade);

    Optional<UnidadeMedida> buscarPorId(Long id);

    Optional<UnidadeMedida> buscarPorSigla(String sigla);

    List<UnidadeMedida> listarTodas();

    void deletar(Long id);
}