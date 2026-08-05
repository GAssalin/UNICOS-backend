package br.com.unicos.ms_cliente.dto;

import br.com.unicos.ms_cliente.enums.TipoObservacaoCliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteObservacaoRequestDTO {

    @NotNull(message = "O identificador do cliente é obrigatório.")
    private Long clienteId;

    private String titulo;

    @NotBlank(message = "A descrição da observação é obrigatória.")
    private String descricao;

    @NotNull(message = "O tipo da observação é obrigatório.")
    private TipoObservacaoCliente tipo;
}