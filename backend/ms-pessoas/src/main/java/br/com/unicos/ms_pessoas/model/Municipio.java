package br.com.unicos.ms_pessoas.model;

import br.com.unicos.ms_pessoas.enums.Uf;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um município (cidade) vinculado a um estado (UF).
 */
@Entity
@Table(name = "municipio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Municipio {

    /**
     * Identificador único do município.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do município.
     */
    @NotBlank(message = "O nome do município é obrigatório.")
    @Size(max = 100, message = "O nome do município deve ter no máximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Unidade Federativa (Estado) ao qual o município pertence.
     */
    @NotNull(message = "A UF é obrigatória.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Uf uf;
}
