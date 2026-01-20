package br.com.unicos.ms_permissao.repository;

import br.com.unicos.ms_permissao.model.RoleUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface RoleUsuarioRepository extends JpaRepository<RoleUsuario, Long> {

    @Query("""
           select ru.roleNome
           from RoleUsuario ru
           where ru.usuarioId = :usuarioId
             and ru.ativo = true
           """)
    Set<String> findRolesByUserId(Long usuarioId);

}
