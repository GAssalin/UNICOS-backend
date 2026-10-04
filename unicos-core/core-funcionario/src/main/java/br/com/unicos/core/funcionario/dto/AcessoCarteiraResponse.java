package br.com.unicos.core.funcionario.dto;

import br.com.unicos.core.funcionario.enums.EscopoCarteira;

/**
 * Acesso de um usuário à carteira de clientes na empresa do contexto atual.
 *
 * <p>Trafega apenas entre serviços, por endpoints {@code /internal/**}.</p>
 *
 * @param usuarioId       usuário consultado
 * @param funcionarioId   funcionário vinculado ao usuário ou {@code null} quando o usuário não é funcionário da empresa
 * @param escopo          clientes que o usuário pode acessar
 * @param podeTerCarteira indica se o usuário pode ser o vendedor responsável por clientes
 *                        (funcionário da empresa que não foi desligado)
 */
public record AcessoCarteiraResponse(
        Long usuarioId,
        Long funcionarioId,
        EscopoCarteira escopo,
        boolean podeTerCarteira
) {}
