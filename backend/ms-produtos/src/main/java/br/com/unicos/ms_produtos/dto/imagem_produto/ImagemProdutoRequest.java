package br.com.unicos.ms_produtos.dto.imagem_produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de imagens de produto.
 *
 * <p>
 * O produto e a empresa (tenant) são resolvidos automaticamente
 * pelo backend a partir do contexto da requisição.
 * </p>
 */
public record ImagemProdutoRequest(

        @NotBlank(message = "A URL da imagem é obrigatória.")
        @Size(max = 500, message = "A URL da imagem deve ter no máximo 500 caracteres.")
        String url,

        @Size(max = 255, message = "A descrição alternativa deve ter no máximo 255 caracteres.")
        String descricaoAlt,

        @NotNull(message = "É obrigatório informar se a imagem é principal.")
        Boolean principal,

        Integer ordemExibicao
) {}
