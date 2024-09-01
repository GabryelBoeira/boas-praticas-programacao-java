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
public class ValidacaoTutorComAdocaoEmAndamento implements ValidacaoSolicidacaoAdocao {

    private final AdocaoRepository adocaoRepository;
    private final TutorService tutorService;

    public ValidacaoTutorComAdocaoEmAndamento(AdocaoRepository adocaoRepository, TutorService tutorService) {
        this.adocaoRepository = adocaoRepository;
        this.tutorService = tutorService;
    }

    @Override
    public void validar(SolicitarAdocaoDTO dto) {
        Long totalAdocoes = adocaoRepository.countByTutorIdAndStatus(dto.tutorId(), StatusAdocao.APROVADO);

        if (totalAdocoes >= 5L) {
            throw new ValidationException("Tutor chegou ao limite máximo de 5 adoções!");
        }
    }
}
