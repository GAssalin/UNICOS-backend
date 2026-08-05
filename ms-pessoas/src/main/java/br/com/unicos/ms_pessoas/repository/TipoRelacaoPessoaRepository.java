package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_pessoas.model.TipoRelacaoPessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link TipoRelacaoPessoa}.
 * <p>
 * Permite o gerenciamento dos tipos de vínculos possíveis entre pessoas,
 * utilizados em cadastros corporativos, processos contratuais, dependentes,
 * representantes legais e demais relações definidas dentro do UniCoS.
 */
@Repository
public interface TipoRelacaoPessoaRepository extends BaseTenantRepository<TipoRelacaoPessoa, Long> {

    /**
     * Busca um tipo de relação pelo nome exato.
     *
     * @param nome Nome do tipo de relação (ex.: Pai, Sócio, Responsável).
     * @return Tipo de relação correspondente, caso exista.
     */
    Optional<TipoRelacaoPessoa> findByNome(String nome);

    /**
     * Lista tipos de relação cujo nome contenha o termo informado,
     * ignorando diferenças de maiúsculas e minúsculas.
     *
     * @param nome Parte do nome do tipo de relação.
     * @return Lista de tipos de relação encontrados.
     */
    List<TipoRelacaoPessoa> findByNomeContainingIgnoreCase(String nome);
}
