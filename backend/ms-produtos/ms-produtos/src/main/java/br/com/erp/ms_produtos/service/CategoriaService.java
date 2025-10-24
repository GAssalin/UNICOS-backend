package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService extends CrudService<Categoria, Long> {
    Optional<Categoria> buscarPorNome(String nome);
    List<Categoria> listarTodas();
}