package br.com.alura.adopet.api.model;

import br.com.alura.adopet.api.dto.tutor.AtualizarTutorDTO;
import br.com.alura.adopet.api.dto.tutor.CadastrarTutorDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tutores")
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String telefone;

    private String email;

    @OneToMany(mappedBy = "tutor")
    @JsonManagedReference("tutor_adocoes")
    private List<Adocao> adocoes;

    public Tutor() {}

    public Tutor(CadastrarTutorDTO dto) {
        this.nome = dto.nome();
        this.telefone = dto.telefone();
        this.email = dto.email();
    }

    public void atualizarTutor(AtualizarTutorDTO dto) {
        this.id = dto.id();
        this.nome = dto.nome();
        this.telefone = dto.telefone();
        this.email = dto.email();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public List<Adocao> getAdocoes() {
        return adocoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tutor tutor = (Tutor) o;
        return Objects.equals(id, tutor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
