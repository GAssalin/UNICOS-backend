package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.ms_pessoas.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pela persistência de {@link Colaborador}.
 */
@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    /**
     * Busca todos os colaboradores de uma empresa.
     */
    List<Colaborador> findByEmpresaId(Long empresaId);

    /**
     * Busca colaboradores ativos de uma empresa.
     */
    List<Colaborador> findByEmpresaIdAndAtivoTrue(Long empresaId);

    /**
     * Busca colaborador pela matrícula.
     */
    Optional<Colaborador> findByMatricula(String matricula);
}
