package br.com.erp.ms_produtos.repository;

import br.com.erp.ms_produtos.model.AtributoPersonalizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtributoPersonalizadoRepository extends JpaRepository<AtributoPersonalizado, Long> {

    List<AtributoPersonalizado> findByProdutoId(Long produtoId);
}