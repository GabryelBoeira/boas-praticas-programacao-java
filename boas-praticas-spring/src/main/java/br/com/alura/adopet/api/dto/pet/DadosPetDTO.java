package br.com.alura.adopet.api.dto.pet;

import br.com.alura.adopet.api.model.Pet;

public record DadosPetDTO(Long id,
                          String tipo,
                          String nome,
                          String raca,
                          Integer idade,
                          String cor,
                          Float peso,
                          boolean adotado) {

    public DadosPetDTO(Pet pet) {
        this(pet.getId(),
            pet.getTipo().name(),
            pet.getNome(),
            pet.getRaca(),
            pet.getIdade(),
            pet.getCor(),
            pet.getPeso(),
            pet.getAdotado());
    }
}
