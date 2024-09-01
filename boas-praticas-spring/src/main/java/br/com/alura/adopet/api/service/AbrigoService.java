package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.utils.ValidationUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AbrigoService {

    private AbrigoRepository abrigoRepository;

    @Autowired
    public AbrigoService(AbrigoRepository abrigoRepository) {
        this.abrigoRepository = abrigoRepository;
    }

    public List<Abrigo> listarTodos() {
        return abrigoRepository.findAll();
    }

    @Transactional
    public void cadastrar(Abrigo abrigo) throws ValidationException {
        boolean nomeJaCadastrado = abrigoRepository.existsByNome(abrigo.getNome());
        boolean telefoneJaCadastrado = abrigoRepository.existsByTelefone(abrigo.getTelefone());
        boolean emailJaCadastrado = abrigoRepository.existsByEmail(abrigo.getEmail());

        if (nomeJaCadastrado || telefoneJaCadastrado || emailJaCadastrado) {
            throw new ValidationException("Dados já cadastrados para outro abrigo!");
        }
        abrigoRepository.save(abrigo);
    }

    /**
     * Lista os pets de um abrigo, a partir do id ou nome do abrigo.
     * @param idOuNome id ou nome do abrigo
     * @return lista de pets do abrigo
     * @throws ValidationException se o abrigo nao existe
     */
    public List<Pet> listarPetsByParam(String idOuNome) {
        try {
            if (ValidationUtils.isNumero(idOuNome)) {
                return abrigoRepository.getReferenceById(Long.parseLong(idOuNome)).getPets();
            }
            return abrigoRepository.findByNome(idOuNome).getPets();
        } catch (EntityNotFoundException e) {
            throw new ValidationException("Nao existe um abrigo com esse nome ou id");
        }
    }

    @Transactional
    public void cadastrarPetNoAbrigo(String idOuNome, Pet pet) {
        Abrigo abrigo;
        try {
            if (ValidationUtils.isNumero(idOuNome)) {
                abrigo = abrigoRepository.getReferenceById(Long.parseLong(idOuNome));
            } else {
                abrigo = abrigoRepository.findByNome(idOuNome);
            }
            pet.setAbrigo(abrigo);
            pet.setAdotado(false);
            abrigo.getPets().add(pet);
            abrigoRepository.save(abrigo);
        } catch (EntityNotFoundException e) {
            throw new ValidationException("Nao existe um abrigo com esse nome ou id");
        }
    }

}
