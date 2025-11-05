package br.com.unicos.ms_estoque.repository;

import br.com.unicos.ms_estoque.enums.TipoLocalEstoque;
import br.com.unicos.ms_estoque.model.EstoqueLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstoqueLocalRepository extends JpaRepository<EstoqueLocal, Long> {

    /**
     * Busca locais de estoque de um determinado tipo (ex: DEPOSITO, LOJA, TERCEIRO).
     */
    List<EstoqueLocal> findByTipo(TipoLocalEstoque tipo);

    /**
     * Busca todos os locais de estoque vinculados a uma empresa específica.
     */
    List<EstoqueLocal> findByEmpresaId(Long empresaId);

    /**
     * Verifica se já existe um local de estoque com o nome informado.
     */
    boolean existsByNomeIgnoreCase(String nome);
}
