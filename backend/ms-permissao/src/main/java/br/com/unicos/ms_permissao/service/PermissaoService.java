package br.com.unicos.ms_permissao.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.core.usuario.auth.context.UserContext;
import br.com.unicos.core.usuario.auth.dto.UsuarioRoleIdsResponse;
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
        validarNomeDuplicado(request.nome());

        Permissao entity = Permissao.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .build();

        return mapper.toResponse(save(entity));
    }

    @Transactional
    public PermissaoResponse atualizar(Long id, PermissaoRequest request) {
        Permissao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada: " + id));

        if (!entity.getNome().equalsIgnoreCase(request.nome())) {
            validarNomeDuplicado(request.nome());
            entity.setNome(request.nome());
        }

        entity.setDescricao(request.descricao());

        return mapper.toResponse(save(entity));
    }

    @Transactional(readOnly = true)
    public PermissaoResponse buscarPorId(Long id) {
        Permissao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada: " + id));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<PermissaoResponse> listar(String nome, Pageable pageable) {
        Page<Permissao> page;

        if (nome == null || nome.isBlank())
            page = findAllByEmpresaId(TenantContext.getEmpresaId(), pageable);
        else
            page = repository.findByNomeContainingIgnoreCaseAndEmpresaId(nome, TenantContext.getEmpresaId(), pageable);

        return page.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public boolean usuarioPossuiPermissao(String nomePermissao) {
        UsuarioRoleIdsResponse usuarioRole = usuarioService.buscarRoleIdsDoUsuario(UserContext.getUsuarioId());
        if (usuarioRole == null || usuarioRole.roleId() == null)
            return false;

        return rolePermissaoRepository.rolePossuiPermissao(
                TenantContext.getEmpresaId(),
                usuarioRole.roleId(),
                nomePermissao
        );
    }

    @Transactional(readOnly = true)
    public List<String> listarPermissoesDoUsuarioLogado() {
        Long userId = UserContext.getUsuarioId();

        UsuarioRoleIdsResponse usuarioRole = usuarioService.buscarRoleIdsDoUsuario(userId);
        if (usuarioRole == null || usuarioRole.roleId() == null)
            return List.of();

        return rolePermissaoRepository.listarNomesPermissoesDaRole(
                TenantContext.getEmpresaId(),
                usuarioRole.roleId()
        );
    }

    public void deletar(Long id) {
        if (!existsById(id))
            throw new EntityNotFoundException("Permissão não encontrada: " + id);
        repository.deleteById(id);
    }

    private void validarNomeDuplicado(String nome) {
        if (repository.existsByNomeContainingIgnoreCaseAndEmpresaId(nome, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe uma permissão cadastrada com o nome informado.");
    }
}
