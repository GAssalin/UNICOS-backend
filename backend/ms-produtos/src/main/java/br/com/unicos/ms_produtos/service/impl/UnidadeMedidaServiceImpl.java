package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaListDTO;
import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaRequest;
import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaResponse;
import br.com.unicos.ms_produtos.mapper.UnidadeMedidaMapper;
import br.com.unicos.ms_produtos.model.UnidadeMedida;
import br.com.unicos.ms_produtos.repository.UnidadeMedidaRepository;
import br.com.unicos.ms_produtos.service.interfaces.UnidadeMedidaService;
import br.com.unicos.ms_produtos.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link UnidadeMedidaService}
 * com isolamento multi-tenant via empresaId.
 */
@Service
@RequiredArgsConstructor
public class UnidadeMedidaServiceImpl implements UnidadeMedidaService {

    private final UnidadeMedidaRepository repository;
    private final UnidadeMedidaMapper mapper;
    private final ModelMapper modelMapper;

    // ============================================================
    // Criar
    // ============================================================

    @Override
    @Transactional
    public UnidadeMedidaResponse salvar(UnidadeMedidaRequest request) {

        Long empresaId = TenantContext.getEmpresaId();

        if (repository.existsByEmpresaIdAndSiglaIgnoreCase(empresaId, request.sigla())) {
            throw new IllegalArgumentException("Já existe uma unidade com esta sigla para a empresa.");
        }

        UnidadeMedida unidade = new UnidadeMedida();
        unidade.setEmpresaId(empresaId);
        unidade.setNome(request.nome());
        unidade.setSigla(request.sigla());
        unidade.setDescricao(request.descricao());
        unidade.setAtivo(true);

        return mapper.toResponse(repository.save(unidade));
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @Override
    @Transactional
    public UnidadeMedidaResponse atualizar(Long id, UnidadeMedidaRequest request) {

        Long empresaId = TenantContext.getEmpresaId();

        UnidadeMedida entidade = repository.findByEmpresaIdAndId(empresaId, id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Unidade de medida não encontrada.")
                );

        Optional<UnidadeMedida> outra =
                repository.findByEmpresaIdAndSiglaIgnoreCase(empresaId, request.sigla());

        if (outra.isPresent() && !outra.get().getId().equals(id)) {
            throw new IllegalArgumentException("Já existe outra unidade com esta sigla para a empresa.");
        }

        modelMapper.map(request, entidade);

        return mapper.toResponse(repository.save(entidade));
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<UnidadeMedidaResponse> buscarPorId(Long id) {
        return repository
                .findByEmpresaIdAndId(TenantContext.getEmpresaId(), id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Listar todas
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<UnidadeMedidaResponse> listarTodas() {
        return repository
                .findByEmpresaIdOrderByNomeAsc(TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ============================================================
    // Excluir
    // ============================================================

    @Override
    @Transactional
    public void deletar(Long id) {

        Long empresaId = TenantContext.getEmpresaId();

        UnidadeMedida unidade = repository.findByEmpresaIdAndId(empresaId, id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Unidade de medida não encontrada.")
                );

        repository.delete(unidade);
    }

    // ============================================================
    // Buscar por nome
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<UnidadeMedidaResponse> buscarPorNome(String nome) {
        return repository
                .findByEmpresaIdAndNomeIgnoreCase(TenantContext.getEmpresaId(), nome)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Buscar por sigla
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<UnidadeMedidaResponse> buscarPorSigla(String sigla) {
        return repository
                .findByEmpresaIdAndSiglaIgnoreCase(TenantContext.getEmpresaId(), sigla)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Buscar por nome contendo
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<UnidadeMedidaListDTO> buscarPorNomeContendo(String nome) {
        return repository
                .findByEmpresaIdAndNomeContainingIgnoreCase(
                        TenantContext.getEmpresaId(), nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listagem simples
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<UnidadeMedidaListDTO> listarSimples() {
        return repository
                .findByEmpresaIdOrderByNomeAsc(TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Verificar sigla
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public boolean verificarSiglaExistente(String sigla) {
        return repository.existsByEmpresaIdAndSiglaIgnoreCase(
                TenantContext.getEmpresaId(), sigla);
    }
}
