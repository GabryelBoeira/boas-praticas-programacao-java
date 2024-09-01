package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.service.AbrigoService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/abrigos")
public class AbrigoController {

    private AbrigoService abrigoService;

    @Autowired
    public AbrigoController(AbrigoService abrigoService) {
        this.abrigoService = abrigoService;
    }

    @GetMapping
    public ResponseEntity<List<Abrigo>> listar() {
        return ResponseEntity.ok(abrigoService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<String> cadastrarAbrigo(@RequestBody @Valid Abrigo abrigo) {
        try {
            abrigoService.cadastrar(abrigo);
            return ResponseEntity.ok("Abrigo Cadastrado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{idOuNome}/pets")
    public ResponseEntity<List<Pet>> listarPetsByParam(@PathVariable String idOuNome) {
        try {
            return ResponseEntity.ok(abrigoService.listarPetsByParam(idOuNome));
        } catch (ValidationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{idOuNome}/pets")
    public ResponseEntity<String> cadastrarPet(@PathVariable String idOuNome, @RequestBody @Valid Pet pet) {
        try {
            abrigoService.cadastrarPetNoAbrigo(idOuNome, pet);
            return ResponseEntity.ok("Pet Cadastrado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
