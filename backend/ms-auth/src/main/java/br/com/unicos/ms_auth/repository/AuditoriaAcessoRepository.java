package br.com.unicos.ms_auth.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_auth.enums.TipoAcaoAcesso;
import br.com.unicos.ms_auth.model.AuditoriaAcesso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link AuditoriaAcesso}.
 * <p>
 * Em arquitetura multi-tenant, todos os registros de auditoria são isolados
 * por empresa (tenant), identificada pelo campo {@code empresaId}.
 * </p>
 *
 * <p>
 * Os dados de auditoria são altamente sensíveis e utilizados para
 * rastreabilidade, segurança, compliance e investigações.
 * Por esse motivo, todas as consultas são obrigatoriamente paginadas
 * e restritas ao tenant.
 * </p>
 *
 * <p>
 * Os métodos básicos de acesso por tenant (buscar por ID, verificar existência
 * e listar por empresa) são herdados de {@link BaseTenantRepository}.
 * </p>
 */
@Repository
public interface AuditoriaAcessoRepository extends BaseTenantRepository<AuditoriaAcesso, Long> {

    /**
     * Lista registros de auditoria de um determinado usuário
     * dentro de uma empresa (tenant), de forma paginada.
     *
     * @param username  Nome do usuário.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Informações de paginação e ordenação.
     * @return Página de registros de auditoria do usuário.
     */
    Page<AuditoriaAcesso> findByUsernameAndEmpresaId(String username, Long empresaId, Pageable pageable);

    /**
     * Lista registros de auditoria de um determinado tipo de ação
     * dentro de uma empresa (tenant), de forma paginada.
     *
     * @param acao      Tipo da ação ({@link TipoAcaoAcesso}).
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Informações de paginação e ordenação.
     * @return Página de registros de auditoria do tipo informado.
     */
    Page<AuditoriaAcesso> findByAcaoAndEmpresaId(TipoAcaoAcesso acao, Long empresaId, Pageable pageable);

    /**
     * Lista registros de auditoria ocorridos dentro de um intervalo de tempo,
     * restritos a uma empresa (tenant), de forma paginada.
     *
     * @param inicio    Data/hora inicial.
     * @param fim       Data/hora final.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Informações de paginação e ordenação.
     * @return Página de registros de auditoria no período informado.
     */
    Page<AuditoriaAcesso> findByDataEventoBetweenAndEmpresaId(LocalDateTime inicio, LocalDateTime fim, Long empresaId, Pageable pageable);
}
