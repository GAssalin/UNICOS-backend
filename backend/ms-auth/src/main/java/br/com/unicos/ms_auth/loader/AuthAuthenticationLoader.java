package br.com.unicos.ms_auth.loader;

import br.com.unicos.core.usuario.auth.dto.UsuarioAuthResponse;
import br.com.unicos.ms_auth.client.UsuarioClient;
import br.com.unicos.ms_auth.repository.RoleUsuarioRepository;
import br.com.unicos.ms_auth.security_access.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthAuthenticationLoader {

    private final UsuarioClient usuarioAuthClient;
    private final PasswordEncoder passwordEncoder;
    private final RoleUsuarioRepository roleUsuarioRepository;

    public AuthenticatedUser authenticate(String email, String senha) {

        UsuarioAuthResponse user = usuarioAuthClient.buscarPorEmail(email);

        if (!passwordEncoder.matches(senha, user.passwordHash()))
            throw new BadCredentialsException("Credenciais inválidas");

        return new AuthenticatedUser(
                user.userId(),
                user.login(),
                user.passwordHash(),
                user.empresaId(),
                roleUsuarioRepository.findRolesByUsuario(user.userId(), user.empresaId()).stream().toList()
        );
    }

}
