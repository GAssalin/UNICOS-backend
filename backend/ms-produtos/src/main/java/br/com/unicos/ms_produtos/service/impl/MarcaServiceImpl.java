package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.marca.MarcaListDTO;
import br.com.unicos.ms_produtos.dto.marca.MarcaRequest;
import br.com.unicos.ms_produtos.dto.marca.MarcaResponse;
import br.com.unicos.ms_produtos.mapper.MarcaMapper;
import br.com.unicos.ms_produtos.model.Marca;
import br.com.unicos.ms_produtos.repository.MarcaRepository;
import br.com.unicos.ms_produtos.service.MarcaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelo gerenciamento de marcas.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository repository;
    private final MarcaMapper mapper;

    // ============================================================
    // SALVAR
    // ============================================================

    @Override
    public MarcaResponse salvar(MarcaRequest request) {

        if (repository.existsByNomeIgnoreCase(request.nome())) {
            throw new IllegalArgumentException("Já existe uma marca cadastrada com este nome.");
        }

        Marca marca = Marca.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .paisOrigem(request.paisOrigem())
                .build();

        return mapper.toResponse(repository.save(marca));
    }

    // ============================================================
    // ATUALIZAR
    // ============================================================

    @Override
    public MarcaResponse atualizar(Long id, MarcaRequest request) {

        Marca existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Marca não encontrada com ID: " + id));

        Optional<Marca> outraMarcaMesmoNome =
                repository.findByNomeIgnoreCase(request.nome());

        if (outraMarcaMesmoNome.isPresent() && !outraMarcaMesmoNome.get().getId().equals(id)) {
            throw new IllegalArgumentException("Já existe outra marca cadastrada com este nome.");
        }

        existente.setNome(request.nome());
        existente.setDescricao(request.descricao());
        existente.setPaisOrigem(request.paisOrigem());

        return mapper.toResponse(repository.save(existente));
    }

    // ============================================================
    // CONSULTAS
    // ============================================================

    @Override
    public Optional<MarcaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    @Override
    public List<MarcaResponse> listarTodas() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<MarcaListDTO> listarSimples() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Override
    public List<MarcaResponse> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ============================================================
    // DELETE E VALIDAÇÃO
    // ============================================================

    @Override
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Marca não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public boolean existePorNome(String nome) {
        return repository.existsByNomeIgnoreCase(nome);
    }
}
