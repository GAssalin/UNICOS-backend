package br.com.unicos.ms_cliente.service;

import br.com.unicos.core.funcionario.dto.AcessoCarteiraResponse;
import br.com.unicos.core.funcionario.enums.EscopoCarteira;
import br.com.unicos.core.usuario.context.UserContext;
import br.com.unicos.ms_cliente.client.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Regras de acesso à carteira de clientes.
 *
 * <p>
 * O escopo vem do papel do funcionário no ms-funcionario: vendedores acessam apenas os clientes
 * dos quais são o vendedor responsável; supervisores, gerentes, diretores e usuários que não são
 * funcionários acessam todos os clientes da empresa. Em todos os casos, as ações permitidas
 * continuam dependendo das permissões da role.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CarteiraService {

    private final FuncionarioService funcionarioService;

    /**
     * Indica se o usuário autenticado acessa apenas a própria carteira. Qualquer escopo diferente
     * de {@link EscopoCarteira#TODAS} é tratado como restrito.
     */
    public boolean isRestritaAoUsuarioAtual() {
        AcessoCarteiraResponse acesso = funcionarioService.buscarAcessoCarteira(UserContext.getUsuarioId());
        return acesso == null || acesso.escopo() != EscopoCarteira.TODAS;
    }

    /**
     * Valida o vendedor responsável indicado para um cliente: precisa ser um funcionário da empresa
     * que não foi desligado. O próprio usuário sempre pode assumir o cliente.
     */
    public void validarVendedor(Long vendedorId) {
        if (vendedorId == null || vendedorId.equals(UserContext.getUsuarioId()))
            return;

        AcessoCarteiraResponse acesso = funcionarioService.buscarAcessoCarteira(vendedorId);

        if (acesso == null || !acesso.podeTerCarteira())
            throw new IllegalArgumentException("O vendedor informado não é um funcionário ativo da empresa.");
    }
}
