package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.tutor.AtualizarTutorDTO;
import br.com.alura.adopet.api.dto.tutor.CadastrarTutorDTO;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TutorService {

    private TutorRepository tutorRepository;

    @Autowired
    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    @Transactional
    public String cadastrar(CadastrarTutorDTO cadastrar) throws ValidationException {
        if (tutorRepository.existsByTelefoneOrEmail(cadastrar.telefone(), cadastrar.email())) {
            throw new ValidationException("Dados já cadastrados para outro tutor!");
        }

        tutorRepository.save(new Tutor(cadastrar));
        return "Tutor cadastrado com sucesso!";
    }

    @Transactional
    public void atualizar(AtualizarTutorDTO atualizar) {
        Tutor tutor = tutorRepository.getReferenceById(atualizar.id());
        tutor.atualizarTutor(atualizar);
    }

    public Tutor buscarPorId(Long id) {
        return tutorRepository.getReferenceById(id);
    }
}
