package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNomeIgnoreCase(String nome);

    List<Categoria> findByNomeContainingIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    List<Categoria> findAllByOrderByNomeAsc();
}