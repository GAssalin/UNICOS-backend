package br.com.unicos.ms_empresa.repository;

import br.com.unicos.ms_empresa.enums.StatusEmpresa;
import br.com.unicos.ms_empresa.enums.TipoEmpresa;
import br.com.unicos.ms_empresa.model.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Empresa}.
 *
 * <p>
 * A empresa é o próprio tenant e por isso não possui {@code empresaId}. O isolamento é feito pelo
 * escopo da empresa do usuário autenticado: ela mesma e as filiais vinculadas a ela
 * ({@code matrizId}). Os métodos herdados de {@link JpaRepository} não aplicam esse escopo.
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
     * Lista a empresa informada e suas filiais, com filtros opcionais ({@code null} ignora o filtro).
     *
     * @param empresaId empresa do usuário autenticado.
     * @param status    status da empresa.
     * @param tipo      tipo da empresa (MATRIZ ou FILIAL).
     * @param matrizId  identificador da matriz.
     * @param pageable  parâmetros de paginação.
     * @return página de empresas dentro do escopo.
     */
    @Query("""
            select e from Empresa e
            where (e.id = :empresaId or e.matrizId = :empresaId)
              and (:status is null or e.statusEmpresa = :status)
              and (:tipo is null or e.tipoEmpresa = :tipo)
              and (:matrizId is null or e.matrizId = :matrizId)
            """)
    Page<Empresa> pesquisarNoEscopo(
            @Param("empresaId") Long empresaId,
            @Param("status") StatusEmpresa status,
            @Param("tipo") TipoEmpresa tipo,
            @Param("matrizId") Long matrizId,
            Pageable pageable
    );
}
