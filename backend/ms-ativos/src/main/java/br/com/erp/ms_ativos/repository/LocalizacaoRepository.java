package br.com.erp.ms_ativos.repository;

import br.com.erp.ms_ativos.model.Localizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Localizacao}.
 *
 * Fornece métodos personalizados para consultas específicas de localizações,
 * além dos métodos CRUD padrão fornecidos pelo {@link JpaRepository}.
 */
@Repository
public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {

    /**
     * Lista todas as localizações pertencentes a uma filial específica.
     *
     * @param filialId ID da filial.
     * @return Lista de localizações vinculadas à filial.
     */
    List<Localizacao> findByFilialId(Long filialId);
}