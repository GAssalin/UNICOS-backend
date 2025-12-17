package br.com.unicos.ms_auth.loader;

import br.com.unicos.ms_auth.model.Usuario;
import br.com.unicos.ms_auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioAuthenticationLoader {

    private final UsuarioRepository usuarioRepository;

    public Usuario carregarPorEmail(String email) {
        return usuarioRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BadCredentialsException("Usuário inexistente ou inativo"));
    }

}
