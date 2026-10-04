package br.com.unicos.ms_cliente.dto.cliente;

import br.com.unicos.ms_cliente.enums.StatusCliente;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ClienteRequest(
    @NotNull(message = "O identificador da pessoa é obrigatório.")
    Long pessoaId,
    Long vendedorId,
    Long filialId,
    @Size(max = 50, message = "O código interno deve ter no máximo 50 caracteres.")
    String codigoInterno,
    @NotNull(message = "O status do cliente é obrigatório.")
    StatusCliente status,
    Long categoriaId,
    @Size(max = 1000, message = "A observação geral deve ter no máximo 1000 caracteres.")
    String observacaoGeral,
    @NotNull(message = "Informe se o cliente pode comprar a prazo.")
    Boolean permiteVendaAPrazo,
    @PositiveOrZero(message = "O limite de crédito não pode ser negativo.")
    @Digits(integer = 13, fraction = 2, message = "O limite de crédito deve ter até 13 dígitos inteiros e 2 decimais.")
    BigDecimal limiteCredito
) { }
