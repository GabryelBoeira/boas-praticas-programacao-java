package br.com.alura.adopet.api.repository;

import br.com.alura.adopet.api.enums.StatusAdocao;
import br.com.alura.adopet.api.model.Adocao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdocaoRepository extends JpaRepository<Adocao, Long> {

    /**
     * Verifica se ja existe uma solicita o de adocao em andamento para o pet informado.
     *
     * @param petId o id do pet
     * @param status  o status da solicitacao de adocao
     * @return true se ja  existe uma solicitacao de adocao em andamento, false caso otherwise
     */
    boolean existsByPetIdAndStatus(Long petId, StatusAdocao status);

    /**
     * Verifica se j  existe uma solicita o de ado o em andamento para o tutor informado.
     *
     * @param tutorId o id do tutor
     * @param status  o status da solicitaco de adocao
     * @return true se ja  existe uma solicitacao de adocao em andamento, false caso contrario
     */
    boolean existsByTutorIdAndStatus(Long tutorId, StatusAdocao status);

    /**
     * Conta quantas adocoes existem em andamento para o tutor informado.
     *
     * @param tutorId o id do tutor
     * @param status  o status da solicitacao de adocao
     * @return o total de adocoes pelo status e pelo tutor
     */
    Long countByTutorIdAndStatus(Long tutorId, StatusAdocao status);

}
