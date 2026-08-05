package br.com.unicos.ms_empresa.repository;

import br.com.unicos.ms_empresa.enums.StatusEmpresa;
import br.com.unicos.ms_empresa.enums.TipoEmpresa;
import br.com.unicos.ms_empresa.model.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Empresa}.
 *
 * <p>
 * Como a entidade {@link Empresa} não utiliza mais o atributo {@code empresaId},
 * este repositório não deve mais herdar de {@code BaseTenantRepository}.
 * </p>
 */
@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    /**
     * Recupera uma empresa pelo CNPJ.
     *
     * @param cnpj CNPJ da empresa (sem formatação).
     * @return {@link Optional} contendo a empresa, se encontrada.
     */
    Optional<Empresa> findByCnpj(String cnpj);

    /**
     * Verifica se já existe uma empresa cadastrada com o mesmo CNPJ.
     *
     * @param cnpj CNPJ da empresa.
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByCnpj(String cnpj);

    /**
     * Lista empresas filtrando pelo status operacional.
     *
     * @param statusEmpresa Status da empresa.
     * @param pageable      Parâmetros de paginação.
     * @return Página de empresas filtradas por status.
     */
    Page<Empresa> findByStatusEmpresa(StatusEmpresa statusEmpresa, Pageable pageable);

    /**
     * Lista empresas filtrando pelo tipo (MATRIZ ou FILIAL).
     *
     * @param tipoEmpresa Tipo da empresa.
     * @param pageable    Parâmetros de paginação.
     * @return Página de empresas filtradas por tipo.
     */
    Page<Empresa> findByTipoEmpresa(TipoEmpresa tipoEmpresa, Pageable pageable);

    /**
     * Lista empresas de uma matriz específica.
     *
     * @param matrizId identificador da matriz.
     * @param pageable parâmetros de paginação.
     * @return página de empresas vinculadas à matriz.
     */
    Page<Empresa> findByMatrizId(Long matrizId, Pageable pageable);

    /**
     * Busca empresas por matriz e tipo.
     *
     * @param matrizId    identificador da matriz.
     * @param tipoEmpresa tipo da empresa.
     * @param pageable    parâmetros de paginação.
     * @return página filtrada.
     */
    Page<Empresa> findByMatrizIdAndTipoEmpresa(Long matrizId, TipoEmpresa tipoEmpresa, Pageable pageable);

    /**
     * Busca empresas por matriz e status.
     *
     * @param matrizId      identificador da matriz.
     * @param statusEmpresa status da empresa.
     * @param pageable      parâmetros de paginação.
     * @return página filtrada.
     */
    Page<Empresa> findByMatrizIdAndStatusEmpresa(Long matrizId, StatusEmpresa statusEmpresa, Pageable pageable);
}