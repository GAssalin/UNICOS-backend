package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_pessoas.enums.TipoEndereco;
import br.com.unicos.ms_pessoas.model.Endereco;
import br.com.unicos.ms_pessoas.model.Municipio;
import br.com.unicos.ms_pessoas.model.Pessoa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório da entidade {@link Endereco}, sempre restrito à empresa (tenant).
 */
@Repository
public interface EnderecoRepository extends BaseTenantRepository<Endereco, Long> {

    List<Endereco> findByEmpresaId(Long empresaId);

    List<Endereco> findByPessoaAndEmpresaId(Pessoa pessoa, Long empresaId);

    List<Endereco> findByPessoaAndTipoAndEmpresaId(Pessoa pessoa, TipoEndereco tipo, Long empresaId);

    Optional<Endereco> findByPessoaAndPrincipalTrueAndEmpresaId(Pessoa pessoa, Long empresaId);

    List<Endereco> findByMunicipioAndEmpresaId(Municipio municipio, Long empresaId);

    List<Endereco> findByCepAndEmpresaId(String cep, Long empresaId);
}
