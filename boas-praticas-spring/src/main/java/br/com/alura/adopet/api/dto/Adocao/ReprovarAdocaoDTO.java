package br.com.alura.adopet.api.dto.Adocao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReprovarAdocaoDTO(
        @NotNull
        Long idAdocao,

        @NotBlank
        String justificativa
) {
}
