package br.com.alura.adopet.api.model;

import br.com.alura.adopet.api.enums.StatusAdocao;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "adocoes")
public class Adocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime data;

    private String motivo;

    @Enumerated(EnumType.STRING)
    private StatusAdocao status;

    private String justificativaStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("tutor_adocoes")
    private Tutor tutor;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonManagedReference("adocao_pets")
    private Pet pet;

    public Adocao() {}

    public Adocao(Pet pet, Tutor tutor, String motivo) {
        this.pet= pet;
        this.tutor = tutor;
        this.data = LocalDateTime.now();
        this.motivo = motivo;
        this.status= StatusAdocao.AGUARDANDO_AVALIACAO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public StatusAdocao getStatus() {
        return status;
    }

    public void setStatus(StatusAdocao status) {
        this.status = status;
    }

    public String getJustificativaStatus() {
        return justificativaStatus;
    }

    public void setJustificativaStatus(String justificativaStatus) {
        this.justificativaStatus = justificativaStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adocao adocao = (Adocao) o;
        return Objects.equals(id, adocao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void marcaComoAprovada() {
        this.status = StatusAdocao.APROVADO;
    }

    public void marcaComoReprovada(String justificativa) {
        this.justificativaStatus = justificativa;
        this.status = StatusAdocao.REPROVADO;
    }
}
