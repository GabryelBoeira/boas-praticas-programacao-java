package br.com.alura.adopet.api.validacao;

import br.com.alura.adopet.api.dto.Adocao.SolicitarAdocaoDTO;
import br.com.alura.adopet.api.enums.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.service.PetService;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoTutorComLimiteDeAdocao implements ValidacaoSolicidacaoAdocao {

    private final AdocaoRepository adocaoRepository;

    public ValidacaoTutorComLimiteDeAdocao(AdocaoRepository adocaoRepository) {
        this.adocaoRepository = adocaoRepository;
    }

    @Override
    public void validar(SolicitarAdocaoDTO dto) {
        if (adocaoRepository.existsByTutorIdAndStatus(dto.tutorId(), StatusAdocao.AGUARDANDO_AVALIACAO)) {
            throw new ValidationException("Tutor já possui outra adoção aguardando avaliação!");
        }
    }
}
