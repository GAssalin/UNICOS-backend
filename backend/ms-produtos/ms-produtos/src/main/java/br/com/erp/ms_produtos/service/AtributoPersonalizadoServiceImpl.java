package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.model.AtributoPersonalizado;
import br.com.erp.ms_produtos.repository.AtributoPersonalizadoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AtributoPersonalizadoServiceImpl implements AtributoPersonalizadoService {

    private final AtributoPersonalizadoRepository atributoRepository;

    @Override
    @Transactional
    public AtributoPersonalizado salvar(AtributoPersonalizado atributo) {
        return atributoRepository.save(atributo);
    }

    @Override
    @Transactional
    public AtributoPersonalizado atualizar(Long id, AtributoPersonalizado atributo) {
        AtributoPersonalizado existente = atributoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Atributo não encontrado"));
        atributo.setId(existente.getId());
        return atributoRepository.save(atributo);
    }

    @Override
    public Optional<AtributoPersonalizado> buscarPorId(Long id) {
        return atributoRepository.findById(id);
    }

    @Override
    public List<AtributoPersonalizado> listarPorProduto(Long produtoId) {
        return atributoRepository.findByProdutoId(produtoId);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        atributoRepository.deleteById(id);
    }
}