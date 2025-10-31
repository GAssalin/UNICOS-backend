package br.com.erp.ms_empresa.repository;

import br.com.erp.ms_empresa.model.Filial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade Filial.
 *
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {

    /**
     * Lista todas as filiais associadas a uma empresa matriz específica.
     *
     * @param empresaId ID da empresa matriz.
     * @return Lista de filiais vinculadas à empresa informada.
     */
    List<Filial> findByEmpresaId(Long empresaId);

    /**
     * Busca uma filial pelo seu CNPJ.
     *
     * @param cnpj CNPJ da filial.
     * @return Optional contendo a filial, se encontrada.
     */
    Optional<Filial> findByCnpj(String cnpj);

    /**
     * Busca todas as filiais localizadas em uma determinada cidade.
     *
     * @param cidade Nome da cidade.
     * @return Lista de filiais localizadas na cidade informada.
     */
    List<Filial> findByCidadeIgnoreCase(String cidade);

    /**
     * Lista todas as filiais de uma determinada unidade federativa (UF).
     *
     * @param uf Sigla da unidade federativa (ex: SP, RJ, MG).
     * @return Lista de filiais que pertencem ao estado informado.
     */
    List<Filial> findByUfIgnoreCase(String uf);

    /**
     * Verifica se já existe uma filial cadastrada com o mesmo CNPJ.
     *
     * @param cnpj CNPJ da filial.
     * @return true se já existir uma filial com o CNPJ informado, false caso contrário.
     */
    boolean existsByCnpj(String cnpj);

    /**
     * Lista todas as filiais ordenadas alfabeticamente pela razão social.
     *
     * @return Lista de filiais ordenadas por razão social.
     */
    List<Filial> findAllByOrderByRazaoSocialAsc();
}