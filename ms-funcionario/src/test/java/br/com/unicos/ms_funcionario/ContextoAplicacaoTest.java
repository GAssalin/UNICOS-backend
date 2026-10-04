package br.com.unicos.ms_funcionario;

import br.com.unicos.core.auth.interno.TokenInternoService;
import br.com.unicos.core.auth.service.JwtClaims;
import br.com.unicos.ms_funcionario.client.PermissaoService;
import br.com.unicos.ms_funcionario.client.PessoaService;
import br.com.unicos.ms_funcionario.enums.PapelFuncionario;
import br.com.unicos.ms_funcionario.enums.StatusFuncionario;
import br.com.unicos.ms_funcionario.model.Cargo;
import br.com.unicos.ms_funcionario.model.Funcionario;
import br.com.unicos.ms_funcionario.repository.CargoRepository;
import br.com.unicos.ms_funcionario.repository.FuncionarioRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.Instant;
import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Sobe o contexto completo (H2) e valida a cadeia de segurança, o isolamento por empresa,
 * a hierarquia e o escopo de acesso à carteira de clientes.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContextoAplicacaoTest {

    private static final String TOKEN_INTERNO = "token-interno-de-teste";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @MockitoBean
    private PermissaoService permissaoService;

    @MockitoBean
    private PessoaService pessoaService;

    @BeforeEach
    void setUp() {
        funcionarioRepository.deleteAllInBatch();
        cargoRepository.deleteAllInBatch();
        when(permissaoService.usuarioPossuiPermissao(anyString())).thenReturn(true);
    }

    @Test
    void deveExigirTokenNasRotasDeNegocio() throws Exception {
        mockMvc.perform(get("/v1/funcionarios"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void deveExigirTokenInternoNosEndpointsInternos() throws Exception {
        mockMvc.perform(get("/internal/funcionarios/usuarios/1/carteira").header(HttpHeaders.AUTHORIZATION, bearer(1L, 1L)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveCadastrarFuncionariosComHierarquia() throws Exception {
        Long gerenteCargo = criarCargo("Gerente Comercial", "GERENTE");
        Long vendedorCargo = criarCargo("Vendedor Externo", "VENDEDOR");

        Long gerente = idDe(criarFuncionario(1L, 20L, gerenteCargo, null)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.papel").value("GERENTE"))
                .andExpect(jsonPath("$.escopoCarteira").value("TODAS"))
                .andReturn().getResponse().getContentAsString());

        criarFuncionario(2L, 10L, vendedorCargo, gerente)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.superiorId").value(gerente))
                .andExpect(jsonPath("$.cargoNome").value("Vendedor Externo"))
                .andExpect(jsonPath("$.escopoCarteira").value("PROPRIA"));

        mockMvc.perform(get("/v1/funcionarios/" + gerente + "/subordinados").header(HttpHeaders.AUTHORIZATION, bearer(1L, 1L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].usuarioId").value(10));

        mockMvc.perform(get("/v1/funcionarios").param("papel", "VENDEDOR").header(HttpHeaders.AUTHORIZATION, bearer(1L, 1L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));

        // Pessoa e usuário já vinculados a outro funcionário.
        criarFuncionario(2L, 30L, vendedorCargo, null)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Já existe funcionário para essa pessoa."));
        criarFuncionario(3L, 10L, vendedorCargo, null)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("O usuário já está vinculado a outro funcionário."));
    }

    @Test
    void deveInformarEscopoDaCarteiraConformeOPapel() throws Exception {
        Cargo vendedor = cargo(1L, "Vendedor", PapelFuncionario.VENDEDOR);
        Cargo gerente = cargo(1L, "Gerente", PapelFuncionario.GERENTE);
        funcionario(1L, vendedor, 1L, 10L, StatusFuncionario.ATIVO);
        funcionario(1L, gerente, 2L, 20L, StatusFuncionario.ATIVO);
        funcionario(1L, vendedor, 3L, 11L, StatusFuncionario.DESLIGADO);
        funcionario(1L, gerente, 4L, 21L, StatusFuncionario.DESLIGADO);

        acessoCarteira(1L, 10L)
                .andExpect(jsonPath("$.escopo").value("PROPRIA"))
                .andExpect(jsonPath("$.podeTerCarteira").value(true));
        acessoCarteira(1L, 20L)
                .andExpect(jsonPath("$.escopo").value("TODAS"))
                .andExpect(jsonPath("$.podeTerCarteira").value(true));

        // Desligados não recebem clientes e perdem a visão gerencial.
        acessoCarteira(1L, 11L)
                .andExpect(jsonPath("$.escopo").value("PROPRIA"))
                .andExpect(jsonPath("$.podeTerCarteira").value(false));
        acessoCarteira(1L, 21L)
                .andExpect(jsonPath("$.escopo").value("PROPRIA"))
                .andExpect(jsonPath("$.podeTerCarteira").value(false));

        // Usuário sem cadastro de funcionário (ex.: administrador): sem restrição de carteira.
        acessoCarteira(1L, 99L)
                .andExpect(jsonPath("$.funcionarioId").doesNotExist())
                .andExpect(jsonPath("$.escopo").value("TODAS"))
                .andExpect(jsonPath("$.podeTerCarteira").value(false));
    }

    @Test
    void deveIsolarFuncionariosPorEmpresa() throws Exception {
        Cargo cargoOutraEmpresa = cargo(2L, "Vendedor", PapelFuncionario.VENDEDOR);
        Funcionario deOutraEmpresa = funcionario(2L, cargoOutraEmpresa, 1L, 10L, StatusFuncionario.ATIVO);
        String token = bearer(1L, 1L);

        mockMvc.perform(get("/v1/funcionarios/" + deOutraEmpresa.getId()).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/v1/funcionarios").header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));

        // Cargo e superior precisam pertencer à empresa do token.
        criarFuncionario(5L, null, cargoOutraEmpresa.getId(), null).andExpect(status().isNotFound());
        Long cargoProprio = cargo(1L, "Vendedor", PapelFuncionario.VENDEDOR).getId();
        criarFuncionario(5L, null, cargoProprio, deOutraEmpresa.getId()).andExpect(status().isNotFound());

        // O vínculo do usuário 10 com a outra empresa não vale para a empresa do token.
        acessoCarteira(1L, 10L).andExpect(jsonPath("$.escopo").value("TODAS"));
    }

    @Test
    void deveImpedirHierarquiaCircular() throws Exception {
        Cargo cargo = cargo(1L, "Supervisor", PapelFuncionario.SUPERVISOR);
        Funcionario chefe = funcionario(1L, cargo, 1L, null, StatusFuncionario.ATIVO);
        Funcionario intermediario = funcionario(1L, cargo, 2L, null, StatusFuncionario.ATIVO, chefe.getId());
        Funcionario base = funcionario(1L, cargo, 3L, null, StatusFuncionario.ATIVO, intermediario.getId());
        String token = bearer(1L, 1L);

        mockMvc.perform(put("/v1/funcionarios/" + chefe.getId())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(1L, null, cargo.getId(), base.getId(), "ATIVO", null)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(containsString("circular")));

        mockMvc.perform(put("/v1/funcionarios/" + chefe.getId())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(1L, null, cargo.getId(), chefe.getId(), "ATIVO", null)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("O funcionário não pode ser superior de si mesmo."));

        // Superior de outros funcionários não pode ser excluído.
        mockMvc.perform(delete("/v1/funcionarios/" + intermediario.getId()).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value(containsString("superior de outros funcionários")));
    }

    @Test
    void usuarioNaoDeveAmpliarOProprioAcesso() throws Exception {
        Cargo vendedor = cargo(1L, "Vendedor", PapelFuncionario.VENDEDOR);
        Cargo gerente = cargo(1L, "Gerente", PapelFuncionario.GERENTE);
        Funcionario proprio = funcionario(1L, vendedor, 1L, 10L, StatusFuncionario.ATIVO);
        String token = bearer(1L, 10L);

        mockMvc.perform(put("/v1/funcionarios/" + proprio.getId())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(1L, 10L, gerente.getId(), null, "ATIVO", null)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value(containsString("próprio cadastro")));

        mockMvc.perform(put("/v1/funcionarios/cargos/" + vendedor.getId())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome": "Vendedor", "papel": "GERENTE"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("O usuário não pode alterar o papel do próprio cargo."));

        mockMvc.perform(delete("/v1/funcionarios/" + proprio.getId()).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("O usuário não pode excluir o próprio cadastro de funcionário."));

        // Dados que não alteram o acesso continuam editáveis.
        mockMvc.perform(put("/v1/funcionarios/" + proprio.getId())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(1L, 10L, vendedor.getId(), null, "ATIVO", "V-10")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matricula").value("V-10"));
    }

    @Test
    void deveConsultarOProprioCadastroSemPermissaoEspecifica() throws Exception {
        when(permissaoService.usuarioPossuiPermissao(anyString())).thenReturn(false);
        funcionario(1L, cargo(1L, "Vendedor", PapelFuncionario.VENDEDOR), 1L, 10L, StatusFuncionario.ATIVO);

        mockMvc.perform(get("/v1/funcionarios/me").header(HttpHeaders.AUTHORIZATION, bearer(1L, 10L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.papel").value("VENDEDOR"))
                .andExpect(jsonPath("$.escopoCarteira").value("PROPRIA"));
        mockMvc.perform(get("/v1/funcionarios/me").header(HttpHeaders.AUTHORIZATION, bearer(1L, 99L)))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/v1/funcionarios").header(HttpHeaders.AUTHORIZATION, bearer(1L, 10L)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveProtegerCargos() throws Exception {
        Cargo cargo = cargo(1L, "Vendedor", PapelFuncionario.VENDEDOR);
        funcionario(1L, cargo, 1L, null, StatusFuncionario.ATIVO);

        mockMvc.perform(delete("/v1/funcionarios/cargos/" + cargo.getId()).header(HttpHeaders.AUTHORIZATION, bearer(1L, 1L)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("O cargo possui funcionários vinculados e não pode ser excluído."));

        // Nomes de cargo são únicos na empresa, sem diferenciar maiúsculas.
        criarCargo("vendedor", "VENDEDOR", status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Já existe cargo com esse nome."));
    }

    @Test
    void deveValidarDadosDoFuncionario() throws Exception {
        Long cargo = criarCargo("Vendedor", "VENDEDOR");
        String token = bearer(1L, 1L);

        mockMvc.perform(post("/v1/funcionarios")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.pessoaId").exists())
                .andExpect(jsonPath("$.errors.cargoId").exists())
                .andExpect(jsonPath("$.errors.dataAdmissao").exists())
                .andExpect(jsonPath("$.errors.status").exists());

        // Funcionário desligado exige a data de desligamento.
        mockMvc.perform(post("/v1/funcionarios")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(1L, null, cargo, null, "DESLIGADO", null)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Informe a data de desligamento do funcionário desligado."));

        doThrow(new EntityNotFoundException("Pessoa não encontrada: 999")).when(pessoaService).validarPessoa(999L);
        criarFuncionario(999L, null, cargo, null)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Pessoa não encontrada: 999"));
    }

    // ============================================================
    // AUX
    // ============================================================

    private Long criarCargo(String nome, String papel) throws Exception {
        return idDe(criarCargo(nome, papel, status().isCreated()).andReturn().getResponse().getContentAsString());
    }

    private ResultActions criarCargo(String nome, String papel, ResultMatcher esperado) throws Exception {
        return mockMvc.perform(post("/v1/funcionarios/cargos")
                        .header(HttpHeaders.AUTHORIZATION, bearer(1L, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome": "%s", "papel": "%s"}
                                """.formatted(nome, papel)))
                .andExpect(esperado);
    }

    private ResultActions criarFuncionario(Long pessoaId, Long usuarioId, Long cargoId, Long superiorId) throws Exception {
        return mockMvc.perform(post("/v1/funcionarios")
                .header(HttpHeaders.AUTHORIZATION, bearer(1L, 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(pessoaId, usuarioId, cargoId, superiorId, "ATIVO", null)));
    }

    private ResultActions acessoCarteira(Long empresaId, Long usuarioId) throws Exception {
        return mockMvc.perform(get("/internal/funcionarios/usuarios/" + usuarioId + "/carteira")
                        .header(HttpHeaders.AUTHORIZATION, bearer(empresaId, 1L))
                        .header(TokenInternoService.HEADER, TOKEN_INTERNO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(usuarioId));
    }

    private static String json(Long pessoaId, Long usuarioId, Long cargoId, Long superiorId, String status, String matricula) {
        return """
                {"pessoaId": %d, "usuarioId": %s, "cargoId": %d, "superiorId": %s,
                 "dataAdmissao": "2024-01-10", "status": "%s", "matricula": %s}
                """.formatted(pessoaId, usuarioId, cargoId, superiorId, status,
                matricula == null ? "null" : "\"" + matricula + "\"");
    }

    private static Long idDe(String json) {
        return ((Number) JsonPath.read(json, "$.id")).longValue();
    }

    private Cargo cargo(Long empresaId, String nome, PapelFuncionario papel) {
        return cargoRepository.save(Cargo.builder()
                .empresaId(empresaId)
                .nome(nome)
                .papel(papel)
                .build());
    }

    private Funcionario funcionario(Long empresaId, Cargo cargo, Long pessoaId, Long usuarioId, StatusFuncionario status) {
        return funcionario(empresaId, cargo, pessoaId, usuarioId, status, null);
    }

    private Funcionario funcionario(Long empresaId, Cargo cargo, Long pessoaId, Long usuarioId, StatusFuncionario status, Long superiorId) {
        return funcionarioRepository.save(Funcionario.builder()
                .empresaId(empresaId)
                .cargo(cargo)
                .pessoaId(pessoaId)
                .usuarioId(usuarioId)
                .superiorId(superiorId)
                .dataAdmissao(LocalDate.of(2024, 1, 10))
                .dataDesligamento(status == StatusFuncionario.DESLIGADO ? LocalDate.of(2025, 6, 30) : null)
                .status(status)
                .build());
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
