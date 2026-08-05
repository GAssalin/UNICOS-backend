package br.com.unicos.ms_cliente.dto;

import br.com.unicos.ms_cliente.enums.StatusCliente;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ClienteResponseDTO {
    private Long id;
    private Long empresaId;
    private Long pessoaId;
    private Long vendedorId;
    private String nomeVendedor;
    private Long filialId;
    private String codigoInterno;
    private StatusCliente status;
    private Long categoriaId;
    private String categoriaNome;
    private String observacaoGeral;
    private Boolean permiteVendaAPrazo;
    private BigDecimal limiteCredito;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}