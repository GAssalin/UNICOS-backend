package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_pessoas.model.PessoaFisica;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório da entidade {@link PessoaFisica}.
 *
 * <p>
 * O CPF é único em toda a base (restrição do banco), por isso {@link #existsByCpf(String)}
 * e {@link #findByCpf(String)} não filtram por empresa e devem ser usados apenas para validar
 * duplicidade. Consultas de leitura usam as variantes com {@code empresaId}.
 * </p>
 */
@Repository
public interface PessoaFisicaRepository extends BaseTenantRepository<PessoaFisica, Long> {

    Optional<PessoaFisica> findByCpf(String cpf);

    Optional<PessoaFisica> findByCpfAndEmpresaId(String cpf, Long empresaId);

    List<PessoaFisica> findByEmpresaId(Long empresaId);

    List<PessoaFisica> findByNomeSocialAndEmpresaId(String nomeSocial, Long empresaId);

    List<PessoaFisica> findByNomeContainingIgnoreCaseAndEmpresaId(String nome, Long empresaId);
}
