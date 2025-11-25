package tech.build.consultas.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_atendimento")
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTime;

    @ManyToOne(optional = false)
    private Paciente paciente;

    @ManyToMany
    @JoinTable(
            name = "atendimento_procedimento",
            joinColumns = @JoinColumn(name = "atendimento_id"),
            inverseJoinColumns = @JoinColumn(name = "procedimento_id")
    )
    private List<Procedimento> procedimentos;

    @Enumerated(EnumType.STRING)
    private AtendimentoTipo tipo;

    private String planNumber; // opcional (somente obrigatório se tipo == PLANO)

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // id do usuário que efetuou o atendimento

    private BigDecimal valorTotal;

    public Atendimento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public List<Procedimento> getProcedimentos() {
        return procedimentos;
    }

    public void setProcedimentos(List<Procedimento> procedimentos) {
        this.procedimentos = procedimentos;
    }

    public AtendimentoTipo getTipo() {
        return tipo;
    }

    public void setTipo(AtendimentoTipo tipo) {
        this.tipo = tipo;
    }

    public String getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(String planNumber) {
        this.planNumber = planNumber;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
}
