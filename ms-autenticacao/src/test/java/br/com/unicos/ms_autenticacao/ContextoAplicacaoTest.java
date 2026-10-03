package br.com.unicos.ms_autenticacao;

import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.core.usuario.dto.UsuarioAuthResponse;
import br.com.unicos.ms_autenticacao.service.UsuarioService;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContextoAplicacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenCoreService tokenCoreService;

    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    void deveAutenticarERenovarTokens() throws Exception {
        when(usuarioService.buscarUsuarioPorEmail("admin@unicos.com"))
                .thenReturn(new UsuarioAuthResponse(1L, "admin", passwordEncoder.encode("senha-de-teste"), 1L, true));
        when(usuarioService.buscarUsuarioPorId(1L))
                .thenReturn(new UsuarioAuthResponse(1L, "admin", "hash", 1L, true));

        MvcResult login = mockMvc.perform(post("/v1/autenticacao/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"admin@unicos.com\", \"senha\": \"senha-de-teste\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String corpo = login.getResponse().getContentAsString();
        String accessToken = corpo.replaceAll(".*\"tokenAccess\":\"([^\"]+)\".*", "$1");
        String refreshToken = corpo.replaceAll(".*\"refreshToken\":\"([^\"]+)\".*", "$1");

        assertThat(tokenCoreService.validarToken("Bearer " + accessToken)).isNotNull();

        mockMvc.perform(post("/v1/autenticacao/atualizar-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\": \"" + refreshToken + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenAccess").isNotEmpty());
    }

    @Test
    void deveResponder401ParaUsuarioInexistente() throws Exception {
        Request request = Request.create(Request.HttpMethod.GET, "/internal/auth/by-email", Map.of(), null,
                StandardCharsets.UTF_8, null);
        when(usuarioService.buscarUsuarioPorEmail("x@unicos.com"))
                .thenThrow(new FeignException.NotFound("não encontrado", request, null, Map.of()));

        mockMvc.perform(post("/v1/autenticacao/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"x@unicos.com\", \"senha\": \"qualquer\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void deveValidarCorpoDoLogin() throws Exception {
        mockMvc.perform(post("/v1/autenticacao/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void deveNegarRotasNaoPublicas() throws Exception {
        mockMvc.perform(get("/v1/autenticacao/qualquer"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }
}
