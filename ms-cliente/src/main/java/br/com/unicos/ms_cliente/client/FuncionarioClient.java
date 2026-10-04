package br.com.unicos.ms_cliente.client;

import br.com.unicos.core.funcionario.dto.AcessoCarteiraResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms-funcionario",
        contextId = "FuncionarioClient"
)
public interface FuncionarioClient {

    @GetMapping("/internal/funcionarios/usuarios/{usuarioId}/carteira")
    AcessoCarteiraResponse buscarAcessoCarteira(@PathVariable("usuarioId") Long usuarioId);
}
