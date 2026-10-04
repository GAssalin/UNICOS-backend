package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_pessoas.enums.Uf;
import br.com.unicos.ms_pessoas.model.Municipio;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório da entidade {@link Municipio}.
 *
 * <p>
 * Municípios são dados de referência compartilhados: as consultas de leitura não filtram por
 * empresa. Alterações e exclusões são permitidas apenas à empresa que cadastrou o registro.
 * </p>
 */
@Repository
public interface MunicipioRepository extends BaseTenantRepository<Municipio, Long> {

    List<Municipio> findAllByOrderByNomeAsc();

    Optional<Municipio> findFirstByNomeIgnoreCaseAndUf(String nome, Uf uf);

    List<Municipio> findByNomeContainingIgnoreCase(String nome);

    List<Municipio> findByUf(Uf uf);

    Optional<Municipio> findFirstByCodigoIbge(String codigoIbge);
}
