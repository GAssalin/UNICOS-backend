package br.com.unicos.ms_cliente.dto.cliente_observacao;

import br.com.unicos.ms_cliente.enums.TipoObservacaoCliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteObservacaoRequest(

    @NotNull(message = "O identificador do cliente é obrigatório.")
    Long clienteId,

    String titulo,

    @NotBlank(message = "A descrição da observação é obrigatória.")
    String descricao,

    @NotNull(message = "O tipo da observação é obrigatório.")
    TipoObservacaoCliente tipo
) { }
