package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.model.PessoaRelacao;
import br.com.unicos.ms_pessoas.model.TipoRelacaoPessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link PessoaRelacao}.
 * <p>
 * Permite a consulta de vínculos entre pessoas, essenciais para processos
 * corporativos como identificação de responsáveis, dependentes, sócios,
 * representantes legais e outras relações definidas dentro do UniCoS.
 */
@Repository
public interface PessoaRelacaoRepository extends BaseTenantRepository<PessoaRelacao, Long> {

    /**
     * Lista todas as relações em que a pessoa é o ator principal.
     *
     * @param pessoa Pessoa principal da relação.
     * @return Lista de relações encontradas.
     */
    List<PessoaRelacao> findByPessoa(Pessoa pessoa);

    /**
     * Lista todas as relações em que a pessoa é o indivíduo relacionado.
     *
     * @param relacionado Pessoa relacionada.
     * @return Lista de relações encontradas.
     */
    List<PessoaRelacao> findByRelacionado(Pessoa relacionado);

    /**
     * Lista relações filtrando por tipo de vínculo.
     *
     * @param tipoRelacao Tipo da relação (ex.: Pai, Sócio, Responsável).
     * @return Lista de relações do tipo informado.
     */
    List<PessoaRelacao> findByTipoRelacao(TipoRelacaoPessoa tipoRelacao);

    /**
     * Lista relações entre duas pessoas específicas.
     *
     * @param pessoa      Pessoa principal.
     * @param relacionado Pessoa relacionada.
     * @return Lista de vínculos encontrados.
     */
    List<PessoaRelacao> findByPessoaAndRelacionado(Pessoa pessoa, Pessoa relacionado);

    /**
     * Busca relações onde o nome da pessoa principal contenha o termo informado (ignore case).
     * Permite pesquisas diretas sem necessidade de carregar a entidade Pessoa antes.
     *
     * @param nome      Nome da Pessoa.
     * @return Lista de vínculos encontrados.
     */
    List<PessoaRelacao> findByPessoa_NomeContainingIgnoreCase(String nome);

    /**
     * Busca relações onde o nome da pessoa relacionada contenha o termo informado (ignore case).
     *
     * @param nome      Nome da Pessoa.
     * @return Lista de vínculos encontrados.
     */
    List<PessoaRelacao> findByRelacionado_NomeContainingIgnoreCase(String nome);
}
