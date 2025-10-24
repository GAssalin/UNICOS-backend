package br.com.erp.ms_produtos.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, ID> {
    T salvar(T entity);

    T atualizar(ID id, T entity);

    Optional<T> buscarPorId(ID id);

    List<T> listarTodos();

    void deletar(ID id);
}