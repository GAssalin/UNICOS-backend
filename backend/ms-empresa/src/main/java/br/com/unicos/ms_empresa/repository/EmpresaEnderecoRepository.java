package br.com.unicos.ms_empresa.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaEndereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link EmpresaEndereco}.
 * <p>
 * Centraliza consultas relacionadas aos endereços institucionais da empresa,
 * respeitando o contexto multi-tenant.
 * </p>
 *
 * <p>
 * Utilizado em fluxos como:
 * <ul>
 *     <li>Cadastro e manutenção de endereços fiscais e comerciais</li>
 *     <li>Definição de endereço principal da empresa</li>
 *     <li>Integrações fiscais e logísticas</li>
 * </ul>
 * </p>
 */
@Repository
public interface EmpresaEnderecoRepository extends BaseTenantRepository<EmpresaEndereco, Long> {

    /**
     * Lista os endereços institucionais de uma empresa dentro do tenant,
     * com suporte à paginação.
     *
     * @param empresa   Empresa proprietária dos endereços.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de endereços da empresa.
     */
    Page<EmpresaEndereco> findByEmpresaAndEmpresaId(
            Empresa empresa,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Lista os endereços institucionais de uma empresa filtrando pelo tipo,
     * respeitando o contexto multi-tenant.
     *
     * @param empresa     Empresa proprietária dos endereços.
     * @param tipoEndereco Tipo do endereço institucional.
     * @param empresaId   Identificador da empresa (tenant).
     * @param pageable    Parâmetros de paginação.
     * @return Página de endereços filtrados por tipo.
     */
    Page<EmpresaEndereco> findByEmpresaAndTipoEnderecoAndEmpresaId(
            Empresa empresa,
            TipoEnderecoEmpresa tipoEndereco,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Recupera o endereço principal de uma empresa dentro do tenant.
     *
     * @param empresa   Empresa proprietária do endereço.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o endereço principal, se existir.
     */
    Optional<EmpresaEndereco> findByEmpresaAndPrincipalTrueAndEmpresaId(
            Empresa empresa,
            Long empresaId
    );

    /**
     * Verifica se já existe um endereço cadastrado para a empresa
     * com o mesmo logradouro, número e CEP, evitando duplicidade.
     *
     * @param empresa   Empresa proprietária do endereço.
     * @param logradouro Logradouro do endereço.
     * @param numero     Número do endereço.
     * @param cep        CEP do endereço.
     * @param empresaId  Identificador da empresa (tenant).
     * @return {@code true} se o endereço já existir; {@code false} caso contrário.
     */
    boolean existsByEmpresaAndLogradouroAndNumeroAndCepAndEmpresaId(
            Empresa empresa,
            String logradouro,
            String numero,
            String cep,
            Long empresaId
    );

    /**
     * Remove todos os endereços institucionais de uma empresa
     * dentro do tenant.
     *
     * <p>
     * Normalmente utilizado em processos de exclusão lógica
     * ou reconfiguração completa de endereços.
     * </p>
     *
     * @param empresa   Empresa proprietária dos endereços.
     * @param empresaId Identificador da empresa (tenant).
     */
    void deleteByEmpresaAndEmpresaId(
            Empresa empresa,
            Long empresaId
    );
}
