package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.Adocao.AprovarAdocaoDTO;
import br.com.alura.adopet.api.dto.Adocao.ReprovarAdocaoDTO;
import br.com.alura.adopet.api.dto.Adocao.SolicitarAdocaoDTO;
import br.com.alura.adopet.api.service.AdocaoService;
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
@RequestMapping("/adocoes")
public class AdocaoController {

    private final AdocaoService adocaoService;

    @Autowired
    public AdocaoController(AdocaoService adocaoService) {
        this.adocaoService = adocaoService;
    }

    @PostMapping
    public ResponseEntity<String> solicitar(@RequestBody @Valid SolicitarAdocaoDTO adocao) {
        try {
            adocaoService.solicitar(adocao);
            return ResponseEntity.ok("Adoção solicitada com sucesso!");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/aprovar")
    public ResponseEntity<String> aprovar(@RequestBody @Valid AprovarAdocaoDTO adocao) {
        adocaoService.aprovar(adocao);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reprovar")
    public ResponseEntity<String> reprovar(@RequestBody @Valid ReprovarAdocaoDTO adocao) {
        adocaoService.reprovar(adocao);
        return ResponseEntity.ok().build();
    }

}
