package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.model.UnidadeMedida;
import br.com.erp.ms_produtos.repository.UnidadeMedidaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnidadeMedidaServiceImpl implements UnidadeMedidaService {

    private final UnidadeMedidaRepository unidadeMedidaRepository;

    @Override
    @Transactional
    public UnidadeMedida salvar(UnidadeMedida unidade) {
        return unidadeMedidaRepository.save(unidade);
    }

    @Override
    @Transactional
    public UnidadeMedida atualizar(Long id, UnidadeMedida unidade) {
        UnidadeMedida existente = unidadeMedidaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unidade de medida não encontrada"));
        unidade.setId(existente.getId());
        return unidadeMedidaRepository.save(unidade);
    }

    @Override
    public Optional<UnidadeMedida> buscarPorId(Long id) {
        return unidadeMedidaRepository.findById(id);
    }

    @Override
    public Optional<UnidadeMedida> buscarPorSigla(String sigla) {
        return unidadeMedidaRepository.findBySiglaIgnoreCase(sigla);
    }

    @Override
    public List<UnidadeMedida> listarTodas() {
        return unidadeMedidaRepository.findAll();
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        unidadeMedidaRepository.deleteById(id);
    }
}