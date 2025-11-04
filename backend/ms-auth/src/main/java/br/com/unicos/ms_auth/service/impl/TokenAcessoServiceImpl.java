package br.com.unicos.ms_auth.service.impl;

import br.com.unicos.ms_auth.dto.TokenAcessoResponse;
import br.com.unicos.ms_auth.model.TokenAcesso;
import br.com.unicos.ms_auth.model.Usuario;
import br.com.unicos.ms_auth.repository.TokenAcessoRepository;
import br.com.unicos.ms_auth.repository.UsuarioRepository;
import br.com.unicos.ms_auth.service.TokenAcessoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link TokenAcessoService}.
 * <p>
 * Responsável pela gestão de tokens de acesso JWT emitidos.
 */
@Service
@RequiredArgsConstructor
public class TokenAcessoServiceImpl implements TokenAcessoService {

    private final TokenAcessoRepository tokenAcessoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public TokenAcessoResponse salvar(String token, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        TokenAcesso tokenAcesso = TokenAcesso.builder()
                .token(token)
                .usuario(usuario)
                .dataEmissao(LocalDateTime.now())
                .dataExpiracao(LocalDateTime.now().plusHours(4))
                .valido(true)
                .build();

        return modelMapper.map(tokenAcessoRepository.save(tokenAcesso), TokenAcessoResponse.class);
    }

    @Override
    @Transactional
    public void invalidarToken(String token) {
        TokenAcesso tokenAcesso = tokenAcessoRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Token não encontrado."));
        tokenAcesso.setValido(false);
        tokenAcessoRepository.save(tokenAcesso);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TokenAcessoResponse> buscarPorToken(String token) {
        return tokenAcessoRepository.findByToken(token)
                .map(t -> modelMapper.map(t, TokenAcessoResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TokenAcessoResponse> listarTokensValidosPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        return tokenAcessoRepository.findByUsuarioAndValidoTrue(usuario).stream()
                .map(t -> modelMapper.map(t, TokenAcessoResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletarPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
        List<TokenAcesso> tokens = tokenAcessoRepository.findByUsuarioAndValidoTrue(usuario);
        tokens.forEach(token -> token.setValido(false));
        tokenAcessoRepository.saveAll(tokens);
    }
}
