package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_pessoas.model.PessoaJuridica;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório da entidade {@link PessoaJuridica}.
 *
 * <p>
 * O CNPJ é único em toda a base (restrição do banco), por isso {@link #findByCnpj(String)}
 * não filtra por empresa e deve ser usado apenas para validar duplicidade.
 * </p>
 */
@Repository
public interface PessoaJuridicaRepository extends BaseTenantRepository<PessoaJuridica, Long> {

    Optional<PessoaJuridica> findByCnpj(String cnpj);

    Optional<PessoaJuridica> findByCnpjAndEmpresaId(String cnpj, Long empresaId);

    List<PessoaJuridica> findByEmpresaId(Long empresaId);

    List<PessoaJuridica> findByNomeFantasiaAndEmpresaId(String nomeFantasia, Long empresaId);

    List<PessoaJuridica> findByNomeContainingIgnoreCaseAndEmpresaId(String nome, Long empresaId);
}
