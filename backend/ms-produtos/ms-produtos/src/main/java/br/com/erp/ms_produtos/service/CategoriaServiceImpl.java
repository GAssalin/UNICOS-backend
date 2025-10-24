package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.model.Categoria;
import br.com.erp.ms_produtos.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl extends AbstractCrudService<Categoria, Long> implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional
    public Categoria atualizar(Long id, Categoria categoria) {
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        categoria.setId(existente.getId());
        return categoriaRepository.save(categoria);
    }

    @Override
    public Optional<Categoria> buscarPorNome(String nome) {
        return categoriaRepository.findByNomeIgnoreCase(nome);
    }

    @Override
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }
}