package br.com.unicos.ms_auth.repository;

import br.com.unicos.ms_auth.enums.TipoAcaoAcesso;
import br.com.unicos.ms_auth.model.AuditoriaAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados da entidade AuditoriaAcesso.
 * <p>
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface AuditoriaAcessoRepository extends JpaRepository<AuditoriaAcesso, Long> {

    /**
     * Lista todos os registros de auditoria de um determinado usuário.
     *
     * @param username Nome do usuário.
     * @return Lista de registros de auditoria.
     */
    List<AuditoriaAcesso> findByUsername(String username);

    /**
     * Lista registros de auditoria de um determinado tipo de ação.
     *
     * @param acao Tipo da ação (ex: LOGIN_SUCESSO, LOGIN_FALHA).
     * @return Lista de registros de auditoria correspondentes.
     */
    List<AuditoriaAcesso> findByAcao(TipoAcaoAcesso acao);

    /**
     * Lista registros de auditoria ocorridos dentro de um intervalo de tempo.
     *
     * @param inicio Data/hora inicial.
     * @param fim    Data/hora final.
     * @return Lista de registros no período informado.
     */
    List<AuditoriaAcesso> findByDataEventoBetween(LocalDateTime inicio, LocalDateTime fim);
}
