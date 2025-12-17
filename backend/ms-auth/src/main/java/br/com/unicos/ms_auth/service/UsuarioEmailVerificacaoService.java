package br.com.unicos.ms_auth.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_auth.dto.verificacao.UsuarioEmailVerificacaoListDTO;
import br.com.unicos.ms_auth.mapper.UsuarioEmailVerificacaoMapper;
import br.com.unicos.ms_auth.model.Usuario;
import br.com.unicos.ms_auth.model.UsuarioEmailVerificacao;
import br.com.unicos.ms_auth.repository.UsuarioEmailVerificacaoRepository;
import br.com.unicos.ms_auth.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pelo fluxo de verificação de e-mail do usuário.
 *
 * <p>
 * Fluxos suportados:
 * <ul>
 *   <li>Geração de token</li>
 *   <li>Confirmação de e-mail</li>
 *   <li>Reenvio de token</li>
 *   <li>Limpeza de tokens expirados</li>
 * </ul>
 *
 * <p>
 * Tokens são armazenados apenas como hash por segurança.
 * Todos os fluxos são isolados por empresa (tenant).
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioEmailVerificacaoService {

    private static final int EXPIRACAO_MINUTOS = 30;

    private final UsuarioRepository usuarioRepository;
    private final UsuarioEmailVerificacaoRepository verificacaoRepository;
    private final UsuarioEmailVerificacaoMapper usuarioEmailVerificacaoMapper;

    /**
     * Gera um novo token de verificação para um usuário.
     */
    @Transactional
    public String gerarTokenParaUsuario(Long usuarioId) {

        Usuario usuario = buscarUsuario(usuarioId);

        if (usuario.isEmailVerificado())
            throw new IllegalStateException("O e-mail deste usuário já está verificado.");

        // Invalida token anterior pendente (se existir)
        verificacaoRepository
                .findByUsuarioIdAndUtilizadoFalseAndEmpresaId(usuarioId, TenantContext.getEmpresaId())
                .ifPresent(token -> {
                    token.setUtilizado(true);
                    verificacaoRepository.save(token);
                });

        String token = UUID.randomUUID().toString();
        String tokenHash = gerarHash(token);

        UsuarioEmailVerificacao verificacao = UsuarioEmailVerificacao.builder()
                .usuario(usuario)
                .tokenHash(tokenHash)
                .expiracao(LocalDateTime.now().plusMinutes(EXPIRACAO_MINUTOS))
                .utilizado(false)
                .empresaId(TenantContext.getEmpresaId())
                .build();

        verificacaoRepository.save(verificacao);

        log.info(
                "Token de verificação gerado para usuário {} no tenant {}",
                usuario.getEmail(),
                TenantContext.getEmpresaId()
        );

        return token;
    }

    /**
     * Confirma o e-mail do usuário a partir do token recebido.
     */
    @Transactional
    public Usuario confirmarEmail(String token) {

        String tokenHash = gerarHash(token);
        LocalDateTime agora = LocalDateTime.now();

        UsuarioEmailVerificacao verificacao = verificacaoRepository
                .findByTokenHashAndExpiracaoAfterAndEmpresaId(
                        tokenHash,
                        agora,
                        TenantContext.getEmpresaId()
                )
                .orElseThrow(() -> new IllegalArgumentException("Token inválido ou expirado."));

        if (verificacao.isUtilizado())
            throw new IllegalStateException("Este link de verificação já foi utilizado.");

        Usuario usuario = verificacao.getUsuario();

        usuario.setEmailVerificado(true);
        verificacao.setUtilizado(true);

        usuarioRepository.save(usuario);
        verificacaoRepository.save(verificacao);

        log.info(
                "E-mail verificado com sucesso para o usuário {} no tenant {}",
                usuario.getEmail(),
                TenantContext.getEmpresaId()
        );

        return usuario;
    }

    /**
     * Reenvia um novo token de verificação para o usuário.
     */
    public String reenviarToken(Long usuarioId) {
        return gerarTokenParaUsuario(usuarioId);
    }

    /**
     * Marca tokens expirados como utilizados (rotina de limpeza).
     */
    @Transactional
    public void limparTokensExpirados() {

        LocalDateTime agora = LocalDateTime.now();

        List<UsuarioEmailVerificacao> expirados = verificacaoRepository
                .findByExpiracaoBeforeAndEmpresaId(agora, TenantContext.getEmpresaId(), null)
                .getContent();

        expirados.forEach(token -> token.setUtilizado(true));

        verificacaoRepository.saveAll(expirados);

        log.info(
                "Tokens de verificação expirados processados: {} (tenant {})",
                expirados.size(),
                TenantContext.getEmpresaId()
        );
    }

    @Transactional(readOnly = true)
    public Page<UsuarioEmailVerificacaoListDTO> listarPendentes(Long empresaId, Pageable pageable) {
        return verificacaoRepository
                .findByUtilizadoFalseAndEmpresaId(empresaId, pageable)
                .map(usuarioEmailVerificacaoMapper::toListDTO);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioEmailVerificacaoListDTO> listarExpirados(Long empresaId, Pageable pageable) {
        return verificacaoRepository
                .findByExpiracaoBeforeAndEmpresaId(
                        LocalDateTime.now(),
                        empresaId,
                        pageable
                )
                .map(usuarioEmailVerificacaoMapper::toListDTO);
    }

    // ============================================================
    // Métodos auxiliares
    // ============================================================

    @Transactional(readOnly = true)
    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .filter(u -> TenantContext.getEmpresaId().equals(u.getEmpresaId()))
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado no tenant informado: " + usuarioId));
    }

    private String gerarHash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(
                    rawToken.getBytes(StandardCharsets.UTF_8)
            );

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Erro ao gerar hash do token de verificação", e);
        }
    }
}
