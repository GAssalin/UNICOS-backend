package br.com.unicos.ms_cliente;

import br.com.unicos.core.auth.service.JwtClaims;
import br.com.unicos.core.usuario.dto.UsuarioRoleIdsResponse;
import br.com.unicos.ms_cliente.client.PermissaoService;
import br.com.unicos.ms_cliente.client.UsuarioService;
import br.com.unicos.ms_cliente.dto.internal.RoleResumoResponse;
import br.com.unicos.ms_cliente.enums.StatusCliente;
import br.com.unicos.ms_cliente.model.Cliente;
import br.com.unicos.ms_cliente.repository.ClienteRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Sobe o contexto completo (H2) e valida a cadeia de segurança e o tratamento de erros.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContextoAplicacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @MockitoBean
    private PermissaoService permissaoService;

    @MockitoBean
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        when(permissaoService.usuarioPossuiPermissao(anyString())).thenReturn(true);
    }

    @Test
    void deveExigirTokenNasRotasDeNegocio() throws Exception {
        mockMvc.perform(get("/v1/clientes"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void deveListarRegistrosDaEmpresaDoToken() throws Exception {
        mockMvc.perform(get("/v1/clientes").header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isOk());
    }

    @Test
    void deveExigirTokenInternoNosEndpointsInternos() throws Exception {
        mockMvc.perform(get("/internal/qualquer").header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveResponder404ParaRegistroInexistente() throws Exception {
        mockMvc.perform(get("/v1/clientes/999").header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void vendedorDeveAcessarApenasOsPropriosClientes() throws Exception {
        when(usuarioService.buscarRoleIdsDoUsuario(10L)).thenReturn(new UsuarioRoleIdsResponse(10L, 5L));
        when(permissaoService.buscarNomeRoleById(5L)).thenReturn(new RoleResumoResponse(5L, "VENDEDOR"));

        Cliente proprio = clienteRepository.save(cliente(100L, 10L));
        Cliente deOutroVendedor = clienteRepository.save(cliente(101L, 20L));
        String vendedor = bearer(1L, 10L);

        mockMvc.perform(get("/v1/clientes/" + proprio.getId()).header(HttpHeaders.AUTHORIZATION, vendedor))
                .andExpect(status().isOk());
        mockMvc.perform(get("/v1/clientes/" + deOutroVendedor.getId()).header(HttpHeaders.AUTHORIZATION, vendedor))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/v1/clientes/" + deOutroVendedor.getId()).header(HttpHeaders.AUTHORIZATION, vendedor))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/v1/clientes/status/ATIVO").header(HttpHeaders.AUTHORIZATION, vendedor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(proprio.getId()));

        // Um vendedor não transfere o cliente para outro vendedor.
        mockMvc.perform(put("/v1/clientes/" + proprio.getId())
                        .header(HttpHeaders.AUTHORIZATION, vendedor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"pessoaId": 100, "vendedorId": 20, "status": "ATIVO", "permiteVendaAPrazo": false}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendedorId").value(10));
    }

    @Test
    void deveValidarCamposObrigatoriosDoCliente() throws Exception {
        mockMvc.perform(post("/v1/clientes")
                        .header(HttpHeaders.AUTHORIZATION, bearer(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"pessoaId\": 1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.status").exists())
                .andExpect(jsonPath("$.errors.permiteVendaAPrazo").exists());
    }

    private static Cliente cliente(Long pessoaId, Long vendedorId) {
        return Cliente.builder()
                .pessoaId(pessoaId)
                .vendedorId(vendedorId)
                .status(StatusCliente.ATIVO)
                .permiteVendaAPrazo(false)
                .empresaId(1L)
                .build();
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
