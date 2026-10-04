package br.com.unicos.ms_cliente;

import br.com.unicos.core.auth.service.JwtClaims;
import br.com.unicos.core.funcionario.dto.AcessoCarteiraResponse;
import br.com.unicos.core.funcionario.enums.EscopoCarteira;
import br.com.unicos.ms_cliente.client.FuncionarioService;
import br.com.unicos.ms_cliente.client.PermissaoService;
import br.com.unicos.ms_cliente.client.UsuarioService;
import br.com.unicos.ms_cliente.enums.StatusCliente;
import br.com.unicos.ms_cliente.model.Cliente;
import br.com.unicos.ms_cliente.repository.ClienteObservacaoRepository;
import br.com.unicos.ms_cliente.repository.ClienteRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.anyLong;
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
 * Sobe o contexto completo (H2) e valida a cadeia de segurança, o tratamento de erros e o acesso
 * à carteira de clientes.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContextoAplicacaoTest {

    private static final Long VENDEDOR = 10L;
    private static final Long OUTRO_VENDEDOR = 20L;
    private static final Long VENDEDOR_DESLIGADO = 11L;
    private static final Long GERENTE = 30L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteObservacaoRepository clienteObservacaoRepository;

    @MockitoBean
    private PermissaoService permissaoService;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private FuncionarioService funcionarioService;

    @BeforeEach
    void setUp() {
        clienteObservacaoRepository.deleteAllInBatch();
        clienteRepository.deleteAllInBatch();
        when(permissaoService.usuarioPossuiPermissao(anyString())).thenReturn(true);

        // Por padrão, o usuário não é funcionário (ex.: administrador): sem restrição de carteira.
        when(funcionarioService.buscarAcessoCarteira(anyLong()))
                .thenAnswer(inv -> new AcessoCarteiraResponse(inv.getArgument(0), null, EscopoCarteira.TODAS, false));
        when(funcionarioService.buscarAcessoCarteira(VENDEDOR))
                .thenReturn(new AcessoCarteiraResponse(VENDEDOR, 1L, EscopoCarteira.PROPRIA, true));
        when(funcionarioService.buscarAcessoCarteira(OUTRO_VENDEDOR))
                .thenReturn(new AcessoCarteiraResponse(OUTRO_VENDEDOR, 2L, EscopoCarteira.PROPRIA, true));
        when(funcionarioService.buscarAcessoCarteira(VENDEDOR_DESLIGADO))
                .thenReturn(new AcessoCarteiraResponse(VENDEDOR_DESLIGADO, 3L, EscopoCarteira.PROPRIA, false));
        when(funcionarioService.buscarAcessoCarteira(GERENTE))
                .thenReturn(new AcessoCarteiraResponse(GERENTE, 4L, EscopoCarteira.TODAS, true));
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
        Cliente proprio = clienteRepository.save(cliente(100L, VENDEDOR));
        Cliente deOutroVendedor = clienteRepository.save(cliente(101L, OUTRO_VENDEDOR));
        String vendedor = bearer(1L, VENDEDOR);

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

        // O filtro por vendedor não dá acesso à carteira de outro vendedor.
        mockMvc.perform(get("/v1/clientes").param("vendedorId", OUTRO_VENDEDOR.toString()).header(HttpHeaders.AUTHORIZATION, vendedor))
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
                .andExpect(jsonPath("$.vendedorId").value(VENDEDOR));

        // Clientes cadastrados por um vendedor entram sempre na própria carteira.
        mockMvc.perform(post("/v1/clientes")
                        .header(HttpHeaders.AUTHORIZATION, vendedor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"pessoaId": 102, "vendedorId": 20, "status": "ATIVO", "permiteVendaAPrazo": false}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vendedorId").value(VENDEDOR));
    }

    @Test
    void vendedorDeveRegistrarObservacoesApenasNaPropriaCarteira() throws Exception {
        Cliente proprio = clienteRepository.save(cliente(110L, VENDEDOR));
        Cliente deOutroVendedor = clienteRepository.save(cliente(111L, OUTRO_VENDEDOR));
        String vendedor = bearer(1L, VENDEDOR);

        mockMvc.perform(post("/v1/clientes/observacoes")
                        .header(HttpHeaders.AUTHORIZATION, vendedor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(observacao(deOutroVendedor.getId())))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/v1/clientes/observacoes")
                        .header(HttpHeaders.AUTHORIZATION, vendedor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(observacao(proprio.getId())))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/v1/clientes/observacoes")
                        .header(HttpHeaders.AUTHORIZATION, bearer(1L, GERENTE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(observacao(deOutroVendedor.getId())))
                .andExpect(status().isCreated());
    }

    @Test
    void gestorDeveAcessarTodasAsCarteiras() throws Exception {
        Cliente doVendedor = clienteRepository.save(cliente(120L, VENDEDOR));
        clienteRepository.save(cliente(121L, OUTRO_VENDEDOR));
        clienteRepository.save(cliente(122L, OUTRO_VENDEDOR));
        String gerente = bearer(1L, GERENTE);

        mockMvc.perform(get("/v1/clientes/" + doVendedor.getId()).header(HttpHeaders.AUTHORIZATION, gerente))
                .andExpect(status().isOk());
        mockMvc.perform(get("/v1/clientes").header(HttpHeaders.AUTHORIZATION, gerente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3));
        mockMvc.perform(get("/v1/clientes").param("vendedorId", OUTRO_VENDEDOR.toString()).header(HttpHeaders.AUTHORIZATION, gerente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2));
        mockMvc.perform(get("/v1/clientes/status/ATIVO").param("vendedorId", VENDEDOR.toString()).header(HttpHeaders.AUTHORIZATION, gerente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));

        // Transferência de carteira entre vendedores.
        mockMvc.perform(put("/v1/clientes/" + doVendedor.getId())
                        .header(HttpHeaders.AUTHORIZATION, gerente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"pessoaId": 120, "vendedorId": 20, "status": "ATIVO", "permiteVendaAPrazo": false}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendedorId").value(OUTRO_VENDEDOR));
    }

    @Test
    void clienteSoPodeSerAtribuidoAFuncionarioAtivo() throws Exception {
        Cliente cliente = clienteRepository.save(cliente(130L, VENDEDOR));
        String gerente = bearer(1L, GERENTE);

        mockMvc.perform(post("/v1/clientes")
                        .header(HttpHeaders.AUTHORIZATION, gerente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"pessoaId": 131, "vendedorId": 99, "status": "ATIVO", "permiteVendaAPrazo": false}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("O vendedor informado não é um funcionário ativo da empresa."));
        mockMvc.perform(put("/v1/clientes/" + cliente.getId())
                        .header(HttpHeaders.AUTHORIZATION, gerente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"pessoaId": 130, "vendedorId": 11, "status": "ATIVO", "permiteVendaAPrazo": false}
                                """))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/v1/clientes")
                        .header(HttpHeaders.AUTHORIZATION, gerente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"pessoaId": 132, "vendedorId": 10, "status": "ATIVO", "permiteVendaAPrazo": false}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vendedorId").value(VENDEDOR));
    }

    @Test
    void naoDeveLiberarClientesSemOEscopoDaCarteira() throws Exception {
        when(funcionarioService.buscarAcessoCarteira(VENDEDOR))
                .thenThrow(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de funcionários temporariamente indisponível"));

        mockMvc.perform(get("/v1/clientes").header(HttpHeaders.AUTHORIZATION, bearer(1L, VENDEDOR)))
                .andExpect(status().isServiceUnavailable());
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

    private static String observacao(Long clienteId) {
        return """
                {"clienteId": %d, "descricao": "Contato realizado.", "tipo": "COMERCIAL"}
                """.formatted(clienteId);
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
