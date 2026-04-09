package br.com.unicos.ms_permissao.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_permissao.model.RolePermissao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissaoRepository extends BaseTenantRepository<RolePermissao, Long> {

    boolean existsByRoleIdAndPermissaoIdAndEmpresaId(Long roleId, Long permissaoId, Long empresaId);

    List<RolePermissao> findByAtivoTrueAndEmpresaId(Long empresaId);

    @Query("""
        select count(rp) > 0
        from RolePermissao rp
        join rp.permissao p
        where rp.empresaId = :empresaId
          and rp.role.id = :roleId
          and p.nome = :nomePermissao
          and rp.ativo = true
          and p.ativo = true
    """)
    boolean rolePossuiPermissao(@Param("empresaId") Long empresaId,
                                @Param("roleId") Long roleId,
                                @Param("nomePermissao") String nomePermissao);
}
