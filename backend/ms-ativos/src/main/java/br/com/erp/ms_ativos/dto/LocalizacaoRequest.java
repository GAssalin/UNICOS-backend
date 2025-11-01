package br.com.erp.ms_ativos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalizacaoRequest {

    @NotBlank
    @Size(max = 100)
    private String descricao;

    @Size(max = 10)
    private String andar;

    @Size(max = 20)
    private String bloco;

    @NotNull
    private Long filialId;
}