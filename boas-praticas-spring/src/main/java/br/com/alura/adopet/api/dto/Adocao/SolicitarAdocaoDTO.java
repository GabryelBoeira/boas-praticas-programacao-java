package br.com.alura.adopet.api.dto.Adocao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SolicitarAdocaoDTO(
        @NotNull
        Long tutorId,
        @NotNull
        Long petId,
        @NotBlank
        String motivo
) {
}
