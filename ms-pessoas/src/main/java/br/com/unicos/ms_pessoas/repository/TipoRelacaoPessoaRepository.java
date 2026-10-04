package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_pessoas.model.TipoRelacaoPessoa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório da entidade {@link TipoRelacaoPessoa}.
 *
 * <p>
 * Tipos de relação são dados de referência compartilhados (o nome é único em toda a base):
 * as consultas de leitura não filtram por empresa. Alterações e exclusões são permitidas apenas
 * à empresa que cadastrou o registro.
 * </p>
 */
@Repository
public interface TipoRelacaoPessoaRepository extends BaseTenantRepository<TipoRelacaoPessoa, Long> {

    List<TipoRelacaoPessoa> findAllByOrderByNomeAsc();

    Optional<TipoRelacaoPessoa> findByNomeIgnoreCase(String nome);

    List<TipoRelacaoPessoa> findByNomeContainingIgnoreCase(String nome);
}
