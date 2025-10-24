package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.model.AtributoPersonalizado;

import java.util.List;
import java.util.Optional;

public interface AtributoPersonalizadoService {
    AtributoPersonalizado salvar(AtributoPersonalizado atributo);

    AtributoPersonalizado atualizar(Long id, AtributoPersonalizado atributo);

    Optional<AtributoPersonalizado> buscarPorId(Long id);

    List<AtributoPersonalizado> listarPorProduto(Long produtoId);

    void deletar(Long id);
}