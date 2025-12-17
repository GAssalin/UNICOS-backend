package br.com.unicos.ms_auth.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_auth.model.Permissao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Permissao}.
 * <p>
 * Trata-se de um catálogo global de permissões do sistema,
 * compartilhado entre todos os tenants do UniCoS.
 * </p>
 *
 * <p>
 * As permissões são associadas às empresas indiretamente
 * por meio da entidade {@code Permissao}.
 * </p>
 */
@Repository
public interface PermissaoRepository extends BaseTenantRepository<Permissao, Long> {
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
    Page<Permissao> findByNomeContainingIgnoreCaseAndEmpresaId(String nome, Long empresaId, Pageable pageable);
}
