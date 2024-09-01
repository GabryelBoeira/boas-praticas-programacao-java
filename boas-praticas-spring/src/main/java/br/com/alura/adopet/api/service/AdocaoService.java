package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.Adocao.AprovarAdocaoDTO;
import br.com.alura.adopet.api.dto.Adocao.ReprovarAdocaoDTO;
import br.com.alura.adopet.api.dto.Adocao.SolicitarAdocaoDTO;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.enums.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdocaoService {

    private final AdocaoRepository adocaoRepository;
    private final EmailService emailService;
    private final PetService petService;
    private final TutorService tutorService;

    @Autowired
    public AdocaoService(AdocaoRepository adocaoRepository, EmailService emailService, PetService petService, TutorService tutorService) {
        this.adocaoRepository = adocaoRepository;
        this.emailService = emailService;
        this.petService = petService;
        this.tutorService = tutorService;
    }

    @Transactional
    public void solicitar(SolicitarAdocaoDTO solicitar) throws ValidationException {

        Pet pet = petService.buscarPorId(solicitar.petId());
        if (pet.getAdotado()) throw new ValidationException("Pet já foi adotado!");

        Tutor tutor = tutorService.buscarPorId(solicitar.tutorId());
        if (tutor == null) throw new ValidationException("Tutor não encontrado. Favor informar um tutor existente");

        List<Adocao> adocoes = adocaoRepository.findAll();
        for (Adocao a : adocoes) {
            if (a.getTutor() == tutor && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                throw new ValidationException("Tutor já possui outra adoção aguardando avaliação!");
            }
        }
        for (Adocao a : adocoes) {
            if (a.getPet() == pet && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                throw new ValidationException("Pet já está aguardando avaliação para ser adotado!");
            }
        }
        for (Adocao a : adocoes) {
            int contador = 0;
            if (a.getTutor() == tutor && a.getStatus() == StatusAdocao.APROVADO) {
                contador = contador + 1;
            }
            if (contador == 5) {
                throw new ValidationException("Tutor chegou ao limite máximo de 5 adoções!");
            }
        }

        Adocao adocao = new Adocao();
        adocao.setPet(pet);
        adocao.setTutor(tutor);
        adocao.setData(LocalDateTime.now());
        adocao.setStatus(StatusAdocao.AGUARDANDO_AVALIACAO);
        adocao.setMotivo(solicitar.motivo());
        adocaoRepository.save(adocao);

        emailService.enviarEmail(
                adocao.getPet().getAbrigo().getEmail(),
                "Solicitação de adoção",
                "Olá " + adocao.getPet().getAbrigo().getNome() + "!\n\nUma solicitação de adoção foi registrada hoje para o pet: " + adocao.getPet().getNome() + ". \nFavor avaliar para aprovação ou reprovação."
        );
    }

    /*
     * Envia email para o abrigo com a solicitação de adoção.
     * O email informa que uma solicitação de adoção foi feita
     * para o pet e pede para o abrigo avaliar para aprovação
     * ou reprovação.
     *
     * @param adocao informação da adoção
     */
    @Transactional
    public void aprovar(AprovarAdocaoDTO aprovar) {
        Adocao adocao = adocaoRepository.findById(aprovar.idAdocao()).orElseThrow(() -> new ValidationException("Adoção não encontrada!"));
        adocao.setStatus(StatusAdocao.APROVADO);
        adocaoRepository.save(adocao);

        emailService.enviarEmail(adocao.getTutor().getEmail(),
                "Adoção aprovada",
                "Parabéns " + adocao.getTutor().getNome() + "!\n\nSua adoção do pet " + adocao.getPet().getNome() + ", solicitada em " + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + ", foi aprovada.\nFavor entrar em contato com o abrigo " + adocao.getPet().getAbrigo().getNome() + " para agendar a busca do seu pet."
        );
    }

    @Transactional
    public void reprovar(ReprovarAdocaoDTO reprovar) {
        Adocao adocao = adocaoRepository.findById(reprovar.idAdocao()).orElseThrow(() -> new ValidationException("Adoção não encontrada!"));
        adocao.setJustificativaStatus(reprovar.justificativa());
        adocao.setStatus(StatusAdocao.REPROVADO);
        adocaoRepository.save(adocao);

        emailService.enviarEmail(adocao.getTutor().getEmail(),
                "Adoção reprovada",
                "Olá " + adocao.getTutor().getNome() + "!\n\nInfelizmente sua adoção do pet " + adocao.getPet().getNome() + ", solicitada em " + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + ", foi reprovada pelo abrigo " + adocao.getPet().getAbrigo().getNome() + " com a seguinte justificativa: " + adocao.getJustificativaStatus()
        );
    }

}
