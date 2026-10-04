package br.com.unicos.ms_pessoas.usuario.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_pessoas.usuario.dto.verificacao.UsuarioEmailVerificacaoListDTO;
import br.com.unicos.ms_pessoas.usuario.mapper.UsuarioEmailVerificacaoMapper;
import br.com.unicos.ms_pessoas.usuario.model.Usuario;
import br.com.unicos.ms_pessoas.usuario.model.UsuarioEmailVerificacao;
import br.com.unicos.ms_pessoas.usuario.repository.UsuarioEmailVerificacaoRepository;
import br.com.unicos.ms_pessoas.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pelo fluxo de verificação de e-mail do usuário.
 */
@Service
@Slf4j
public class UsuarioEmailVerificacaoService extends BaseTenantService<UsuarioEmailVerificacao, Long> {

    private static final int EXPIRACAO_MINUTOS = 30;

    private final UsuarioEmailVerificacaoRepository verificacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioEmailVerificacaoMapper usuarioEmailVerificacaoMapper;

    public UsuarioEmailVerificacaoService(UsuarioRepository usuarioRepository, UsuarioEmailVerificacaoRepository verificacaoRepository, UsuarioEmailVerificacaoMapper usuarioEmailVerificacaoMapper) {
        super(verificacaoRepository);
        this.usuarioRepository = usuarioRepository;
        this.verificacaoRepository = verificacaoRepository;
        this.usuarioEmailVerificacaoMapper = usuarioEmailVerificacaoMapper;
    }

    /**
     * Gera um novo token de verificação, invalidando os tokens pendentes anteriores do usuário.
     *
     * @return token em texto puro (somente o hash é armazenado)
     */
    @Transactional
    public String gerarTokenParaUsuario(Long usuarioId) {
        Long empresaId = TenantContext.getEmpresaId();
        Usuario usuario = usuarioRepository.findByIdAndEmpresaId(usuarioId, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + usuarioId));

        if (usuario.isEmailVerificado())
            throw new IllegalStateException("O e-mail deste usuário já está verificado.");

        List<UsuarioEmailVerificacao> pendentes =
                verificacaoRepository.findByUsuarioIdAndUtilizadoFalseAndEmpresaId(usuarioId, empresaId);
        pendentes.forEach(verificacao -> verificacao.setUtilizado(true));
        verificacaoRepository.saveAll(pendentes);

        String token = UUID.randomUUID().toString();

        UsuarioEmailVerificacao verificacao = UsuarioEmailVerificacao.builder()
                .usuario(usuario)
                .tokenHash(gerarHash(token))
                .expiracao(LocalDateTime.now().plusMinutes(EXPIRACAO_MINUTOS))
                .utilizado(false)
                .empresaId(empresaId)
                .build();

        verificacaoRepository.save(verificacao);

        log.info("Token de verificação de e-mail gerado para o usuário {} (tenant {}).", usuarioId, empresaId);

        return token;
    }

    /**
     * Confirma o e-mail a partir do token recebido pelo usuário.
     *
     * <p>Endpoint público: o token aleatório é a credencial e identifica usuário e empresa.</p>
     */
    @Transactional
    public void confirmarEmail(String token) {
        if (token == null || token.isBlank())
            throw new IllegalArgumentException("Token inválido ou expirado.");

        UsuarioEmailVerificacao verificacao = verificacaoRepository
                .findByTokenHashAndExpiracaoAfter(gerarHash(token.trim()), LocalDateTime.now())
                .orElseThrow(() -> new IllegalArgumentException("Token inválido ou expirado."));

        if (verificacao.isUtilizado())
            throw new IllegalStateException("Este link de verificação já foi utilizado.");

        Usuario usuario = verificacao.getUsuario();

        usuario.setEmailVerificado(true);
        verificacao.setUtilizado(true);

        usuarioRepository.save(usuario);
        verificacaoRepository.save(verificacao);

        log.info("E-mail verificado com sucesso para o usuário {} (tenant {}).", usuario.getId(), usuario.getEmpresaId());
    }

    @Transactional
    public String reenviarToken(Long usuarioId) {
        return gerarTokenParaUsuario(usuarioId);
    }

    @Transactional
    public void limparTokensExpirados() {
        Long empresaId = TenantContext.getEmpresaId();

        List<UsuarioEmailVerificacao> expirados = verificacaoRepository
                .findByExpiracaoBeforeAndUtilizadoFalseAndEmpresaId(LocalDateTime.now(), empresaId);

        expirados.forEach(verificacao -> verificacao.setUtilizado(true));
        verificacaoRepository.saveAll(expirados);

        log.info("Tokens de verificação expirados processados: {} (tenant {}).", expirados.size(), empresaId);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioEmailVerificacaoListDTO> listarPendentes(Long empresaId, Pageable pageable) {
        return verificacaoRepository
                .findByUtilizadoFalseAndEmpresaId(resolverEmpresa(empresaId), pageable)
                .map(usuarioEmailVerificacaoMapper::toListDTO);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioEmailVerificacaoListDTO> listarExpirados(Long empresaId, Pageable pageable) {
        return verificacaoRepository
                .findByExpiracaoBeforeAndEmpresaId(LocalDateTime.now(), resolverEmpresa(empresaId), pageable)
                .map(usuarioEmailVerificacaoMapper::toListDTO);
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    /**
     * O parâmetro {@code empresaId} é mantido por compatibilidade; só é aceito quando
     * corresponde à empresa do usuário autenticado.
     */
    private static Long resolverEmpresa(Long empresaIdInformado) {
        Long empresaId = TenantContext.getEmpresaId();

        if (empresaIdInformado != null && !empresaIdInformado.equals(empresaId))
            throw new AccessDeniedException("Acesso negado aos dados de outra empresa.");

        return empresaId;
    }

    private static String gerarHash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(rawToken.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Erro ao gerar hash do token de verificação", e);
        }
    }
}
