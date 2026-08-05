package br.com.unicos.ms_cliente.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClienteCategoriaResponseDTO {

    private Long id;
    private Long empresaId;
    private String nome;
    private String descricao;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}