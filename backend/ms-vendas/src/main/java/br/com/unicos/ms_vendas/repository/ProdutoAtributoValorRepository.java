package br.com.unicos.ms_vendas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_vendas.model.ProdutoAtributoValor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ProdutoAtributoValor}.
 *
 * <p>
 * Centraliza consultas relacionadas aos valores de atributos associados
 * a produtos, garantindo isolamento por {@code empresaId}.
 * </p>
 *
 * <p>
 * Este repositório é utilizado em fluxos como:
 * <ul>
 *     <li>Associação de atributos a produtos</li>
 *     <li>Consulta de atributos configurados por produto</li>
 *     <li>Validação de duplicidade de atributos</li>
 * </ul>
 * </p>
 */
@Repository
public interface ProdutoAtributoValorRepository extends BaseTenantRepository<ProdutoAtributoValor, Long> {

    /**
     * Recupera um registro específico dentro do tenant.
     *
     * @param id        Identificador do registro.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o registro, se encontrado.
     */
    Optional<ProdutoAtributoValor> findByIdAndEmpresaId(Long id, Long empresaId);

    /**
     * Lista todos os valores de atributos associados a um produto dentro do tenant.
     *
     * @param produtoId Identificador do produto.
     * @param empresaId Identificador da empresa (tenant).
     * @return Lista de atributos do produto.
     */
    List<ProdutoAtributoValor> findByProdutoIdAndEmpresaId(Long produtoId, Long empresaId);

    /**
     * Verifica se já existe um valor de atributo específico
     * associado ao produto dentro do tenant.
     *
     * @param produtoId  Identificador do produto.
     * @param atributoId Identificador do atributo.
     * @param empresaId  Identificador da empresa (tenant).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByProdutoIdAndAtributoIdAndEmpresaId(
            Long produtoId,
            Long atributoId,
            Long empresaId
    );

    /**
     * Remove todos os atributos de um produto dentro do tenant.
     *
     * @param produtoId Identificador do produto.
     * @param empresaId Identificador da empresa (tenant).
     */
    void deleteByProdutoIdAndEmpresaId(Long produtoId, Long empresaId);

}
