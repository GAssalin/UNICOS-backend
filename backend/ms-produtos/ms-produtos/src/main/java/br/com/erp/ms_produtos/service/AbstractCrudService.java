package br.com.erp.ms_produtos.service;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudService<T, ID> implements CrudService<T, ID> {

    protected JpaRepository<T, ID> repository;

    protected AbstractCrudService() {
    }

    protected AbstractCrudService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public T salvar(T entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public void deletar(ID id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<T> buscarPorId(ID id) {
        return repository.findById(id);
    }

    @Override
    public List<T> listarTodos() {
        return repository.findAll();
    }
}