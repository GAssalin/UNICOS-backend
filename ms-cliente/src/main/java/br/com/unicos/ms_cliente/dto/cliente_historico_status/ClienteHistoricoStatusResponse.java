package br.com.unicos.ms_cliente.dto.cliente_historico_status;

import br.com.unicos.ms_cliente.enums.StatusCliente;

import java.time.LocalDateTime;

public record ClienteHistoricoStatusResponse(

    Long id,
    Long empresaId,
    Long clienteId,
    StatusCliente statusAnterior,
    StatusCliente statusNovo,
    String motivo,
    Boolean ativo,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) { }
