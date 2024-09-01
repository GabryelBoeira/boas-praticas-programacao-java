package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.pet.DadosPetDTO;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<DadosPetDTO> listarTodosDisponiveis() {
        return petRepository.findAllByAdotadoEquals(false)
                .stream()
                .map(DadosPetDTO::new)
                .toList();
    }

    public Pet buscarPorId(Long id) {
        return petRepository.getReferenceById(id);
    }

}
