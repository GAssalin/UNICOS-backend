package br.com.unicos.ms_estoque.service;

import br.com.unicos.ms_estoque.dto.EstoqueLocalListDTO;
import br.com.unicos.ms_estoque.dto.EstoqueLocalRequest;
import br.com.unicos.ms_estoque.dto.EstoqueLocalResponse;
import br.com.unicos.ms_estoque.enums.TipoLocalEstoque;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas aos locais de estoque.
 */
public interface EstoqueLocalService {

    /**
     * Cria um novo local de estoque.
     */
    @Transactional
    EstoqueLocalResponse salvar(EstoqueLocalRequest request);

    /**
     * Atualiza um local de estoque existente.
     */
    @Transactional
    EstoqueLocalResponse atualizar(Long id, EstoqueLocalRequest request);

    /**
     * Exclui um local de estoque pelo ID.
     */
    @Transactional
    void excluir(Long id);

    /**
     * Lista todos os locais de estoque cadastrados.
     */
    List<EstoqueLocalListDTO> listarTodos();

    /**
     * Busca um local de estoque pelo ID.
     */
    EstoqueLocalResponse buscarPorId(Long id);

    /**
     * Busca locais de estoque por tipo.
     */
    List<EstoqueLocalListDTO> buscarPorTipo(TipoLocalEstoque tipo);

    /**
     * Busca locais de estoque pertencentes a uma empresa.
     */
    List<EstoqueLocalListDTO> buscarPorEmpresa(Long empresaId);
}
