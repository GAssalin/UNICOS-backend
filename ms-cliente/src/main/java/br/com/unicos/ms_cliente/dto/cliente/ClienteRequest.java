package br.com.unicos.ms_cliente.dto.cliente;

import br.com.unicos.ms_cliente.enums.StatusCliente;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ClienteRequest(
    @NotNull(message = "O identificador da pessoa é obrigatório.")
    Long pessoaId,
    Long vendedorId,
    Long filialId,
    String codigoInterno,
    StatusCliente status,
    Long categoriaId,
    String observacaoGeral,
    Boolean permiteVendaAPrazo,
    BigDecimal limiteCredito
) { }
