package br.com.unicos.ms_empresa.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_empresa.enums.TipoContatoEmpresa;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaContato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link EmpresaContato}.
 * <p>
 * Centraliza consultas relacionadas aos canais de contato institucionais
 * de uma empresa, respeitando o contexto multi-tenant.
 * </p>
 *
 * <p>
 * Utilizado em fluxos como:
 * <ul>
 *     <li>Cadastro e manutenção de contatos institucionais</li>
 *     <li>Definição de contato principal da empresa</li>
 *     <li>Exibição de informações públicas da empresa</li>
 * </ul>
 * </p>
 */
@Repository
public interface EmpresaContatoRepository extends BaseTenantRepository<EmpresaContato, Long> {

    /**
     * Lista os contatos institucionais de uma empresa dentro do tenant,
     * com suporte à paginação.
     *
     * @param empresa   Empresa proprietária dos contatos.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de contatos da empresa.
     */
    Page<EmpresaContato> findByEmpresaAndEmpresaId(
            Empresa empresa,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Lista os contatos institucionais de uma empresa filtrando pelo tipo,
     * respeitando o contexto multi-tenant.
     *
     * @param empresa   Empresa proprietária dos contatos.
     * @param tipo      Tipo do contato institucional.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação.
     * @return Página de contatos filtrados por tipo.
     */
    Page<EmpresaContato> findByEmpresaAndTipoContatoAndEmpresaId(
            Empresa empresa,
            TipoContatoEmpresa tipo,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Recupera o contato principal de uma empresa dentro do tenant.
     *
     * @param empresa   Empresa proprietária do contato.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o contato principal, se existir.
     */
    Optional<EmpresaContato> findByEmpresaAndPrincipalTrueAndEmpresaId(
            Empresa empresa,
            Long empresaId
    );

    /**
     * Verifica se já existe um contato institucional com o mesmo valor
     * para a empresa dentro do tenant, evitando duplicidade.
     *
     * @param empresa   Empresa proprietária do contato.
     * @param valor     Valor do contato (e-mail, telefone, etc.).
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se o contato já existir; {@code false} caso contrário.
     */
    boolean existsByEmpresaAndValorAndEmpresaId(
            Empresa empresa,
            String valor,
            Long empresaId
    );

    /**
     * Remove todos os contatos institucionais de uma empresa
     * dentro do tenant.
     *
     * <p>
     * Normalmente utilizado em processos de exclusão lógica
     * ou reconfiguração completa dos canais de contato.
     * </p>
     *
     * @param empresa   Empresa proprietária dos contatos.
     * @param empresaId Identificador da empresa (tenant).
     */
    void deleteByEmpresaAndEmpresaId(
            Empresa empresa,
            Long empresaId
    );
}
