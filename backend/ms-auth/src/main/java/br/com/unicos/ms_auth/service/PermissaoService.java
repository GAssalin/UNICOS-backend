package br.com.unicos.ms_auth.service;

import br.com.unicos.ms_auth.dto.permissao.PermissaoRequest;
import br.com.unicos.ms_auth.dto.permissao.PermissaoResponse;
import br.com.unicos.ms_auth.mapper.PermissaoMapper;
import br.com.unicos.ms_auth.model.Permissao;
import br.com.unicos.ms_auth.repository.PermissaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação do serviço responsável pelas regras de negócio
 * relacionadas à entidade {@link Permissao}.
 */
@Service
@RequiredArgsConstructor
public class PermissaoService {

    private final PermissaoRepository repository;
    private final PermissaoMapper mapper;

    // ============================================================
    // CREATE
    // ============================================================

    @Transactional
    public PermissaoResponse salvar(PermissaoRequest request) {
        validarNomeDuplicado(request.nome());

        Permissao entity = Permissao.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .build();

        return mapper.toResponse(repository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Transactional
    public PermissaoResponse atualizar(Long id, PermissaoRequest request) {
        Permissao entity = buscarEntidadePorId(id);

        if (!entity.getNome().equalsIgnoreCase(request.nome())) {
            validarNomeDuplicado(request.nome());
            entity.setNome(request.nome());
        }

        entity.setDescricao(request.descricao());

        return mapper.toResponse(repository.save(entity));
    }

    // ============================================================
    // GET BY ID
    // ============================================================

    @Transactional(readOnly = true)
    public PermissaoResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarEntidadePorId(id));
    }

    // ============================================================
    // LISTAGEM ADMINISTRATIVA (PAGINADA)
    // ============================================================

    @Transactional(readOnly = true)
    public Page<PermissaoResponse> listar(String nome, Pageable pageable) {
        Page<Permissao> page;

        if (nome == null || nome.isBlank())
            page = repository.findAll(pageable);
        else
            page = repository.findByNomeContainingIgnoreCase(nome, pageable);

        return page.map(mapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    public void deletar(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Permissão não encontrada: " + id);
        repository.deleteById(id);
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Permissao buscarEntidadePorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Permissão não encontrada: " + id));
    }

    private void validarNomeDuplicado(String nome) {
        if (repository.existsByNome(nome))
            throw new IllegalArgumentException("Já existe uma permissão cadastrada com o nome informado.");
    }
}
