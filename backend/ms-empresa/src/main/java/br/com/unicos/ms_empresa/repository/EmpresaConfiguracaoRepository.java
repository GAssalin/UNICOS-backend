package br.com.unicos.ms_empresa.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_empresa.model.EmpresaConfiguracao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link EmpresaConfiguracao}.
 */
@Repository
public interface EmpresaConfiguracaoRepository extends BaseTenantRepository<EmpresaConfiguracao, Long> {

    /**
     * Recupera uma configuração específica do tenant pelo nome da chave.
     *
     * @param chave     Nome da chave de configuração.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo a configuração, se encontrada.
     */
    Optional<EmpresaConfiguracao> findByChaveAndEmpresaId(String chave, Long empresaId);

    /**
     * Verifica se já existe uma configuração cadastrada no tenant com a mesma chave.
     *
     * @param chave     Nome da chave de configuração.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByChaveAndEmpresaId(String chave, Long empresaId);

    /**
     * Lista todas as configurações do tenant com suporte à paginação.
     *
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de configurações.
     */
    Page<EmpresaConfiguracao> findByEmpresaId(Long empresaId, Pageable pageable);

    /**
     * Remove uma configuração específica do tenant.
     *
     * @param chave     Nome da chave da configuração.
     * @param empresaId Identificador da empresa (tenant).
     */
    void deleteByChaveAndEmpresaId(String chave, Long empresaId);
}