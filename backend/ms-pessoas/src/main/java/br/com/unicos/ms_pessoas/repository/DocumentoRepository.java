package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_pessoas.enums.TipoDocumento;
import br.com.unicos.ms_pessoas.model.Documento;
import br.com.unicos.ms_pessoas.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Documento}.
 * <p>
 * Permite a consulta e gerenciamento de documentos pessoais ou empresariais,
 * como CPF, RG, CNPJ e demais identificações formais vinculadas às pessoas
 * cadastradas no UniCoS.
 */
@Repository
public interface DocumentoRepository extends BaseTenantRepository<Documento, Long> {

    /**
     * Busca documento pelo seu número.
     *
     * @param numero Número do documento (sem formatação).
     * @return Documento correspondente, caso exista.
     */
    Optional<Documento> findByNumero(String numero);

    /**
     * Lista todos os documentos de um determinado tipo.
     *
     * @param tipo Tipo do documento (CPF, RG, CNPJ etc.).
     * @return Lista de documentos do tipo informado.
     */
    List<Documento> findByTipo(TipoDocumento tipo);

    /**
     * Lista todos os documentos pertencentes a uma pessoa específica.
     *
     * @param pessoa Pessoa proprietária dos documentos.
     * @return Lista de documentos dessa pessoa.
     */
    List<Documento> findByPessoa(Pessoa pessoa);

    /**
     * Busca um documento específico pelo tipo e pela pessoa.
     *
     * @param pessoa Pessoa proprietária do documento.
     * @param tipo   Tipo do documento.
     * @return Documento correspondente, caso exista.
     */
    Optional<Documento> findByPessoaAndTipo(Pessoa pessoa, TipoDocumento tipo);
}
