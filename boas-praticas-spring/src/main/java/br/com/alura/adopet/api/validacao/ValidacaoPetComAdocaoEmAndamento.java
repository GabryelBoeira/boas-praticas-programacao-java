package br.com.alura.adopet.api.validacao;

import br.com.alura.adopet.api.dto.Adocao.SolicitarAdocaoDTO;
import br.com.alura.adopet.api.enums.StatusAdocao;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.service.PetService;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidacaoPetComAdocaoEmAndamento implements ValidacaoSolicidacaoAdocao {

    private final AdocaoRepository adocaoRepository;

    public ValidacaoPetComAdocaoEmAndamento(AdocaoRepository adocaoRepository) {
        this.adocaoRepository = adocaoRepository;
    }

    @Override
    public void validar(SolicitarAdocaoDTO dto) {
        if (adocaoRepository.existsByPetIdAndStatus(dto.petId(), StatusAdocao.AGUARDANDO_AVALIACAO)) {
            throw new ValidationException("Pet já está aguardando avaliação para ser adotado!");
        }
    }

}
