package br.com.unicos.ms_autenticacao.loader;

import br.com.unicos.core.auth.model.AuthenticatedUser;
import br.com.unicos.core.usuario.dto.UsuarioAuthResponse;
import br.com.unicos.ms_autenticacao.service.UsuarioService;
import feign.FeignException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AutenticacaoLoader {

    private static final String MENSAGEM_CREDENCIAIS_INVALIDAS = "Usuário ou senha inválidos";

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Hash de um valor descartável, comparado quando o usuário não existe para que o tempo
     * de resposta não revele quais e-mails estão cadastrados.
     */
    private final String hashFicticio;

    public AutenticacaoLoader(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.hashFicticio = passwordEncoder.encode(UUID.randomUUID().toString());
    }

    public AuthenticatedUser authenticate(String email, String senha) {
        final UsuarioAuthResponse user = buscarUsuarioPorEmail(email);

        if (user == null || user.passwordHash() == null) {
            passwordEncoder.matches(senha, hashFicticio);
            throw new BadCredentialsException(MENSAGEM_CREDENCIAIS_INVALIDAS);
        }

        if (!passwordEncoder.matches(senha, user.passwordHash()))
            throw new BadCredentialsException(MENSAGEM_CREDENCIAIS_INVALIDAS);

        if (!user.ativo())
            throw new DisabledException("Usuário desabilitado");

        return new AuthenticatedUser(
                user.userId(),
                user.login(),
                user.passwordHash(),
                user.empresaId()
        );
    }

    private UsuarioAuthResponse buscarUsuarioPorEmail(String email) {
        try {
            return usuarioService.buscarUsuarioPorEmail(email);
        } catch (FeignException.NotFound ex) {
            return null;
        }
    }
}
