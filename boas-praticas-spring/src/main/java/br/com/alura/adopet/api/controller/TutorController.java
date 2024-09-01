package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.tutor.AtualizarTutorDTO;
import br.com.alura.adopet.api.dto.tutor.CadastrarTutorDTO;
import br.com.alura.adopet.api.service.TutorService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tutores")
public class TutorController {

    private TutorService tutorService;

    @Autowired
    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody @Valid CadastrarTutorDTO tutorDTO) {
        try {
            return ResponseEntity.ok(tutorService.cadastrar(tutorDTO));
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<String> atualizar(@RequestBody @Valid AtualizarTutorDTO tutorDTO) {
        tutorService.atualizar(tutorDTO);
        return ResponseEntity.ok().build();
    }

}
