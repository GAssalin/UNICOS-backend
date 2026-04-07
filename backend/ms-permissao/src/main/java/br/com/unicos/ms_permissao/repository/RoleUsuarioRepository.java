package br.com.unicos.ms_permissao.repository;

import br.com.unicos.ms_permissao.model.RoleUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface RoleUsuarioRepository extends JpaRepository<RoleUsuario, Long> {

    @Query("""
           select ru.roleNome
           from RoleUsuario ru
           where ru.usuarioId = :usuarioId
             and ru.ativo = true
           """)
    Set<String> findRolesByUserId(Long usuarioId);

    @Query("""
        select count(rp) > 0
        from RolePermissao rp
        join rp.permissao p
        join rp.role r
        join RoleUsuario ru on ru.role = r
        where rp.empresaId = :empresaId
          and ru.empresaId = :empresaId
          and ru.usuarioId = :usuarioId
          and p.nome = :nomePermissao
    """)
    boolean usuarioPossuiPermissao(@Param("empresaId") Long empresaId, @Param("usuarioId") Long usuarioId, @Param("nomePermissao") String nomePermissao);

}
