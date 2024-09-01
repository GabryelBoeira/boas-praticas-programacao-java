package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.pet.DadosPetDTO;
import br.com.alura.adopet.api.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    private PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public ResponseEntity<List<DadosPetDTO>> listarTodosDisponiveis() {
        return ResponseEntity.ok(petService.listarTodosDisponiveis());
    }

}
