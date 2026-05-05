package br.com.unicos.ms_cliente.dto;

import br.com.unicos.ms_cliente.enums.StatusCliente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClienteHistoricoStatusResponseDTO {

    private Long id;
    private Long empresaId;
    private Long clienteId;
    private StatusCliente statusAnterior;
    private StatusCliente statusNovo;
    private String motivo;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}