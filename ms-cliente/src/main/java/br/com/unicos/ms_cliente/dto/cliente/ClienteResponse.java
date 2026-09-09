package br.com.unicos.ms_cliente.dto.cliente;

import br.com.unicos.ms_cliente.enums.StatusCliente;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ClienteResponse(
    Long id,
    Long empresaId,
    Long pessoaId,
    Long vendedorId,
    String nomeVendedor,
    Long filialId,
    String codigoInterno,
    StatusCliente status,
    Long categoriaId,
    String categoriaNome,
    String observacaoGeral,
    Boolean permiteVendaAPrazo,
    BigDecimal limiteCredito,
    Boolean ativo,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) { }
