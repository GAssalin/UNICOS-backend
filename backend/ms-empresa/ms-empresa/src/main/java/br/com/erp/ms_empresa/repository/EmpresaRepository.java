package br.com.erp.ms_empresa.repository;

import br.com.erp.ms_empresa.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade Empresa.
 *
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
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
     * Verifica se já existe uma empresa cadastrada com o mesmo CNPJ.
     *
     * @param cnpj CNPJ da empresa.
     * @return true se já existir, false caso contrário.
     */
    boolean existsByCnpj(String cnpj);

    /**
     * Lista todas as empresas ordenadas por razão social.
     *
     * @return Lista de empresas ordenadas alfabeticamente pela razão social.
     */
    List<Empresa> findAllByOrderByRazaoSocialAsc();
}