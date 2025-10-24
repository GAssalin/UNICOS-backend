package br.com.erp.ms_produtos.repository;

import br.com.erp.ms_produtos.model.UnidadeMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnidadeMedidaRepository extends JpaRepository<UnidadeMedida, Long> {

    Optional<UnidadeMedida> findBySiglaIgnoreCase(String sigla);

    boolean existsBySiglaIgnoreCase(String sigla);
}