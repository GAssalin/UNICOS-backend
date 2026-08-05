package br.com.unicos.ms_empresa.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_empresa.model.EmpresaParametro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link EmpresaParametro}.
 */
@Repository
public interface EmpresaParametroRepository extends BaseTenantRepository<EmpresaParametro, Long> {

    /**
     * Recupera um parâmetro específico do tenant pelo nome da chave.
     *
     * @param chave     Nome da chave do parâmetro.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o parâmetro, se encontrado.
     */
    Optional<EmpresaParametro> findByChaveAndEmpresaId(String chave, Long empresaId);

    /**
     * Verifica se já existe um parâmetro cadastrado no tenant com a mesma chave.
     *
     * @param chave     Nome da chave do parâmetro.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByChaveAndEmpresaId(String chave, Long empresaId);

    /**
     * Lista todos os parâmetros do tenant com suporte à paginação.
     *
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de parâmetros.
     */
    Page<EmpresaParametro> findByEmpresaId(Long empresaId, Pageable pageable);

    /**
     * Remove um parâmetro específico do tenant.
     *
     * @param chave     Nome da chave do parâmetro.
     * @param empresaId Identificador da empresa (tenant).
     */
    void deleteByChaveAndEmpresaId(String chave, Long empresaId);
}