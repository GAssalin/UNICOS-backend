package br.com.unicos.ms_funcionario.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_funcionario.model.Cargo;
import org.springframework.stereotype.Repository;

/**
 * Repositório responsável pelo acesso aos cargos da empresa.
 */
@Repository
public interface CargoRepository extends BaseTenantRepository<Cargo, Long> {

    boolean existsByNomeIgnoreCaseAndEmpresaId(String nome, Long empresaId);

    boolean existsByNomeIgnoreCaseAndEmpresaIdAndIdNot(String nome, Long empresaId, Long id);
}
