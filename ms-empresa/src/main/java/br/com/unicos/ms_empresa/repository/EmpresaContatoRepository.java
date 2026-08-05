package br.com.unicos.ms_empresa.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_empresa.enums.TipoContatoEmpresa;
import br.com.unicos.ms_empresa.model.EmpresaContato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link EmpresaContato}.
 */
@Repository
public interface EmpresaContatoRepository extends BaseTenantRepository<EmpresaContato, Long> {

    /**
     * Lista os contatos institucionais do tenant com suporte à paginação.
     *
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de contatos.
     */
    Page<EmpresaContato> findByEmpresaId(Long empresaId, Pageable pageable);

    /**
     * Lista os contatos institucionais do tenant filtrando pelo tipo.
     *
     * @param tipo      Tipo do contato institucional.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação.
     * @return Página de contatos filtrados por tipo.
     */
    Page<EmpresaContato> findByTipoContatoAndEmpresaId(
            TipoContatoEmpresa tipo,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Recupera o contato principal do tenant.
     *
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o contato principal, se existir.
     */
    Optional<EmpresaContato> findByPrincipalTrueAndEmpresaId(Long empresaId);

    /**
     * Verifica se já existe um contato institucional com o mesmo valor no tenant.
     *
     * @param valor     Valor do contato.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se o contato já existir; {@code false} caso contrário.
     */
    boolean existsByValorAndEmpresaId(String valor, Long empresaId);

    /**
     * Remove todos os contatos institucionais do tenant.
     *
     * @param empresaId Identificador da empresa (tenant).
     */
    void deleteByEmpresaId(Long empresaId);
}