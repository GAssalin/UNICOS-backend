package br.com.unicos.ms_filial.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_filial.model.EnderecoFilial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link EnderecoFilial}.
 * <p>
 * Centraliza consultas relacionadas aos endereços de filiais,
 * respeitando o contexto multi-tenant.
 * </p>
 */
@Repository
public interface EnderecoFilialRepository extends BaseTenantRepository<EnderecoFilial, Long> {

    /**
     * Lista endereços por filial dentro do tenant com paginação.
     */
    Page<EnderecoFilial> findByFilialIdAndEmpresaId(
            Long filialId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera o endereço por id e filial dentro do tenant.
     */
    Optional<EnderecoFilial> findByIdAndFilialIdAndEmpresaId(
            Long id,
            Long filialId,
            Long tenantId
    );

    /**
     * Verifica duplicidade de endereço na mesma filial dentro do tenant.
     */
    boolean existsByFilialIdAndLogradouroAndNumeroAndCepAndEmpresaId(
            Long filialId,
            String logradouro,
            String numero,
            String cep,
            Long tenantId
    );

    /**
     * Remove todos os endereços de uma filial dentro do tenant.
     */
    void deleteByFilialIdAndEmpresaId(
            Long filialId,
            Long tenantId
    );
}
