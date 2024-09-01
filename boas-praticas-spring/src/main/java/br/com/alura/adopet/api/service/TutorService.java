package br.com.alura.adopet.api.service;

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
    public String cadastrar(Tutor tutor) throws ValidationException {
        boolean telefoneJaCadastrado = tutorRepository.existsByTelefone(tutor.getTelefone());
        boolean emailJaCadastrado = tutorRepository.existsByEmail(tutor.getEmail());

        if (telefoneJaCadastrado || emailJaCadastrado) {
            throw new ValidationException("Dados já cadastrados para outro tutor!");
        }

        tutorRepository.save(tutor);
        return "Tutor cadastrado com sucesso!";
    }

    public void atualizar(Tutor tutor) {
        tutorRepository.save(tutor);
    }

}
