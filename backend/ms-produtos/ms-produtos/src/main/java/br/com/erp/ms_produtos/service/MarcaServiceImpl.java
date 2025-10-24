package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.model.Marca;
import br.com.erp.ms_produtos.repository.MarcaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarcaServiceImpl extends AbstractCrudService<Marca, Long> implements MarcaService {

    private final MarcaRepository marcaRepository;

    @Override
    @Transactional
    public Marca atualizar(Long id, Marca marca) {
        Marca existente = marcaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Marca não encontrada"));
        marca.setId(existente.getId());
        return marcaRepository.save(marca);
    }

    @Override
    public Optional<Marca> buscarPorNome(String nome) {
        return marcaRepository.findByNomeIgnoreCase(nome);
    }

    @Override
    public List<Marca> listarTodas() {
        return marcaRepository.findAll();
    }
}