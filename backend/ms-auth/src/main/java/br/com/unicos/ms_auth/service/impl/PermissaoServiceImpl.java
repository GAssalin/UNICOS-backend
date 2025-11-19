package br.com.unicos.ms_auth.service.impl;

import br.com.unicos.ms_auth.dto.permissao.PermissaoRequest;
import br.com.unicos.ms_auth.dto.permissao.PermissaoResponse;
import br.com.unicos.ms_auth.model.Permissao;
import br.com.unicos.ms_auth.repository.PermissaoRepository;
import br.com.unicos.ms_auth.service.PermissaoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação do serviço responsável pelas regras de negócio
 * relacionadas à entidade Permissao.
 */
@Service
@RequiredArgsConstructor
public class PermissaoServiceImpl implements PermissaoService {

    private final PermissaoRepository permissaoRepository;

    /**
     * Cria uma nova permissão no sistema.
     */
    @Override
    public PermissaoResponse salvar(PermissaoRequest request) {

        if (permissaoRepository.existsByCodigo(request.codigo())) {
            throw new IllegalArgumentException("Já existe uma permissão cadastrada com o código informado.");
        }

        Permissao entity = Permissao.builder()
                .codigo(request.codigo())
                .descricao(request.descricao())
                .build();

        permissaoRepository.save(entity);

        return toResponse(entity);
    }

    /**
     * Atualiza uma permissão existente.
     */
    @Override
    public PermissaoResponse atualizar(Long id, PermissaoRequest request) {

        Permissao entity = permissaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada: " + id));

        // Se o código for alterado, verificar duplicidade
        if (!entity.getCodigo().equals(request.codigo()) &&
                permissaoRepository.existsByCodigo(request.codigo())) {

            throw new IllegalArgumentException("Já existe uma permissão cadastrada com o código informado.");
        }

        entity.setCodigo(request.codigo());
        entity.setDescricao(request.descricao());

        permissaoRepository.save(entity);

        return toResponse(entity);
    }

    /**
     * Busca uma permissão pelo ID.
     */
    @Override
    public PermissaoResponse buscarPorId(Long id) {

        Permissao entity = permissaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada: " + id));

        return toResponse(entity);
    }

    /**
     * Lista todas as permissões cadastradas.
     */
    @Override
    public List<PermissaoResponse> listarTodas() {

        return permissaoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Remove uma permissão do sistema.
     */
    @Override
    public void deletar(Long id) {

        if (!permissaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Permissão não encontrada: " + id);
        }

        permissaoRepository.deleteById(id);
    }

    /**
     * Verifica se já existe uma permissão com o código informado.
     */
    @Override
    public boolean existePorCodigo(String codigo) {
        return permissaoRepository.existsByCodigo(codigo);
    }

    /**
     * Converte entidade Permissao em PermissaoResponse.
     */
    private PermissaoResponse toResponse(Permissao entity) {
        return new PermissaoResponse(
                entity.getId(),
                entity.getCodigo(),
                entity.getDescricao()
        );
    }
}
