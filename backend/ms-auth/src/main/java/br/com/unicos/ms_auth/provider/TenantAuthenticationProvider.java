package br.com.unicos.ms_auth.provider;

import br.com.unicos.ms_auth.loader.AuthAuthenticationLoader;
import br.com.unicos.ms_auth.security_access.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantAuthenticationProvider implements AuthenticationProvider {

    private final AuthAuthenticationLoader loader;

    @Override
    public Authentication authenticate(Authentication authentication) {

        String email = authentication.getName();
        String senha = authentication.getCredentials().toString();

        AuthenticatedUser user;

        try {
            user = loader.authenticate(email, senha);
        } catch (DisabledException | BadCredentialsException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BadCredentialsException("Usuário inexistente ou senha inválida");
        }

        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
