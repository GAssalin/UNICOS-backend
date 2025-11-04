package br.com.unicos.ms_empresa.repository;

import br.com.unicos.ms_empresa.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Empresa}.
 * <p>
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo {@link JpaRepository}.
 */
@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    /**
     * Busca uma empresa pelo seu CNPJ.
     *
     * @param cnpj CNPJ da empresa.
     * @return Optional contendo a empresa, se encontrada.
     */
    Optional<Empresa> findByCnpj(String cnpj);

    /**
     * Busca empresas cuja razão social contenha um determinado termo,
     * ignorando maiúsculas e minúsculas.
     *
     * @param razaoSocial Termo de busca.
     * @return Lista de empresas correspondentes ao termo informado.
     */
    List<Empresa> findByRazaoSocialContainingIgnoreCase(String razaoSocial);

    /**
     * Busca empresas cujo nome fantasia contenha um determinado termo,
     * ignorando maiúsculas e minúsculas.
     *
     * @param nomeFantasia Termo de busca.
     * @return Lista de empresas correspondentes ao termo informado.
     */
    List<Empresa> findByNomeFantasiaContainingIgnoreCase(String nomeFantasia);

    /**
     * Busca uma empresa pelo nome fantasia exato.
     *
     * @param nomeFantasia Nome fantasia exato.
     * @return Optional contendo a empresa, se encontrada.
     */
    Optional<Empresa> findByNomeFantasia(String nomeFantasia);

    /**
     * Verifica se já existe uma empresa cadastrada com o mesmo CNPJ.
     *
     * @param cnpj CNPJ da empresa.
     * @return true se já existir, false caso contrário.
     */
    boolean existsByCnpj(String cnpj);

    /**
     * Lista todas as empresas ordenadas por razão social (ordem ascendente).
     *
     * @return Lista de empresas ordenadas alfabeticamente pela razão social.
     */
    List<Empresa> findAllByOrderByRazaoSocialAsc();

    /**
     * Lista todas as empresas que possuem inscrição estadual cadastrada.
     *
     * @return Lista de empresas com inscrição estadual.
     */
    List<Empresa> findByInscricaoEstadualIsNotNull();

    /**
     * Lista todas as empresas que possuem inscrição municipal cadastrada.
     *
     * @return Lista de empresas com inscrição municipal.
     */
    List<Empresa> findByInscricaoMunicipalIsNotNull();

    /**
     * Lista empresas cuja razão social ou nome fantasia contenham o termo informado.
     * <p>
     * Útil para buscas genéricas no front-end.
     *
     * @param termo Termo parcial de busca (pode ser parte do nome fantasia ou razão social).
     * @return Lista de empresas correspondentes ao termo.
     */
    List<Empresa> findByRazaoSocialContainingIgnoreCaseOrNomeFantasiaContainingIgnoreCase(String termo, String termo2);
}
