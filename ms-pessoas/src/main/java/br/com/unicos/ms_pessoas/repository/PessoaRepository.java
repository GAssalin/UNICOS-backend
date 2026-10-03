package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import br.com.unicos.ms_pessoas.model.Pessoa;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório da entidade base {@link Pessoa} (física ou jurídica), sempre restrito à empresa (tenant).
 */
@Repository
public interface PessoaRepository extends BaseTenantRepository<Pessoa, Long> {

    List<Pessoa> findByEmpresaId(Long empresaId);

    List<Pessoa> findByNomeAndEmpresaId(String nome, Long empresaId);

    List<Pessoa> findByNomeContainingIgnoreCaseAndEmpresaId(String nome, Long empresaId);

    List<Pessoa> findByTipoPessoaAndEmpresaId(TipoPessoa tipo, Long empresaId);
}
