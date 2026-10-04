package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_pessoas.enums.TipoDocumento;
import br.com.unicos.ms_pessoas.model.Documento;
import br.com.unicos.ms_pessoas.model.Pessoa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório da entidade {@link Documento}.
 *
 * <p>
 * O número do documento é único em toda a base (restrição do banco), por isso
 * {@link #findByNumero(String)} não filtra por empresa e deve ser usado apenas para validar duplicidade.
 * </p>
 */
@Repository
public interface DocumentoRepository extends BaseTenantRepository<Documento, Long> {

    Optional<Documento> findByNumero(String numero);

    List<Documento> findByEmpresaId(Long empresaId);

    List<Documento> findByTipoAndEmpresaId(TipoDocumento tipo, Long empresaId);

    List<Documento> findByPessoaAndEmpresaId(Pessoa pessoa, Long empresaId);

    Optional<Documento> findByPessoaAndTipoAndEmpresaId(Pessoa pessoa, TipoDocumento tipo, Long empresaId);
}
