package br.com.alura.adopet.api.validacao;

import br.com.alura.adopet.api.dto.Adocao.SolicitarAdocaoDTO;
import br.com.alura.adopet.api.enums.StatusAdocao;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.service.TutorService;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidacaoTutorComAdocaoEmAndamento {

    private final AdocaoRepository adocaoRepository;
    private final TutorService tutorService;

    public ValidacaoTutorComAdocaoEmAndamento(AdocaoRepository adocaoRepository, TutorService tutorService) {
        this.adocaoRepository = adocaoRepository;
        this.tutorService = tutorService;
    }

    public void validar(SolicitarAdocaoDTO dto) {
        List<Adocao> adocoes = adocaoRepository.findAll();
        Tutor tutor = tutorService.buscarPorId(dto.tutorId());

        for (Adocao a : adocoes) {
            int contador = 0;
            if (a.getTutor() == tutor && a.getStatus() == StatusAdocao.APROVADO) {
                contador = contador + 1;
            }
            if (contador == 5) {
                throw new ValidationException("Tutor chegou ao limite máximo de 5 adoções!");
            }
        }
    }
}
