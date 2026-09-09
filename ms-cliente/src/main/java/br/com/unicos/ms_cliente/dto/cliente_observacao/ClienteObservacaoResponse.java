package br.com.unicos.ms_cliente.dto.cliente_observacao;

import br.com.unicos.ms_cliente.enums.TipoObservacaoCliente;

import java.time.LocalDateTime;

public record ClienteObservacaoResponse(

    Long id,
    Long empresaId,
    Long clienteId,
    String titulo,
    String descricao,
    TipoObservacaoCliente tipo,
    Boolean ativo,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) { }
