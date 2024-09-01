package br.com.alura.adopet.api.repository;

import br.com.alura.adopet.api.model.Abrigo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbrigoRepository extends JpaRepository<Abrigo, Long> {

    boolean existsByTelefoneOrEmailOrNome(String telefone, String email, String nome);

    Abrigo findByNome(String nome);

}
