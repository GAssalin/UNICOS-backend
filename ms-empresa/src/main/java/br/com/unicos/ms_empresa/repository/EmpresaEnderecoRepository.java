package br.com.unicos.ms_empresa.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;
import br.com.unicos.ms_empresa.model.EmpresaEndereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link EmpresaEndereco}.
 */
@Repository
public interface EmpresaEnderecoRepository extends BaseTenantRepository<EmpresaEndereco, Long> {

    /**
     * Lista os endereços institucionais do tenant com suporte à paginação.
     *
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de endereços.
     */
    Page<EmpresaEndereco> findByEmpresaId(Long empresaId, Pageable pageable);

    /**
     * Lista os endereços institucionais do tenant filtrando pelo tipo.
     *
     * @param tipoEndereco Tipo do endereço institucional.
     * @param empresaId    Identificador da empresa (tenant).
     * @param pageable     Parâmetros de paginação.
     * @return Página de endereços filtrados por tipo.
     */
    Page<EmpresaEndereco> findByTipoEnderecoAndEmpresaId(
            TipoEnderecoEmpresa tipoEndereco,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Recupera o endereço principal do tenant.
     *
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o endereço principal, se existir.
     */
    Optional<EmpresaEndereco> findByPrincipalTrueAndEmpresaId(Long empresaId);

    /**
     * Verifica se já existe um endereço cadastrado no tenant
     * com o mesmo logradouro, número e CEP.
     *
     * @param logradouro Logradouro do endereço.
     * @param numero     Número do endereço.
     * @param cep        CEP do endereço.
     * @param empresaId  Identificador da empresa (tenant).
     * @return {@code true} se o endereço já existir; {@code false} caso contrário.
     */
    boolean existsByLogradouroAndNumeroAndCepAndEmpresaId(
            String logradouro,
            String numero,
            String cep,
            Long empresaId
    );

    /**
     * Remove todos os endereços institucionais do tenant.
     *
     * @param empresaId Identificador da empresa (tenant).
     */
    void deleteByEmpresaId(Long empresaId);
}