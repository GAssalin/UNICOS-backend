package br.com.unicos.ms_cliente.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_cliente.model.ClienteCategoria;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ClienteCategoria}.
 *
 * <p>
 * Gerencia categorias comerciais de clientes dentro do tenant.
 * </p>
 */
@Repository
public interface ClienteCategoriaRepository extends BaseTenantRepository<ClienteCategoria, Long> {

    /**
     * Busca categoria por nome dentro do tenant.
     *
     * @param nome nome da categoria
     * @param tenantId identificador da empresa
     * @return categoria encontrada
     */
    Optional<ClienteCategoria> findByNomeAndEmpresaId(String nome, Long tenantId);

    /**
     * Verifica se já existe categoria com o mesmo nome no tenant.
     *
     * @param nome nome da categoria
     * @param tenantId identificador da empresa
     * @return true se existir
     */
    boolean existsByNomeAndEmpresaId(String nome, Long tenantId);
}