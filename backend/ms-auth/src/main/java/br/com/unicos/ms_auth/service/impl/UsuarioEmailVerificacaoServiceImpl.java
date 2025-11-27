package br.com.unicos.ms_auth.service.impl;

import br.com.unicos.ms_auth.model.Usuario;
import br.com.unicos.ms_auth.model.UsuarioEmailVerificacao;
import br.com.unicos.ms_auth.repository.UsuarioEmailVerificacaoRepository;
import br.com.unicos.ms_auth.repository.UsuarioRepository;
import br.com.unicos.ms_auth.service.interfaces.UsuarioEmailVerificacaoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementação responsável pelo fluxo de verificação de e-mail do usuário.
 * <p>
 * Inclui:
 * - geração do token de verificação
 * - validação da expiração
 * - marcação do e-mail como verificado
 * - reenvio de token
 * - limpeza de tokens expirados
 * <p>
 * Tokens são armazenados como hash por segurança.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UsuarioEmailVerificacaoServiceImpl implements UsuarioEmailVerificacaoService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioEmailVerificacaoRepository verificacaoRepository;

    private static final int EXPIRACAO_MINUTOS = 30;

    @Override
    public String gerarTokenParaUsuario(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + usuarioId));

        if (usuario.isEmailVerificado())
            throw new IllegalStateException("O e-mail deste usuário já está verificado.");

        verificacaoRepository.findByUsuarioIdAndUtilizadoFalse(usuarioId)
                .ifPresent(token -> {
                    token.setUtilizado(true);
                    verificacaoRepository.save(token);
                });

        String token = UUID.randomUUID().toString();
        String tokenHash = hash(token);

        UsuarioEmailVerificacao verificacao = UsuarioEmailVerificacao.builder()
                .usuario(usuario)
                .tokenHash(tokenHash)
                .expiracao(LocalDateTime.now().plusMinutes(EXPIRACAO_MINUTOS))
                .utilizado(false)
                .build();

        verificacaoRepository.save(verificacao);

        return token;
    }

    @Override
    public void confirmarEmail(String token) {

        String tokenHash = hash(token);
        LocalDateTime agora = LocalDateTime.now();

        UsuarioEmailVerificacao verificacao = verificacaoRepository
                .findByTokenHashAndExpiracaoAfter(tokenHash, agora)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido ou expirado."));

        if (verificacao.isUtilizado())
            throw new IllegalStateException("Este link de verificação já foi utilizado.");

        Usuario usuario = verificacao.getUsuario();

        usuario.setEmailVerificado(true);
        verificacao.setUtilizado(true);

        usuarioRepository.save(usuario);
        verificacaoRepository.save(verificacao);

        log.info("E-mail verificado com sucesso para o usuário {}", usuario.getEmail());
    }

    @Override
    public String reenviarToken(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + usuarioId));

        if (usuario.isEmailVerificado())
            throw new IllegalStateException("O e-mail deste usuário já está verificado.");

        return gerarTokenParaUsuario(usuarioId);
    }

    @Override
    public void limparTokensExpirados() {
        LocalDateTime agora = LocalDateTime.now();

        List<UsuarioEmailVerificacao> expirados = verificacaoRepository.findByExpiracaoBefore(agora);

        expirados.forEach(token -> token.setUtilizado(true));

        verificacaoRepository.saveAll(expirados);

        log.info("Tokens de verificação expirados marcados como utilizados: {}", expirados.size());
    }

    private String hash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();

            for (byte b : hashedBytes)
                sb.append(String.format("%02x", b));

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash do token", e);
        }
    }
}
