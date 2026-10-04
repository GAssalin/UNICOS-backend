package br.com.unicos.ms_estoque;

import br.com.unicos.core.auth.service.JwtClaims;
import br.com.unicos.ms_estoque.client.PermissaoService;
import br.com.unicos.ms_estoque.enums.StatusEstoque;
import br.com.unicos.ms_estoque.model.Estoque;
import br.com.unicos.ms_estoque.repository.EstoqueRepository;
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
    private EstoqueRepository estoqueRepository;

    @MockitoBean
    private PermissaoService permissaoService;

    @BeforeEach
    void setUp() {
        when(permissaoService.usuarioPossuiPermissao(anyString())).thenReturn(true);
    }

    @Test
    void deveExigirTokenNasRotasDeNegocio() throws Exception {
        mockMvc.perform(get("/v1/estoques"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void deveListarRegistrosDaEmpresaDoToken() throws Exception {
        mockMvc.perform(get("/v1/estoques").header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isOk());
    }

    @Test
    void deveExigirTokenInternoNosEndpointsInternos() throws Exception {
        mockMvc.perform(get("/internal/qualquer").header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveResponder404ParaRegistroInexistente() throws Exception {
        mockMvc.perform(get("/v1/movimentacoes-estoque/999").header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void deveIsolarEstoquesEReferenciasPorEmpresa() throws Exception {
        Estoque daEmpresa = estoqueRepository.save(estoque("ISO-1", 1L, null));
        Estoque deOutraEmpresa = estoqueRepository.save(estoque("ISO-2", 2L, null));

        mockMvc.perform(get("/v1/estoques/" + deOutraEmpresa.getId()).header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/v1/estoques-produtos")
                        .header(HttpHeaders.AUTHORIZATION, bearer(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"estoqueId": %d, "produtoId": 1, "quantidadeAtual": 10,
                                 "quantidadeReservada": 0, "quantidadeDisponivel": 10}
                                """.formatted(deOutraEmpresa.getId())))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/v1/movimentacoes-estoque")
                        .header(HttpHeaders.AUTHORIZATION, bearer(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movimentacao("TRANSFERENCIA", daEmpresa.getId(), deOutraEmpresa.getId(), "TRF-ISO")))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRegistrarEntradaSemEstoqueDeOrigemEPesquisarNoBanco() throws Exception {
        Estoque destino = estoqueRepository.save(estoque("MOV-1", 1L, null));

        mockMvc.perform(post("/v1/movimentacoes-estoque")
                        .header(HttpHeaders.AUTHORIZATION, bearer(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movimentacao("ENTRADA", null, destino.getId(), "NF_100%")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/v1/movimentacoes-estoque")
                        .header(HttpHeaders.AUTHORIZATION, bearer(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movimentacao("SAIDA", null, destino.getId(), "REQ-1")))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/v1/movimentacoes-estoque/pesquisa")
                        .param("documentoReferencia", "nf_100%")
                        .header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));

        mockMvc.perform(get("/v1/movimentacoes-estoque/pesquisa")
                        .param("documentoReferencia", "NF_100%")
                        .header(HttpHeaders.AUTHORIZATION, bearer(2L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void naoDeveCriarCicloNaHierarquiaDeEstoques() throws Exception {
        Estoque raiz = estoqueRepository.save(estoque("HIER-1", 1L, null));
        Estoque filho = estoqueRepository.save(estoque("HIER-2", 1L, raiz.getId()));

        mockMvc.perform(put("/v1/estoques/" + raiz.getId())
                        .header(HttpHeaders.AUTHORIZATION, bearer(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codigo": "HIER-1", "nome": "Raiz", "statusEstoque": "ATIVO", "estoquePaiId": %d}
                                """.formatted(filho.getId())))
                .andExpect(status().isBadRequest());
    }

    private static Estoque estoque(String codigo, Long empresaId, Long estoquePaiId) {
        return Estoque.builder()
                .codigo(codigo)
                .nome("Estoque " + codigo)
                .statusEstoque(StatusEstoque.ATIVO)
                .estoquePaiId(estoquePaiId)
                .empresaId(empresaId)
                .build();
    }

    private static String movimentacao(String tipo, Long origemId, Long destinoId, String documento) {
        return """
                {"tipoMovimentacao": "%s", "estoqueOrigemId": %s, "estoqueDestinoId": %s,
                 "dataMovimentacao": "2026-01-10T10:00:00", "documentoReferencia": "%s",
                 "statusMovimentacao": "PENDENTE"}
                """.formatted(tipo, origemId, destinoId, documento);
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
