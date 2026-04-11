package br.com.unicos.ms_autenticacao.loader;

import br.com.unicos.core.usuario.auth.dto.UsuarioAuthResponse;
import br.com.unicos.ms_autenticacao.client.UsuarioClient;
import br.com.unicos.ms_autenticacao.model.AuthenticatedUser;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutenticacaoLoader {

    private final UsuarioClient usuarioAuthClient;
    private final PasswordEncoder passwordEncoder;

    public AuthenticatedUser authenticate(String email, String senha) {
        final UsuarioAuthResponse user = buscarUsuarioPorEmail(email);

        if (user == null || !passwordEncoder.matches(senha, user.passwordHash())) {
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }

        return new AuthenticatedUser(
                user.userId(),
                user.login(),
                user.passwordHash(),
                user.empresaId()
        );
    }

    private UsuarioAuthResponse buscarUsuarioPorEmail(String email) {
        try {
            return usuarioAuthClient.buscarPorEmail(email);
        } catch (FeignException.NotFound ex) {
            throw new BadCredentialsException("Usuário ou senha inválidos");
        } catch (FeignException.Forbidden ex) {
            throw new DisabledException("Usuário desabilitado");
        } catch (FeignException.Unauthorized ex) {
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }
    }
}