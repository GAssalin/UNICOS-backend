package br.com.unicos.ms_auth.provider;

import br.com.unicos.ms_auth.loader.UsuarioAuthenticationLoader;
import br.com.unicos.ms_auth.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioAuthenticationLoader loader;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {

        String email = authentication.getName();
        String senha = authentication.getCredentials().toString();

        Usuario usuario = loader.carregarPorEmail(email);

        if (!passwordEncoder.matches(senha, usuario.getPassword()))
            throw new BadCredentialsException("Usuário inexistente ou senha inválida");

        return new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}