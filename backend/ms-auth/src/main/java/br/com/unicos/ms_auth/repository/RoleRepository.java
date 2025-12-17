package br.com.unicos.ms_auth.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_auth.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    // ============================================================
    // Consultas pontuais (resultado único)
    // ============================================================

    /**
     * Busca um papel pelo nome exato.
     *
     * @param nome Nome do papel (ex.: ADMIN, GERENTE).
     * @return {@link Optional} contendo o papel, caso exista.
     */
    Optional<Role> findByNome(String nome);

    /**
     * Verifica se já existe um papel com o nome informado.
     *
     * @param nome Nome do papel.
     * @return {@code true} se existir, {@code false} caso contrário.
     */
    boolean existsByNome(String nome);

    // ============================================================
    // Consultas paginadas (uso administrativo)
    // ============================================================

    /**
     * Lista papéis cujo nome contenha o termo informado,
     * ignorando diferenças de maiúsculas e minúsculas, de forma paginada.
     *
     * <p>
     * Utilizado em telas administrativas,
     * cadastros e filtros textuais.
     * </p>
     *
     * @param nome     Parte do nome do papel.
     * @param pageable Informações de paginação e ordenação.
     * @return Página de papéis encontrados.
     */
    Page<Role> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
