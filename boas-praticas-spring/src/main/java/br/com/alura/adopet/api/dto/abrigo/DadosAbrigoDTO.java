package br.com.alura.adopet.api.dto.abrigo;

import br.com.alura.adopet.api.model.Abrigo;

public record DadosAbrigoDTO(Long id, String nome, String telefone, String email) {

    public DadosAbrigoDTO(Abrigo abrigo) {
        this(abrigo.getId(),
                abrigo.getNome(),
                abrigo.getTelefone(),
                abrigo.getEmail());
    }
}
