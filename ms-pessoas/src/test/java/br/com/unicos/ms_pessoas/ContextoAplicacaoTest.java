package br.com.unicos.ms_pessoas;

import br.com.unicos.core.auth.service.JwtClaims;
import br.com.unicos.ms_pessoas.client.PermissaoService;
import br.com.unicos.ms_pessoas.usuario.dto.permissao.RoleResumoResponse;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Sobe o contexto completo (H2) e valida a cadeia de segurança, o tratamento de erros e
 * as consultas derivadas dos repositórios.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContextoAplicacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PermissaoService permissaoService;

    @Test
    void deveExigirTokenNasRotasDeNegocio() throws Exception {
        mockMvc.perform(get("/v1/pessoas"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void deveExigirTokenInternoNosEndpointsInternos() throws Exception {
        mockMvc.perform(get("/internal/auth/by-email").param("email", "admin@unicos.com"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/internal/auth/by-email").param("email", "inexistente@unicos.com")
                        .header("X-Internal-Token", "token-interno-de-teste"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarSomenteComPermissao() throws Exception {
        when(permissaoService.usuarioPossuiPermissao(anyString())).thenReturn(true);

        mockMvc.perform(get("/v1/pessoas").header(HttpHeaders.AUTHORIZATION, bearer()))
                .andExpect(status().isOk());

        when(permissaoService.usuarioPossuiPermissao("PESSOA_FISICA_LISTAR")).thenReturn(false);

        mockMvc.perform(get("/v1/pessoas-fisicas").header(HttpHeaders.AUTHORIZATION, bearer()))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveCriarEConsultarPessoaFisicaNaEmpresaDoToken() throws Exception {
        when(permissaoService.usuarioPossuiPermissao(anyString())).thenReturn(true);

        String corpo = """
                {"nome": "Maria Teste", "cpf": "12345678901", "dataNascimento": "1990-05-10"}
                """;

        mockMvc.perform(post("/v1/pessoas-fisicas")
                        .header(HttpHeaders.AUTHORIZATION, bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.nome").value("Maria Teste"));

        mockMvc.perform(get("/v1/pessoas-fisicas/cpf/12345678901").header(HttpHeaders.AUTHORIZATION, bearer(2L)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveValidarCorpoDaRequisicao() throws Exception {
        when(permissaoService.usuarioPossuiPermissao(anyString())).thenReturn(true);

        mockMvc.perform(post("/v1/pessoas-fisicas")
                        .header(HttpHeaders.AUTHORIZATION, bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.cpf").exists());
    }

    @Test
    void devePermitirConfirmacaoDeEmailSemToken() throws Exception {
        mockMvc.perform(patch("/v1/verificacao-email/confirmar").param("token", "token-inexistente"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveProtegerCadastroDeUsuariosContraSenhaFracaEAutoelevacao() throws Exception {
        // Usuário administrador distinto do usuário criado no teste.
        String admin = bearer(1L, 999L);
        when(permissaoService.usuarioPossuiPermissao(anyString())).thenReturn(true);
        when(permissaoService.buscarRolePorId(anyLong()))
                .thenAnswer(invocacao -> new RoleResumoResponse(invocacao.getArgument(0), "ROLE"));

        mockMvc.perform(post("/v1/usuarios")
                        .header(HttpHeaders.AUTHORIZATION, admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuario("operador", "1234567", 2L)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.password").exists());

        String criado = mockMvc.perform(post("/v1/usuarios")
                        .header(HttpHeaders.AUTHORIZATION, admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuario("operador", "senha-forte-123", 2L)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long id = ((Number) JsonPath.read(criado, "$.id")).longValue();

        // O próprio usuário não pode se promover para outra role.
        mockMvc.perform(put("/v1/usuarios/" + id)
                        .header(HttpHeaders.AUTHORIZATION, bearer(1L, id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuario("operador", "senha-forte-123", 1L)))
                .andExpect(status().isConflict());

        // Desativado, o usuário deixa de ter role nas verificações de permissão.
        mockMvc.perform(get("/internal/usuarios/" + id + "/role")
                        .header(HttpHeaders.AUTHORIZATION, admin)
                        .header("X-Internal-Token", "token-interno-de-teste"))
                .andExpect(jsonPath("$.idRole").value(2));

        mockMvc.perform(patch("/v1/usuarios/" + id + "/desativar").header(HttpHeaders.AUTHORIZATION, admin))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/internal/usuarios/" + id + "/role")
                        .header(HttpHeaders.AUTHORIZATION, admin)
                        .header("X-Internal-Token", "token-interno-de-teste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idRole").value(nullValue()));
    }

    private static String usuario(String login, String senha, Long roleId) {
        return """
                {"login": "%s", "password": "%s", "email": "%s@unicos.com", "roleId": %d}
                """.formatted(login, senha, login, roleId);
    }

    private static String bearer() {
        return bearer(1L);
    }

    private static String bearer(Long empresaId) {
        return bearer(empresaId, 1L);
    }

    private static String bearer(Long empresaId, Long usuarioId) {
        return "Bearer " + JWT.create()
                .withIssuer("unicos-teste")
                .withClaim(JwtClaims.TIPO, JwtClaims.TIPO_ACCESS)
                .withClaim(JwtClaims.USUARIO_ID, usuarioId)
                .withClaim(JwtClaims.TENANT_ID, empresaId)
                .withExpiresAt(Instant.now().plusSeconds(300))
                .sign(Algorithm.HMAC256("segredo-de-teste-com-mais-de-32-caracteres"));
    }
}
