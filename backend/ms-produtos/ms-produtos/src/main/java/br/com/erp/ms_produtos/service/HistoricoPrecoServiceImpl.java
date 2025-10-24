package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.model.HistoricoPreco;
import br.com.erp.ms_produtos.repository.HistoricoPrecoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoricoPrecoServiceImpl implements HistoricoPrecoService {

    private final HistoricoPrecoRepository historicoPrecoRepository;

    @Override
    @Transactional
    public HistoricoPreco salvar(HistoricoPreco historico) {
        return historicoPrecoRepository.save(historico);
    }

    @Override
    @Transactional
    public HistoricoPreco atualizar(Long id, HistoricoPreco historico) {
        HistoricoPreco existente = historicoPrecoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Histórico de preço não encontrado"));
        historico.setId(existente.getId());
        return historicoPrecoRepository.save(historico);
    }

    @Override
    public Optional<HistoricoPreco> buscarPorId(Long id) {
        return historicoPrecoRepository.findById(id);
    }

    @Override
    public List<HistoricoPreco> listarPorProduto(Long produtoId) {
        return historicoPrecoRepository.findByProdutoIdOrderByDataAlteracaoDesc(produtoId);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        historicoPrecoRepository.deleteById(id);
    }
}