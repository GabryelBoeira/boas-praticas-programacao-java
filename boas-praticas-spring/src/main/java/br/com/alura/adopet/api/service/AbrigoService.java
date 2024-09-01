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
        boolean dadosCadastrados = abrigoRepository.existsByTelefoneOrEmailOrNome(abrigoDTO.telefone(), abrigoDTO.email(), abrigoDTO.nome());

        if (dadosCadastrados) {
            throw new ValidationException("Dados já cadastrados para outro abrigo!");
        }

        abrigoRepository.save(new Abrigo(abrigoDTO));
    }

    /**
     * Lista os pets de um abrigo, a partir do id ou nome do abrigo.
     *
     * @param idOuNome id ou nome do abrigo
     * @return lista de pets do abrigo
     * @throws ValidationException se o abrigo nao existe
     */
    public List<DadosPetDTO> listarPetsByParam(String idOuNome) {
        return procurarAbrigoPorIdOuNome(idOuNome)
                .getPets()
                .stream()
                .map(DadosPetDTO::new)
                .toList();
    }

    @Transactional
    public void cadastrarPetNoAbrigo(String idOuNome, CadastrarPetDTO petDTO) {
        Abrigo abrigo = procurarAbrigoPorIdOuNome(idOuNome);

        abrigo.getPets().add(new Pet(petDTO, abrigo));
        abrigoRepository.save(abrigo);
    }

    /**
     * Busca um abrigo pelo id ou nome
     *
     * @param idOuNome id ou nome do abrigo
     * @return abrigo
     * @throws ValidationException se o abrigo nao existe
     */
    public Abrigo procurarAbrigoPorIdOuNome(String idOuNome) {
        try {
            if (ValidationUtils.isNumero(idOuNome)) {
                return abrigoRepository.findById(Long.parseLong(idOuNome)).orElseThrow(EntityNotFoundException::new);
            }
            return abrigoRepository.findByNome(idOuNome);
        } catch (EntityNotFoundException e) {
            throw new ValidationException("Nao existe um abrigo com esse nome ou id");
        }
    }

}
