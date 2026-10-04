package br.com.unicos.ms_permissao.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.core.usuario.context.UserContext;
import br.com.unicos.core.usuario.dto.UsuarioRoleIdsResponse;
import br.com.unicos.ms_permissao.client.UsuarioService;
import br.com.unicos.ms_permissao.dto.permissao.PermissaoRequest;
import br.com.unicos.ms_permissao.dto.permissao.PermissaoResponse;
import br.com.unicos.ms_permissao.mapper.PermissaoMapper;
import br.com.unicos.ms_permissao.model.Permissao;
import br.com.unicos.ms_permissao.repository.PermissaoRepository;
import br.com.unicos.ms_permissao.repository.RolePermissaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Regras de negócio das permissões.
 *
 * <p>
 * As permissões formam um catálogo global: qualquer empresa pode consultá-las e vinculá-las
 * às suas roles, mas apenas a empresa que cadastrou uma permissão pode alterá-la ou excluí-la.
 * </p>
 */
@Service
@Slf4j
public class PermissaoService extends BaseTenantService<Permissao, Long> {

    private final PermissaoRepository repository;
    private final RolePermissaoRepository rolePermissaoRepository;
    private final UsuarioService usuarioService;
    private final PermissaoMapper mapper;

    public PermissaoService(PermissaoRepository repository, RolePermissaoRepository rolePermissaoRepository, UsuarioService usuarioService, PermissaoMapper mapper) {
        super(repository);
        this.repository = repository;
        this.rolePermissaoRepository = rolePermissaoRepository;
        this.usuarioService = usuarioService;
        this.mapper = mapper;
    }

    @Transactional
    public PermissaoResponse salvar(PermissaoRequest request) {
        validarNomeDisponivel(request.nome());

        Permissao entity = Permissao.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .empresaId(TenantContext.getEmpresaId())
                .build();

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public PermissaoResponse atualizar(Long id, PermissaoRequest request) {
        Permissao entity = buscarDaEmpresa(id);

        if (!entity.getNome().equalsIgnoreCase(request.nome()))
            validarNomeDisponivel(request.nome());

        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public PermissaoResponse buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public Page<PermissaoResponse> listar(String nome, Pageable pageable) {
        Page<Permissao> page = (nome == null || nome.isBlank())
                ? repository.findAll(pageable)
                : repository.findByNomeContainingIgnoreCase(nome, pageable);

        return page.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public boolean usuarioPossuiPermissao(String nomePermissao) {
        Long roleId = buscarRoleDoUsuarioLogado();

        if (roleId == null)
            return false;

        return rolePermissaoRepository.rolePossuiPermissao(TenantContext.getEmpresaId(), roleId, nomePermissao);
    }

    @Transactional(readOnly = true)
    public List<String> listarPermissoesDoUsuarioLogado() {
        Long roleId = buscarRoleDoUsuarioLogado();

        if (roleId == null)
            return List.of();

        return rolePermissaoRepository.listarNomesPermissoesDaRole(TenantContext.getEmpresaId(), roleId);
    }

    @Transactional
    public void deletar(Long id) {
        repository.delete(buscarDaEmpresa(id));
    }

    private Long buscarRoleDoUsuarioLogado() {
        UsuarioRoleIdsResponse usuarioRole = usuarioService.buscarRoleIdsDoUsuario(UserContext.getUsuarioId());
        return usuarioRole != null ? usuarioRole.idRole() : null;
    }

    private Permissao buscarDaEmpresa(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada: " + id));
    }

    private void validarNomeDisponivel(String nome) {
        if (repository.existsByNomeIgnoreCase(nome))
            throw new IllegalArgumentException("Já existe uma permissão cadastrada com o nome informado.");
    }
}
