package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.abrigo.CadastrarAbrigoDTO;
import br.com.alura.adopet.api.dto.abrigo.DadosAbrigoDTO;
import br.com.alura.adopet.api.dto.pet.CadastrarPetDTO;
import br.com.alura.adopet.api.dto.pet.DadosPetDTO;
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

    public List<DadosAbrigoDTO> listarTodos() {
        return abrigoRepository.findAll()
                .stream()
                .map(DadosAbrigoDTO::new)
                .toList();
    }

    @Transactional
    public void cadastrar(CadastrarAbrigoDTO abrigoDTO) throws ValidationException {
        boolean nomeJaCadastrado = abrigoRepository.existsByNome(abrigoDTO.nome());
        boolean telefoneJaCadastrado = abrigoRepository.existsByTelefone(abrigoDTO.telefone());
        boolean emailJaCadastrado = abrigoRepository.existsByEmail(abrigoDTO.email());

        if (nomeJaCadastrado || telefoneJaCadastrado || emailJaCadastrado) {
            throw new ValidationException("Dados já cadastrados para outro abrigo!");
        }

        abrigoRepository.save(new Abrigo(abrigoDTO));
    }

    /**
     * Lista os pets de um abrigo, a partir do id ou nome do abrigo.
     * @param idOuNome id ou nome do abrigo
     * @return lista de pets do abrigo
     * @throws ValidationException se o abrigo nao existe
     */
    public List<DadosPetDTO> listarPetsByParam(String idOuNome) {
        try {
            List<Pet> pets;
            if (ValidationUtils.isNumero(idOuNome)) {
                pets =  abrigoRepository.getReferenceById(Long.parseLong(idOuNome)).getPets();
            }
            pets = abrigoRepository.findByNome(idOuNome).getPets();

            return pets.stream().map(DadosPetDTO::new).toList();
        } catch (EntityNotFoundException e) {
            throw new ValidationException("Nao existe um abrigo com esse nome ou id");
        }
    }

    @Transactional
    public void cadastrarPetNoAbrigo(String idOuNome, CadastrarPetDTO petDTO) {
        Abrigo abrigo;
        try {
            if (ValidationUtils.isNumero(idOuNome)) {
                abrigo = abrigoRepository.getReferenceById(Long.parseLong(idOuNome));
            } else {
                abrigo = abrigoRepository.findByNome(idOuNome);
            }

            Pet novoPet = new Pet(petDTO);
            novoPet.setAbrigo(abrigo);
            novoPet.setAdotado(false);
            abrigo.getPets().add(novoPet);
            abrigoRepository.save(abrigo);
        } catch (EntityNotFoundException e) {
            throw new ValidationException("Nao existe um abrigo com esse nome ou id");
        }
    }

}
