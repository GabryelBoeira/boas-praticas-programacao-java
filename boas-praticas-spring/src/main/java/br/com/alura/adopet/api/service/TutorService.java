package br.com.alura.adopet.api.service;

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
    public String cadastrar(CadastrarTutorDTO tutorDTO) throws ValidationException {
        boolean telefoneJaCadastrado = tutorRepository.existsByTelefone(tutorDTO.telefone());
        boolean emailJaCadastrado = tutorRepository.existsByEmail(tutorDTO.email());

        if (telefoneJaCadastrado || emailJaCadastrado) {
            throw new ValidationException("Dados já cadastrados para outro tutor!");
        }

        tutorRepository.save(new Tutor(tutorDTO));
        return "Tutor cadastrado com sucesso!";
    }

    public void atualizar(CadastrarTutorDTO tutorDTO) {
        tutorRepository.save((new Tutor(tutorDTO)));
    }

    protected Tutor buscarPorId(Long id) {
        return tutorRepository.getReferenceById(id);
    }
}
