package br.com.unicos.core.tenant.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Implementação base abstrata para services multi-tenant.
 *
 * <p>
 * Esta classe centraliza a lógica padrão de acesso ao repositório
 * multi-tenant, garantindo isolamento pelo {@code empresaId} do
 * {@link TenantContext} da requisição atual.
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

    /**
     * Busca a entidade pelo identificador, restrita à empresa do contexto atual.
     */
    public Optional<T> findById(ID id) {
        return repository.findByIdAndEmpresaId(id, TenantContext.getEmpresaId());
    }

    /**
     * Verifica a existência da entidade na empresa do contexto atual.
     */
    public boolean existsById(ID id) {
        return repository.existsByIdAndEmpresaId(id, TenantContext.getEmpresaId());
    }

    public Page<T> findAllByEmpresaId(Long empresaId, Pageable pageable) {
        return repository.findAllByEmpresaId(empresaId, pageable);
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    /**
     * Remove a entidade somente se ela pertencer à empresa do contexto atual.
     */
    public void deleteById(ID id) {
        findById(id).ifPresent(repository::delete);
    }
}
