package br.com.unicos.ms_produto;

import br.com.unicos.core.auth.service.JwtClaims;
import br.com.unicos.ms_produto.client.PermissaoService;
import br.com.unicos.ms_produto.enums.TipoProduto;
import br.com.unicos.ms_produto.model.Produto;
import br.com.unicos.ms_produto.model.ProdutoAtributo;
import br.com.unicos.ms_produto.repository.ProdutoAtributoRepository;
import br.com.unicos.ms_produto.repository.ProdutoRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoAtributoRepository atributoRepository;

    @MockitoBean
    private PermissaoService permissaoService;

    @BeforeEach
    void setUp() {
        when(permissaoService.usuarioPossuiPermissao(anyString())).thenReturn(true);
    }

    @Test
    void deveExigirTokenNasRotasDeNegocio() throws Exception {
        mockMvc.perform(get("/v1/produtos"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void deveListarRegistrosDaEmpresaDoToken() throws Exception {
        mockMvc.perform(get("/v1/produtos").header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isOk());
    }

    @Test
    void deveExigirTokenInternoNosEndpointsInternos() throws Exception {
        mockMvc.perform(get("/internal/qualquer").header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveResponder404ParaRegistroInexistente() throws Exception {
        mockMvc.perform(get("/v1/produtos/999").header(HttpHeaders.AUTHORIZATION, bearer(1L)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void naoDeveVincularAtributoDeOutraEmpresaAoProduto() throws Exception {
        Produto produto = produtoRepository.save(Produto.builder()
                .codigo("P-ATR").nome("Produto").tipoProduto(TipoProduto.PRODUTO).unidadeMedida("UN")
                .empresaId(1L).build());
        ProdutoAtributo atributoDaEmpresa = atributoRepository.save(
                ProdutoAtributo.builder().nome("Cor").empresaId(1L).build());
        ProdutoAtributo atributoDeOutraEmpresa = atributoRepository.save(
                ProdutoAtributo.builder().nome("Cor").empresaId(2L).build());

        mockMvc.perform(post("/v1/produtos/atributos-valores")
                        .header(HttpHeaders.AUTHORIZATION, bearer(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(valorAtributo(produto.getId(), atributoDeOutraEmpresa.getId())))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/v1/produtos/atributos-valores")
                        .header(HttpHeaders.AUTHORIZATION, bearer(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(valorAtributo(produto.getId(), atributoDaEmpresa.getId())))
                .andExpect(status().is2xxSuccessful());
    }

    private static String valorAtributo(Long produtoId, Long atributoId) {
        return """
                {"produtoId": %d, "atributoId": %d, "valor": "Preto"}
                """.formatted(produtoId, atributoId);
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
