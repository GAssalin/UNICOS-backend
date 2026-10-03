package br.com.unicos.ms_autenticacao.loader;

import br.com.unicos.core.auth.model.AuthenticatedUser;
import br.com.unicos.core.usuario.dto.UsuarioAuthResponse;
import br.com.unicos.ms_autenticacao.service.UsuarioService;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AutenticacaoLoaderTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);
    private final UsuarioService usuarioService = mock(UsuarioService.class);
    private final AutenticacaoLoader loader = new AutenticacaoLoader(usuarioService, passwordEncoder);

    @Test
    void deveAutenticarUsuarioAtivoComSenhaCorreta() {
        when(usuarioService.buscarUsuarioPorEmail("a@unicos.com"))
                .thenReturn(new UsuarioAuthResponse(1L, "a", passwordEncoder.encode("senha"), 3L, true));

        AuthenticatedUser user = loader.authenticate("a@unicos.com", "senha");

        assertThat(user.getUserId()).isEqualTo(1L);
        assertThat(user.getEmpresaId()).isEqualTo(3L);
    }

    @Test
    void deveRejeitarSenhaIncorretaAntesDeRevelarQueUsuarioEstaInativo() {
        when(usuarioService.buscarUsuarioPorEmail("a@unicos.com"))
                .thenReturn(new UsuarioAuthResponse(1L, "a", passwordEncoder.encode("senha"), 3L, false));

        assertThatThrownBy(() -> loader.authenticate("a@unicos.com", "errada"))
                .isInstanceOf(BadCredentialsException.class);
        assertThatThrownBy(() -> loader.authenticate("a@unicos.com", "senha"))
                .isInstanceOf(DisabledException.class);
    }

    @Test
    void deveTratarUsuarioInexistenteComoCredencialInvalida() {
        Request request = Request.create(Request.HttpMethod.GET, "/internal/auth/by-email", Map.of(), null,
                StandardCharsets.UTF_8, null);
        when(usuarioService.buscarUsuarioPorEmail("x@unicos.com"))
                .thenThrow(new FeignException.NotFound("não encontrado", request, null, Map.of()));

        assertThatThrownBy(() -> loader.authenticate("x@unicos.com", "senha"))
                .isInstanceOf(BadCredentialsException.class);
    }
}
