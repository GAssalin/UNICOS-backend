package br.com.erp.ms_produtos.service.impl;

import br.com.erp.ms_produtos.dto.CategoriaListDTO;
import br.com.erp.ms_produtos.dto.CategoriaRequestDTO;
import br.com.erp.ms_produtos.dto.CategoriaResponseDTO;
import br.com.erp.ms_produtos.mapper.CategoriaMapper;
import br.com.erp.ms_produtos.model.Categoria;
import br.com.erp.ms_produtos.repository.CategoriaRepository;
import br.com.erp.ms_produtos.service.AbstractCrudService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pelas operações de negócio relacionadas à entidade Categoria.
 */
@Service
public class CategoriaServiceImpl extends AbstractCrudService<Categoria, Long> {

    private final CategoriaRepository repository;

    public CategoriaServiceImpl(CategoriaRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * Atualiza uma entidade Categoria existente no banco.
     */
    @Override
    @Transactional
    public Categoria atualizar(Long id, Categoria entity) {
        Categoria existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + id));

        existente.setNome(entity.getNome());
        existente.setDescricao(entity.getDescricao());

        return repository.save(existente);
    }

    /**
     * Lista todas as categorias em formato reduzido (id e nome).
     *
     * @return Lista de CategoriaListDTO.
     */
    public List<CategoriaListDTO> listarResumido() {
        List<Categoria> categorias = listarTodos();
        return CategoriaMapper.toListDTO(categorias);
    }

    /**
     * Cria uma nova categoria.
     *
     * @param dto CategoriaRequestDTO com os dados da categoria.
     * @return CategoriaResponseDTO com os dados da categoria criada.
     */
    @Transactional
    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        Categoria categoria = CategoriaMapper.toEntity(dto);
        Categoria salvo = salvar(categoria);
        return CategoriaMapper.toResponseDTO(salvo);
    }

    /**
     * Atualiza uma categoria existente via DTO.
     *
     * @param id  ID da categoria.
     * @param dto CategoriaRequestDTO com os novos dados.
     * @return CategoriaResponseDTO com os dados atualizados.
     */
    @Transactional
    public CategoriaResponseDTO atualizarDTO(Long id, CategoriaRequestDTO dto) {
        Categoria existente = buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + id));

        CategoriaMapper.updateEntity(existente, dto);
        Categoria atualizado = salvar(existente);

        return CategoriaMapper.toResponseDTO(atualizado);
    }

    /**
     * Retorna os detalhes de uma categoria específica.
     *
     * @param id ID da categoria.
     * @return CategoriaResponseDTO.
     */
    public CategoriaResponseDTO buscarDTOporId(Long id) {
        Categoria categoria = buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + id));

        return CategoriaMapper.toResponseDTO(categoria);
    }

    /**
     * Remove uma categoria pelo ID.
     *
     * @param id ID da categoria.
     */
    @Transactional
    public void deletarPorId(Long id) {
        if (buscarPorId(id).isEmpty()) {
            throw new RuntimeException("Categoria não encontrada com ID: " + id);
        }
        deletar(id);
    }
}