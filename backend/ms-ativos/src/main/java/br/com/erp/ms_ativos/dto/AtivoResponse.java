package br.com.erp.ms_ativos.dto;

import br.com.erp.ms_ativos.enums.StatusAtivo;
import br.com.erp.ms_ativos.enums.TipoAtivo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtivoResponse {

    private Long id;
    private String nome;
    private String codigoPatrimonial;
    private String descricao;
    private TipoAtivo tipo;
    private StatusAtivo status;
    private LocalDate dataAquisicao;
    private BigDecimal valorAquisicao;
    private BigDecimal valorAtual;
    private Long empresaId;
    private Long filialId;
    private Long responsavelId;
    private Long localizacaoId;
}