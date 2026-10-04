package br.com.unicos.ms_funcionario.controller.internal;

import br.com.unicos.core.funcionario.dto.AcessoCarteiraResponse;
import br.com.unicos.ms_funcionario.service.FuncionarioService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints consumidos apenas por outros microserviços (exigem o token interno).
 * A empresa vem do JWT repassado pelo serviço chamador.
 */
@Hidden
@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class FuncionarioInternalController {

    private final FuncionarioService funcionarioService;

    /**
     * Escopo de acesso do usuário à carteira de clientes, usado pelo ms-cliente.
     */
    @GetMapping("/funcionarios/usuarios/{usuarioId}/carteira")
    public AcessoCarteiraResponse buscarAcessoCarteira(@PathVariable("usuarioId") Long usuarioId) {
        return funcionarioService.buscarAcessoCarteira(usuarioId);
    }
}
