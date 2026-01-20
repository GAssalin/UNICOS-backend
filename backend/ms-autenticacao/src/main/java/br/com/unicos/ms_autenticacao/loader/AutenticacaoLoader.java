package br.com.unicos.ms_autenticacao.loader;

import br.com.unicos.core.usuario.auth.dto.UsuarioAuthResponse;
import br.com.unicos.ms_autenticacao.client.PermissaoClient;
import br.com.unicos.ms_autenticacao.client.UsuarioClient;
import br.com.unicos.ms_autenticacao.model.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutenticacaoLoader {

    private final UsuarioClient usuarioAuthClient;
    private final PasswordEncoder passwordEncoder;
    private final PermissaoClient permissaoClient;

    public AuthenticatedUser authenticate(String email, String senha) {

        UsuarioAuthResponse user = usuarioAuthClient.buscarPorEmail(email);

        if (!passwordEncoder.matches(senha, user.passwordHash()))
            throw new BadCredentialsException("Credenciais inválidas");

        return new AuthenticatedUser(
                user.userId(),
                user.login(),
                user.passwordHash(),
                user.empresaId(),
                permissaoClient.findRolesByUserId(user.userId()).stream().toList()
        );
    }

}
