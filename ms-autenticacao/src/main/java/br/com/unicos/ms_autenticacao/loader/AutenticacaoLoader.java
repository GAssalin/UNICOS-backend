package br.com.unicos.ms_autenticacao.loader;

import br.com.unicos.core.auth.model.AuthenticatedUser;
import br.com.unicos.core.usuario.auth.dto.UsuarioAuthResponse;
import br.com.unicos.ms_autenticacao.service.UsuarioService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutenticacaoLoader {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticatedUser authenticate(String email, String senha) {
        final UsuarioAuthResponse user = buscarUsuarioPorEmail(email);

        if (user == null || !passwordEncoder.matches(senha, user.passwordHash()))
            throw new BadCredentialsException("Usuário ou senha inválidos");

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
        } catch (FeignException.NotFound | FeignException.Unauthorized ex) {
            throw new BadCredentialsException("Usuário ou senha inválidos");
        } catch (FeignException.Forbidden ex) {
            throw new DisabledException("Usuário desabilitado");
        }
    }
}