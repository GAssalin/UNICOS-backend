package br.com.unicos.ms_funcionario.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_funcionario.enums.PapelFuncionario;
import br.com.unicos.ms_funcionario.enums.StatusFuncionario;
import br.com.unicos.ms_funcionario.model.Funcionario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos funcionários da empresa.
 */
@Repository
public interface FuncionarioRepository extends BaseTenantRepository<Funcionario, Long> {

    /**
     * Lista os funcionários da empresa, filtrando opcionalmente por status e papel do cargo.
     */
    @Query("""
            select f from Funcionario f
            where f.empresaId = :empresaId
              and (:status is null or f.status = :status)
              and (:papel is null or f.cargo.papel = :papel)
            """)
    Page<Funcionario> pesquisar(
            @Param("empresaId") Long empresaId,
            @Param("status") StatusFuncionario status,
            @Param("papel") PapelFuncionario papel,
            Pageable pageable
    );

    Optional<Funcionario> findByUsuarioIdAndEmpresaId(Long usuarioId, Long empresaId);

    Page<Funcionario> findBySuperiorIdAndEmpresaId(Long superiorId, Long empresaId, Pageable pageable);

    boolean existsByPessoaIdAndEmpresaId(Long pessoaId, Long empresaId);

    boolean existsByUsuarioIdAndEmpresaId(Long usuarioId, Long empresaId);

    boolean existsByMatriculaAndEmpresaId(String matricula, Long empresaId);

    boolean existsBySuperiorIdAndEmpresaId(Long superiorId, Long empresaId);

    boolean existsByCargoIdAndEmpresaId(Long cargoId, Long empresaId);

    boolean existsByUsuarioIdAndCargoIdAndEmpresaId(Long usuarioId, Long cargoId, Long empresaId);
}
