package br.com.unicos.ms_estoque.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Representa um lote ou série de produto, com controle de validade e rastreabilidade.
 */
@Entity
@Table(name = "lote_serie")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoteSerie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String codigo;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Column(length = 255)
    private String observacao;
}
