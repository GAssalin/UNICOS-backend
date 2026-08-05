package br.com.unicos.ms_cliente.dto;

import br.com.unicos.ms_cliente.enums.TipoObservacaoCliente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClienteObservacaoResponseDTO {

    private Long id;
    private Long empresaId;
    private Long clienteId;
    private String titulo;
    private String descricao;
    private TipoObservacaoCliente tipo;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}