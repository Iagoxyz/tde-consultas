package tech.build.consultas.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_procedimento")
public class Procedimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long procedimentoId;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "valor_plan")
    private BigDecimal valorPlan;

    @Column(name = "valor_particular")
    private BigDecimal valorParticular;

    public Procedimento() {
    }

    public Long getProcedimentoId() {
        return procedimentoId;
    }

    public void setProcedimentoId(Long procedimentoId) {
        this.procedimentoId = procedimentoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorPlan() {
        return valorPlan;
    }

    public void setValorPlan(BigDecimal valorPlan) {
        this.valorPlan = valorPlan;
    }

    public BigDecimal getValorParticular() {
        return valorParticular;
    }

    public void setValorParticular(BigDecimal valorParticular) {
        this.valorParticular = valorParticular;
    }
}
