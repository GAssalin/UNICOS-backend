package br.com.unicos.ms_permissao.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_permissao.model.Permissao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Repositório da entidade {@link Permissao}.
 *
 * <p>
 * As permissões formam um catálogo global (o nome é único em toda a base e corresponde às
 * verificações feitas pelos microserviços): podem ser consultadas e vinculadas a roles por
 * qualquer empresa, mas só são alteradas pela empresa que as cadastrou.
 * </p>
 */
@Repository
public interface PermissaoRepository extends BaseTenantRepository<Permissao, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    Page<Permissao> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
