package tech.build.consultas.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_user")
public class Usuario {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "nome")
    private String nome;

    @Enumerated(EnumType.STRING)
    private UsuarioTipo tipo;

    @Column(name = "senha")
    private String senha;

    public Usuario() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UsuarioTipo getTipo() {
        return tipo;
    }

    public void setTipo(UsuarioTipo tipo) {
        this.tipo = tipo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
