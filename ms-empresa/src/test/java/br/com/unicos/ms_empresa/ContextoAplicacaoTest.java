package br.com.unicos.ms_empresa;

import br.com.unicos.core.auth.service.JwtClaims;
import br.com.unicos.ms_empresa.client.PermissaoService;
import br.com.unicos.ms_empresa.enums.RegimeTributario;
import br.com.unicos.ms_empresa.enums.StatusEmpresa;
import br.com.unicos.ms_empresa.enums.TipoEmpresa;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.repository.EmpresaRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
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
 * Sobe o contexto completo (H2) e valida a cadeia de segurança e o isolamento por empresa.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContextoAplicacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpresaRepository empresaRepository;

    @MockitoBean
    private PermissaoService permissaoService;

    @BeforeEach
    void setUp() {
        when(permissaoService.usuarioPossuiPermissao(anyString())).thenReturn(true);
    }

    @Test
    void deveExigirTokenNasRotasDeNegocio() throws Exception {
        mockMvc.perform(get("/v1/empresas"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void deveListarSubRecursosDaPropriaEmpresa() throws Exception {
        mockMvc.perform(get("/v1/empresas/1/contatos").header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isOk());
    }

    @Test
    void deveNegarSubRecursosDeOutraEmpresa() throws Exception {
        mockMvc.perform(get("/v1/empresas/2/contatos").header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveResponder404ParaEmpresaInexistente() throws Exception {
        mockMvc.perform(get("/v1/empresas/999").header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void deveRestringirEmpresasAPropriaEmpresaESuasFiliais() throws Exception {
        Empresa matriz = empresaRepository.save(empresa("11111111000111", TipoEmpresa.MATRIZ, null));
        empresaRepository.save(empresa("22222222000122", TipoEmpresa.FILIAL, matriz.getId()));
        Empresa outroCliente = empresaRepository.save(empresa("33333333000133", TipoEmpresa.MATRIZ, null));
        String token = bearer(matriz.getId());

        mockMvc.perform(get("/v1/empresas").header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2));

        mockMvc.perform(get("/v1/empresas/" + outroCliente.getId()).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/v1/empresas/cnpj/33333333000133").header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound());
        mockMvc.perform(put("/v1/empresas/" + outroCliente.getId())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizacao(TipoEmpresa.MATRIZ)))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/v1/empresas/" + outroCliente.getId()).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound());

        assertThat(empresaRepository.findById(outroCliente.getId())).isPresent();
    }

    @Test
    void deveCadastrarApenasFiliaisVinculadasAEmpresaDoUsuario() throws Exception {
        Empresa matriz = empresaRepository.save(empresa("44444444000144", TipoEmpresa.MATRIZ, null));
        String token = bearer(matriz.getId());

        mockMvc.perform(post("/v1/empresas")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cadastro("55555555000155", TipoEmpresa.MATRIZ)))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/v1/empresas")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cadastro("66666666000166", TipoEmpresa.FILIAL)))
                .andExpect(status().isCreated());

        assertThat(empresaRepository.findByCnpj("66666666000166"))
                .hasValueSatisfying(filial -> assertThat(filial.getMatrizId()).isEqualTo(matriz.getId()));

        mockMvc.perform(delete("/v1/empresas/" + matriz.getId()).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isConflict());
    }

    private static Empresa empresa(String cnpj, TipoEmpresa tipo, Long matrizId) {
        return Empresa.builder()
                .razaoSocial("Empresa " + cnpj)
                .cnpj(cnpj)
                .tipoEmpresa(tipo)
                .statusEmpresa(StatusEmpresa.ATIVA)
                .regimeTributario(RegimeTributario.SIMPLES_NACIONAL)
                .matrizId(matrizId)
                .build();
    }

    private static String cadastro(String cnpj, TipoEmpresa tipo) {
        return """
                {"razaoSocial": "Nova %s", "cnpj": "%s", "tipoEmpresa": "%s",
                 "statusEmpresa": "ATIVA", "regimeTributario": "LUCRO_REAL"}
                """.formatted(cnpj, cnpj, tipo);
    }

    private static String atualizacao(TipoEmpresa tipo) {
        return """
                {"razaoSocial": "Alterada", "tipoEmpresa": "%s", "statusEmpresa": "ENCERRADA",
                 "regimeTributario": "LUCRO_REAL"}
                """.formatted(tipo);
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
