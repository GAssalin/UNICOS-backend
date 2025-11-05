package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

/**
 * Entidade que representa uma marca ou fabricante de produtos.
 * Pode estar associada a diversos produtos do catálogo.
 */
@Entity
@Table(name = "marca")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da marca é obrigatório.")
    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(length = 100)
    private String paisOrigem;

    @OneToMany(mappedBy = "marca")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Produto> produtos;

}
