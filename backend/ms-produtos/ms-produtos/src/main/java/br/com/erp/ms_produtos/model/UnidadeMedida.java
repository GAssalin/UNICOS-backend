package br.com.erp.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "unidade_medida")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnidadeMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da unidade é obrigatório.")
    @Column(nullable = false, length = 50)
    private String nome;

    @NotBlank(message = "A sigla é obrigatória.")
    @Column(nullable = false, length = 10, unique = true)
    private String sigla;

}
