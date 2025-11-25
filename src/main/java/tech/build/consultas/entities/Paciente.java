package tech.build.consultas.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "tb_paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pacienteId;

    @Column(name = "paciente_cpf", unique = true)
    private String pacienteCpf;

    @Column(name = "paciente_nome")
    private String pacienteNome;

    @Column(name = "paciente_email", unique = true)
    private String pacienteEmail;

    @Column(name = "paciente_telefone")
    private String pacienteTelefone;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Embedded
    private Endereco endereco;

    @Embedded
    private Responsavel responsavel;

    public Paciente() {
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getPacienteCpf() {
        return pacienteCpf;
    }

    public void setPacienteCpf(String pacienteCpf) {
        this.pacienteCpf = pacienteCpf;
    }

    public String getPacienteNome() {
        return pacienteNome;
    }

    public void setPacienteNome(String pacienteNome) {
        this.pacienteNome = pacienteNome;
    }

    public String getPacienteEmail() {
        return pacienteEmail;
    }

    public void setPacienteEmail(String pacienteEmail) {
        this.pacienteEmail = pacienteEmail;
    }

    public String getPacienteTelefone() {
        return pacienteTelefone;
    }

    public void setPacienteTelefone(String pacienteTelefone) {
        this.pacienteTelefone = pacienteTelefone;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Responsavel getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Responsavel responsavel) {
        this.responsavel = responsavel;
    }

    public int getAge() {
        if (dataNascimento == null) return 0;
        return Period.between(this.dataNascimento, LocalDate.now()).getYears();
    }
}
