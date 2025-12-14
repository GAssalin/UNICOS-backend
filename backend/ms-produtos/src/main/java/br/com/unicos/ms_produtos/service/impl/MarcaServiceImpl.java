package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.tenant.TenantContext;
import br.com.unicos.ms_produtos.dto.marca.MarcaListDTO;
import br.com.unicos.ms_produtos.dto.marca.MarcaRequest;
import br.com.unicos.ms_produtos.dto.marca.MarcaResponse;
import br.com.unicos.ms_produtos.mapper.MarcaMapper;
import br.com.unicos.ms_produtos.model.Marca;
import br.com.unicos.ms_produtos.repository.MarcaRepository;
import br.com.unicos.ms_produtos.service.interfaces.MarcaService;
import br.com.unicos.ms_produtos.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelo gerenciamento de marcas,
 * com isolamento total por empresa (tenant).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository repository;
    private final MarcaMapper mapper;

    // ============================================================
    // SALVAR
    // ============================================================

    @Override
    public MarcaResponse salvar(MarcaRequest request) {

        Long empresaId = TenantContext.getEmpresaId();

        if (repository.existsByEmpresaIdAndNomeIgnoreCase(empresaId, request.nome())) {
            throw new IllegalArgumentException(
                    "Já existe uma marca cadastrada com este nome para esta empresa."
            );
        }

        Marca marca = Marca.builder()
                .empresaId(empresaId)
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

        Long empresaId = TenantContext.getEmpresaId();

        Marca existente = repository.findByEmpresaIdAndId(empresaId, id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Marca não encontrada para esta empresa.")
                );

        Optional<Marca> outraMarcaMesmoNome =
                repository.findByEmpresaIdAndNomeIgnoreCase(empresaId, request.nome());

        if (outraMarcaMesmoNome.isPresent()
                && !outraMarcaMesmoNome.get().getId().equals(id)) {
            throw new IllegalArgumentException(
                    "Já existe outra marca cadastrada com este nome para esta empresa."
            );
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
    @Transactional(readOnly = true)
    public Optional<MarcaResponse> buscarPorId(Long id) {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaIdAndId(empresaId, id)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaResponse> listarTodas() {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaIdOrderByNomeAsc(empresaId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaListDTO> listarSimples() {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaIdOrderByNomeAsc(empresaId)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaResponse> buscarPorNome(String nome) {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaIdAndNomeContainingIgnoreCase(empresaId, nome)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ============================================================
    // DELETE E VALIDAÇÃO
    // ============================================================

    @Override
    public void deletar(Long id) {

        Long empresaId = TenantContext.getEmpresaId();

        if (!repository.existsByEmpresaIdAndId(empresaId, id)) {
            throw new EntityNotFoundException(
                    "Marca não encontrada para esta empresa."
            );
        }

        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNome(String nome) {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.existsByEmpresaIdAndNomeIgnoreCase(empresaId, nome);
    }
}
