package br.com.unicos.ms_produto.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_produto.model.ProdutoCodigoBarras;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ProdutoCodigoBarras}.
 *
 * <p>
 * Centraliza consultas relacionadas aos códigos de barras (EAN/GTIN)
 * associados a produtos, garantindo isolamento por {@code empresaId}.
 * </p>
 *
 * <p>
 * Este repositório é utilizado em fluxos como:
 * <ul>
 *     <li>Cadastro e manutenção de múltiplos códigos de barras por produto</li>
 *     <li>Definição e consulta de código principal</li>
 *     <li>Busca de produto via leitura de código de barras</li>
 * </ul>
 * </p>
 */
@Repository
public interface ProdutoCodigoBarrasRepository extends BaseTenantRepository<ProdutoCodigoBarras, Long> {

    /**
     * Recupera um registro específico dentro do tenant.
     *
     * @param id        Identificador do registro.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o registro, se encontrado.
     */
    Optional<ProdutoCodigoBarras> findByIdAndEmpresaId(Long id, Long empresaId);

    /**
     * Recupera um código de barras dentro do tenant.
     *
     * @param codigoBarras Código de barras (EAN/GTIN).
     * @param empresaId    Identificador da empresa (tenant).
     * @return {@link Optional} contendo o registro, se encontrado.
     */
    Optional<ProdutoCodigoBarras> findByCodigoBarrasAndEmpresaId(String codigoBarras, Long empresaId);

    /**
     * Verifica se já existe um código de barras cadastrado dentro do tenant.
     *
     * @param codigoBarras Código de barras (EAN/GTIN).
     * @param empresaId    Identificador da empresa (tenant).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByCodigoBarrasAndEmpresaId(String codigoBarras, Long empresaId);

    /**
     * Lista os códigos de barras de um produto dentro do tenant.
     *
     * @param produtoId Identificador do produto.
     * @param empresaId Identificador da empresa (tenant).
     * @return Lista de códigos de barras do produto.
     */
    List<ProdutoCodigoBarras> findByProdutoIdAndEmpresaId(Long produtoId, Long empresaId);

    /**
     * Recupera o código principal de um produto dentro do tenant.
     *
     * @param produtoId Identificador do produto.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o código principal, se existir.
     */
    Optional<ProdutoCodigoBarras> findByProdutoIdAndPrincipalTrueAndEmpresaId(Long produtoId, Long empresaId);

    /**
     * Remove todos os códigos de barras de um produto dentro do tenant.
     *
     * @param produtoId Identificador do produto.
     * @param empresaId Identificador da empresa (tenant).
     */
    void deleteByProdutoIdAndEmpresaId(Long produtoId, Long empresaId);

}
