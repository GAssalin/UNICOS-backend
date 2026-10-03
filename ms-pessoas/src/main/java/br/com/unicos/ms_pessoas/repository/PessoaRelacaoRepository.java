package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.model.PessoaRelacao;
import br.com.unicos.ms_pessoas.model.TipoRelacaoPessoa;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório da entidade {@link PessoaRelacao}, sempre restrito à empresa (tenant).
 */
@Repository
public interface PessoaRelacaoRepository extends BaseTenantRepository<PessoaRelacao, Long> {

    List<PessoaRelacao> findByEmpresaId(Long empresaId);

    List<PessoaRelacao> findByPessoaAndEmpresaId(Pessoa pessoa, Long empresaId);

    List<PessoaRelacao> findByRelacionadoAndEmpresaId(Pessoa relacionado, Long empresaId);

    List<PessoaRelacao> findByTipoRelacaoAndEmpresaId(TipoRelacaoPessoa tipoRelacao, Long empresaId);

    List<PessoaRelacao> findByPessoaAndRelacionadoAndEmpresaId(Pessoa pessoa, Pessoa relacionado, Long empresaId);

    List<PessoaRelacao> findByPessoa_NomeContainingIgnoreCaseAndEmpresaId(String nome, Long empresaId);

    List<PessoaRelacao> findByRelacionado_NomeContainingIgnoreCaseAndEmpresaId(String nome, Long empresaId);
}
