package br.com.unicos.ms_permissao;

import br.com.unicos.core.auth.service.JwtClaims;
import br.com.unicos.core.usuario.dto.UsuarioRoleIdsResponse;
import br.com.unicos.ms_permissao.client.UsuarioService;
import br.com.unicos.ms_permissao.model.Permissao;
import br.com.unicos.ms_permissao.model.Role;
import br.com.unicos.ms_permissao.model.RolePermissao;
import br.com.unicos.ms_permissao.repository.PermissaoRepository;
import br.com.unicos.ms_permissao.repository.RolePermissaoRepository;
import br.com.unicos.ms_permissao.repository.RoleRepository;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Sobe o contexto completo (H2) e valida a autorização real (role × permissão no banco),
 * o isolamento por empresa e os endpoints internos.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContextoAplicacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired
    private RolePermissaoRepository rolePermissaoRepository;

    @MockitoBean
    private UsuarioService usuarioService;

    private Role roleAdmin;

    @BeforeEach
    void setUp() {
        rolePermissaoRepository.deleteAll();
        roleRepository.deleteAll();
        permissaoRepository.deleteAll();

        roleAdmin = roleRepository.save(Role.builder().nome("ADMIN").empresaId(1L).build());
        roleRepository.save(Role.builder().nome("ADMIN").empresaId(2L).build());

        Permissao listarRoles = permissaoRepository.save(Permissao.builder().nome("ROLE_LISTAR").empresaId(1L).build());
        Permissao criarVinculo = permissaoRepository.save(Permissao.builder().nome("ROLE_PERMISSAO_CRIAR").empresaId(1L).build());

        rolePermissaoRepository.save(RolePermissao.builder().empresaId(1L).role(roleAdmin).permissao(listarRoles).build());
        rolePermissaoRepository.save(RolePermissao.builder().empresaId(1L).role(roleAdmin).permissao(criarVinculo).build());

        when(usuarioService.buscarRoleIdsDoUsuario(anyLong()))
                .thenReturn(new UsuarioRoleIdsResponse(1L, roleAdmin.getId()));
    }

    @Test
    void deveExigirTokenNasRotasDeNegocio() throws Exception {
        mockMvc.perform(get("/v1/roles"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void deveListarSomenteAsRolesDaEmpresaDoToken() throws Exception {
        mockMvc.perform(get("/v1/roles").header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(roleAdmin.getId()));
    }

    @Test
    void deveNegarRotaSemPermissaoVinculadaARole() throws Exception {
        mockMvc.perform(get("/v1/permissoes").header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isForbidden());
    }

    @Test
    void devePermitirConsultarAsPropriasPermissoesSemPermissaoEspecifica() throws Exception {
        mockMvc.perform(get("/v1/permissoes/minhas").header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void naoDeveCriarVinculoEmOutraEmpresa() throws Exception {
        String corpo = """
                {"empresaId": 2, "roleId": %d, "permissaoId": 1}
                """.formatted(roleAdmin.getId());

        mockMvc.perform(post("/v1/role-permissao")
                        .header(HttpHeaders.AUTHORIZATION, bearer(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isForbidden());
    }

    @Test
    void naoDeveExcluirPermissaoUsadaPorRolesDeOutraEmpresa() throws Exception {
        Permissao excluir = permissaoRepository.save(Permissao.builder().nome("PERMISSAO_EXCLUIR").empresaId(1L).build());
        rolePermissaoRepository.save(RolePermissao.builder().empresaId(1L).role(roleAdmin).permissao(excluir).build());

        Role roleOutraEmpresa = roleRepository.save(Role.builder().nome("VENDAS").empresaId(2L).build());
        Permissao compartilhada = permissaoRepository.save(Permissao.builder().nome("PRODUTO_LISTAR").empresaId(1L).build());
        rolePermissaoRepository.save(RolePermissao.builder().empresaId(2L).role(roleOutraEmpresa).permissao(compartilhada).build());

        mockMvc.perform(delete("/v1/permissoes/" + compartilhada.getId()).header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isConflict());
    }

    @Test
    void deveResponderVerificacaoInternaDePermissao() throws Exception {
        mockMvc.perform(post("/internal/permissao/check").param("nomePermissao", "ROLE_LISTAR")
                        .header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/internal/permissao/check").param("nomePermissao", "ROLE_LISTAR")
                        .header(HttpHeaders.AUTHORIZATION, bearer(1L))
                        .header("X-Internal-Token", "token-interno-de-teste"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    private static String bearer(Long empresaId) {
        return "Bearer " + JWT.create()
                .withIssuer("unicos-teste")
                .withClaim(JwtClaims.TIPO, JwtClaims.TIPO_ACCESS)
                .withClaim(JwtClaims.USUARIO_ID, 1L)
                .withClaim(JwtClaims.TENANT_ID, empresaId)
                .withExpiresAt(Instant.now().plusSeconds(300))
                .sign(Algorithm.HMAC256("segredo-de-teste-com-mais-de-32-caracteres"));
    }
}
