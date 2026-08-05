package br.com.unicos.ms_cliente.dto;

import br.com.unicos.ms_cliente.enums.StatusCliente;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ClienteRequestDTO {
    @NotNull(message = "O identificador da pessoa é obrigatório.")
    private Long pessoaId;
    private Long vendedorId;
    private Long filialId;
    private String codigoInterno;
    private StatusCliente status;
    private Long categoriaId;
    private String observacaoGeral;
    private Boolean permiteVendaAPrazo;
    private BigDecimal limiteCredito;
}