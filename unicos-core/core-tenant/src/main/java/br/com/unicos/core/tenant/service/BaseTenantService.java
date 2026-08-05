package br.com.unicos.core.tenant.service;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementação base abstrata para services multi-tenant.
 *
 * <p>
 * Esta classe centraliza a lógica padrão de acesso ao repositório
 * multi-tenant, garantindo isolamento por {@code empresaId}.
 * </p>
 *
 * <p>
 * Services concretos devem estender esta classe e podem sobrescrever
 * métodos para aplicar regras de negócio específicas.
 * </p>
 *
 * @param <T>  Tipo da entidade.
 * @param <ID> Tipo do identificador da entidade.
 */
@RequiredArgsConstructor
public abstract class BaseTenantService<T, ID> {

    protected final BaseTenantRepository<T, ID> repository;

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    public Page<T> findAllByEmpresaId(Long empresaId, Pageable pageable) {
        return repository.findAllByEmpresaId(empresaId, pageable);
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }
}
