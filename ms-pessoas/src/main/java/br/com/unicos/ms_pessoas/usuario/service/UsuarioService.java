package br.com.unicos.ms_pessoas.usuario.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.core.usuario.context.UserContext;
import br.com.unicos.core.usuario.dto.UsuarioAuthResponse;
import br.com.unicos.core.usuario.dto.UsuarioResumoResponse;
import br.com.unicos.ms_pessoas.client.PermissaoService;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import br.com.unicos.ms_pessoas.usuario.dto.permissao.RoleResumoResponse;
import br.com.unicos.ms_pessoas.usuario.dto.usuario.UsuarioRequest;
import br.com.unicos.ms_pessoas.usuario.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_pessoas.usuario.mapper.UsuarioMapper;
import br.com.unicos.ms_pessoas.usuario.model.Usuario;
import br.com.unicos.ms_pessoas.usuario.model.UsuarioEmailVerificacao;
import br.com.unicos.ms_pessoas.usuario.repository.UsuarioEmailVerificacaoRepository;
import br.com.unicos.ms_pessoas.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UsuarioService extends BaseTenantService<Usuario, Long> {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioEmailVerificacaoRepository verificacaoRepository;
    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final PermissaoService permissaoService;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioEmailVerificacaoRepository verificacaoRepository, PessoaRepository pessoaRepository, PasswordEncoder passwordEncoder, UsuarioMapper usuarioMapper, PermissaoService permissaoService) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.verificacaoRepository = verificacaoRepository;
        this.pessoaRepository = pessoaRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.permissaoService = permissaoService;
    }

    // ============================================================
    // CONSULTAS INTERNAS (ms-autenticacao)
    // ============================================================

    /**
     * Dados para autenticação por e-mail. A busca é global, pois a empresa só é conhecida após o login.
     */
    @Transactional(readOnly = true)
    public UsuarioAuthResponse buscarParaAutenticacao(String email) {
        return toAuthResponse(usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado")));
    }

    /**
     * Dados para renovação de token, a partir do identificador presente no refresh token.
     */
    @Transactional(readOnly = true)
    public UsuarioAuthResponse buscarParaAutenticacao(Long id) {
        return toAuthResponse(usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado")));
    }

    /**
     * Nome de exibição do usuário: o nome da pessoa vinculada ou, na ausência dela, o login.
     */
    @Transactional(readOnly = true)
    public UsuarioResumoResponse buscarResumo(Long id) {
        Usuario usuario = buscarUsuario(id);

        String nome = usuario.getPessoaId() == null
                ? usuario.getLogin()
                : pessoaRepository.findByIdAndEmpresaId(usuario.getPessoaId(), usuario.getEmpresaId())
                        .map(Pessoa::getNome)
                        .orElse(usuario.getLogin());

        return new UsuarioResumoResponse(usuario.getId(), nome);
    }

    // ============================================================
    // CRUD
    // ============================================================

    @Transactional
    public UsuarioResponse salvar(UsuarioRequest request) {
        validarLoginDisponivel(request.login());
        validarEmailDisponivel(request.email());
        validarPessoa(request.pessoaId());

        RoleResumoResponse role = permissaoService.buscarRolePorId(request.roleId());

        Usuario usuario = Usuario.builder()
                .login(request.login())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .pessoaId(request.pessoaId())
                .roleId(role.id())
                .ativo(request.ativo() != null ? request.ativo() : true)
                .empresaId(TenantContext.getEmpresaId())
                .emailVerificado(false)
                .build();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuarioSalvo, role.nome());
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = buscarUsuario(id);

        if (!usuario.getLogin().equalsIgnoreCase(request.login()))
            validarLoginDisponivel(request.login());
        usuario.setLogin(request.login());

        if (!usuario.getEmail().equalsIgnoreCase(request.email())) {
            validarEmailDisponivel(request.email());
            usuario.setEmail(request.email());
            usuario.setEmailVerificado(false);
            invalidarVerificacoesPendentes(usuario);
        }

        validarPessoa(request.pessoaId());
        usuario.setPessoaId(request.pessoaId());

        if (request.ativo() != null) {
            validarAlteracaoDoProprioStatus(usuario, request.ativo());
            usuario.setAtivo(request.ativo());
        }

        validarAlteracaoDaPropriaRole(usuario, request.roleId());
        RoleResumoResponse role = permissaoService.buscarRolePorId(request.roleId());
        usuario.setRoleId(role.id());

        if (request.password() != null && !request.password().isBlank())
            usuario.setPassword(passwordEncoder.encode(request.password()));

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuarioAtualizado, role.nome());
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        return toResponseComRole(buscarUsuario(id));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorLogin(String login) {
        Usuario usuario = usuarioRepository
                .findByLoginIgnoreCaseAndEmpresaId(login, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + login));

        return toResponseComRole(usuario);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarTodos(Pageable pageable) {
        return comRoles(usuarioRepository.findAllByEmpresaId(TenantContext.getEmpresaId(), pageable));
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarAtivos(Pageable pageable) {
        return comRoles(usuarioRepository
                .findByAtivoTrueAndEmailVerificadoTrueAndEmpresaId(TenantContext.getEmpresaId(), pageable));
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarInativos(Pageable pageable) {
        return comRoles(usuarioRepository
                .findByAtivoFalseAndEmailVerificadoTrueAndEmpresaId(TenantContext.getEmpresaId(), pageable));
    }

    @Transactional
    public void ativar(Long id) {
        Usuario usuario = buscarUsuario(id);

        if (Boolean.TRUE.equals(usuario.getAtivo()))
            return;

        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void desativar(Long id) {
        Usuario usuario = buscarUsuario(id);

        if (Boolean.FALSE.equals(usuario.getAtivo()))
            return;

        validarAlteracaoDoProprioStatus(usuario, false);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = buscarUsuario(id);

        if (usuario.getId().equals(UserContext.getUsuarioId()))
            throw new IllegalStateException("O usuário não pode excluir a própria conta.");

        usuarioRepository.delete(usuario);
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Usuario buscarUsuario(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));
    }

    private UsuarioAuthResponse toAuthResponse(Usuario usuario) {
        return new UsuarioAuthResponse(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getPassword(),
                usuario.getEmpresaId(),
                Boolean.TRUE.equals(usuario.getAtivo())
        );
    }

    private UsuarioResponse toResponseComRole(Usuario usuario) {
        String roleNome = permissaoService.buscarRolePorId(usuario.getRoleId()).nome();
        return usuarioMapper.toResponse(usuario, roleNome);
    }

    /**
     * Resolve o nome de cada role uma única vez por página, evitando uma chamada ao
     * ms-permissao para cada usuário.
     */
    private Page<UsuarioResponse> comRoles(Page<Usuario> usuarios) {
        Map<Long, String> nomesRoles = new HashMap<>();

        return usuarios.map(usuario -> usuarioMapper.toResponse(
                usuario,
                nomesRoles.computeIfAbsent(usuario.getRoleId(), roleId -> permissaoService.buscarRolePorId(roleId).nome())
        ));
    }

    /**
     * Login e e-mail são únicos em toda a base, pois identificam o usuário antes de se conhecer a empresa.
     */
    private void validarLoginDisponivel(String login) {
        if (usuarioRepository.existsByLoginIgnoreCase(login))
            throw new IllegalArgumentException("Já existe um usuário com o login informado.");
    }

    private void validarEmailDisponivel(String email) {
        if (usuarioRepository.existsByEmailIgnoreCase(email))
            throw new IllegalArgumentException("Já existe um usuário com o e-mail informado.");
    }

    private void validarPessoa(Long pessoaId) {
        if (pessoaId != null && !pessoaRepository.existsByIdAndEmpresaId(pessoaId, TenantContext.getEmpresaId()))
            throw new EntityNotFoundException("Pessoa não encontrada: " + pessoaId);
    }

    private void validarAlteracaoDoProprioStatus(Usuario usuario, boolean novoStatus) {
        if (!novoStatus && usuario.getId().equals(UserContext.getUsuarioId()))
            throw new IllegalStateException("O usuário não pode desativar a própria conta.");
    }

    /**
     * Impede a autoelevação de privilégios: quem pode editar usuários não pode trocar a própria role.
     */
    private void validarAlteracaoDaPropriaRole(Usuario usuario, Long novaRoleId) {
        if (usuario.getId().equals(UserContext.getUsuarioId()) && !usuario.getRoleId().equals(novaRoleId))
            throw new IllegalStateException("O usuário não pode alterar a própria role.");
    }

    /**
     * Links de verificação emitidos para o e-mail anterior não podem confirmar o novo endereço.
     */
    private void invalidarVerificacoesPendentes(Usuario usuario) {
        List<UsuarioEmailVerificacao> pendentes = verificacaoRepository
                .findByUsuarioIdAndUtilizadoFalseAndEmpresaId(usuario.getId(), usuario.getEmpresaId());
        pendentes.forEach(verificacao -> verificacao.setUtilizado(true));
        verificacaoRepository.saveAll(pendentes);
    }
}
