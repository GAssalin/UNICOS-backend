package br.com.unicos.ms_auth.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_auth.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Role}.
 * <p>
 * Representa os papéis de acesso (roles) do sistema,
 * utilizados no modelo de autorização do UniCoS.
 * </p>
 *
 * <p>
 * Trata-se de um catálogo global de papéis,
 * compartilhado entre todos os tenants da plataforma.
 * </p>
 */
@Repository
public interface RoleRepository extends BaseTenantRepository<Role, Long> {
    /**
     * Verifica se já existe uma entidade com o nome informado.
     *
     * @param nome Nome da entidade.
     * @return {@code true} se existir, {@code false} caso contrário.
     */
    boolean existsByNomeContainingIgnoreCaseAndEmpresaId(String nome, Long empresaId);

    /**
     * Lista entidades cujo nome contenha o termo informado,
     * ignorando diferenças de maiúsculas e minúsculas, de forma paginada.
     *
     * <p>
     * Método indicado para telas administrativas,
     * cadastros e buscas textuais.
     * </p>
     *
     * @param nome     Parte do nome da entidade.
     * @param pageable Informações de paginação e ordenação.
     * @return Página de entidades encontradas.
     */
    Page<Role> findByNomeContainingIgnoreCaseAndEmpresaId(String nome, Long empresaId, Pageable pageable);
}
