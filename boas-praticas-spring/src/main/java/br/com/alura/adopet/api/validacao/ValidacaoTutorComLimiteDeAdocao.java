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
public class ValidacaoTutorComLimiteDeAdocao {

    private final AdocaoRepository adocaoRepository;
    private final PetService petService;

    public ValidacaoTutorComLimiteDeAdocao(AdocaoRepository adocaoRepository, PetService petService) {
        this.adocaoRepository = adocaoRepository;
        this.petService = petService;
    }

    public void validar(SolicitarAdocaoDTO dto) {
        List<Adocao> adocoes = adocaoRepository.findAll();
        Pet pet = petService.buscarPorId(dto.petId());

        for (Adocao a : adocoes) {
            if (a.getPet() == pet && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                throw new ValidationException("Pet já está aguardando avaliação para ser adotado!");
            }
        }
    }
}
