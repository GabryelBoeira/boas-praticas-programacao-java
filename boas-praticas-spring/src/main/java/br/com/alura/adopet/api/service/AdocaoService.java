package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdocaoService {

    private final AdocaoRepository adocaoRepository;
    private final EmailService emailService;

    @Autowired
    public AdocaoService(AdocaoRepository adocaoRepository, EmailService emailService) {
        this.adocaoRepository = adocaoRepository;
        this.emailService = emailService;
    }

    public void solicitar(Adocao adocao) throws ValidationException {
        if (adocao.getPet().getAdotado()) throw new ValidationException("Pet já foi adotado!");

        List<Adocao> adocoes = adocaoRepository.findAll();
        for (Adocao a : adocoes) {
            if (a.getTutor() == adocao.getTutor() && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                throw new ValidationException("Tutor já possui outra adoção aguardando avaliação!");
            }
        }
        for (Adocao a : adocoes) {
            if (a.getPet() == adocao.getPet() && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                throw new ValidationException("Pet já está aguardando avaliação para ser adotado!");
            }
        }
        for (Adocao a : adocoes) {
            int contador = 0;
            if (a.getTutor() == adocao.getTutor() && a.getStatus() == StatusAdocao.APROVADO) {
                contador = contador + 1;
            }
            if (contador == 5) {
                throw new ValidationException("Tutor chegou ao limite máximo de 5 adoções!");
            }
        }

        adocao.setData(LocalDateTime.now());
        adocao.setStatus(StatusAdocao.AGUARDANDO_AVALIACAO);
        adocaoRepository.save(adocao);

        emailService.enviarEmail(
                adocao.getPet().getAbrigo().getEmail(),
                "Solicitação de adoção",
                "Olá " + adocao.getPet().getAbrigo().getNome() + "!\n\nUma solicitação de adoção foi registrada hoje para o pet: " + adocao.getPet().getNome() + ". \nFavor avaliar para aprovação ou reprovação."
        );
    }

    public void aprovar(Adocao adocao) {
        adocao.setStatus(StatusAdocao.APROVADO);
        adocaoRepository.save(adocao);

        emailService.enviarEmail(adocao.getTutor().getEmail(),
                "Adoção aprovada",
                "Parabéns " + adocao.getTutor().getNome() + "!\n\nSua adoção do pet " + adocao.getPet().getNome() + ", solicitada em " + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + ", foi aprovada.\nFavor entrar em contato com o abrigo " + adocao.getPet().getAbrigo().getNome() + " para agendar a busca do seu pet."
        );
    }

    public void reprovar(Adocao adocao) {
        adocao.setStatus(StatusAdocao.REPROVADO);
        adocaoRepository.save(adocao);


        emailService.enviarEmail(adocao.getTutor().getEmail(),
                "Adoção reprovada",
                "Olá " + adocao.getTutor().getNome() + "!\n\nInfelizmente sua adoção do pet " + adocao.getPet().getNome() + ", solicitada em " + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + ", foi reprovada pelo abrigo " + adocao.getPet().getAbrigo().getNome() + " com a seguinte justificativa: " + adocao.getJustificativaStatus()
        );
    }

}
