package br.com.erp.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "atributo_personalizado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtributoPersonalizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @NotBlank(message = "O nome do atributo é obrigatório.")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O valor do atributo é obrigatório.")
    @Column(nullable = false, length = 100)
    private String valor;

}
