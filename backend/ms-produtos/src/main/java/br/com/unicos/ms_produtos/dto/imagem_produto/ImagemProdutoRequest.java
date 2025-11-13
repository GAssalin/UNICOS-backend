package br.com.unicos.ms_produtos.dto.imagem_produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de imagens de produto.
 *
 * <p>
 * Representa os dados necessários para registrar uma imagem associada
 * a um produto, permitindo definir URL, texto alternativo, ordem
 * de exibição e se ela é a imagem principal.
 * </p>
 */
public record ImagemProdutoRequest(
        @NotBlank(message = "A URL da imagem é obrigatória.")
        @Size(max = 500)
        String url,
        @Size(max = 255)
        String descricaoAlt,
        @NotNull
        Boolean principal,
        Integer ordemExibicao
) {}
